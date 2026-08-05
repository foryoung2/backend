package com.foryoung.foryoung.member.service;

import com.foryoung.foryoung.auth.dto.JwtTokenResponse;
import com.foryoung.foryoung.global.jwt.JwtTokenProvider;
import com.foryoung.foryoung.auth.service.TokenService;
import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.member.entity.MemberStatus;
import com.foryoung.foryoung.member.entity.Role;
import com.foryoung.foryoung.member.repository.MemberRepository;
import com.foryoung.foryoung.oauth.dto.KakaoTokenResponse;
import com.foryoung.foryoung.oauth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;
    private final TokenService tokenService;


    @Transactional
    public JwtTokenResponse login(String code) {

        KakaoTokenResponse kakaoToken = kakaoService.requestKakaoAccessToken(code);

        String email = kakaoService.requestUserEmail(kakaoToken.getAccessToken());

        Member member = findOrCreateMember(email);

        if (member.isDeleted()) {
            throw new IllegalStateException("Deleted account cannot login");
        }

        JwtTokenResponse response = jwtTokenProvider.generateToken(member);

        tokenService.saveRefreshToken(member, response);

        return response;
    }


    @Transactional
    public void withdraw(Long memberId) {

        Member member = findMemberById(memberId);

        tokenService.deleteRefreshToken(member.getEmail());

        member.withdraw();

    }


    public Member findMemberById(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));

        if (member.isDeleted()) {
            throw new IllegalStateException("Deleted member");
        }

        return member;
    }


    private Member findOrCreateMember(String email) {

        return memberRepository.findByEmail(email)
                .orElseGet(() -> createMember(email));
    }


    private Member createMember(String email) {

        Member member = Member.builder()
                .email(email)
                .nickname("user_" + UUID.randomUUID().toString().substring(0,8))
                .role(Role.ROLE_USER)
                .status(MemberStatus.ACTIVE)
                .build();

        return memberRepository.save(member);
    }


}