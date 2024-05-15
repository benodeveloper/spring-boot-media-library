package com.benodeveloper.medialibrary.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static com.benodeveloper.medialibrary.utils.ImageUtils.*;

@Service
public class ImageService {

    private final StorageService storageService;

    public ImageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public Path saveImage(MultipartFile file, String filename) throws Exception {
        // TODO: handle upload in a separate directory + convert image to jpeg or webp +
        // crop images, ...
        if (!Arrays.asList(ALLOWED_CONTENT_TYPE).contains(file.getContentType()))
            throw new Exception("Unsupported image content type");
        if(!Objects.equals(file.getContentType(), JPG_CONTENT_TYPE)) {
//            ConvertImageToJPG(file.getInputStream());
        }

        return storageService.saveFile(file, filename);
    }
}
