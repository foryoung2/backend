package com.foryoung.foryoung.performance.repository;

import com.foryoung.foryoung.performance.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

}