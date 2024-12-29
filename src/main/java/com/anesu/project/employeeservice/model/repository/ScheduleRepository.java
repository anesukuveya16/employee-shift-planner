package com.anesu.project.employeeservice.model.repository;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
  Optional<List<Schedule>> findByEmployeeIdAndDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate);

  /**
   * Finds schedules for a specific employee within a given calendar week.
   *
   * @param employeeId the ID of the employee
   * @param startOfCalenderWeek the start of the date range
   * @param endOfCalenderWeek the end of the date range
   * @return a matching schedule.
   */
  Optional<Schedule> findByEmployeeIdAndCalendarWeek(
      Long employeeId, LocalDate startOfCalenderWeek, LocalDate endOfCalenderWeek);
}
