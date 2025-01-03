package com.anesu.project.employeeservice.entity.shift;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ShiftRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long employeeId;
  private LocalDateTime shiftDate;

  @Enumerated(EnumType.STRING)
  private ShiftRequestStatus status;

  private String rejectionReason;

  private Long shiftLengthInHours;

  private ShiftType shiftType;

  public Long getId() {
    return id;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public LocalDateTime getShiftDate() {
    return shiftDate;
  }

  public ShiftRequestStatus getStatus() {
    return status;
  }

  public String getRejectionReason() {
    return rejectionReason;
  }

  public Long getShiftLengthInHours() {
    return shiftLengthInHours;
  }

  public void setShiftLengthInHours(Long shiftLengthInHours) {
    this.shiftLengthInHours = shiftLengthInHours;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEmployeeId(Long employeeId) {
    this.employeeId = employeeId;
  }

  public void setShiftDate(LocalDateTime shiftDate) {
    this.shiftDate = shiftDate;
  }

  public void setStatus(ShiftRequestStatus status) {
    this.status = status;
  }

  public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  public ShiftType getShiftType() {
    return shiftType;
  }

  public void setShiftType(ShiftType shiftType) {
    this.shiftType = shiftType;
  }
}
