package com.foryoung.foryoung.auth.userdetails;

import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {


    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        if (member.isDeleted()) {
            throw new UsernameNotFoundException("Deleted member");
        }

        return new CustomUserDetails(member.getId(), member.getEmail(), member.getRole());
    }


}