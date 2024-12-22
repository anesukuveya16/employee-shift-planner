package com.anesu.project.employeeservice.model;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface for managing employee schedules.
 *
 * <p>This service interface provides the structure for managing employee schedules, including
 * creating, updating, and retrieving schedules, and integrating vacation and shift requests.
 *
 * @author Anesu
 * @version 1.0
 * @since 2024-12-18
 */
public interface SchedulingService {

  /**
   * Creates or updates an employee's schedule for a specific date.
   *
   * @param schedule the schedule to create or update
   * @return the created or updated schedule
   */
  Schedule createSchedule(Schedule schedule);

  /*
  * Makes updates to an existing schedule...e.g, an employee calls in sick then the
  *   approved shift/schedule should be updated to reflect this
  */
  Schedule updateSchedule(Long scheduleId, Schedule schedule);

  /**
   * Retrieves an employee's schedule within a specific date range.
   *
   * @param employeeId the ID of the employee
   * @param startDate the start date of the range
   * @param endDate the end date of the range
   * @return a list of schedules within the specified date range
   */
  List<Schedule> getSchedulesByEmployeeAndDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate);

  /**
   * Retrieves schedules for a specific office location and date.
   *
   * @param officeLocationId the ID of the office location
   * @param date the specific date
   * @return a list of schedules for the specified office location and date
   */
  List<Schedule> getSchedulesByLocationAndDate(Long officeLocationId, LocalDate date);

  List<Schedule> getSchedulesByEmployeeId(Long employeeId);

  /**
   * Integrates approved vacation requests into the schedule by marking affected days as
   * unavailable.
   *
   * @param vacationRequestId the ID of the approved vacation request
   * @return a list of updated schedules reflecting the vacation
   */
  List<Schedule> integrateVacationRequest(Long vacationRequestId);

  /**
   * Integrates approved shift requests into the schedule.
   *
   * @param shiftRequestId the ID of the approved shift request
   * @return the updated schedule reflecting the shift
   */
  Schedule integrateShiftRequest(Long shiftRequestId);

  /**
   * Deletes an employee's schedule for a specific date.
   *
   * @param employeeId the ID of the employee
   * @param date the date of the schedule to delete
   * @return true if the schedule was deleted, false otherwise
   */
  boolean deleteSchedule(Long employeeId, LocalDate date);
}
