package com.anesu.project.employeeservice.service.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import com.anesu.project.employeeservice.entity.shift.ShiftEntry;
import com.anesu.project.employeeservice.service.exception.InvalidScheduleException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduleValidatorTest {

  private static final LocalDate START_DATE = LocalDate.of(2024, 12, 20);
  private static final LocalDate END_DATE = LocalDate.of(2024, 12, 28);

  private ScheduleValidator cut;

  @BeforeEach
  void setUp() {
    cut = new ScheduleValidator();
  }

  @Test
  void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
    // Given
    Schedule schedule = givenSchedule(END_DATE, START_DATE, null);

    // When
    InvalidScheduleException exception =
        assertThrows(InvalidScheduleException.class, () -> cut.validate(schedule));

    // Then
    assertEquals("Start date cannot be after end date", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenEndDateIsNull() {
    // Given
    Schedule schedule = givenSchedule(START_DATE, null, null);

    // When
    InvalidScheduleException exception =
        assertThrows(InvalidScheduleException.class, () -> cut.validate(schedule));

    // Then
    assertEquals("Start date or end date is null", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenStartDateIsNull() {
    // Given
    Schedule schedule = givenSchedule(null, END_DATE, null);

    // When
    InvalidScheduleException exception =
        assertThrows(InvalidScheduleException.class, () -> cut.validate(schedule));

    // Then
    assertEquals("Start date or end date is null", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionIfShiftOrVacationWithinTheScheduleIsNull() {
    // Given
    Schedule schedule = givenSchedule(START_DATE, END_DATE, null);

    // When
    InvalidScheduleException exception =
        assertThrows(InvalidScheduleException.class, () -> cut.validate(schedule));

    // Then

    assertEquals("Shifts or vacations are null", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionIfShiftIsMoreThanMaxWorkingTimePerDay() {
    // Given

    ShiftEntry shiftEntry = new ShiftEntry();
    shiftEntry.setWorkingHours(10);

    Schedule schedule = givenSchedule(START_DATE, END_DATE, List.of(shiftEntry));

    // When
    InvalidScheduleException exception =
        assertThrows(InvalidScheduleException.class, () -> cut.validate(schedule));

    // Then
    assertEquals("Shift exceeds maximum working hours", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenMaximumWeeklyWorkingHoursAreExceeded() {
    // Given
    Schedule schedule = givenSchedule(START_DATE, END_DATE, givenMultipleShiftEntries());

    // When
    InvalidScheduleException exception =
        assertThrows(InvalidScheduleException.class, () -> cut.validate(schedule));

    // Then
    assertEquals("Weekly working hours exceed maximum limit", exception.getMessage());
  }

  private Schedule givenSchedule(
      LocalDate startDate, LocalDate endDate, List<ShiftEntry> shiftEntries) {
    Schedule schedule = new Schedule();
    schedule.setStartDate(startDate);
    schedule.setEndDate(endDate);

    schedule.setShifts(shiftEntries);
    return schedule;
  }

  private List<ShiftEntry> givenMultipleShiftEntries() {

    List<ShiftEntry> shiftEntries = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      ShiftEntry shiftEntry = new ShiftEntry();
      shiftEntry.setWorkingHours(6);
      shiftEntry.setDate(LocalDate.of(2024, 12, 28));

      shiftEntries.add(shiftEntry);
    }

    return shiftEntries;
  }
}
