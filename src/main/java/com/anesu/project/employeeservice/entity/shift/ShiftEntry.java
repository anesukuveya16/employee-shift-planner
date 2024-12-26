package com.anesu.project.employeeservice.entity.shift;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class ShiftEntry {
  private Long shiftId;
  private LocalDate shiftDate;
  private ShiftType shiftType;
  private Long workingHours;

  public ShiftEntry() {}

  public Long getShiftId() {
    return shiftId;
  }

  public void setShiftId(Long shiftId) {
    this.shiftId = shiftId;
  }

  public LocalDate getShiftDate() {
    return shiftDate;
  }

  public void setShiftDate(LocalDate date) {
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
