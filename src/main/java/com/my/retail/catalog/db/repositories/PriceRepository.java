package com.my.retail.catalog.db.repositories;

import com.my.retail.catalog.db.entities.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface PriceRepository extends MongoRepository<Price, Long> {
    Optional<Price> findPriceById(long id);
}
