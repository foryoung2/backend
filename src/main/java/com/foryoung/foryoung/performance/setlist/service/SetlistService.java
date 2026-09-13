package com.foryoung.foryoung.performance.setlist.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.performance.schedule.entity.PerformanceSchedule;
import com.foryoung.foryoung.performance.schedule.repository.PerformanceScheduleRepository;
import com.foryoung.foryoung.performance.setlist.dto.SetlistCreateRequest;
import com.foryoung.foryoung.performance.setlist.dto.SetlistResponse;
import com.foryoung.foryoung.performance.setlist.dto.SetlistUpdateRequest;
import com.foryoung.foryoung.performance.setlist.entity.Setlist;
import com.foryoung.foryoung.performance.setlist.entity.Song;
import com.foryoung.foryoung.performance.setlist.repository.SetlistRepository;
import com.foryoung.foryoung.performance.setlist.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SetlistService {


    private final PerformanceScheduleRepository scheduleRepository;
    private final SetlistRepository setlistRepository;
    private final SongRepository songRepository;


    @Transactional
    public SetlistResponse createSetlist(Long scheduleId,
                                         SetlistCreateRequest request) {

        PerformanceSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_SCHEDULE_NOT_FOUND));

        String title = request.getTitle().trim();
        String artist = request.getArtist().trim();

        String normalizedTitle = normalize(title);
        String normalizedArtist = normalize(artist);

        Song song = songRepository.findByNormalizedArtistAndNormalizedTitle(normalizedArtist, normalizedTitle)
                .orElseGet(() ->
                        songRepository.save(
                                Song.builder()
                                        .title(title)
                                        .artist(artist)
                                        .normalizedTitle(normalizedTitle)
                                        .normalizedArtist(normalizedArtist)
                                        .build()
                        )
                );

        Integer maxTrackNumber = setlistRepository.findMaxTrackNumber(schedule);

        Integer trackNumber = maxTrackNumber + 1;
        Setlist setlist = Setlist.builder()
                .performanceSchedule(schedule)
                .song(song)
                .trackNumber(trackNumber)
                .build();

        Setlist savedSetlist = setlistRepository.save(setlist);

        return SetlistResponse.from(savedSetlist);

    }


    @Transactional
    public SetlistResponse updateSetlist(Long scheduleId,
                                         Long setlistId,
                                         SetlistUpdateRequest request) {

        PerformanceSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_SCHEDULE_NOT_FOUND));

        Setlist setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new CustomException(ErrorCode.SETLIST_NOT_FOUND));

        if(!setlist.getPerformanceSchedule().getId().equals(schedule.getId())) {
            throw new CustomException(ErrorCode.SETLIST_NOT_FOUND);
        }

        String title = request.getTitle().trim();
        String artist = request.getArtist().trim();

        String normalizedTitle = normalize(title);
        String normalizedArtist = normalize(artist);

        Song song = songRepository.findByNormalizedArtistAndNormalizedTitle(normalizedArtist, normalizedTitle)
                .orElseGet(() -> songRepository.save(
                        Song.builder()
                                .title(title)
                                .artist(artist)
                                .normalizedTitle(normalizedTitle)
                                .normalizedArtist(normalizedArtist)
                                .build()
                ));

        setlist.updateSong(song);

        return SetlistResponse.from(setlist);

    }


    @Transactional
    public void reorderSetlists(Long scheduleId,
                                List<Long> setlistIds) {

        PerformanceSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_SCHEDULE_NOT_FOUND));

        List<Setlist> setlists = setlistRepository
                .findByPerformanceScheduleOrderByTrackNumberAsc(schedule);

        if (setlists.size() != setlistIds.size()) {
            throw new CustomException(ErrorCode.INVALID_SETLIST_ORDER);
        }

        Map<Long, Setlist> setlistMap = setlists.stream()
                .collect(Collectors.toMap(
                        Setlist::getId,
                        setlist -> setlist)
                );

        if (setlistMap.size() != setlistIds.size()) {
            throw new CustomException(ErrorCode.INVALID_SETLIST_ORDER);
        }

        for (Long setlistId : setlistIds) {
            if (!setlistMap.containsKey(setlistId)) {
                throw new CustomException(ErrorCode.INVALID_SETLIST_ORDER);
            }
        }

        for (int i = 0; i < setlistIds.size(); i++) {
            Setlist setlist = setlistMap.get(setlistIds.get(i));
            setlist.updateTrackNumber(-(i + 1));
        }

        setlistRepository.flush();

        for (int i = 0; i < setlistIds.size(); i++) {
            Setlist setlist = setlistMap.get(setlistIds.get(i));
            setlist.updateTrackNumber(i + 1);
        }

    }



    public List<SetlistResponse> getSetlists(Long scheduleId) {

        PerformanceSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_SCHEDULE_NOT_FOUND));

        return setlistRepository
                .findByPerformanceScheduleOrderByTrackNumberAsc(schedule)
                .stream()
                .map(SetlistResponse::from)
                .toList();

    }


    @Transactional
    public void deleteSetlist(Long scheduleId, Long setlistId) {

        PerformanceSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_SCHEDULE_NOT_FOUND));

        Setlist setlist = setlistRepository.findById(setlistId)
                .orElseThrow(() -> new CustomException(ErrorCode.SETLIST_NOT_FOUND));

        if(!setlist.getPerformanceSchedule().getId().equals(schedule.getId())) {
            throw new CustomException(ErrorCode.SETLIST_NOT_FOUND);
        }

        Integer deletedTrackNumber = setlist.getTrackNumber();

        setlistRepository.delete(setlist);

        setlistRepository.decreaseTrackNumbers(schedule, deletedTrackNumber);

    }


    private String normalize(String value) {

        return value
                .replaceAll("\\s+", "")
                .toLowerCase();

    }


}