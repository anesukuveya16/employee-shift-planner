package com.anesu.project.employeeservice.service;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import com.anesu.project.employeeservice.entity.schedule.Schedule;
// import com.anesu.project.employeeservice.model.repository.ScheduleRepository;
// import com.anesu.project.employeeservice.service.exception.ScheduleNotFoundException;
// import com.anesu.project.employeeservice.service.exception.ValidationException;
//// import com.anesu.project.employeeservice.service.util.SchedulingValidator;
// import java.time.LocalDate;
// import java.util.Collections;
// import java.util.List;
// import java.util.Optional;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceImplTest {
  //
  //  @Mock private ScheduleRepository scheduleRepositoryMock;
  //
  //  private SchedulingValidator schedulingValidator;
  //  private SchedulingServiceImpl cut;
  //
  //  @BeforeEach
  //  void setUp() {
  //    schedulingValidator = new SchedulingValidator();
  //    cut = new SchedulingServiceImpl(scheduleRepositoryMock, schedulingValidator);
  //  }
  //
  //  @Test
  //  void createSchedule_ShouldSaveSchedule() {
  //    // Given
  //    Schedule schedule = new Schedule();
  //    schedule.setEmployeeId(1L);
  //    schedule.setStartDate(LocalDate.now());
  //    schedule.setEndDate(LocalDate.now().plusDays(5));
  //
  //    when(scheduleRepositoryMock.findByEmployeeIdAndDateRange(
  //            schedule.getEmployeeId(), schedule.getStartDate(), schedule.getEndDate()))
  //        .thenReturn(Collections.emptyList());
  //
  //    when(scheduleRepositoryMock.save(any(Schedule.class))).thenReturn(schedule);
  //
  //    // When
  //    Schedule createdSchedule = cut.createSchedule(schedule);
  //
  //    // Then
  //    assertNotNull(createdSchedule);
  //    assertEquals(schedule, createdSchedule);
  //    verify(scheduleRepositoryMock, times(1)).save(schedule);
  //  }
  //
  //  @Test
  //  void createSchedule_ShouldThrowException_WhenOverlappingScheduleExists() {
  //    // Given
  //    Schedule schedule = new Schedule();
  //    schedule.setEmployeeId(1L);
  //    schedule.setStartDate(LocalDate.now());
  //    schedule.setEndDate(LocalDate.now().plusDays(5));
  //
  //    Schedule existingSchedule = new Schedule();
  //    existingSchedule.setEmployeeId(1L);
  //    existingSchedule.setStartDate(LocalDate.now());
  //    existingSchedule.setEndDate(LocalDate.now().plusDays(3));
  //
  //    when(scheduleRepositoryMock.findByEmployeeIdAndDateRange(
  //            schedule.getEmployeeId(), schedule.getStartDate(), schedule.getEndDate()))
  //        .thenReturn(List.of(existingSchedule));
  //
  //    // When & Then
  //    ValidationException exception =
  //        assertThrows(ValidationException.class, () -> cut.createSchedule(schedule));
  //
  //    assertThat(exception.getMessage()).isEqualTo("Schedule overlaps with an existing
  // schedule.");
  //  }
  //
  //  @Test
  //  void updateSchedule_ShouldUpdateSchedule() throws ScheduleNotFoundException {
  //    // Given
  //    Long scheduleId = 1L;
  //    Schedule existingSchedule = new Schedule();
  //    existingSchedule.setId(scheduleId);
  //    existingSchedule.setEmployeeId(1L);
  //    existingSchedule.setStartDate(LocalDate.now());
  //    existingSchedule.setEndDate(LocalDate.now().plusDays(5));
  //
  //    Schedule updatedSchedule = new Schedule();
  //    updatedSchedule.setEmployeeId(1L);
  //    updatedSchedule.setStartDate(LocalDate.now().plusDays(2));
  //    updatedSchedule.setEndDate(LocalDate.now().plusDays(7));
  //
  //    when(scheduleRepositoryMock.findById(scheduleId)).thenReturn(Optional.of(existingSchedule));
  //    when(scheduleRepositoryMock.findByEmployeeIdAndDateRange(
  //            updatedSchedule.getEmployeeId(),
  //            updatedSchedule.getStartDate(),
  //            updatedSchedule.getEndDate()))
  //        .thenReturn(Collections.emptyList());
  //    when(scheduleRepositoryMock.save(any(Schedule.class))).thenReturn(updatedSchedule);
  //
  //    // When
  //    Schedule result = cut.updateSchedule(scheduleId, updatedSchedule);
  //
  //    // Then
  //    assertNotNull(result);
  //    assertEquals(updatedSchedule, result);
  //    verify(scheduleRepositoryMock, times(1)).save(updatedSchedule);
  //  }
  //
  //  @Test
  //  void updateSchedule_ShouldThrowException_WhenScheduleNotFound() {
  //    // Given
  //    Long scheduleId = 1L;
  //    Schedule updatedSchedule = new Schedule();
  //
  //    when(scheduleRepositoryMock.findById(scheduleId)).thenReturn(Optional.empty());
  //
  //    // When & Then
  //    assertThrows(
  //        ScheduleNotFoundException.class, () -> cut.updateSchedule(scheduleId, updatedSchedule));
  //  }
  //
  //  @Test
  //  void getSchedulesByEmployeeAndDateRange_ShouldReturnSchedules() {
  //    // Given
  //    Long employeeId = 1L;
  //    LocalDate startDate = LocalDate.now();
  //    LocalDate endDate = LocalDate.now().plusDays(5);
  //
  //    Schedule schedule = new Schedule();
  //    schedule.setEmployeeId(employeeId);
  //
  //    when(scheduleRepositoryMock.findByEmployeeIdAndDateRange(employeeId, startDate, endDate))
  //        .thenReturn(List.of(schedule));
  //
  //    // When
  //    List<Schedule> schedules =
  //        cut.getSchedulesByEmployeeAndDateRange(employeeId, startDate, endDate);
  //
  //    // Then
  //    assertNotNull(schedules);
  //    assertFalse(schedules.isEmpty());
  //    assertEquals(1, schedules.size());
  //  }
  //
  //  @Test
  //  void isEmployeeAvailable_ShouldReturnTrue_WhenNoScheduleExists() {
  //    // Given
  //    Long employeeId = 1L;
  //    LocalDate date = LocalDate.now();
  //
  //    when(scheduleRepositoryMock.findByEmployeeIdAndDateRange(employeeId, date, date))
  //        .thenReturn(Collections.emptyList());
  //
  //    // When
  //    boolean isAvailable = cut.isEmployeeAvailable(employeeId, date);
  //
  //    // Then
  //    assertTrue(isAvailable);
  //  }
  //
  //  @Test
  //  void isEmployeeAvailable_ShouldReturnFalse_WhenScheduleExists() {
  //    // Given
  //    Long employeeId = 1L;
  //    LocalDate date = LocalDate.now();
  //
  //    Schedule schedule = new Schedule();
  //    schedule.setEmployeeId(employeeId);
  //
  //    when(scheduleRepositoryMock.findByEmployeeIdAndDateRange(employeeId, date, date))
  //        .thenReturn(List.of(schedule));
  //
  //    // When
  //    boolean isAvailable = cut.isEmployeeAvailable(employeeId, date);
  //
  //    // Then
  //    assertFalse(isAvailable);
  //  }
}
