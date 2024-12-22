package com.anesu.project.employeeservice.model.repository;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

  List<Schedule> findSchedulesByEmployeeAndDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate);

  List<Schedule> findSchedulesByLocationAndDate(Long officeLocationId, LocalDate date);

  List<Schedule> integrateVacationRequest(Long vacationRequestId);

  List<Schedule> findScheduleByEmployeeId(Long employeeId);

  Schedule integrateShiftRequest(Long shiftRequestId);

  int deleteByEmployeeAndDate(Long employeeId, LocalDate date);
}
