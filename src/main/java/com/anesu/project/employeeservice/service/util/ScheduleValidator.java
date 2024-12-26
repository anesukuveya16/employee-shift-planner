package com.anesu.project.employeeservice.service.util;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import com.anesu.project.employeeservice.entity.shift.ShiftEntry;
import com.anesu.project.employeeservice.service.exception.InvalidScheduleException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ScheduleValidator {

  private static final int MAX_WORKING_HOURS_PER_SHIFT = 8;
  private static final int MAX_WORKING_HOURS_PER_WEEK = 40;

  public void validate(Schedule schedule) throws InvalidScheduleException {
    validateDates(schedule);
    validateWorkingHours(schedule);
  }

  private void validateDates(Schedule schedule) throws InvalidScheduleException {
    if (schedule.getStartDate() == null || schedule.getEndDate() == null) {
      throw new InvalidScheduleException("Start date or end date is null");
    }
    if (schedule.getStartDate().isAfter(schedule.getEndDate())) {
      throw new InvalidScheduleException("Start date cannot be after end date");
    }
    if (schedule.getShifts() == null && schedule.getVacations() == null) {
      throw new InvalidScheduleException("Shifts or vacations are null");
    }
  }

  private void validateWorkingHours(Schedule schedule) throws InvalidScheduleException {
    // Validate working hours per shift
    for (ShiftEntry shift : schedule.getShifts()) {
      if (shift.getWorkingHours() > MAX_WORKING_HOURS_PER_SHIFT) {
        throw new InvalidScheduleException("Shift exceeds maximum working hours");
      }
    }

    // Validate working hours per week
    Map<LocalDate, Long> dailyWorkingHours =
        schedule.getShifts().stream()
            .collect(
                Collectors.groupingBy(
                    ShiftEntry::getShiftDate, Collectors.summingLong(ShiftEntry::getWorkingHours)));

    for (LocalDate date : dailyWorkingHours.keySet()) {
      long weeklyHours = calculateWeeklyHours(dailyWorkingHours, date);
      if (weeklyHours > MAX_WORKING_HOURS_PER_WEEK) {
        throw new InvalidScheduleException("Weekly working hours exceed maximum limit");
      }
    }
  }

  private long calculateWeeklyHours(Map<LocalDate, Long> dailyWorkingHours, LocalDate date) {
    LocalDate weekStart = date.with(DayOfWeek.MONDAY);
    LocalDate weekEnd = date.with(DayOfWeek.SUNDAY);

    return dailyWorkingHours.entrySet().stream()
        .filter(entry -> !entry.getKey().isBefore(weekStart) && !entry.getKey().isAfter(weekEnd))
        .mapToLong(Map.Entry::getValue)
        .sum();
  }
}
