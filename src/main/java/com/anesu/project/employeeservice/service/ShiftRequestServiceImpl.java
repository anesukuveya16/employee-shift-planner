package com.anesu.project.employeeservice.service;

import com.anesu.project.employeeservice.entity.shift.ShiftRequest;
import com.anesu.project.employeeservice.entity.shift.ShiftRequestStatus;
import com.anesu.project.employeeservice.model.ScheduleService;
import com.anesu.project.employeeservice.model.ShiftRequestService;
import com.anesu.project.employeeservice.model.repository.ShiftRequestRepository;
import com.anesu.project.employeeservice.service.exception.ShiftRequestNotFoundException;
import com.anesu.project.employeeservice.service.util.ShiftRequestValidator;
import java.time.LocalDate;
import java.util.List;

public class ShiftRequestServiceImpl implements ShiftRequestService {

  private final ShiftRequestRepository shiftRequestRepository;
  private final ShiftRequestValidator shiftRequestValidator;
  private final ScheduleService scheduleService;

  public ShiftRequestServiceImpl(
      ShiftRequestRepository shiftRequestRepository,
      ShiftRequestValidator shiftRequestValidator,
      ScheduleService scheduleService) {
    this.shiftRequestRepository = shiftRequestRepository;
    this.shiftRequestValidator = shiftRequestValidator;
    this.scheduleService = scheduleService;
  }

  @Override
  public ShiftRequest createShiftRequest(ShiftRequest shiftRequest) {
    validateShiftRequest(shiftRequest);
    shiftRequest.setStatus(ShiftRequestStatus.PENDING);
    return shiftRequestRepository.save(shiftRequest);
  }

  /**
   * When a ShiftRequest has been approved:
   * <li>1. It needs to be saved in the ShiftRequest DB.
   * <li>2. Update the schedule to include the approved ShiftRequest, which is now a ShiftEntry.
   */
  @Override
  public ShiftRequest approveShiftRequest(Long employeeId, Long shiftRequestId)
      throws ShiftRequestNotFoundException {

    ShiftRequest shiftRequest =
        getShiftRequestByIdAndStatus(shiftRequestId, ShiftRequestStatus.PENDING);
    shiftRequest.setStatus(ShiftRequestStatus.APPROVED);
    ShiftRequest approvedShiftRequest = shiftRequestRepository.save(shiftRequest);

    scheduleService.addShiftToSchedule(1L, approvedShiftRequest);

    return approvedShiftRequest;
  }

  @Override
  public ShiftRequest rejectShiftRequest(Long shiftRequestId, String rejectionReason)
      throws ShiftRequestNotFoundException {
    ShiftRequest shiftRequest =
        getShiftRequestByIdAndStatus(shiftRequestId, ShiftRequestStatus.PENDING);
    shiftRequest.setStatus(ShiftRequestStatus.REJECTED);
    shiftRequest.setRejectionReason(rejectionReason);
    return shiftRequestRepository.save(shiftRequest);
  }

  @Override
  public ShiftRequest getShiftRequestByIdAndStatus(Long shiftRequestId, ShiftRequestStatus status)
      throws ShiftRequestNotFoundException {
    return shiftRequestRepository
        .findByIdAndStatus(shiftRequestId, status)
        .orElseThrow(
            () ->
                new ShiftRequestNotFoundException(
                    "Could not find pending shift request with ID: " + shiftRequestId));
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
