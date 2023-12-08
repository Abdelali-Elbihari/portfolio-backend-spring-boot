package com.abdelalielbihari.portfolio.repository;

import com.abdelalielbihari.portfolio.model.About;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutRepository extends MongoRepository<About, String> {

}
