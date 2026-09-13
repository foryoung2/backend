package com.foryoung.foryoung.performance.setlist.entity;

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
                        name = "uk_song_artist_title",
                        columnNames = {"normalized_artist", "normalized_title"}
                )
        }
)
public class Song {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column(name = "normalized_artist", nullable = false)
    private String normalizedArtist;

    @Column(name = "normalized_title", nullable = false)
    private String normalizedTitle;


}