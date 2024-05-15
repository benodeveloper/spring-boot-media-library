package com.benodeveloper.medialibrary.controllers;

import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media/images")
public class ImageController {

    public static final String[] ALLOWED_CONTENT_TYPE = new String[]{
        "image/gif",
        "image/jpeg",
        "image/png",
        "image/svg+xml",
        "image/tiff",
        "image/x-tiff",
        "image/apng",
        "image/avif",
        "image/webp",};

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        // TODO: handle upload in a separate directory + convert image to jpeg or webp + crop images, ...
        try {
            if (!Arrays.asList(ALLOWED_CONTENT_TYPE).contains(file.getContentType())) {
                throw new Exception("Unsupported image content type");
            }
            return ResponseEntity.ok(file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
