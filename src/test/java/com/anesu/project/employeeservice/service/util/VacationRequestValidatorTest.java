package com.anesu.project.employeeservice.service.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.anesu.project.employeeservice.entity.Employee;
import com.anesu.project.employeeservice.entity.vacation.VacationRequest;
import com.anesu.project.employeeservice.entity.vacation.VacationRequestStatus;
import com.anesu.project.employeeservice.model.repository.VacationRequestRepository;
import com.anesu.project.employeeservice.service.exception.InvalidVacationRequestException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VacationRequestValidatorTest {

  @Mock private VacationRequestRepository vacationRequestRepositoryMock;

  private Employee employee;
  private VacationRequestValidator cut;

  @BeforeEach
  void setUp() {
    employee = new Employee();
    employee.setFirstName("Marge");
    employee.setLastName("Simpson");
    employee.setId(1L);
    cut = new VacationRequestValidator();
  }

  @Test
  void shouldThrowExceptionWhenVacationLimitExceededWithAllVacationsInCurrentYear() {
    // Given
    List<VacationRequest> usedVacationDays = createVacationRequests(20, 10);

    VacationRequest givenVacationRequest = createVacationRequest(10);

    when(vacationRequestRepositoryMock.findByEmployeeIdAndYearOverlap(anyLong(), any(), any()))
        .thenReturn(usedVacationDays);

    // When & Then
    assertThrows(
        InvalidVacationRequestException.class,
        () -> cut.validateVacationRequest(givenVacationRequest, vacationRequestRepositoryMock));
  }

  @Test
  void shouldNotThrowExceptionWhenSpanningPreviousYearWithinLimit() {
    // Given
    List<VacationRequest> usedVacationDays =
        createVacationRequestsWithPreviousYearSpanningFiveDaysIntoCurrentYear();

    VacationRequest givenVacationRequest = createVacationRequest(20);

    when(vacationRequestRepositoryMock.findByEmployeeIdAndYearOverlap(anyLong(), any(), any()))
        .thenReturn(usedVacationDays);

    // When & Then
    cut.validateVacationRequest(givenVacationRequest, vacationRequestRepositoryMock);
  }

  @Test
  void shouldThrowExceptionWhenSpanningPreviousYearExceedsLimit() {
    // Given
    List<VacationRequest> usedVacationDays =
        createVacationRequestsWithPreviousYearSpanningFiveDaysIntoCurrentYear();

    VacationRequest givenVacationRequest = createVacationRequest(30);

    when(vacationRequestRepositoryMock.findByEmployeeIdAndYearOverlap(anyLong(), any(), any()))
        .thenReturn(usedVacationDays);

    // When & Then
    assertThrows(
        InvalidVacationRequestException.class,
        () -> cut.validateVacationRequest(givenVacationRequest, vacationRequestRepositoryMock));
  }

  @Test
  void shouldNotThrowExceptionWhenVacationWithinLimit() {
    // Given
    List<VacationRequest> usedVacationDays = createVacationRequests(10);

    VacationRequest givenVacationRequest = createVacationRequest(10);

    when(vacationRequestRepositoryMock.findByEmployeeIdAndYearOverlap(anyLong(), any(), any()))
        .thenReturn(usedVacationDays);

    // When & Then
    cut.validateVacationRequest(givenVacationRequest, vacationRequestRepositoryMock);
  }

  @Test
  void shouldThrowExceptionForRealisticMultiVacationScenarioExceedingLimit() {
    // Given
    List<VacationRequest> usedVacationDays = createRealisticVacationScenario();

    VacationRequest newVacationRequest = createVacationRequest(12);

    when(vacationRequestRepositoryMock.findByEmployeeIdAndYearOverlap(anyLong(), any(), any()))
        .thenReturn(usedVacationDays);

    // When & Then
    assertThrows(
        InvalidVacationRequestException.class,
        () -> cut.validateVacationRequest(newVacationRequest, vacationRequestRepositoryMock));
  }

  @Test
  void shouldNotThrowExceptionWhenRemainingVacationDaysMatchNewRequestSpanningIntoNextYear() {
    // Given
    List<VacationRequest> usedVacationDays = createRealisticVacationScenario();

    VacationRequest newVacationRequest = createVacationRequestSpanningIntoNextYear(7);

    when(vacationRequestRepositoryMock.findByEmployeeIdAndYearOverlap(anyLong(), any(), any()))
        .thenReturn(usedVacationDays);

    // When & Then
    cut.validateVacationRequest(newVacationRequest, vacationRequestRepositoryMock);
  }

  // Helper Methods
  private List<VacationRequest> createVacationRequests(int... durations) {
    LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
    return Arrays.stream(durations)
        .mapToObj(duration -> createVacationRequestWithDuration(start, duration))
        .toList();
  }

  private List<VacationRequest>
      createVacationRequestsWithPreviousYearSpanningFiveDaysIntoCurrentYear() {
    LocalDate startPreviousYear = LocalDate.of(LocalDate.now().getYear() - 1, 12, 20);
    return List.of(createVacationRequestWithDuration(startPreviousYear, 15));
  }

  private List<VacationRequest> createRealisticVacationScenario() {
    return List.of(
        createVacationRequestWithDuration(LocalDate.of(LocalDate.now().getYear() - 1, 12, 25), 15),
        createVacationRequestWithDuration(LocalDate.of(LocalDate.now().getYear(), 3, 1), 10),
        createVacationRequestWithDuration(LocalDate.of(LocalDate.now().getYear(), 6, 15), 5));
  }

  private VacationRequest createVacationRequest(int days) {
    return createVacationRequestWithDuration(LocalDate.of(LocalDate.now().getYear(), 11, 1), days);
  }

  private VacationRequest createVacationRequestSpanningIntoNextYear(int days) {
    return createVacationRequestWithDuration(LocalDate.of(LocalDate.now().getYear(), 12, 26), days);
  }

  private VacationRequest createVacationRequestWithDuration(LocalDate startDate, int days) {
    VacationRequest vacationRequest = new VacationRequest();
    vacationRequest.setEmployee(employee);
    vacationRequest.setStartDate(startDate);
    vacationRequest.setEndDate(startDate.plusDays(days - 1));
    vacationRequest.setStatus(VacationRequestStatus.PENDING);
    return vacationRequest;
  }
}
