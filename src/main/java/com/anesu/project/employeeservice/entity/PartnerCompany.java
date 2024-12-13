package com.anesu.project.employeeservice.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PartnerCompany {
  @NonNull private String companyName;
  @NonNull private String address;
  @NonNull private String phoneNumber;
  private String email;
  @NonNull private String managerName;
}
