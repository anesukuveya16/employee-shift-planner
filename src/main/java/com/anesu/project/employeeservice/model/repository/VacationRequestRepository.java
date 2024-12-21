package com.anesu.project.employeeservice.model.repository;

import com.anesu.project.employeeservice.entity.Employee;
import com.anesu.project.employeeservice.entity.vacation.VacationRequest;
import com.anesu.project.employeeservice.entity.vacation.VacationRequestStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
  @Query("SELECT e FROM Employee e JOIN FETCH e.vacationRequests WHERE e.id = :id")
  Employee findEmployeeWithVacationRequests(@Param("id") Long employeeId);

  @Query("SELECT vr FROM VacationRequest vr WHERE vr.employee.id IN :employeeIds")
  List<VacationRequest> findVacationRequestsByEmployeeIds(
      @Param("employeeIds") List<Long> employeeIds);

  List<VacationRequest> findByEmployeeId(Long employeeId);

  List<VacationRequest> findByEmployeeIdAndDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate);

  List<VacationRequest> findByOfficeLocationAndDateRangeAndStatus(
      Long officeLocationId,
      LocalDate startDate,
      LocalDate endDate,
      List<VacationRequestStatus> approved);

  @Query(
      "SELECT v FROM VacationRequest v WHERE v.employee.id = :employeeId "
          + "AND (v.startDate <= :endOfYear AND v.endDate >= :startOfYear)")
  List<VacationRequest> findByEmployeeIdAndYearOverlap(
      @Param("employeeId") Long employeeId,
      @Param("startOfYear") LocalDate startOfYear,
      @Param("endOfYear") LocalDate endOfYear);
}
