
package com.farmket.repository;

import com.farmket.model.Enquiry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EnquiryRepository
        extends MongoRepository<Enquiry, String> {

    List<Enquiry> findByFarmerEmail(String farmerEmail);

    List<Enquiry> findByBuyerEmail(String buyerEmail);
}
