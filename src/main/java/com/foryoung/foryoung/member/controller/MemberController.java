package com.foryoung.foryoung.member.controller;

import com.foryoung.foryoung.auth.dto.JwtTokenResponse;
import com.foryoung.foryoung.auth.userdetails.CustomUserDetails;
import com.foryoung.foryoung.member.dto.NicknameUpdateRequest;
import com.foryoung.foryoung.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {


    private final MemberService memberService;


    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> login(@RequestParam String code) {

        return ResponseEntity.ok(memberService.login(code));

    }


    @PatchMapping("/me/nickname")
    public ResponseEntity<Void> updateNickname(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @Valid @RequestBody NicknameUpdateRequest request ) {

        memberService.updateNickname(userDetails.getMemberId(), request);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/me")
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {

        memberService.withdraw(userDetails.getMemberId());

        return ResponseEntity.noContent().build();

    }


}