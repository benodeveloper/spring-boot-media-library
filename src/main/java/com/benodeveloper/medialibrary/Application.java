package com.benodeveloper.medialibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.benodeveloper.medialibrary.properties.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		StorageProperties.class
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
