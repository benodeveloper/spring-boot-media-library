package com.benodeveloper.medialibrary.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.benodeveloper.medialibrary.entities.Media;
import com.benodeveloper.medialibrary.services.MediaService;
import com.benodeveloper.medialibrary.services.StorageService;

@RestController
@RequestMapping(path = "api/media")
public class MediaController {

    @Autowired
    private final StorageService storageService;
    private final MediaService mediaService;

    public MediaController(StorageService storageService, MediaService mediaService) {
        this.storageService = storageService;
        this.mediaService = mediaService;
    }

    /**
     * Get All Media
     *
     * @return
     * @throws IOException
     */
    @GetMapping
    public ResponseEntity<Iterable<Media>> getMedia() throws IOException {
        return ResponseEntity.ok(mediaService.getAllMedia());
    }

    /**
     * Get file by filename.
     * s
     * @param filename
     * @return
     */
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<?> getResource(@PathVariable("filename") String filename) {
        try {
            var file = storageService.loadResource(filename);
            if (!file.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.get().getFilename() + "\"")
                    .body(file.get());
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Get media by uuid.
     *
     * @param uuid
     * @return
     */
    @GetMapping(path = "/{uuid}")
    public ResponseEntity<?> getMediaByUUID(@PathVariable("uuid") String uuid) {
        var media = mediaService.getByUUID(uuid);
        if (media.isPresent()) {
            return ResponseEntity.ok(media.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Upload a single file
     *
     * @param file
     * @return
     */
    @PostMapping(path = "/upload")
    public ResponseEntity<?> uploadMedia(@RequestParam("media") MultipartFile file) {
        UUID mediaUUID = UUID.randomUUID();
        try {
            Path path = storageService.saveFile(file, mediaUUID.toString());

            Media media = Media.builder()
                    .UUID(mediaUUID)
                    .fileName(path.getFileName().toString())
                    .name(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .path(path.toString())
                    .build();

            return ResponseEntity.ok(mediaService.saveMedia(media));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Delete Media
     *
     * @param uuid
     * @return
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteMedia(@PathVariable("uuid") String uuid) {
        try {
            mediaService.deleteMedia(uuid);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }
}
