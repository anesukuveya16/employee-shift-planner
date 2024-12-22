package com.anesu.project.employeeservice.model;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import java.time.LocalDate;
import java.util.List;

/** Service interface for managing employee schedules. */
public interface ScheduleService {

  /**
   * Creates a new schedule for an employee.
   *
   * @param schedule the schedule to be created
   * @return the created schedule
   */
  Schedule createSchedule(Schedule schedule);

  /**
   * Updates an existing schedule.
   *
   * @param scheduleId the ID of the schedule to update
   * @param updatedSchedule the updated schedule data
   * @return the updated schedule
   */
  Schedule updateSchedule(Long scheduleId, Schedule updatedSchedule);

  /**
   * Retrieves a schedule by its ID.
   *
   * @param scheduleId the ID of the schedule to retrieve
   * @return the found schedule, or null if not found
   */
  Schedule getScheduleById(Long scheduleId);

  /**
   * Retrieves schedules for a specific employee within a date range.
   *
   * @param employeeId the ID of the employee
   * @param startDate the start of the date range
   * @param endDate the end of the date range
   * @return a list of matching schedules
   */
  List<Schedule> getSchedulesByEmployeeAndDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate);

  /**
   * Deletes a schedule by its ID.
   *
   * @param scheduleId the ID of the schedule to delete
   */
  void deleteSchedule(Long scheduleId);
}
