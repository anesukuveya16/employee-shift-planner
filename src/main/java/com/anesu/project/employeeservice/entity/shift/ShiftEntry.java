package com.anesu.project.employeeservice.entity.shift;

import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.Builder;

/**
 * A ShiftEntry is an entry in the schedule that is equivalent to a {@link ShiftRequest} with {@link
 * ShiftRequestStatus} APPROVED.
 */
@Builder
@Embeddable
public class ShiftEntry {
  private Long shiftId;
  private LocalDateTime shiftDate;
  private ShiftType shiftType;
  private Long workingHours;

  public ShiftEntry() {}

  public static ShiftEntry from(ShiftRequest approvedShiftRequest) {
    return builder()
        .shiftDate(approvedShiftRequest.getShiftDate())
        .shiftType(approvedShiftRequest.getShiftType())
        .workingHours(approvedShiftRequest.getShiftLengthInHours())
        .build();
  }

  public Long getShiftId() {
    return shiftId;
  }

  public void setShiftId(Long shiftId) {
    this.shiftId = shiftId;
  }

  public LocalDateTime getShiftDate() {
    return shiftDate;
  }

  public void setShiftDate(LocalDateTime date) {
    this.shiftDate = date;
  }

  public ShiftType getShiftType() {
    return shiftType;
  }

  public void setShiftType(ShiftType shiftType) {
    this.shiftType = shiftType;
  }

  public Long getWorkingHours() {
    return workingHours;
  }

  public void setWorkingHours(Long workingHours) {
    this.workingHours = workingHours;
  }
}
