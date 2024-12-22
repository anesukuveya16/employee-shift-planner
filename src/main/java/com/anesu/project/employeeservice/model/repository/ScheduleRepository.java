package com.anesu.project.employeeservice.model.repository;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  /**
   * Finds schedules for a specific employee within a date range.
   *
   * @param employeeId the ID of the employee
   * @param startDate the start of the date range
   * @param endDate the end of the date range
   * @return a list of matching schedules
   */
  List<Schedule> findByEmployeeIdAndDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate);
}
