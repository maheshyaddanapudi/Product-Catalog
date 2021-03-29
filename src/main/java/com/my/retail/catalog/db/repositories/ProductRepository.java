package com.my.retail.catalog.db.repositories;

import com.my.retail.catalog.db.entities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ProductRepository extends MongoRepository<Product, Long> {

	Optional<Product> findProductById(long id);

	List<Product> findAll();
}
