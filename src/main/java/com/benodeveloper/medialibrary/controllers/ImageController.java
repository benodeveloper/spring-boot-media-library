package com.benodeveloper.medialibrary.controllers;

import java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.benodeveloper.medialibrary.entities.Media;
import org.springframework.web.multipart.MultipartFile;
import com.benodeveloper.medialibrary.query.ImageQuery;
import com.benodeveloper.medialibrary.utils.ImageUtils;
import com.benodeveloper.medialibrary.services.MediaService;
import com.benodeveloper.medialibrary.services.StorageService;
import com.benodeveloper.medialibrary.exceptions.ResourceNotFoundException;
import com.benodeveloper.medialibrary.exceptions.InvalidContentTypeException;

import java.nio.file.Paths;

import static com.benodeveloper.medialibrary.utils.ImageUtils.*;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final MediaService mediaService;
    private final StorageService storageService;

    public ImageController(MediaService mediaService, StorageService storageService) {
        this.mediaService = mediaService;
        this.storageService = storageService;
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
        return ResponseEntity.ok(media);
    }

    /**
     * Get Image by name.
     *
     * @param name {@code String}
     * @return
     */
    @GetMapping("/{name}")
    public ResponseEntity<?> getResource(@PathVariable("name") String name, @ModelAttribute ImageQuery query) throws IOException {
        int width = query.getW();
        int height = query.getH();
        // unsupported formats [avif, webp, x-tiff,apng,avif]
        String format = query.getF();
        var media = mediaService.getByName(name).orElseThrow(() -> new ResourceNotFoundException("Image with a given filename ("+name+") not found"));

        Path path = resizeImage(Paths.get(media.getPath()), width, height, format);
        var file = storageService.loadResource(path.toString());

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(Files.probeContentType(path)))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);

    }
}
