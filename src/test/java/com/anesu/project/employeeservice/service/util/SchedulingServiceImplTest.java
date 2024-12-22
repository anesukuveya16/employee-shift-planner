package com.anesu.project.employeeservice.service.util;

import com.anesu.project.employeeservice.entity.schedule.Schedule;
import com.anesu.project.employeeservice.model.repository.ScheduleRepository;
import com.anesu.project.employeeservice.service.ScheduleServiceImpl;
import com.anesu.project.employeeservice.service.exception.InvalidScheduleException;
import com.anesu.project.employeeservice.service.exception.ScheduleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceImplTest {

  @Mock private ScheduleRepository scheduleRepositoryMock;
  @Mock private ScheduleValidator scheduleValidatorMock;

  private ScheduleServiceImpl cut;

  @BeforeEach
  void setUp() {
    cut = new ScheduleServiceImpl(scheduleRepositoryMock, scheduleValidatorMock);
  }

  @Test
  void createSchedule_ShouldSaveCreatedScheduleIfValidationIsPassed() {
    // Given
    Schedule schedule = new Schedule();
    schedule.setEmployeeId(1L);
    doNothing().when(scheduleValidatorMock).validate(schedule);
    when(scheduleRepositoryMock.save(schedule)).thenReturn(schedule);
    // When

    Schedule createdSchedule = cut.createSchedule(schedule);
    // Then

    assertNotNull(createdSchedule);
    assertEquals(schedule, createdSchedule);
    verify(scheduleValidatorMock).validate(schedule);
    verify(scheduleRepositoryMock, times(1)).save(schedule);
  }

  @Test
  void createSchedule_ShouldNotSaveCreatedScheduleIfValidationHasFailed() {
    // Given
    Schedule schedule = new Schedule();
    schedule.setEmployeeId(1L);

    doThrow(InvalidScheduleException.class).when(scheduleValidatorMock).validate(schedule);

    // When
    assertThrows(InvalidScheduleException.class, () -> cut.createSchedule(schedule));

    // Then
    verify(scheduleValidatorMock).validate(schedule);
    verifyNoInteractions(scheduleRepositoryMock);
  }

  @Test
  void shouldUpdateGivenSchedule() {
    // Given
    LocalDate startDate = LocalDate.now().plusDays(2);
    Schedule oldSchedule = givenScheduleWithDurationAndStartDate(4, startDate);
    Schedule newSchedule = givenScheduleWithDurationAndStartDate(8, startDate);

    when(scheduleRepositoryMock.findById(oldSchedule.getId())).thenReturn(Optional.of(oldSchedule));
    doNothing().when(scheduleValidatorMock).validate(any(Schedule.class));
    when(scheduleRepositoryMock.save(any(Schedule.class))).thenReturn(newSchedule);

    // When
    Schedule updateSchedule = cut.updateSchedule(oldSchedule.getId(), newSchedule);

    // Then
    assertNotNull(updateSchedule);
    assertThat(updateSchedule.getTotalWorkingHours()).isEqualTo(newSchedule.getTotalWorkingHours());

    verify(scheduleRepositoryMock, times(1)).findById(oldSchedule.getId());
    verify(scheduleValidatorMock).validate(oldSchedule);
    verify(scheduleRepositoryMock, times(1)).save(oldSchedule);
  }

  @Test
  void shouldNotUpdateScheduleAndThrowExceptionWhenScheduleIsNotFound() {
    // Given
    LocalDate startDate = LocalDate.now().plusDays(2);
    Schedule oldSchedule = givenScheduleWithDurationAndStartDate(4, startDate);
    Schedule newSchedule = givenScheduleWithDurationAndStartDate(8, startDate);

    when(scheduleRepositoryMock.findById(oldSchedule.getId())).thenReturn(Optional.empty());

    // When
    assertThrows(
        ScheduleNotFoundException.class,
        () -> cut.updateSchedule(oldSchedule.getId(), newSchedule));

    // Then
    verify(scheduleRepositoryMock, times(1)).findById(oldSchedule.getId());
    verifyNoInteractions(scheduleValidatorMock);
    verifyNoMoreInteractions(scheduleRepositoryMock);
  }

  @Test
  void shouldNotUpdateScheduleAndThrowExceptionWhenUpdatedScheduleValidationFails() {
    // Given
    LocalDate startDate = LocalDate.now().plusDays(2);
    Schedule oldSchedule = givenScheduleWithDurationAndStartDate(4, startDate);
    Schedule newSchedule = givenScheduleWithDurationAndStartDate(8, startDate);

    when(scheduleRepositoryMock.findById(oldSchedule.getId())).thenReturn(Optional.of(oldSchedule));
    doThrow(InvalidScheduleException.class).when(scheduleValidatorMock).validate(oldSchedule);

    // When
    assertThrows(
        InvalidScheduleException.class, () -> cut.updateSchedule(oldSchedule.getId(), newSchedule));

    // Then
    verify(scheduleRepositoryMock, times(1)).findById(oldSchedule.getId());
    verify(scheduleValidatorMock).validate(oldSchedule);
    verifyNoMoreInteractions(scheduleRepositoryMock);
  }

  @Test
  void deleteSchedule_ShouldThrowExceptionWhenScheduleIsNotFound() {
    // Given
    long employeeId = 1L;
    doThrow(ScheduleNotFoundException.class).when(scheduleRepositoryMock).existsById(employeeId);

    //When
    assertThrows(ScheduleNotFoundException.class, () -> cut.deleteSchedule(employeeId));

    // Then
    verify(scheduleRepositoryMock, times(1)).existsById(employeeId);
    verifyNoMoreInteractions(scheduleRepositoryMock);

  }

  private Schedule givenScheduleWithDurationAndStartDate(
      int totalWorkingHours, LocalDate startDate) {
    Schedule schedule = new Schedule();
    schedule.setEmployeeId(1L);
    schedule.setStartDate(startDate);
    schedule.setEndDate(LocalDate.now().plusDays(2));
    schedule.setTotalWorkingHours(totalWorkingHours);
    return schedule;
  }

}
