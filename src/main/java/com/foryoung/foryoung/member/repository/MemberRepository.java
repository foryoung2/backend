package com.foryoung.foryoung.member.repository;

import com.foryoung.foryoung.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByEmail(String email);


    List<Member> findByNicknameContainingIgnoreCase(String nickname);


    boolean existsByNicknameAndIdNot(String nickname, Long memberId);


}