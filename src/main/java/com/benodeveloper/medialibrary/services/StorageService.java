package com.benodeveloper.medialibrary.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.benodeveloper.medialibrary.properties.StorageProperties;

@Service
public class StorageService {

    private final Path uploadPath;

    public StorageService(StorageProperties properties) {
        this.uploadPath = Paths.get(properties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    /**
     * Create upload directory if not exists, called in the Bootstrap file.
     */
    public void init() {
        try {
            FileUtils.forceDelete(uploadPath.toFile());
            FileUtils.forceMkdir(uploadPath.toFile());

        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload! : (" + e.getMessage() + ")");
        }
    }

    /**
     * Load resource from uploads directory.
     * 
     * @param filename
     * @return
     * @throws MalformedURLException
     */
    public Optional<Resource> loadResource(String filename) throws MalformedURLException {
        var file = uploadPath.resolve(filename);
        if(Files.exists(file) && Files.isReadable(file)) {
            var resource = new UrlResource(file.toUri());
            return Optional.of(resource);
        }
        return  Optional.empty();
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.uploadPath, 1).filter(path -> !path.equals(this.uploadPath))
                    .map(this.uploadPath::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    /**
     * Save the file in the uploads directory, return the file path
     *
     * @param file
     * @param filename
     * @return
     * @throws Exception
     * @throws IOException
     */
    public Path saveFile(MultipartFile file, String filename) throws Exception {
        // get file extension
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        filename = filename + "." + fileExtension;
        Path path = this.uploadPath.resolve(filename);

        if (Files.exists(path)) {
            throw new Exception("The file is already existed");
        }

        Files.copy(file.getInputStream(), path);
        return path;
    }
}
