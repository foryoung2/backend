package com.foryoung.foryoung.venue.repository;

import com.foryoung.foryoung.venue.entity.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VenueRepository extends JpaRepository<Venue, Long> {


    Page<Venue> findByNameContainingIgnoreCase(String keyword, Pageable pageable);


    Optional<Venue> findByName(String name);


}