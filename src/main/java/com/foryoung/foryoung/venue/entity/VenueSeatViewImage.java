package com.foryoung.foryoung.venue.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VenueSeatViewImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venue_view_id", nullable = false)
    private VenueSeatView venueSeatView;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int displayOrder;


    public VenueSeatViewImage(VenueSeatView venueSeatView,
                              String imageUrl,
                              int displayOrder) {

        this.venueSeatView = venueSeatView;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;

    }


}