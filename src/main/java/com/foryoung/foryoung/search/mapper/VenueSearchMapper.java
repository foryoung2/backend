package com.foryoung.foryoung.search.mapper;

import com.foryoung.foryoung.search.document.VenueDocument;
import com.foryoung.foryoung.search.dto.VenueSearchResponse;
import com.foryoung.foryoung.venue.entity.Venue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VenueSearchMapper {


    VenueDocument toVenueDocument(Venue venue);


    VenueSearchResponse toVenueSearchResponse(VenueDocument document);


}