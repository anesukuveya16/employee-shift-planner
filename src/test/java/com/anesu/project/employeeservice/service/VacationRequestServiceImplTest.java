package com.anesu.project.employeeservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.anesu.project.employeeservice.entity.Employee;
import com.anesu.project.employeeservice.entity.vacation.VacationRequest;
import com.anesu.project.employeeservice.entity.vacation.VacationRequestStatus;
import com.anesu.project.employeeservice.model.repository.VacationRequestRepository;
import com.anesu.project.employeeservice.service.exception.VacationRequestNotFoundException;
import com.anesu.project.employeeservice.service.util.VacationRequestValidator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VacationRequestServiceImplTest {

  @Mock private VacationRequestRepository vacationRequestRepositoryMock;
  @Mock private VacationRequestValidator vacationRequestValidatorMock;

  private Employee employee;

  private VacationRequestServiceImpl cut;

  @BeforeEach
  void setUp() {
    employee = new Employee();
    employee.setFirstName("Marge");
    employee.setLastName("Simpson");
    employee.setId(1L);
    cut =
        new VacationRequestServiceImpl(vacationRequestRepositoryMock, vacationRequestValidatorMock);
  }

  @Test
  void createVacationRequest_ShouldSaveVacationRequestWithPendingStatus() {
    // Given
    VacationRequest givenVacationRequest = givenVacationRequest();

    doNothing()
        .when(vacationRequestValidatorMock)
        .validateVacationRequest(givenVacationRequest, vacationRequestRepositoryMock);

    when(vacationRequestRepositoryMock.save(any(VacationRequest.class)))
        .thenReturn(givenVacationRequest);

    // When
    VacationRequest createdVacationRequest = cut.createVacationRequest(givenVacationRequest);

    // Then
    assertNotNull(createdVacationRequest);
    assertThat(createdVacationRequest.getStatus()).isEqualTo(VacationRequestStatus.PENDING);
    verify(vacationRequestRepositoryMock, times(1)).save(givenVacationRequest);
    verify(vacationRequestValidatorMock, times(1))
        .validateVacationRequest(givenVacationRequest, vacationRequestRepositoryMock);
  }

  @Test
  void withdrawVacationRequest_ShouldSetStatusToWithdrawn()
      throws VacationRequestNotFoundException {
    // Given
    VacationRequest givenPendingVacationRequest =
        givenVacationRequestWithStatus(VacationRequestStatus.PENDING);

    when(vacationRequestRepositoryMock.findById(givenPendingVacationRequest.getId()))
        .thenReturn(Optional.of(givenPendingVacationRequest));

    doNothing()
        .when(vacationRequestValidatorMock)
        .validateWithdrawalRequest(
            givenPendingVacationRequest.getEmployee().getId(), givenPendingVacationRequest);

    when(vacationRequestRepositoryMock.save(any(VacationRequest.class)))
        .thenReturn(givenPendingVacationRequest);

    // When
    VacationRequest updatedVacationRequest =
        cut.withdrawVacationRequest(
            givenPendingVacationRequest.getId(), givenPendingVacationRequest.getEmployee().getId());

    // Then
    assertThat(updatedVacationRequest.getStatus()).isEqualTo(VacationRequestStatus.WITHDRAWN);
    verify(vacationRequestValidatorMock, times(1))
        .validateWithdrawalRequest(
            givenPendingVacationRequest.getEmployee().getId(), givenPendingVacationRequest);
    verify(vacationRequestRepositoryMock, times(1)).save(givenPendingVacationRequest);
  }

  @Test
  void withdrawVacationRequest_ShouldThrowException_WhenVacationRequestNotFound() {
    // Given
    Long vacationRequestId = 1L;
    Long employeeId = 1L;

    when(vacationRequestRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

    // When & Then
    assertThrows(
        VacationRequestNotFoundException.class,
        () -> cut.withdrawVacationRequest(vacationRequestId, employeeId));
  }

  @Test
  void getVacationRequestsByEmployeeId_ShouldReturnVacationRequests() {
    // Given
    VacationRequest givenVacationRequest = givenVacationRequest();

    when(vacationRequestRepositoryMock.findByEmployeeId(employee.getId()))
        .thenReturn(List.of(givenVacationRequest));

    // When
    List<VacationRequest> vacationRequests = cut.getVacationRequestsByEmployeeId(employee.getId());

    // Then
    assertNotNull(vacationRequests);
    assertThat(vacationRequests).hasSize(1);
    verify(vacationRequestRepositoryMock, times(1)).findByEmployeeId(employee.getId());
  }

  @Test
  void getVacationRequestsByDateRange_ShouldReturnVacationRequestsInRange() {
    // Given
    VacationRequest givenVacationRequest = givenVacationRequest();

    when(vacationRequestRepositoryMock.findByEmployeeIdAndDateRange(
            employee.getId(),
            givenVacationRequest.getStartDate(),
            givenVacationRequest.getEndDate()))
        .thenReturn(List.of(givenVacationRequest));

    // When
    List<VacationRequest> vacationRequests =
        cut.getVacationRequestsByDateRange(
            employee.getId(),
            givenVacationRequest.getStartDate(),
            givenVacationRequest.getEndDate());

    // Then
    assertNotNull(vacationRequests);
    assertThat(vacationRequests).hasSize(1);
    verify(vacationRequestRepositoryMock, times(1))
        .findByEmployeeIdAndDateRange(
            employee.getId(),
            givenVacationRequest.getStartDate(),
            givenVacationRequest.getEndDate());
  }

  @Test
  void getTeamCalendar_ShouldReturnTeamVacationRequests() {
    // Given

    VacationRequest givenApprovedVacationRequest =
        givenVacationRequestWithStatus(VacationRequestStatus.APPROVED);

    when(vacationRequestRepositoryMock.findByOfficeLocationAndDateRangeAndStatus(
            givenApprovedVacationRequest.getOfficeLocationId(),
            givenApprovedVacationRequest.getStartDate(),
            givenApprovedVacationRequest.getEndDate(),
            List.of(VacationRequestStatus.APPROVED, VacationRequestStatus.PENDING)))
        .thenReturn(List.of(givenApprovedVacationRequest));

    // When
    List<VacationRequest> teamCalendar =
        cut.getTeamCalendar(
            givenApprovedVacationRequest.getOfficeLocationId(),
            givenApprovedVacationRequest.getStartDate(),
            givenApprovedVacationRequest.getEndDate());

    // Then
    assertNotNull(teamCalendar);
    assertThat(teamCalendar).hasSize(1);
    verify(vacationRequestRepositoryMock, times(1))
        .findByOfficeLocationAndDateRangeAndStatus(
            givenApprovedVacationRequest.getOfficeLocationId(),
            givenApprovedVacationRequest.getStartDate(),
            givenApprovedVacationRequest.getEndDate(),
            List.of(VacationRequestStatus.APPROVED, VacationRequestStatus.PENDING));
  }

  private VacationRequest givenVacationRequestWithStatus(VacationRequestStatus status) {
    Long officeLocationId = 1L;
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusDays(5);
    VacationRequest vacationRequest = new VacationRequest();
    vacationRequest.setOfficeLocationId(officeLocationId);
    vacationRequest.setStartDate(startDate);
    vacationRequest.setEndDate(endDate);
    vacationRequest.setEmployee(employee);
    vacationRequest.setStatus(status);

    return vacationRequest;
  }

  private VacationRequest givenVacationRequest() {
    VacationRequest vacationRequest = new VacationRequest();
    vacationRequest.setEmployee(employee);
    vacationRequest.setStartDate(LocalDate.now());
    vacationRequest.setEndDate(LocalDate.now().plusDays(5));
    return vacationRequest;
  }
}
