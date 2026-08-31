package com.foryoung.foryoung.venue.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.global.image.ImageStorageService;
import com.foryoung.foryoung.global.image.ImageType;
import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.member.service.MemberService;
import com.foryoung.foryoung.venue.dto.VenueSeatViewCreateRequest;
import com.foryoung.foryoung.venue.dto.VenueSeatViewResponse;
import com.foryoung.foryoung.venue.entity.Venue;
import com.foryoung.foryoung.venue.entity.VenueSeatView;
import com.foryoung.foryoung.venue.entity.VenueSeatViewImage;
import com.foryoung.foryoung.venue.mapper.VenueSeatViewMapper;
import com.foryoung.foryoung.venue.repository.VenueRepository;
import com.foryoung.foryoung.venue.repository.VenueSeatViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VenueSeatViewService {


    private static final int MAX_IMAGE_COUNT = 10;

    private final VenueRepository venueRepository;
    private final VenueSeatViewRepository seatViewRepository;

    private final ImageStorageService imageStorageService;
    private final MemberService memberService;

    private final VenueSeatViewMapper seatViewMapper;


    @Transactional
    public VenueSeatViewResponse createVenueSeatView(Long memberId,
                                                     Long venueId,
                                                     VenueSeatViewCreateRequest request,
                                                     List<MultipartFile> images) {

        validateImages(images);

        Member member = memberService.findMemberById(memberId);

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new CustomException(ErrorCode.VENUE_NOT_FOUND));

        VenueSeatView venueSeatView = VenueSeatView.builder()
                .venue(venue)
                .member(member)
                .seatInfo(request.getSeatInfo())
                .content(request.getContent())
                .build();

        List<String> savedImageUrls = new ArrayList<>();

        try {

            for (int i = 0; i < images.size(); i++) {

                String imageUrl = imageStorageService.saveImage(images.get(i), ImageType.VENUE_VIEW);

                savedImageUrls.add(imageUrl);

                venueSeatView.addImage(imageUrl, i);

            }

            VenueSeatView savedVenueSeatView = seatViewRepository.save(venueSeatView);

            return toResponse(savedVenueSeatView, memberId);

        } catch (Exception e) {

            deleteSavedImages(savedImageUrls);

            throw e;
        }

    }


    public VenueSeatViewResponse getVenueSeatView(Long venueViewId,
                                                  Long memberId) {

        VenueSeatView seatView = seatViewRepository
                .findWithImagesById(venueViewId)
                .orElseThrow(() -> new CustomException(ErrorCode.VENUE_SEAT_VIEW_NOT_FOUND));


        return toResponse(seatView, memberId);

    }


    public Page<VenueSeatViewResponse> getVenueSeatViews(Long venueId,
                                                         Long memberId,
                                                         Pageable pageable) {

        venueRepository.findById(venueId)
                .orElseThrow(() -> new CustomException(ErrorCode.VENUE_NOT_FOUND));


        return seatViewRepository.findByVenueId(venueId, pageable)
                .map(seatView ->
                        toResponse(seatView, memberId)
                );

    }


    public Page<VenueSeatViewResponse> getMyVenueSeatViews(Long memberId,
                                                           Pageable pageable) {

        return seatViewRepository.findByMemberId(memberId, pageable)
                .map(seatView ->
                        toResponse(seatView, memberId)
                );

    }


    @Transactional
    public void deleteVenueSeatView(Long memberId,
                                    Long venueViewId) {

        VenueSeatView seatView =
                seatViewRepository.findById(venueViewId)
                        .orElseThrow(() -> new CustomException(ErrorCode.VENUE_SEAT_VIEW_NOT_FOUND));

        validateOwner(seatView, memberId);

        deleteImages(seatView);

        seatViewRepository.delete(seatView);

    }


    private VenueSeatViewResponse toResponse(VenueSeatView seatView,
                                             Long memberId) {

        VenueSeatViewResponse response = seatViewMapper.toVenueSeatViewResponse(seatView);


        boolean owner = memberId != null && seatView.getMember().getId().equals(memberId);


        return VenueSeatViewResponse.builder()
                .id(response.getId())
                .venueId(response.getVenueId())
                .venueName(response.getVenueName())
                .writerNickname(response.getWriterNickname())
                .owner(owner)
                .seatInfo(response.getSeatInfo())
                .content(response.getContent())
                .imageUrls(response.getImageUrls())
                .build();

    }


    private void validateImages(List<MultipartFile> images) {

        if (images == null || images.isEmpty()) {
            throw new CustomException(ErrorCode.VENUE_SEAT_VIEW_IMAGE_REQUIRED);
        }

        if (images.size() > MAX_IMAGE_COUNT) {
            throw new CustomException(ErrorCode.VENUE_SEAT_VIEW_IMAGE_LIMIT_EXCEEDED);
        }

    }


    private void validateOwner(VenueSeatView venueSeatView,
                               Long memberId) {

        if (!venueSeatView.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.VENUE_SEAT_VIEW_ACCESS_DENIED);
        }

    }


    private void deleteImages(VenueSeatView venueSeatView) {

        for (VenueSeatViewImage image : venueSeatView.getImages()) {
            imageStorageService.deleteImage(image.getImageUrl(), ImageType.VENUE_VIEW);
        }

    }


    private void deleteSavedImages(List<String> imageUrls) {

        for (String imageUrl : imageUrls) {

            try {

                imageStorageService.deleteImage(imageUrl, ImageType.VENUE_VIEW);

            } catch (Exception deleteException) {

                log.error(
                        "Failed to delete image during compensation: {}",
                        imageUrl,
                        deleteException
                );

            }
        }

    }


}