package com.anesu.project.employeeservice.service;

import com.anesu.project.employeeservice.entity.vacation.VacationRequest;
import com.anesu.project.employeeservice.entity.vacation.VacationRequestStatus;
import com.anesu.project.employeeservice.model.VacationRequestService;
import com.anesu.project.employeeservice.model.repository.VacationRequestRepository;
import com.anesu.project.employeeservice.service.exception.VacationRequestNotFoundException;
import com.anesu.project.employeeservice.service.util.VacationRequestValidator;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VacationRequestServiceImpl implements VacationRequestService {

  private final VacationRequestRepository vacationRequestRepository;
  private final VacationRequestValidator vacationRequestValidator;

  public VacationRequestServiceImpl(
      VacationRequestRepository vacationRequestRepository,
      VacationRequestValidator vacationRequestValidator) {
    this.vacationRequestRepository = vacationRequestRepository;
    this.vacationRequestValidator = vacationRequestValidator;
  }

  @Override
  public VacationRequest createVacationRequest(VacationRequest vacationRequest) {
    validateVacationRequest(vacationRequest);
    vacationRequest.setStatus(VacationRequestStatus.PENDING);
    return vacationRequestRepository.save(vacationRequest);
  }

  @Override
  public VacationRequest withdrawVacationRequest(Long vacationRequestId, Long employeeId)
      throws VacationRequestNotFoundException {
    VacationRequest vacationRequest = getVacationRequestById(vacationRequestId);

    vacationRequestValidator.validateWithdrawalRequest(employeeId, vacationRequest);

    vacationRequest.setStatus(VacationRequestStatus.WITHDRAWN);

    return vacationRequestRepository.save(vacationRequest);
  }

  @Override
  public List<VacationRequest> getVacationRequestsByEmployeeId(Long employeeId) {
    return vacationRequestRepository.findByEmployeeId(employeeId);
  }

  @Override
  public List<VacationRequest> getVacationRequestsByDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate) {
    return vacationRequestRepository.findByEmployeeIdAndDateRange(employeeId, startDate, endDate);
  }

  @Override
  public List<VacationRequest> getTeamCalendar(
      Long officeLocationId, LocalDate startDate, LocalDate endDate) {
    return vacationRequestRepository.findByOfficeLocationAndDateRangeAndStatus(
        officeLocationId,
        startDate,
        endDate,
        List.of(VacationRequestStatus.APPROVED, VacationRequestStatus.PENDING));
  }

  private VacationRequest getVacationRequestById(Long vacationRequestId)
      throws VacationRequestNotFoundException {
    return vacationRequestRepository
        .findById(vacationRequestId)
        .orElseThrow(
            () ->
                new VacationRequestNotFoundException(
                    "Vacation request not found with ID: " + vacationRequestId));
  }

  private void validateVacationRequest(VacationRequest vacationRequest) {
    vacationRequestValidator.validateVacationRequest(vacationRequest, vacationRequestRepository);
  }
}
