package com.foryoung.foryoung.performance.entity;

import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.review.entity.PerformanceReview;
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
                        name = "uk_performance_record_member_schedule",
                        columnNames = {"member_id", "schedule_id"}
                )
        }
)
public class PerformanceRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private PerformanceSchedule schedule;

    @OneToOne(
            mappedBy = "performanceRecord",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private PerformanceReview review;

    private Integer ticketPrice;

    private String seat;


    public void updateRecord(Integer ticketPrice,
                             String seat) {

        if (ticketPrice != null) {
            this.ticketPrice = ticketPrice;
        }

        if (seat != null) {
            this.seat = seat;
        }

    }


}