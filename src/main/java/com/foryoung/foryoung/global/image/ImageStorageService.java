package com.foryoung.foryoung.global.image;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class ImageStorageService {


    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024L;

    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp"
            );

    private final Path uploadRoot = Paths.get("uploads");


    public String saveImage(MultipartFile image,
                            ImageType imageType) {

        validateImage(image);

        Path uploadPath = uploadRoot.resolve(imageType.getDirectory());

        try {

            Files.createDirectories(uploadPath);

            String extension = getExtension(image.getOriginalFilename());

            String fileName = UUID.randomUUID() + extension;

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/images/" + imageType.getDirectory() + "/" + fileName;

        } catch (IOException e) {

            log.error("Failed to save image. filename={}", image.getOriginalFilename(), e);

            throw new CustomException(ErrorCode.IMAGE_SAVE_FAILED);
        }

    }


    public void deleteImage(String imageUrl,
                            ImageType imageType) {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

        Path filePath = uploadRoot
                .resolve(imageType.getDirectory())
                .resolve(fileName);

        try {

            Files.deleteIfExists(filePath);

        } catch (IOException e) {

            log.error("Failed to delete image. imageUrl={}", imageUrl, e);

            throw new CustomException(ErrorCode.IMAGE_DELETE_FAILED);
        }

    }


    private void validateImage(MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_IMAGE);
        }

        if (image.getSize() > MAX_FILE_SIZE) {
            throw new CustomException(ErrorCode.IMAGE_SIZE_EXCEEDED);
        }

        String contentType = image.getContentType();

        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new CustomException(ErrorCode.INVALID_IMAGE_TYPE);
        }

    }


    private String getExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename
                .substring(filename.lastIndexOf("."))
                .toLowerCase();
    }


}