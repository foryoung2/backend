package com.foryoung.foryoung.friend.service;

import com.foryoung.foryoung.friend.dto.FriendRequestResponse;
import com.foryoung.foryoung.friend.dto.FriendResponse;
import com.foryoung.foryoung.friend.dto.MemberSearchResponse;
import com.foryoung.foryoung.friend.entity.FriendStatus;
import com.foryoung.foryoung.friend.entity.Friendship;
import com.foryoung.foryoung.friend.repository.FriendshipRepository;
import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.member.repository.MemberRepository;
import com.foryoung.foryoung.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {


    private final FriendshipRepository friendshipRepository;
    private final MemberRepository memberRepository;

    private final MemberService memberService;


    @Transactional
    public void sendFriendRequest(Long requesterId,
                                  Long receiverId) {

        Member requester = memberService.findMemberById(requesterId);
        Member receiver = memberService.findMemberById(receiverId);

        if(requester.equals(receiver)) {
            throw new CustomException(ErrorCode.CANNOT_REQUEST_YOURSELF);
        }

        String pairKey = createPairKey(requesterId, receiverId);

        if (friendshipRepository.existsByPairKey(pairKey)) {
            throw new CustomException(
                    ErrorCode.FRIEND_REQUEST_ALREADY_EXISTS
            );
        }

        Friendship friendship = Friendship.builder()
                .requester(requester)
                .receiver(receiver)
                .pairKey(pairKey)
                .status(FriendStatus.PENDING)
                .build();

        try {
            friendshipRepository.saveAndFlush(friendship);

        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ALREADY_EXISTS);
        }

    }


    public Page<FriendRequestResponse> getReceivedFriendRequests(Long memberId,
                                                                 Pageable pageable) {

        Member member = memberService.findMemberById(memberId);

        return friendshipRepository.findByReceiverAndStatus(member, FriendStatus.PENDING, pageable)
                .map(FriendRequestResponse::fromReceived);

    }


    public Page<FriendRequestResponse> getSentFriendRequests(Long memberId,
                                                             Pageable pageable) {

        Member member = memberService.findMemberById(memberId);

        return friendshipRepository.findByRequesterAndStatus(member, FriendStatus.PENDING, pageable)
                .map(FriendRequestResponse::fromSent);

    }


    @Transactional
    public void acceptFriendRequest(Long memberId,
                                    Long friendshipId) {

        Member member = memberService.findMemberById(memberId);

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        if(!friendship.getReceiver().equals(member)) {
            throw new CustomException(ErrorCode.CANNOT_PROCESS_FRIEND_REQUEST);
        }

        if(friendship.getStatus() != FriendStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_FRIENDSHIP_STATUS);
        }

        friendship.acceptFriendship();

    }


    @Transactional
    public void rejectFriendRequest(Long memberIid,
                                    Long friendshipId) {

        Member member = memberService.findMemberById(memberIid);

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        if (!friendship.getReceiver().equals(member)) {
            throw new CustomException(ErrorCode.CANNOT_PROCESS_FRIEND_REQUEST);
        }

        if (friendship.getStatus() != FriendStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_FRIENDSHIP_STATUS);
        }

        friendshipRepository.delete(friendship);

    }


    public Page<FriendResponse> getFriends(Long memberId,
                                           Pageable pageable) {

        Member member = memberService.findMemberById(memberId);

        return friendshipRepository
                .findFriendshipsByMemberAndStatus(member, FriendStatus.ACCEPTED, pageable)
                .map(friendship -> FriendResponse.from(friendship, memberId));

    }


    public void deleteFriend(Long memberId,
                             Long friendshipId) {

        Member member = memberService.findMemberById(memberId);

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        if(friendship.getStatus() != FriendStatus.ACCEPTED) {
            throw new CustomException(ErrorCode.INVALID_FRIENDSHIP_STATUS);
        }

        boolean isMember =
                friendship.getRequester().equals(member) || friendship.getReceiver().equals(member);

        if (!isMember) {
            throw new CustomException(ErrorCode.NOT_FRIEND);
        }

        friendshipRepository.delete(friendship);

    }


    @Transactional
    public void blockFriend(Long memberId,
                            Long friendshipId) {

        Member member = memberService.findMemberById(memberId);

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        boolean isMember =
                friendship.getReceiver().equals(member) || friendship.getRequester().equals(member);

        if (!isMember) {
            throw new CustomException(ErrorCode.NOT_FRIEND);
        }

        if (friendship.getStatus() == FriendStatus.BLOCKED) {
            throw new CustomException(ErrorCode.FRIEND_ALREADY_BLOCKED);
        }

        if (friendship.getStatus() != FriendStatus.ACCEPTED) {
            throw new CustomException(ErrorCode.INVALID_FRIENDSHIP_STATUS);
        }

        friendship.blockFriendship(member);

    }


    @Transactional
    public void unblockFriend(Long memberId,
                              Long friendshipId) {

        Member member = memberService.findMemberById(memberId);

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIENDSHIP_NOT_FOUND));

        if (friendship.getStatus() != FriendStatus.BLOCKED) {
            throw new CustomException(ErrorCode.INVALID_FRIENDSHIP_STATUS);
        }

        if (!friendship.getBlockedBy().equals(member)) {
            throw new CustomException(ErrorCode.CANNOT_UNBLOCK_FRIENDSHIP);
        }

        friendshipRepository.delete(friendship);

    }


    public Page<MemberSearchResponse> searchMembersByNickname(String nickname,
                                                              Pageable pageable) {

        return memberRepository
                .findByNicknameContainingIgnoreCase(nickname.trim(), pageable)
                .map(MemberSearchResponse::from);

    }


    private String createPairKey(Long memberId1,
                                 Long memberId2) {

        long smallerId = Math.min(memberId1, memberId2);
        long largerId = Math.max(memberId1, memberId2);

        return smallerId + ":" + largerId;

    }


}