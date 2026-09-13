package com.foryoung.foryoung.friend.repository;

import com.foryoung.foryoung.friend.entity.FriendStatus;
import com.foryoung.foryoung.friend.entity.Friendship;
import com.foryoung.foryoung.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {


    boolean existsByPairKey(String pairKey);


    Page<Friendship> findByReceiverAndStatus(
            Member receiver,
            FriendStatus status,
            Pageable pageable
    );


    Page<Friendship> findByRequesterAndStatus(
            Member requester,
            FriendStatus status,
            Pageable pageable
    );


    @Query("""
            SELECT f
            FROM Friendship f
            WHERE f.status = :status
              AND (f.requester = :member OR f.receiver = :member)
            """)
    Page<Friendship> findFriendshipsByMemberAndStatus(
            @Param("member") Member member,
            @Param("status") FriendStatus status,
            Pageable pageable
    );


}