package com.my.retail.catalog.db.repositories;

import com.my.retail.catalog.db.entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * main customer repository
 */
@Repository
@Transactional
public interface ProductRepository extends CrudRepository<Product, Long> {

	Optional<Product> findProductById(long id);

	List<Product> findAll();
}
