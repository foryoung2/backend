package com.foryoung.foryoung.venue.entity;

import com.foryoung.foryoung.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VenueSeatView {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @OneToMany(
            mappedBy = "venueSeatView",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<VenueSeatViewImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String seatInfo;

    @Column(length = 1000)
    private String content;


    public void addImage(String imageUrl, int displayOrder) {

        VenueSeatViewImage image =
                new VenueSeatViewImage(this, imageUrl, displayOrder);

        images.add(image);

    }


}