package com.anesu.project.employeeservice.model;

import com.anesu.project.employeeservice.entity.vacation.VacationRequest;
import java.time.LocalDate;
import java.util.List;

public interface VacationRequestService {

  VacationRequest createVacationRequest(VacationRequest vacationRequest);

  VacationRequest withdrawVacationRequest(Long vacationRequestId, Long employeeId);

  List<VacationRequest> getVacationRequestsByEmployeeId(Long employeeId);

  List<VacationRequest> getVacationRequestsByDateRange(
      Long employeeId, LocalDate startDate, LocalDate endDate);

  List<VacationRequest> getTeamCalendar(
      Long officeLocationId, LocalDate startDate, LocalDate endDate);
}
