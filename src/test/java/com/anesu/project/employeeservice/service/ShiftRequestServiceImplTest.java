package com.anesu.project.employeeservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.anesu.project.employeeservice.entity.shift.ShiftRequest;
import com.anesu.project.employeeservice.entity.shift.ShiftRequestStatus;
import com.anesu.project.employeeservice.entity.shift.ShiftType;
import com.anesu.project.employeeservice.model.ScheduleService;
import com.anesu.project.employeeservice.model.repository.ShiftRequestRepository;
import com.anesu.project.employeeservice.service.exception.ShiftRequestNotFoundException;
import com.anesu.project.employeeservice.service.exception.ShiftValidationException;
import com.anesu.project.employeeservice.service.util.ShiftRequestValidator;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShiftRequestServiceImplTest {

  @Mock private ShiftRequestRepository shiftRequestRepositoryMock;
  @Mock private ScheduleService scheduleServiceMock;
  private ShiftRequestServiceImpl cut;

  @BeforeEach
  void setUp() {
    cut =
        new ShiftRequestServiceImpl(
            shiftRequestRepositoryMock, new ShiftRequestValidator(), scheduleServiceMock);
  }

  @Test
  void createShiftRequest_ShouldSaveShiftRequest() {
    // Given
    ShiftRequest shiftRequest = new ShiftRequest();
    shiftRequest.setEmployeeId(1L);
    shiftRequest.setShiftDate(LocalDate.now());

    when(shiftRequestRepositoryMock.save(any(ShiftRequest.class))).thenReturn(shiftRequest);

    // When
    ShiftRequest createdShiftRequest = cut.createShiftRequest(shiftRequest);

    // Then
    assertNotNull(createdShiftRequest);
    assertEquals(shiftRequest, createdShiftRequest);
    verify(shiftRequestRepositoryMock, times(1)).save(shiftRequest);
  }

  @Test
  void approveShiftRequest_ShouldChangeStatusToApproved() throws ShiftRequestNotFoundException {

    // Given
    Long shiftRequestId = 1L;
    ShiftRequest shiftRequest = new ShiftRequest();
    shiftRequest.setId(shiftRequestId);
    ShiftRequestStatus status = ShiftRequestStatus.PENDING;
    shiftRequest.setStatus(status);
    shiftRequest.setShiftLengthInHours(6L);
    shiftRequest.setShiftType(ShiftType.NIGHT_SHIFT);
    shiftRequest.setEmployeeId(155L);
    when(shiftRequestRepositoryMock.findByIdAndStatus(shiftRequestId, status))
        .thenReturn(Optional.of(shiftRequest));
    when(shiftRequestRepositoryMock.save(any(ShiftRequest.class))).thenReturn(shiftRequest);

    // When
    ShiftRequest approvedShiftRequest =
        cut.approveShiftRequest(shiftRequest.getEmployeeId(), shiftRequestId);

    // Then
    assertThat(approvedShiftRequest.getStatus()).isEqualTo(ShiftRequestStatus.APPROVED);

    verifyInteractionsWithDependencies(shiftRequest, approvedShiftRequest);
  }

  @Test
  void approveShiftRequest_ShouldThrowException_WhenShiftRequestNotFound() {
    // Given
    Long shiftRequestId = 1L;
    when(shiftRequestRepositoryMock.findByIdAndStatus(shiftRequestId, ShiftRequestStatus.PENDING))
        .thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        ShiftRequestNotFoundException.class, () -> cut.approveShiftRequest(1L, shiftRequestId));
  }

  @Test
  void rejectShiftRequest_ShouldChangeStatusToRejected() throws ShiftRequestNotFoundException {
    // Given
    Long shiftRequestId = 1L;
    String rejectionReason = "Not required";
    ShiftRequest shiftRequest = new ShiftRequest();
    shiftRequest.setId(shiftRequestId);
    ShiftRequestStatus status = ShiftRequestStatus.PENDING;
    shiftRequest.setStatus(status);

    when(shiftRequestRepositoryMock.findByIdAndStatus(shiftRequestId, status))
        .thenReturn(Optional.of(shiftRequest));
    when(shiftRequestRepositoryMock.save(any(ShiftRequest.class))).thenReturn(shiftRequest);

    // When
    ShiftRequest rejectedShiftRequest = cut.rejectShiftRequest(shiftRequestId, rejectionReason);

    // Then
    assertEquals(ShiftRequestStatus.REJECTED, rejectedShiftRequest.getStatus());
    assertEquals(rejectionReason, rejectedShiftRequest.getRejectionReason());
    verify(shiftRequestRepositoryMock, times(1)).save(shiftRequest);
  }

  @Test
  void getShiftRequestById_ShouldThrowException_WhenShiftRequestNotFoundAndStatus() {
    // Given
    Long shiftRequestId = 1L;
    ShiftRequestStatus status = ShiftRequestStatus.PENDING;
    when(shiftRequestRepositoryMock.findByIdAndStatus(shiftRequestId, status))
        .thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        ShiftRequestNotFoundException.class,
        () -> cut.getShiftRequestByIdAndStatus(shiftRequestId, status));
  }

  @Test
  void
      validateShiftRequest_ShouldNotThrowException_WhenShiftRequestHasOverlap_ButDoesNotExceedMaximumLegalWorkingsHours() {
    // Given
    LocalDate shiftDate = LocalDate.now();

    ShiftRequest shiftRequest = new ShiftRequest();
    shiftRequest.setEmployeeId(1L);
    shiftRequest.setShiftDate(shiftDate);
    shiftRequest.setShiftLengthInHours(3L);

    when(shiftRequestRepositoryMock.existsByEmployeeIdAndShiftDate(1L, shiftDate))
        .thenReturn(Optional.of(shiftRequest));

    // When & Then
    assertDoesNotThrow(() -> cut.createShiftRequest(shiftRequest));
  }

  @Test
  void validateShiftRequest_ShouldThrowException_WhenShiftRequestResultsInIllegalWorkingTime() {

    // Given
    LocalDate shiftDate = LocalDate.now();
    ShiftRequest shiftRequest = new ShiftRequest();
    shiftRequest.setEmployeeId(1L);
    shiftRequest.setShiftDate(shiftDate);
    shiftRequest.setShiftLengthInHours(10L);

    when(shiftRequestRepositoryMock.existsByEmployeeIdAndShiftDate(1L, shiftDate))
        .thenReturn(Optional.of(shiftRequest));

    // When & Then
    ShiftValidationException shiftValidationException =
        assertThrows(ShiftValidationException.class, () -> cut.createShiftRequest(shiftRequest));

    assertThat(shiftValidationException.getMessage())
        .isEqualTo(
            "New shift request violates working hours. Employee ID: 1 already has 10 hours for this shift scheduled/recorded. Maximum working hours should not exceed : 10 hours.");
  }

  @Test
  void
      validateShiftRequest_ShouldThrowException_WhenShiftRequestResultsInIllegalWorkingTimeGivenExistingWorkingHoursLessThanMaxLegalWorkingTime() {

    // Given
    LocalDate shiftDate = LocalDate.now();
    ShiftRequest shiftRequest = new ShiftRequest();
    shiftRequest.setEmployeeId(1L);
    shiftRequest.setShiftDate(shiftDate);
    shiftRequest.setShiftLengthInHours(10L);

    ShiftRequest existingShiftRequest = new ShiftRequest();
    existingShiftRequest.setShiftLengthInHours(1L);

    when(shiftRequestRepositoryMock.existsByEmployeeIdAndShiftDate(1L, shiftDate))
        .thenReturn(Optional.of(existingShiftRequest));

    // When & Then
    ShiftValidationException shiftValidationException =
        assertThrows(ShiftValidationException.class, () -> cut.createShiftRequest(shiftRequest));

    assertThat(shiftValidationException.getMessage())
        .isEqualTo(
            "New shift request violates working hours. Employee ID: 1 already has 1 hours for this shift scheduled/recorded. Maximum working hours should not exceed : 10 hours.");
  }

  private void verifyInteractionsWithDependencies(
      ShiftRequest shiftRequest, ShiftRequest approvedShiftRequest) {
    verify(shiftRequestRepositoryMock, times(1)).save(shiftRequest);
    verify(scheduleServiceMock)
        .addShiftToSchedule(shiftRequest.getEmployeeId(), approvedShiftRequest);
  }
}
