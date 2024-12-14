package com.anesu.project.employeeservice.service.util;

import com.anesu.project.employeeservice.entity.vacation.VacationRequest;
import com.anesu.project.employeeservice.entity.vacation.VacationRequestStatus;
import com.anesu.project.employeeservice.model.repository.VacationRequestRepository;

public class VacationRequestValidator {

  // TODO: Validate user still has vacation days left
  public void validateVacationRequest(
      VacationRequest vacationRequest, VacationRequestRepository repository) {
    Long employeeId = vacationRequest.getEmployee().getId();
    boolean overlaps =
        repository
            .findByEmployeeIdAndDateRange(
                employeeId, vacationRequest.getStartDate(), vacationRequest.getEndDate())
            .stream()
            .anyMatch(
                existingRequest ->
                    existingRequest.getStatus().equals(VacationRequestStatus.APPROVED)
                        || existingRequest.getStatus().equals(VacationRequestStatus.PENDING));
    if (overlaps) {
      throw new IllegalArgumentException(
          "Request could not be fulfilled because there is already an approved vacation request for this period for employee: "
              + employeeId);
    }
  }

  public void validateWithdrawalRequest(Long employeeId, VacationRequest vacationRequest) {
    if (!vacationRequest.getStatus().equals(VacationRequestStatus.PENDING)) {
      throw new IllegalStateException("Only pending requests can be withdrawn.");
    }

    if (!vacationRequest.getEmployee().getId().equals(employeeId)) {
      throw new SecurityException("You can only withdraw your own requests.");
    }
  }
}
