package com.benodeveloper.medialibrary.controllers;

import java.util.Arrays;

import com.benodeveloper.medialibrary.entities.Media;
import com.benodeveloper.medialibrary.exceptions.InvalidContentTypeException;
import com.benodeveloper.medialibrary.services.MediaService;
import com.benodeveloper.medialibrary.utils.ImageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

import static com.benodeveloper.medialibrary.utils.ImageUtils.*;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final MediaService mediaService;

    public ImageController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Upload an image with a simple HTTP request.
     *
     * @param file submitted file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        // check if is a valid image content type
        if (!Arrays.asList(ALLOWED_CONTENT_TYPE).contains(file.getContentType()))
            throw new InvalidContentTypeException("Invalid content type o image");
        //
        // copy and save image as media
        Media media = mediaService.saveMultipartFileAsMedia(file);
        // create a copy in JPG
        ImageUtils.ConvertImageToJPG(Paths.get(media.getPath()));
        //Crop 500X500 in the center
//        cropImage(Paths.get(media.getPath()), 500, 500, CROP_POSITION_CENTER);

        return ResponseEntity.ok(media);
    }
}
