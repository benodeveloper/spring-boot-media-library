package com.benodeveloper.medialibrary.controllers;

import com.benodeveloper.medialibrary.entities.Media;
import com.benodeveloper.medialibrary.exceptions.InvalidContentTypeException;
import com.benodeveloper.medialibrary.exceptions.ResourceNotFoundException;
import com.benodeveloper.medialibrary.query.ImageQuery;
import com.benodeveloper.medialibrary.response.MediaResponse;
import com.benodeveloper.medialibrary.services.MediaService;
import com.benodeveloper.medialibrary.services.StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.benodeveloper.medialibrary.utils.ImageUtils.ALLOWED_CONTENT_TYPE;
import static com.benodeveloper.medialibrary.utils.ImageUtils.resizeImage;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<MediaResponse> uploadImage(@RequestParam("image") MultipartFile file) {
        // check if is a valid image content type
        if (!Arrays.asList(ALLOWED_CONTENT_TYPE).contains(file.getContentType()))
            throw new InvalidContentTypeException("Invalid content type o image");
        //
        // copy and save image as media
        Media media = mediaService.saveMultipartFileAsMedia(file);

        String link = linkTo(methodOn(ImageController.class).getResource(media.getUUID().toString(), media.getFilename(), null)).toString();
        MediaResponse response = new MediaResponse(
                media.getFilename(),
                link, "uploads/".concat(media.getUUID().toString().concat("/"+media.getFilename())),
                media.getFileType(),
                media.getSize()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Get Image by name.
     *
     * @param name {@code String}
     * @return
     */
    @GetMapping("/{uuid}/{name:.+}")
    public ResponseEntity<?> getResource(@PathVariable("uuid") String uuid, @PathVariable("name") String name, @ModelAttribute ImageQuery query) {
        // set maximum height and width to 4000
        int width = query.getW();
        int height = query.getH();

        // unsupported formats [avif, webp, x-tiff,apng]
        String format = query.getF();
        var media = mediaService.getByUUIDAndName(uuid, name).orElseThrow(() -> new ResourceNotFoundException("Image with a given filename (" + name + ") not found"));

        Path path = resizeImage(Paths.get(media.getPath()), width, height, format);
        var file = storageService.loadResource(path.toString());

        try {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf(Files.probeContentType(path)))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
