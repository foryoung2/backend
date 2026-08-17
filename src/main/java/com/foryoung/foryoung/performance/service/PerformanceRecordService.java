package com.foryoung.foryoung.performance.service;

import com.foryoung.foryoung.global.exception.CustomException;
import com.foryoung.foryoung.global.exception.ErrorCode;
import com.foryoung.foryoung.member.entity.Member;
import com.foryoung.foryoung.member.service.MemberService;
import com.foryoung.foryoung.performance.dto.PerformanceRecordCreateRequest;
import com.foryoung.foryoung.performance.dto.PerformanceRecordResponse;
import com.foryoung.foryoung.performance.dto.PerformanceRecordUpdateRequest;
import com.foryoung.foryoung.performance.entity.Performance;
import com.foryoung.foryoung.performance.entity.PerformanceRecord;
import com.foryoung.foryoung.performance.entity.PerformanceSchedule;
import com.foryoung.foryoung.performance.mapper.PerformanceRecordMapper;
import com.foryoung.foryoung.performance.repository.PerformanceRecordRepository;
import com.foryoung.foryoung.performance.repository.PerformanceScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceRecordService {


    private final MemberService memberService;

    private final PerformanceScheduleRepository scheduleRepository;
    private final PerformanceRecordRepository recordRepository;

    private final PerformanceRecordMapper recordMapper;


    @Transactional
    public PerformanceRecordResponse createRecord(Long memberId,
                                                  PerformanceRecordCreateRequest request) {

        Member member = memberService.findMemberById(memberId);

        PerformanceSchedule schedule =
                scheduleRepository.findById(request.getScheduleId())
                        .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_SCHEDULE_NOT_FOUND));

        PerformanceRecord record = PerformanceRecord.builder()
                .member(member)
                .schedule(schedule)
                .ticketPrice(request.getTicketPrice())
                .seat(request.getSeat())
                .build();

        PerformanceRecord savedRecord = recordRepository.save(record);

        Performance performance = schedule.getPerformance();

        return PerformanceRecordResponse.builder()
                .id(savedRecord.getId())
                .performanceId(performance.getId())
                .performanceTitle(performance.getTitle())
                .artist(performance.getArtist())
                .venue(performance.getVenue().getName())
                .scheduleId(schedule.getId())
                .performanceDateTime(schedule.getPerformanceDateTime())
                .ticketPrice(savedRecord.getTicketPrice())
                .seat(savedRecord.getSeat())
                .build();

    }


    @Transactional
    public void updateRecord(Long memberId,
                             Long recordId,
                             PerformanceRecordUpdateRequest request) {

        PerformanceRecord record = findMyRecordEntity(memberId, recordId);

        record.updateRecord(request.getTicketPrice(), request.getSeat());

    }


    public List<PerformanceRecordResponse> getMyRecords(Long memberId) {

        return recordRepository
                .findByMember_IdOrderBySchedule_PerformanceDateTimeDesc(memberId)
                .stream()
                .map(recordMapper::toPerformanceRecordResponse)
                .toList();

    }


    public PerformanceRecordResponse getMyRecord(Long memberId,
                                                 Long recordId) {

        PerformanceRecord record = findMyRecordEntity(memberId, recordId);

        return recordMapper.toPerformanceRecordResponse(record);

    }


    @Transactional
    public void deleteRecord(Long memberId,
                             Long recordId) {

        PerformanceRecord record = findMyRecordEntity(memberId, recordId);

        recordRepository.delete(record);

    }


    private PerformanceRecord findMyRecordEntity(Long memberId,
                                                 Long recordId) {

        return recordRepository
                .findByIdAndMember_Id(recordId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERFORMANCE_RECORD_NOT_FOUND));

    }


}