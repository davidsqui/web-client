package com.dasc.webclient.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {

  @Null
  private UUID id;
  @NotBlank
  private String beerName;
  @NotBlank
  private BeerStyle beerStyle;
  private String upc;
  private BigDecimal price;
  private Integer quantityOnHand;
  private OffsetDateTime createdDate;
  private OffsetDateTime lastUpdatedDate;

}
