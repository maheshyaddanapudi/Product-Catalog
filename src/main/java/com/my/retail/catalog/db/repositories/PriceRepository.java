package com.my.retail.catalog.db.repositories;

import com.my.retail.catalog.db.entities.Price;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PriceRepository extends CrudRepository<Price, Long> {
    Optional<Price> findPriceById(long id);
}
