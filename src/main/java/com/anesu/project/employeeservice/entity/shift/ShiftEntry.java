package com.anesu.project.employeeservice.entity.shift;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ShiftEntry {
  private Integer shiftId;
  private LocalDate date;
  private ShiftType shiftType;
  private Integer workingHours;
}
