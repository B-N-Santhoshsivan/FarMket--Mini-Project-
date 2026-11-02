
package com.farmket.repository;

import com.farmket.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository
        extends MongoRepository<Product, String> {

    List<Product> findByFarmerEmail(String farmerEmail);
}

