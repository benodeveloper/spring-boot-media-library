package com.benodeveloper.medialibrary;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.benodeveloper.medialibrary.services.StorageService;

@Component
public class Bootstrap implements CommandLineRunner {

    private final StorageService storageService;

    public Bootstrap(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void run(String... args) throws Exception {
        storageService.init();
    }

}
