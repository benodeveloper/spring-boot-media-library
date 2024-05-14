package com.benodeveloper.medialibrary.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.benodeveloper.medialibrary.entities.Media;
import com.benodeveloper.medialibrary.repositories.MediaRepository;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    /**
     * Save a single media.
     * 
     * @param media
     * @return
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
     * @return
     */
    public Iterable<Media> getAllMedia() {
        return mediaRepository.findAll();
    }

    /**
     * Delete a specific media by uuid, it also delete the file from uploads
     * 
     * @param uuid
     * @throws IOException
     */
    public void deleteMedia(String uuid) throws IOException {
        var media = mediaRepository.findByUUID(UUID.fromString(uuid));
        if(media.isPresent()) {
            var path = Path.of(media.get().getPath());
            if (Files.exists(path)) {
                Files.delete(path);
            }
            mediaRepository.delete(media.get());
        }
    }
}
