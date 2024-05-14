package com.benodeveloper.medialibrary.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "media-library")
public class StorageProperties {

    @Getter
    @Setter
    private String uploadDir;

}
