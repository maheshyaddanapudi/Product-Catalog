package com.my.retail.catalog.mappers;

import com.my.retail.catalog.db.entities.Product;
import com.my.retail.catalog.dto.response.product.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductDTO mapProductToDto(Product product);

    Product mapDtoToProduct(ProductDTO productDTO);
}