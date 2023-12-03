package com.abdelalielbihari.portfolio.util;

import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.model.AboutDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AboutMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    About toAbout(AboutDto aboutDto);

    AboutDto toAboutDto(About about);

    List<AboutDto> toAboutDtoList(List<About> abouts);
}
