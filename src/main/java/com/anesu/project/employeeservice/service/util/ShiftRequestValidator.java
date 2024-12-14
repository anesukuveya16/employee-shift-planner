package com.anesu.project.employeeservice.service.util;

import com.anesu.project.employeeservice.entity.shift.ShiftRequest;
import com.anesu.project.employeeservice.model.ShiftRequestRepository;
import com.anesu.project.employeeservice.service.exception.ShiftValidationException;
import java.util.Optional;

public class ShiftRequestValidator {

  private static final int MAX_LEGAL_WORKING_HOURS = 10;

  public void validateShiftRequest(
      ShiftRequest shiftRequest, ShiftRequestRepository shiftRequestRepository) {

    Optional<ShiftRequest> shiftRequestOptional =
        shiftRequestRepository.existsByEmployeeIdAndShiftDate(
            shiftRequest.getEmployeeId(), shiftRequest.getShiftDate());

    shiftRequestOptional.ifPresent(
        existingShift -> {
          boolean exceedsMaximumWorkingHours =
              existingShift.getShiftLengthInHours() + shiftRequest.getShiftLengthInHours()
                  >= MAX_LEGAL_WORKING_HOURS;

          if (exceedsMaximumWorkingHours) {
            throw new ShiftValidationException(
                "New shift request violates working hours. Employee ID: "
                    + shiftRequest.getEmployeeId()
                    + " already has "
                    + shiftRequestOptional.get().getShiftLengthInHours()
                    + " hours for this shift scheduled/recorded. Maximum working hours should not exceed : "
                    + MAX_LEGAL_WORKING_HOURS
                    + " hours.");
          }
        });
  }
}
