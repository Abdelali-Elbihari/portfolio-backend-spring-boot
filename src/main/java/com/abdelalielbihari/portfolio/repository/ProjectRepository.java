package com.abdelalielbihari.portfolio.repository;

import com.abdelalielbihari.portfolio.domain.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

}
