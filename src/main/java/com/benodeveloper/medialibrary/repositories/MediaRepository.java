package com.benodeveloper.medialibrary.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benodeveloper.medialibrary.entities.Media;

@Repository
public interface  MediaRepository extends JpaRepository<Media, Long> {

    Optional<Media> findByUUID(UUID uuid);
    Optional<Media> findByFilename(String filename);
    Optional<Media> findByName(String name);
}
