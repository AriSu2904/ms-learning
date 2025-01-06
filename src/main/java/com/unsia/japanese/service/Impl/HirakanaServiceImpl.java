package com.unsia.japanese.service.Impl;

import com.unsia.japanese.dto.request.HirakanaRequest;
import com.unsia.japanese.dto.response.FileResponse;
import com.unsia.japanese.dto.response.HirakanaResponse;
import com.unsia.japanese.entity.Audio;
import com.unsia.japanese.entity.Hirakana;
import com.unsia.japanese.entity.Image;
import com.unsia.japanese.repository.HirakanaRepository;
import com.unsia.japanese.service.AudioService;
import com.unsia.japanese.service.HirakanaService;
import com.unsia.japanese.service.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HirakanaServiceImpl implements HirakanaService {

    private final HirakanaRepository hirakanaRepository;
    private final ImageService imageService;
    private final AudioService audioService;

    @Override
    public List<HirakanaResponse> list(String type) {
        log.info("Try to get all hirakana");

        List<Hirakana> hirakanas = hirakanaRepository.findAllByType(type);

        return hirakanas.stream().map(hirakana -> generateResponse(hirakana, hirakana.getImage(), hirakana.getAudio())
        ).toList();
    }

    @Override
    public HirakanaResponse detail(String id) {
        log.info("Try to get hirakana by id: {}", id);

        Hirakana hirakana = hirakanaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hirakana not found"));

        return generateResponse(hirakana, hirakana.getImage(), hirakana.getAudio());
    }

    private static HirakanaResponse generateResponse(Hirakana hirakana, Image hirakana1, Audio hirakana2) {
        return HirakanaResponse.builder()
                .id(hirakana.getId())
                .name(hirakana.getName())
                .type(hirakana.getType())
                .team(hirakana.getTeam())
                .image(FileResponse.builder()
                        .id(hirakana1.getId())
                        .filename(hirakana1.getName())
                        .url("/api/v1/jps/hirakana/files/images/" + hirakana1.getId())
                        .build())
                .audio(FileResponse.builder()
                        .id(hirakana2.getId())
                        .filename(hirakana2.getName())
                        .url("/api/v1/jps/hirakana/files/audios/" + hirakana2.getId())
                        .build())
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public HirakanaResponse create(HirakanaRequest request, List<MultipartFile> files) {
        log.info("Try to create object hirakana: {}", request.getName());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var authorities = authentication.getAuthorities();
        if (authorities.stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            log.error("Not authorized to create hirakana");

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are unauthorized to access this resources");
        }

        try {
            Hirakana hirakana = Hirakana.builder()
                    .name(request.getName())
                    .type(request.getType()) // HIRAGANA or KATAKANA
                    .team(request.getTeam())
                    .build();

            Image savedImg = imageService.save(hirakana, files.get(0));
            Audio savedAudio = audioService.save(hirakana, files.get(1));

            hirakana.setImage(savedImg);
            hirakana.setAudio(savedAudio);

            Hirakana savedHirakana = hirakanaRepository.save(hirakana);

            return generateResponse(savedHirakana, savedImg, savedAudio);
        }catch (Exception e){
            log.error("An error occurred while creating hirakana: {}", request.getName());
            throw e;
        }
    }

    @Override
    public Resource loadImage(String id) {
        return imageService.findById(id);
    }

    @Override
    public Resource loadAudio(String id) {
        return audioService.findById(id);
    }
}
