package com.abdelalielbihari.portfolio.util;

import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.domain.Skill;
import com.abdelalielbihari.portfolio.dto.AboutDto;
import com.abdelalielbihari.portfolio.dto.SkillDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface SkillMapper {

  @Mappings({
      @Mapping(target = "id", ignore = true)
  })
  Skill toSkill(SkillDto skillDto);
  SkillDto toSkillDto(Skill skill);
  List<Skill> toSkills(List<SkillDto> skillDtos);
  List<SkillDto> toSkillDtos(List<Skill> skills);

}
