package com.anesu.project.employeeservice.entity;

import com.anesu.project.employeeservice.entity.shift.ShiftEntry;
import com.anesu.project.employeeservice.entity.shift.ShiftType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import java.util.List;
import lombok.*;

@Getter
@Builder
@ToString
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSchedule {
  private int numberOfVacationDaysLeft;
  private int numberOfVacationDaysUsed;
  private ShiftType shiftType;
  @ElementCollection private List<ShiftEntry> shiftCalendar;
}
