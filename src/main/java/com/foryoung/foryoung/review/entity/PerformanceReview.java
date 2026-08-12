package com.foryoung.foryoung.review.entity;

import com.foryoung.foryoung.performance.entity.PerformanceRecord;
import com.foryoung.foryoung.review.dto.PerformanceReviewUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PerformanceReview {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performance_record_id", nullable = false)
    private PerformanceRecord performanceRecord;

    @OneToMany(
            mappedBy = "performanceReview",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ReviewLike> likes = new ArrayList<>();

    @OneToMany(
            mappedBy = "performanceReview",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ReviewComment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean publicReview;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public void updateReview(String title,
                             String content,
                             Boolean publicReview) {

        if (title != null) {
            this.title = title;
        }

        if (content != null) {
            this.content = content;
        }

        if (publicReview != null) {
            this.publicReview = publicReview;
        }

    }


}