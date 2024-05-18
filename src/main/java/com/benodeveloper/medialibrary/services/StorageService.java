package com.benodeveloper.medialibrary.services;

import com.benodeveloper.medialibrary.exceptions.*;
import com.benodeveloper.medialibrary.properties.StorageProperties;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

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
    public void init() throws IOException {
        try {
            if(!Files.exists(uploadPath)) {

//                FileUtils.forceDelete(uploadPath.toFile());
                FileUtils.forceMkdir(uploadPath.toFile());
            }

        } catch (IOException e) {
            throw new IOException("Could not initialize folder for upload! : (" + e.getMessage() + ")");
        }
    }

    /**
     * Load resource from uploads directory.
     *
     * @param filename
     * @return
     * @throws MalformedURLException
     */
    public Resource loadResource(String filename) throws ResourceException, FileOperationException {
        var file = uploadPath.resolve(filename);
        if (!Files.exists(file)) {
            throw new ResourceNotFoundException("Could not load the file! " + filename);
        }

        if (!Files.isReadable(file)) throw new UnreadableFileException("Could not read the file!");

        try {
            return new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            throw new UnreadableFileException(e.getMessage());
        }

    }

    public Stream<Path> loadAll() throws ResourceNotFoundException {
        try {
            return Files.walk(this.uploadPath, 1).filter(path -> !path.equals(this.uploadPath))
                    .map(this.uploadPath::relativize);
        } catch (IOException e) {
            throw new ResourceNotFoundException("Could not load the files! " + e.getMessage());
        }
    }

    /**
     * Save the file in the uploads directory, return the file path
     *
     * @param file
     * @param dir
     * @return
     * @throws FileCopyOperationException
     */
    public Path saveFile(MultipartFile file, String dir) throws FileOperationException {
        try {
            FileUtils.forceMkdir(this.uploadPath.resolve(dir).toFile());
            Path filePath = this.uploadPath.resolve(dir).resolve(Objects.requireNonNull(file.getOriginalFilename()));
            return SaveMultipartFile(file, filePath);
        } catch (IOException e) {
            throw new FileOperationException(e);
        }

    }

    /**
     * Save Multipart file in a given path.
     *
     * @param file
     * @param path
     * @return
     * @throws FileCopyOperationException
     */
    public static Path SaveMultipartFile(MultipartFile file, Path path) throws FileOperationException {

        if (Files.exists(path)) {
            throw new FileCopyOperationException("The file is already existed");
        }

        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new FileCopyOperationException(e.getMessage());
        }
        return path;
    }
}
