package com.foryoung.foryoung.friend.dto;

import com.foryoung.foryoung.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSearchResponse {


    private Long memberId;

    private String nickname;


    public static MemberSearchResponse from(Member member) {

        return MemberSearchResponse.builder()
                .memberId(member.getId())
                .nickname(member.getDisplayNickname())
                .build();

    }


}