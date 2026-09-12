package com.foryoung.foryoung.ocr.parser;

import com.foryoung.foryoung.ocr.constant.VenueKeywords;
import com.foryoung.foryoung.ocr.dto.PerformanceOcrResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class PerformanceOcrParser {


    private static final Pattern TITLE_PATTERN =
            Pattern.compile(">\\s*(.+?)(?=\\s*\\*\\s*예매)");

    private static final Pattern TITLE_FALLBACK_PATTERN =
            Pattern.compile(
                    "(?:^|\\n)\\s*[>ㆍ·•]\\s*(.+?)(?=\\s*\\*\\s*예매)"
            );

    private static final Pattern PERFORMANCE_DATE_TIME_PATTERN =
            Pattern.compile(
                    "(20\\d{2})\\.(\\d{1,2})\\.(\\d{1,2})"
                            + "\\([^)]*\\)"
                            + "(\\d{1,2})"
                            + "(?:[:.]?(\\d{2}))?"
                            + "\\s*(AM|PM)",
                    Pattern.CASE_INSENSITIVE
            );

    private static final Pattern KOREAN_DATE_TIME_PATTERN =
            Pattern.compile(
                    "(20\\d{2})년\\s*(\\d{1,2})월\\s*(\\d{1,2})일"
                            + "(?:\\s*\\([^)]*\\))?"
                            + "\\s*(\\d{1,2})[시:]\\s*(\\d{1,2})분?"
            );

    private static final Pattern VENUE_MAP_PATTERN =
            Pattern.compile(
                    "([^\\n|｜]{2,100})\\s*[|｜]"
            );

    private static final Pattern VENUE_LOCATION_PATTERN =
            Pattern.compile(
                    "(?:공연장|장소)\\s*[:：]?\\s*([^\\n|｜]{2,100})"
            );

    private static final Pattern SEAT_PATTERN =
            Pattern.compile(
                    "좌석\\s+(.+?)(?=\\s*(?:취소가능|취소하기|\\||」|죄|\\n))"
            );

    private static final Pattern SEAT_DETAIL_PATTERN =
            Pattern.compile(
                    "좌석번호\\s+가격\\s+취소여부\\s*"
                            + "(?:\\n|\\s)+"
                            + "\\d+\\s+"
                            + "\\S+\\s+"
                            + "\\S+\\s+"
                            + "(.+?)\\s+"
                            + "\\d{1,3}(?:,\\d{3})+원"
            );

    private static final Pattern TICKET_PRICE_PATTERN =
            Pattern.compile(
                    "좌석번호\\s+가격\\s+취소여부\\s+"
                            + ".+?"
                            + "\\s+([\\d,]+)원"
                            + "\\s+(?:취소가능|취소불가)"
            );


    public PerformanceOcrResponse parse(String rawText) {

        return PerformanceOcrResponse.builder()
                .title(extractTitle(rawText))
                .performanceDateTime(extractPerformanceDateTime(rawText))
                .venue(extractVenue(rawText))
                .ticketPrice(extractTicketPrice(rawText))
                .seat(extractSeat(rawText))
                //.rawText(rawText)
                .build();

    }


    private String extractTitle(String rawText) {

        Matcher matcher = TITLE_PATTERN.matcher(rawText);

        if (matcher.find()) {
            return cleanText(matcher.group(1));
        }

        matcher = TITLE_FALLBACK_PATTERN.matcher(rawText);

        if (matcher.find()) {
            return cleanText(matcher.group(1));
        }

        return null;

    }


    private LocalDateTime extractPerformanceDateTime(String rawText) {

        Matcher matcher = PERFORMANCE_DATE_TIME_PATTERN.matcher(rawText);

        if (matcher.find()) {

            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));

            int hour = Integer.parseInt(matcher.group(4));

            String minuteGroup = matcher.group(5);
            int minute = minuteGroup != null
                    ? Integer.parseInt(minuteGroup)
                    : 0;

            String amPm = matcher.group(6);

            if ("PM".equalsIgnoreCase(amPm) && hour != 12) {
                hour += 12;
            }

            if ("AM".equalsIgnoreCase(amPm) && hour == 12) {
                hour = 0;
            }

            return LocalDateTime.of(
                    year,
                    month,
                    day,
                    hour,
                    minute
            );
        }

        Matcher koreanMatcher =
                KOREAN_DATE_TIME_PATTERN.matcher(rawText);

        if (koreanMatcher.find()) {

            int year = Integer.parseInt(koreanMatcher.group(1));
            int month = Integer.parseInt(koreanMatcher.group(2));
            int day = Integer.parseInt(koreanMatcher.group(3));

            int hour = Integer.parseInt(koreanMatcher.group(4));
            int minute = Integer.parseInt(koreanMatcher.group(5));

            return LocalDateTime.of(
                    year,
                    month,
                    day,
                    hour,
                    minute
            );
        }

        return null;

    }


    private String extractVenue(String rawText) {

        String candidate1 = extractVenueAfterLocationKeyword(rawText);

        String candidate2 = extractVenueFromMapPattern(rawText);

        String candidate3 = extractVenueFromStartKeyword(rawText);

        List<String> candidates = Stream.of(candidate1, candidate2, candidate3)
                .filter(Objects::nonNull)
                .map(this::cleanVenue)
                .filter(this::isValidVenue)
                .distinct()
                .toList();

        if (candidates.isEmpty()) {
            return null;
        }

        return candidates.stream()
                .max(Comparator.comparingInt(this::venueScore))
                .orElse(null);

    }


    private Integer extractTicketPrice(String rawText) {

        String normalizedText = rawText
                .replaceAll("\\s+", " ")
                .trim();

        Matcher matcher = TICKET_PRICE_PATTERN.matcher(normalizedText);

        if (matcher.find()) {

            return Integer.parseInt(
                    matcher.group(1).replace(",", "")
            );
        }

        return null;

    }


    private String extractSeat(String rawText) {

        Matcher matcher1 = SEAT_PATTERN.matcher(rawText);

        if (matcher1.find()) {

            String seat = cleanSeat(matcher1.group(1));

            if (seat != null && !seat.isBlank()) {
                return seat;
            }
        }

        Matcher matcher2 = SEAT_DETAIL_PATTERN.matcher(rawText);

        if (matcher2.find()) {
            return cleanSeat(matcher2.group(1));
        }

        return null;

    }


    private String extractVenueFromMapPattern(String rawText) {

        Matcher matcher = VENUE_MAP_PATTERN.matcher(rawText);

        String bestCandidate = null;

        while (matcher.find()) {

            String candidate = cleanVenue(matcher.group(1));

            if (!isValidVenue(candidate)) {
                continue;
            }

            if (bestCandidate == null || venueScore(candidate) > venueScore(bestCandidate)) {
                bestCandidate = candidate;
            }
        }

        return bestCandidate;

    }


    private String extractVenueAfterLocationKeyword(String rawText) {

        Matcher matcher = VENUE_LOCATION_PATTERN.matcher(rawText);

        if (matcher.find()) {
            return cleanVenue(matcher.group(1));
        }

        return null;

    }


    private String extractVenueFromStartKeyword(String rawText) {

        String bestCandidate = null;

        for (String keyword : VenueKeywords.VENUE_START_KEYWORDS) {

            Pattern pattern = Pattern.compile(Pattern.quote(keyword) + "[^\\n|｜]{0,50}");

            Matcher matcher = pattern.matcher(rawText);

            while (matcher.find()) {

                String candidate = cleanVenue(matcher.group());

                if (!isValidVenue(candidate)) {
                    continue;
                }

                if (bestCandidate == null || venueScore(candidate) > venueScore(bestCandidate)) {

                    bestCandidate = candidate;
                }
            }
        }

        return bestCandidate;

    }


    private int venueScore(String venue) {

        int score = 0;

        if (venue.matches(".*[가-힣].*")) {
            score += 10;
        }

        for (String keyword : VenueKeywords.VENUE_KEYWORDS) {

            if (containsIgnoreCase(venue, keyword)) {
                score += 10;
            }
        }

        for (String keyword : VenueKeywords.VENUE_START_KEYWORDS) {

            if (containsIgnoreCase(venue, keyword)) {
                score += 20;
            }
        }

        score += Math.min(venue.length(), 20);

        return score;

    }


    private boolean isValidVenue(String venue) {

        if (venue == null || venue.isBlank()) {
            return false;
        }

        String cleaned = venue.trim();

        if (cleaned.length() < 2) {
            return false;
        }

        if (cleaned.matches("\\d+")) {
            return false;
        }

        if (cleaned.contains("예약번호")
                || cleaned.contains("연락처")
                || cleaned.contains("배송")
                || cleaned.contains("예매")
                || cleaned.contains("가격")) {

            return false;
        }

        return VenueKeywords.VENUE_KEYWORDS.stream()
                .anyMatch(keyword ->
                        cleaned.toLowerCase()
                                .contains(keyword.toLowerCase())
                );

    }


    private String cleanVenue(String venue) {

        if (venue == null) {
            return null;
        }

        String cleaned = venue
                .replaceAll("[|｜].*$", "")
                .replaceAll("\\s+", " ")
                .trim();

        String lowerCase = cleaned.toLowerCase();

        int bestStartIndex = -1;

        for (String keyword : VenueKeywords.VENUE_START_KEYWORDS) {

            int index = lowerCase.indexOf(
                    keyword.toLowerCase(Locale.ROOT)
            );

            if (index == -1) {
                continue;
            }

            if (bestStartIndex == -1 || index < bestStartIndex) {
                bestStartIndex = index;
            }
        }

        if (bestStartIndex != -1) {

            return cleaned
                    .substring(bestStartIndex)
                    .trim();
        }

        return cleaned;

    }


    private String cleanSeat(String seat) {

        if (seat == null) {
            return null;
        }

        return seat
                .replaceAll("\\s*[|｜].*$", "")
                .replaceAll("\\s*」.*$", "")
                .replaceAll("\\s*죄.*$", "")
                .replaceAll("(\\d+)\\s*[^가-힣A-Za-z0-9\\s]*$", "$1")
                .replaceAll("\\s+", " ")
                .trim();

    }


    private String cleanText(String text) {

        if (text == null) {
            return null;
        }

        return text
                .replaceAll("\\s+", " ")
                .trim();

    }


    private boolean containsIgnoreCase(String text, String keyword) {

        return text.toLowerCase(Locale.ROOT)
                .contains(keyword.toLowerCase(Locale.ROOT));

    }


}