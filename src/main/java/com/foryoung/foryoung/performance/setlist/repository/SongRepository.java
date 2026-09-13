package com.foryoung.foryoung.performance.setlist.repository;

import com.foryoung.foryoung.performance.setlist.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {


    Optional<Song> findByNormalizedArtistAndNormalizedTitle(
            String normalizedArtist,
            String normalizedTitle
    );


}