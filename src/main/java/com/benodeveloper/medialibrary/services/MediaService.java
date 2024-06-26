package com.benodeveloper.medialibrary.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.benodeveloper.medialibrary.entities.Media;
import com.benodeveloper.medialibrary.repositories.MediaRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;
    private final StorageService storageService;

    public MediaService(MediaRepository mediaRepository, StorageService storageService) {
        this.mediaRepository = mediaRepository;
        this.storageService = storageService;
    }

    /**
     * Copy the {@code MultipartFile} file in uploads directory
     * and save it in the database as {@code Media} media
     *
     * @param file {@code MultipartFile}
     * @return {@code Media}
     * @throws Exception
     */
    public Media saveMultipartFileAsMedia(MultipartFile file) throws Exception {
        UUID mediaUUID = UUID.randomUUID();
        Path path = storageService.saveFile(file, mediaUUID.toString());
        Media media = Media.builder()
                .UUID(mediaUUID)
                .fileName(path.getFileName().toString())
                .name(file.getOriginalFilename())
                .fileType(file.getContentType())
                .size(file.getSize())
                .path(path.toString())
                .build();
        return saveMedia(media);
    }

    /**
     * Save a single media.
     *
     * @param media {@code Media}
     * @return {@code Media}
     * @throws Exception
     */
    public Media saveMedia(Media media) throws Exception {
        if (!Files.exists(Path.of(media.getPath()))) {
            throw new Exception("File does not exists in uploads directory");
        }
        return mediaRepository.save(media);
    }

    /**
     * Get all media.
     *
     * @return {@code Iterable<Media>}
     */
    public Iterable<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    /**
     * Get single media by uuid.
     *
     * @param uuid {@code String}
     * @return {@code Optional<Media>}
     */
    public Optional<Media> getByUUID(String uuid) {
        return mediaRepository.findByUUID(UUID.fromString(uuid));
    }

    /**
     * Delete a specific media by uuid, it also delete the file from uploads
     *
     * @param uuid {@code String}
     * @throws IOException
     */
    public void deleteMedia(String uuid) throws IOException {
        var media = getByUUID(uuid);
        if (media.isPresent()) {
            var path = Path.of(media.get().getPath());
            if (Files.exists(path)) {
                Files.delete(path);
            }
            mediaRepository.delete(media.get());
        }
    }
}
