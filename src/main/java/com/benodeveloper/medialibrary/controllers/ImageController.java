package com.benodeveloper.medialibrary.controllers;

import java.util.Arrays;

import com.benodeveloper.medialibrary.entities.Media;
import com.benodeveloper.medialibrary.services.MediaService;
import com.benodeveloper.medialibrary.utils.ImageUtils;
import com.sun.jdi.InternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;

import static com.benodeveloper.medialibrary.utils.ImageUtils.*;

@RestController
@RequestMapping("/api/media/images")
public class ImageController {
    private final MediaService mediaService;

    public ImageController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Upload a single image
     *
     * @param file submitted file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            // check if is a valid image content type
            if (!Arrays.asList(ALLOWED_CONTENT_TYPE).contains(file.getContentType()))
                throw new InternalException("Invalid content type o image");
            //
            // copy and save image as media
            Media media = mediaService.saveMultipartFileAsMedia(file);
            // create a copy in JPG
            ImageUtils.ConvertImageToJPG(Paths.get(media.getPath()));
            //Crop 500X500 in the center
            cropImage(Paths.get(media.getPath()), 500, 500, CROP_POSITION_CENTER );

            return ResponseEntity.ok(media);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
