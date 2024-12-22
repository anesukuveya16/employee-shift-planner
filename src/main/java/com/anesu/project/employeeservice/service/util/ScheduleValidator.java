package com.anesu.project.employeeservice.service.util;

import com.anesu.project.employeeservice.entity.schedule.Schedule;

public class ScheduleValidator {

  public void validateSchedule(Schedule schedule) {}

  //  public class SchedulingValidator {
  //
  //    private static final int MAX_WEEKLY_WORKING_HOURS = 40;
  //
  //    // Validate that the schedule's date range is valid
  //    public void validateDateRange(Schedule schedule) {
  //      if (schedule.getStartDate().isAfter(schedule.getEndDate())) {
  //        throw new ValidationException("Start date cannot be after end date.");
  //      }
  //    }
  //
  //    // Validate that the start date is not in the past
  //    public void validateStartDate(Schedule schedule) {
  //      if (schedule.getStartDate().isBefore(LocalDate.now())) {
  //        throw new ValidationException("Start date cannot be in the past.");
  //      }
  //    }
  //
  //    // Validate that there are no overlapping schedules for the employee
  //    public void validateNoOverlap(Schedule schedule, List<Schedule> existingSchedules) {
  //      boolean overlaps = existingSchedules.stream()
  //              .anyMatch(existingSchedule ->
  //                      !existingSchedule.getEndDate().isBefore(schedule.getStartDate()) &&
  //                              !existingSchedule.getStartDate().isAfter(schedule.getEndDate()));
  //      if (overlaps) {
  //        throw new ValidationException("Schedule overlaps with an existing schedule for employee
  // ID: " + schedule.getEmployeeId());
  //      }
  //    }
  //
  //    // Validate working hours are within legal limits
  //    public void validateWorkingHours(Schedule schedule) {
  //      if (schedule.getWorkingHours() > MAX_WEEKLY_WORKING_HOURS) {
  //        throw new ValidationException("Working hours cannot exceed " + MAX_WEEKLY_WORKING_HOURS
  // + " hours per week.");
  //      }
  //    }
  //
  //    // Validate that days off are within the schedule's date range
  //    public void validateDaysOff(Schedule schedule) {
  //      if (!schedule.getDaysOff().isEmpty()) {
  //        for (LocalDate dayOff : schedule.getDaysOff()) {
  //          if (dayOff.isBefore(schedule.getStartDate()) || dayOff.isAfter(schedule.getEndDate()))
  // {
  //            throw new ValidationException("Day off " + dayOff + " is outside the schedule's date
  // range.");
  //          }
  //        }
  //      }
  //    }
  //
  //    // Validate shift type
  //    public void validateShiftType(Schedule schedule) {
  //      List<String> validShiftTypes = List.of("morning", "afternoon", "night");
  //      if (!validShiftTypes.contains(schedule.getShiftType().toLowerCase())) {
  //        throw new ValidationException("Invalid shift type: " + schedule.getShiftType() + ".
  // Valid types are morning, afternoon, or night.");
  //      }
  //    }
  //
  //    // Perform all validations for a schedule
  //    public void validateSchedule(Schedule schedule, List<Schedule> existingSchedules) {
  //      validateDateRange(schedule);
  //      validateStartDate(schedule);
  //      validateNoOverlap(schedule, existingSchedules);
  //      validateWorkingHours(schedule);
  //      validateDaysOff(schedule);
  //      validateShiftType(schedule);
  //    }
  //  }

}
