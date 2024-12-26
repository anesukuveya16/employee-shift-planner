package com.anesu.project.employeeservice.model;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import com.anesu.project.employeeservice.entity.shift.ShiftRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing employee schedules.
 */
public interface ScheduleService {

  /**
   * Creates a new {@link Schedule} for an employee.
   *
   * @param schedule the {@link Schedule} to be created
   * @return the created {@link Schedule}
   */
  Schedule createSchedule(Schedule schedule);

  /**
   * Updates an existing {@link Schedule} that was already approved.
   *
   * @param scheduleId the ID of the {@link Schedule} to update
   * @param updatedSchedule the updated {@link Schedule} data
   * @return the updated {@link Schedule}
   */
  Schedule updateSchedule(Long scheduleId, Schedule updatedSchedule);

  /**
   * Updates the {@link Schedule} to add a new shift entry from the just approved {@link ShiftRequest}.
   *
   * @param approvedShiftRequest the approved {@link ShiftRequest}
   * @return the updated {@link Schedule}
   */
  Schedule updateSchedule(ShiftRequest approvedShiftRequest);

  /**
   * Retrieves a {@link Schedule} by its ID.
   *
   * @param scheduleId the ID of the {@link Schedule} to retrieve
   * @return the found {@link Schedule}, or {@code null} if not found
   */
  Schedule getScheduleById(Long scheduleId);

  /**
   * Retrieves schedules for a specific employee within a date range.
   *
   * @param employeeId the ID of the employee
   * @param startDate the start of the {@link LocalDate} range
   * @param endDate the end of the {@link LocalDate} range
   * @return a list of matching {@link Schedule}s
   */
  List<Schedule> getSchedulesByEmployeeAndDateRange(
          Long employeeId, LocalDate startDate, LocalDate endDate);

  /**
   * Deletes a {@link Schedule} by its ID.
   *
   * @param scheduleId the ID of the {@link Schedule} to delete
   */
  void deleteSchedule(Long scheduleId);
}
