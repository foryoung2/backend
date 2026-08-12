package com.foryoung.foryoung.venue.repository;

import com.foryoung.foryoung.venue.entity.VenueSeatView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VenueSeatViewRepository extends JpaRepository<VenueSeatView, Long> {


    @EntityGraph(attributePaths = {
            "venue",
            "member",
            "images"
    })
    Optional<VenueSeatView> findWithImagesById(Long venueViewId);


    Page<VenueSeatView> findByVenueId(Long venueId, Pageable pageable);


    Page<VenueSeatView> findByMemberId(Long memberId, Pageable pageable);


}