package com.foryoung.foryoung.venue.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.search.document.VenueDocument;
import com.foryoung.foryoung.search.mapper.VenueSearchMapper;
import com.foryoung.foryoung.search.repository.VenueSearchRepository;
import com.foryoung.foryoung.venue.dto.VenueCreateRequest;
import com.foryoung.foryoung.venue.dto.VenueResponse;
import com.foryoung.foryoung.venue.entity.Venue;
import com.foryoung.foryoung.venue.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueService {


    private final VenueRepository venueRepository;
    private final VenueSearchRepository venueSearchRepository;
    private final VenueSearchMapper venueSearchMapper;


    @Transactional
    public VenueResponse createVenue(VenueCreateRequest request) {

        String name = request.getName().trim();

        if (venueRepository.existsByName(name)) {
            throw new CustomException(ErrorCode.VENUE_ALREADY_EXISTS);
        }

        Venue venue = Venue.builder()
                .name(name)
                .build();

        Venue savedVenue = venueRepository.save(venue);
        VenueDocument document = venueSearchMapper.toVenueDocument(savedVenue);

        venueSearchRepository.save(document);

        return VenueResponse.from(savedVenue);

    }


}