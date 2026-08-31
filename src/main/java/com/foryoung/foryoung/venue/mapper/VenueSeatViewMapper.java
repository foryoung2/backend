package com.foryoung.foryoung.venue.mapper;

import com.foryoung.foryoung.venue.dto.VenueSeatViewResponse;
import com.foryoung.foryoung.venue.entity.VenueSeatView;
import com.foryoung.foryoung.venue.entity.VenueSeatViewImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenueSeatViewMapper {


    @Mapping(target = "venueId", source = "venue.id")
    @Mapping(target = "venueName", source = "venue.name")
    @Mapping(
            target = "writerNickname",
            expression = "java(venueSeatView.getMember().getDisplayNickname())"
    )
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "imageUrls", source = "images")
    VenueSeatViewResponse toVenueSeatViewResponse(VenueSeatView venueSeatView);


    default String map(VenueSeatViewImage image) {
        return image.getImageUrl();
    }


}