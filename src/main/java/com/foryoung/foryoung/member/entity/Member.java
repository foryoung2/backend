package com.foryoung.foryoung.member.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    private LocalDateTime deletedAt;


    public void withdraw() {

        this.email = null;
        this.nickname = null;
        this.status = MemberStatus.DELETED;
    }


    public boolean isDeleted() {
        return this.status == MemberStatus.DELETED;
    }


    public String getDisplayNickname() {

        return isDeleted()
                ? "탈퇴한 사용자"
                : nickname;
    }


}