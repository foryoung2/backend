package com.foryoung.foryoung.member.repository;

import com.foryoung.foryoung.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByEmail(String email);


    Page<Member> findByNicknameContainingIgnoreCase(
            String nickname,
            Pageable pageable);


    boolean existsByNicknameAndIdNot(String nickname, Long memberId);


}