package com.anesu.project.employeeservice.entity.shift;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class ShiftEntry {
  private Integer shiftId;
  private LocalDate date;
  private ShiftType shiftType;
  private Integer workingHours;

  public ShiftEntry() {}

  public Integer getShiftId() {
    return shiftId;
  }

  public void setShiftId(Integer shiftId) {
    this.shiftId = shiftId;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public ShiftType getShiftType() {
    return shiftType;
  }

  public void setShiftType(ShiftType shiftType) {
    this.shiftType = shiftType;
  }

  public Integer getWorkingHours() {
    return workingHours;
  }

  public void setWorkingHours(Integer workingHours) {
    this.workingHours = workingHours;
  }
}
