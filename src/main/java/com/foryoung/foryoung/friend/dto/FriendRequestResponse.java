package com.foryoung.foryoung.friend.dto;

import com.foryoung.foryoung.friend.entity.FriendStatus;
import com.foryoung.foryoung.friend.entity.Friendship;
import com.foryoung.foryoung.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendRequestResponse {


    private Long friendshipId;

    private Long memberId;

    private String nickname;

    private FriendStatus status;


    public static FriendRequestResponse fromReceived(Friendship friendship) {

        Member requester = friendship.getRequester();

        return FriendRequestResponse.builder()
                .friendshipId(friendship.getId())
                .memberId(requester.getId())
                .nickname(requester.getDisplayNickname())
                .status(friendship.getStatus())
                .build();

    }


    public static FriendRequestResponse fromSent(Friendship friendship) {

        Member receiver = friendship.getReceiver();

        return FriendRequestResponse.builder()
                .friendshipId(friendship.getId())
                .memberId(receiver.getId())
                .nickname(receiver.getDisplayNickname())
                .status(friendship.getStatus())
                .build();
    }


}