package com.foryoung.foryoung.friend.dto;

import com.foryoung.foryoung.friend.entity.Friendship;
import com.foryoung.foryoung.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendResponse {


    private Long friendshipId;

    private Long memberId;

    private String nickname;


    public static FriendResponse from(Friendship friendship,
                                      Long currentMemberId) {

        Member friend;

        if (friendship.getRequester().getId().equals(currentMemberId)) {
            friend = friendship.getReceiver();
        } else {
            friend = friendship.getRequester();
        }

        return FriendResponse.builder()
                .friendshipId(friendship.getId())
                .memberId(friend.getId())
                .nickname(friend.getDisplayNickname())
                .build();

    }


}