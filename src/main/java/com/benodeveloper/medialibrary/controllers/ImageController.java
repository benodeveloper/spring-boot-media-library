package com.benodeveloper.medialibrary.controllers;

import java.nio.file.Path;
import java.util.Arrays;

import com.benodeveloper.medialibrary.entities.Media;
import com.benodeveloper.medialibrary.services.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.UUID;

import com.benodeveloper.medialibrary.services.StorageService;

@RestController
@RequestMapping("/api/media/images")
public class ImageController {

    private  final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws Exception {
//        UUID mediaUUID = UUID.randomUUID();
//        Path path = imageService.saveImage(file);
////         storageService.saveFile(file, mediaUUID.toString());
//
//            Media media = Media.builder()
//                    .UUID(mediaUUID)
//                    .fileName(path.getFileName().toString())
//                    .name(file.getOriginalFilename())
//                    .fileType(file.getContentType())
//                    .size(file.getSize())
//                    .path(path.toString())
//                    .build();
        return ResponseEntity.ok("");
    }
}
