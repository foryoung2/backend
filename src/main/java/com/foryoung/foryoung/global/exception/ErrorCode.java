package com.foryoung.foryoung.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_001", "Invalid access token"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "Invalid refresh token"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH_003", "Refresh token not found"),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "AUTH_004", "Refresh token does not match"),
    LOGOUT_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_005", "Token has been logged out"),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "Member not found"),
    NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER_002", "Nickname already exists"),
    DELETED_MEMBER(HttpStatus.FORBIDDEN, "MEMBER_003", "Deleted member"),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "MEMBER_004", "Authentication required"),

    PERFORMANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "PERFORMANCE_001", "Performance not found"),
    PERFORMANCE_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "PERFORMANCE_002", "Performance record not found"),
    PERFORMANCE_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "PERFORMANCE_003", "Performance review not found"),
    PERFORMANCE_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "PERFORMANCE_004", "Performance schedule not found"),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_001", "Comment not found"),
    COMMENT_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "COMMENT_002", "Comment already deleted"),
    REPLY_CANNOT_HAVE_REPLY(HttpStatus.BAD_REQUEST, "COMMENT_003", "Replies cannot have replies"),
    DELETED_COMMENT_CANNOT_BE_UPDATED(HttpStatus.BAD_REQUEST, "COMMENT_004", "Deleted comment cannot be updated"),

    REVIEW_LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "LIKE_001", "Review like already exists"),
    REVIEW_LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "LIKE_002", "Review like not found"),

    VENUE_NOT_FOUND(HttpStatus.NOT_FOUND, "VENUE_001", "Venue not found"),

    VENUE_SEAT_VIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "VENUE_VIEW_001", "Venue seat view not found"),
    VENUE_SEAT_VIEW_ACCESS_DENIED(HttpStatus.FORBIDDEN, "VENUE_VIEW_002", "You cannot delete another member's venue view"),
    VENUE_SEAT_VIEW_IMAGE_REQUIRED(HttpStatus.BAD_REQUEST, "VENUE_VIEW_003", "At least one image is required"),
    VENUE_SEAT_VIEW_IMAGE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "VENUE_VIEW_004", "Too many images uploaded"),

    INVALID_IMAGE(HttpStatus.BAD_REQUEST, "VENUE_VIEW_IMAGE_001", "Invalid image"),
    INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "VENUE_VIEW_IMAGE_002", "Unsupported image type"),
    IMAGE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "VENUE_VIEW_IMAGE_003", "Image size exceeds the allowed limit"),
    IMAGE_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "VENUE_VIEW_IMAGE_004", "Failed to save image"),
    IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "VENUE_VIEW_IMAGE_005", "Failed to delete image"),

    ELASTICSEARCH_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SEARCH_001", "Search service is temporarily unavailable");
    private final HttpStatus status;
    private final String code;
    private final String message;


}