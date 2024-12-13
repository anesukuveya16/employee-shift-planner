package com.anesu.project.employeeservice.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.*;

@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class GuestEmployee extends Employee {
  @Embedded @NonNull private PartnerCompany partnerCompany;
}
