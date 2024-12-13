package com.anesu.project.employeeservice.service;

import com.anesu.project.employeeservice.model.ShiftRequest;
import com.anesu.project.employeeservice.model.ShiftRequestRepository;
import com.anesu.project.employeeservice.model.ShiftRequestService;
import com.anesu.project.employeeservice.service.exception.ShiftRequestNotFoundException;
import java.time.LocalDate;
import java.util.List;

public class ShiftRequestServiceImpl implements ShiftRequestService {

  private final ShiftRequestRepository shiftRequestRepository;
  private final ShiftRequestValidator shiftRequestValidator;

  public ShiftRequestServiceImpl(
      ShiftRequestRepository shiftRequestRepository, ShiftRequestValidator shiftRequestValidator) {
    this.shiftRequestRepository = shiftRequestRepository;
    this.shiftRequestValidator = shiftRequestValidator;
  }

  @Override
  public ShiftRequest createShiftRequest(ShiftRequest shiftRequest) {
    validateShiftRequest(shiftRequest);
    shiftRequest.setStatus(ShiftRequestStatus.PENDING);
    return shiftRequestRepository.save(shiftRequest);
  }

  @Override
  public ShiftRequest approveShiftRequest(Long shiftRequestId)
      throws ShiftRequestNotFoundException {
    ShiftRequest shiftRequest = getShiftRequestById(shiftRequestId);
    shiftRequest.setStatus(ShiftRequestStatus.APPROVED);
    return shiftRequestRepository.save(shiftRequest);
  }

  @Override
  public ShiftRequest rejectShiftRequest(Long shiftRequestId, String rejectionReason)
      throws ShiftRequestNotFoundException {
    ShiftRequest shiftRequest = getShiftRequestById(shiftRequestId);
    shiftRequest.setStatus(ShiftRequestStatus.REJECTED);
    shiftRequest.setRejectionReason(rejectionReason);
    return shiftRequestRepository.save(shiftRequest);
  }

  @Override
  public ShiftRequest getShiftRequestById(Long shiftRequestId)
      throws ShiftRequestNotFoundException {
    return shiftRequestRepository
        .findById(shiftRequestId)
        .orElseThrow(
            () ->
                new ShiftRequestNotFoundException(
                    "Shift request not found with ID: " + shiftRequestId));
  }

  @Override
  public List<ShiftRequest> getShiftRequestsByEmployee(Long employeeId) {
    return shiftRequestRepository.findByEmployeeId(employeeId);
  }

  @Override
  public List<ShiftRequest> getShiftRequestsByDateRange(LocalDate startDate, LocalDate endDate) {
    return shiftRequestRepository.findByDateRange(startDate, endDate);
  }

  private void validateShiftRequest(ShiftRequest shiftRequest) {
    shiftRequestValidator.validateShiftRequest(shiftRequest, shiftRequestRepository);
  }
}
