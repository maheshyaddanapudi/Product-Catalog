package com.my.retail.catalog.config.external.db;

import com.my.retail.catalog.db.repositories.PriceRepository;
import com.my.retail.catalog.db.repositories.ProductRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = {ProductRepository.class, PriceRepository.class})
public class ExternalMongoDBConfiguration {

}
