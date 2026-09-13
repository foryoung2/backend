package com.foryoung.foryoung.performance.setlist.entity;

import com.foryoung.foryoung.performance.schedule.entity.PerformanceSchedule;
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
                        name = "uk_schedule_track_number",
                        columnNames = {
                                "performance_schedule_id",
                                "track_number"
                        }
                )
        }
)
public class Setlist {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "performance_schedule_id",
            nullable = false
    )
    private PerformanceSchedule performanceSchedule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(nullable = false)
    private Integer trackNumber;


    public void updateSong(Song song) {
        this.song = song;
    }


    public void updateTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }


}