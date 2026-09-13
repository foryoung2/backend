package com.foryoung.foryoung.friend.controller;

import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.friend.dto.FriendRequestResponse;
import com.foryoung.foryoung.friend.dto.FriendResponse;
import com.foryoung.foryoung.friend.dto.MemberSearchResponse;
import com.foryoung.foryoung.friend.service.FriendService;
import com.foryoung.foryoung.global.pagination.PageResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends")
public class FriendController {


    private final FriendService friendService;

    @PostMapping("/{memberId}/request")
    public ResponseEntity<Void> sendFriendRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @PathVariable Long memberId) {

        friendService.sendFriendRequest(userDetails.getMemberId(), memberId);

        return ResponseEntity.ok().build();

    }


    @GetMapping("/requests/received")
    public ResponseEntity<PageResponse<FriendRequestResponse>> getReceivedFriendRequests(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                         @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC)
                                                                                         Pageable pageable) {

        Page<FriendRequestResponse> requests =
                friendService.getReceivedFriendRequests(userDetails.getMemberId(), pageable);

        return ResponseEntity.ok(PageResponse.from(requests));

    }


    @GetMapping("/requests/sent")
    public ResponseEntity<PageResponse<FriendRequestResponse>> getSentFriendRequests(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                     @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC)
                                                                                     Pageable pageable) {

        Page<FriendRequestResponse> requests =
                friendService.getSentFriendRequests(userDetails.getMemberId(), pageable);

        return ResponseEntity.ok(PageResponse.from(requests));

    }


    @PatchMapping("/{friendshipId}/accept")
    public ResponseEntity<Void> acceptFriendRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable Long friendshipId) {

        friendService.acceptFriendRequest(userDetails.getMemberId(), friendshipId);

        return ResponseEntity.ok().build();

    }


    @DeleteMapping("/{friendshipId}/reject")
    public ResponseEntity<Void> rejectFriendRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable Long friendshipId) {

        friendService.rejectFriendRequest(userDetails.getMemberId(), friendshipId);

        return ResponseEntity.noContent().build();

    }


    @GetMapping
    public ResponseEntity<PageResponse<FriendResponse>> getFriends(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC)
                                                                   Pageable pageable) {

        Page<FriendResponse> friends =
                friendService.getFriends(userDetails.getMemberId(), pageable);

        return ResponseEntity.ok(PageResponse.from(friends));

    }


    @DeleteMapping("/{friendshipId}")
    public ResponseEntity<Void> deleteFriend(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Long friendshipId) {

        friendService.deleteFriend(userDetails.getMemberId(), friendshipId);

        return ResponseEntity.noContent().build();

    }


    @PatchMapping("/{friendshipId}/block")
    public ResponseEntity<Void> blockFriend(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long friendshipId) {

        friendService.blockFriend(userDetails.getMemberId(), friendshipId);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{friendshipId}/unblock")
    public ResponseEntity<Void> unblockFriend(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable Long friendshipId) {

        friendService.unblockFriend(userDetails.getMemberId(), friendshipId);

        return ResponseEntity.noContent().build();

    }


    @GetMapping("/search")
    public ResponseEntity<PageResponse<MemberSearchResponse>> searchMembersByNickname(@RequestParam @NotBlank(message = "Keyword must not be blank") String nickname,
                                                                                      @PageableDefault(size = 12, sort = "id", direction = Sort.Direction.DESC)
                                                                                      Pageable pageable) {

        Page<MemberSearchResponse> members =
                friendService.searchMembersByNickname(nickname, pageable);

        return ResponseEntity.ok(PageResponse.from(members));

    }


}