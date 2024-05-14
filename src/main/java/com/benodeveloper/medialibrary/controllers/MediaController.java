package com.benodeveloper.medialibrary.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.benodeveloper.medialibrary.entities.Media;
import com.benodeveloper.medialibrary.services.StorageService;

@RestController
@RequestMapping(path = "api/media")
public class MediaController {

    @Autowired
    private final StorageService storageService;

    public MediaController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * TODO: Get all files as a list
     * 
     * @return
     * @throws IOException
     */
    @GetMapping
    public Stream<Path> getResources() throws IOException {
        return storageService.loadAll();
    }

    /**
     * Upload a single file
     * 
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(path = "/upload")
    public Media uploadMedia(@RequestParam("media") MultipartFile file) throws IOException {
        return storageService.saveFile(file);
    }
}
