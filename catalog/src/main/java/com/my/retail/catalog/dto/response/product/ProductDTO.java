package com.my.retail.catalog.dto.response.product;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductDTO {

    private long id;
    private String name;
    private PriceDTO current_price;

}
