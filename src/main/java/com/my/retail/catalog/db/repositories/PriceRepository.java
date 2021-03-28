package com.my.retail.catalog.db.repositories;

import com.my.retail.catalog.db.entities.Price;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface PriceRepository extends CrudRepository<Price, Long> {
    Optional<Price> findPriceById(long id);
}
