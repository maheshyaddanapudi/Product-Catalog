package com.my.retail.catalog.dto.response.product;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PriceDTO {

    private double value;
    private String currency_code;

}
