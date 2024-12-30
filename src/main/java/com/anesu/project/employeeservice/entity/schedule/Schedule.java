package com.anesu.project.employeeservice.entity.schedule;

import com.anesu.project.employeeservice.entity.shift.ShiftEntry;
import com.anesu.project.employeeservice.entity.vacation.VacationRequest;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long employeeId;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Long totalWorkingHours;

  @ElementCollection private List<ShiftEntry> shifts;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "schedule_id")
  private List<VacationRequest> vacations;

  public List<LocalDateTime> getShiftsInRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
    return shifts.stream()
        .filter(
            shift ->
                !shift.getShiftDate().isBefore(rangeStart)
                    && !shift.getShiftDate().isAfter(rangeEnd))
        .map(ShiftEntry::getShiftDate)
        .toList();
  }

  public List<LocalDate> getVacationsInRange(LocalDate rangeStart, LocalDate rangeEnd) {
    return vacations.stream()
        .filter(
            vacation ->
                vacation.getStartDate().isBefore(rangeEnd.plusDays(1))
                    && vacation.getEndDate().isAfter(rangeStart.minusDays(1)))
        .flatMap(vacation -> vacation.getStartDate().datesUntil(vacation.getEndDate().plusDays(1)))
        .toList();
  }
}
