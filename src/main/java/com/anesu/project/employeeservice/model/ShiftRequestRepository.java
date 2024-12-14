package com.anesu.project.employeeservice.model;

import com.anesu.project.employeeservice.entity.shift.ShiftRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRequestRepository extends JpaRepository<ShiftRequest, Long> {
  List<ShiftRequest> findByEmployeeId(Long employeeId);

  List<ShiftRequest> findByDateRange(LocalDate startDate, LocalDate endDate);

  Optional<ShiftRequest> existsByEmployeeIdAndShiftDate(Long employeeId, LocalDate shiftDate);
}
