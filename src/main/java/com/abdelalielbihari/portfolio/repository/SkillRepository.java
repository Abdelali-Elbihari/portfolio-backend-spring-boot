package com.abdelalielbihari.portfolio.repository;

import com.abdelalielbihari.portfolio.domain.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends MongoRepository<Skill, String> {

}
