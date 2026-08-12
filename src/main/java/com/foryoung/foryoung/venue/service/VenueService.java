package com.foryoung.foryoung.venue.service;

import com.foryoung.foryoung.venue.dto.VenueSearchResponse;
import com.foryoung.foryoung.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueService {


    private final VenueRepository venueRepository;


    public Page<VenueSearchResponse> searchVenues(String keyword,
                                                  Pageable pageable) {

        return venueRepository
                .findByNameContainingIgnoreCase(keyword, pageable)
                .map(VenueSearchResponse::from);

    }


}