package com.foryoung.foryoung.performance.setlist.dto;

import com.foryoung.foryoung.performance.setlist.entity.Setlist;
import com.foryoung.foryoung.performance.setlist.entity.Song;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SetlistResponse {


    private Long id;

    private Integer trackNumber;

    private Long songId;

    private String title;

    private String artist;


    public static SetlistResponse from(Setlist setlist) {

        Song song = setlist.getSong();

        return SetlistResponse.builder()
                .id(setlist.getId())
                .trackNumber(setlist.getTrackNumber())
                .songId(song.getId())
                .title(song.getTitle())
                .artist(song.getArtist())
                .build();

    }


}