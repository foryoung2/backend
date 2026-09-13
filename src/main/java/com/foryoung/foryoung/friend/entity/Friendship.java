package com.foryoung.foryoung.friend.entity;

import com.foryoung.foryoung.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_friendship_pair",
                        columnNames = "pair_key"
                )
        }
)
public class Friendship {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_by_id")
    private Member blockedBy;

    @Column(name = "pair_key", nullable = false, unique = true)
    private String pairKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status;


    public void acceptFriendship() {
        this.status = FriendStatus.ACCEPTED;
    }


    public void blockFriendship(Member member) {
        this.status = FriendStatus.BLOCKED;
        this.blockedBy = member;
    }


}