package com.benodeveloper.medialibrary.services;

import java.util.UUID;
import java.nio.file.Path;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.stream.Stream;
import java.nio.file.StandardCopyOption;

import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.benodeveloper.medialibrary.entities.Media;
import com.benodeveloper.medialibrary.properties.StorageProperties;

@Service
public class StorageService {

    private final Path uploadPath;

    public StorageService(StorageProperties properties) {
        this.uploadPath = Paths.get(properties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    /**
     * Create upload directory if not exists, called in the Bootstrap file.
     */
    public void init() {
        try {
            if (Files.notExists(uploadPath))
                Files.createDirectory(uploadPath);

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.uploadPath, 1).filter(path -> !path.equals(this.uploadPath))
                    .map(this.uploadPath::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    /**
     * Save the file
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public Media saveFile(MultipartFile file) throws IOException {
        String originalFilename = Optional.of(file.getOriginalFilename()).orElseThrow();

        String filename = StringUtils.cleanPath(originalFilename);
        var media = Media.builder()
                .uuid(UUID.randomUUID().toString())
                .fileName(filename)
                .name(originalFilename)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build();

        // TODO: create a copy if file is already exists
        Files.copy(file.getInputStream(), this.uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

        return media;
    }
}
