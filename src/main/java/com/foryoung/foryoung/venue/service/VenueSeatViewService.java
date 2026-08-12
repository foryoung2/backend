package com.foryoung.foryoung.venue.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
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
                                                     VenueSeatViewCreateRequest request,
                                                     List<MultipartFile> images) {

        validateImages(images);

        Member member = memberService.findMemberById(memberId);

        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new CustomException(ErrorCode.VENUE_NOT_FOUND));

        VenueSeatView venueSeatView = VenueSeatView.builder()
                .venue(venue)
                .member(member)
                .seatInfo(request.getSeatInfo())
                .description(request.getDescription())
                .build();

        List<String> savedImageUrls = new ArrayList<>();

        try {
            for (int i = 0; i < images.size(); i++) {

                String imageUrl = imageStorageService.saveImage(images.get(i));

                savedImageUrls.add(imageUrl);

                venueSeatView.addImage(imageUrl, i);
            }

            VenueSeatView savedVenueSeatView = seatViewRepository.save(venueSeatView);

            return seatViewMapper.toVenueSeatViewResponse(savedVenueSeatView);

        } catch (Exception e) {

            deleteSavedImages(savedImageUrls);

            throw e;
        }

    }


    public VenueSeatViewResponse getVenueSeatView(Long venueViewId) {

        VenueSeatView seatView = seatViewRepository.findWithImagesById(venueViewId)
                .orElseThrow(() -> new CustomException(ErrorCode.VENUE_SEAT_VIEW_NOT_FOUND));

        return seatViewMapper.toVenueSeatViewResponse(seatView);

    }


    public Page<VenueSeatViewResponse> getVenueSeatViews(Long venueId,
                                                         Pageable pageable) {

        return seatViewRepository
                .findByVenueId(venueId, pageable)
                .map(seatViewMapper::toVenueSeatViewResponse);
    }


    public Page<VenueSeatViewResponse> getMyVenueSeatViews(Long memberId,
                                                           Pageable pageable) {

        return seatViewRepository
                .findByMemberId(memberId, pageable)
                .map(seatViewMapper::toVenueSeatViewResponse);
    }


    @Transactional
    public void deleteVenueSeatView(Long memberId,
                                    Long venueViewId) {

        VenueSeatView seatView = seatViewRepository.findById(venueViewId)
                .orElseThrow(() -> new CustomException(ErrorCode.VENUE_SEAT_VIEW_NOT_FOUND));

        validateOwner(seatView, memberId);

        deleteImages(seatView);

        seatViewRepository.delete(seatView);

    }


    private void validateImages(List<MultipartFile> images) {

        if (images == null || images.isEmpty()) {
            throw new CustomException(
                    ErrorCode.VENUE_SEAT_VIEW_IMAGE_REQUIRED
            );
        }

        if (images.size() > MAX_IMAGE_COUNT) {
            throw new CustomException(
                    ErrorCode.VENUE_SEAT_VIEW_IMAGE_LIMIT_EXCEEDED
            );
        }

    }


    private void validateOwner(
            VenueSeatView venueSeatView,
            Long memberId) {

        if (!venueSeatView.getMember().getId().equals(memberId)) {
            throw new CustomException(
                    ErrorCode.VENUE_SEAT_VIEW_ACCESS_DENIED
            );
        }

    }


    private void deleteImages(VenueSeatView venueSeatView) {

        for (VenueSeatViewImage image : venueSeatView.getImages()) {
            imageStorageService.deleteImage(
                    image.getImageUrl()
            );
        }

    }


    private void deleteSavedImages(List<String> imageUrls) {

        for (String imageUrl : imageUrls) {

            try {
                imageStorageService.deleteImage(imageUrl);

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