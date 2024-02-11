package com.abdelalielbihari.portfolio.util;

import com.abdelalielbihari.portfolio.domain.User;
import com.abdelalielbihari.portfolio.dto.UserDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

  @Mappings({
      @Mapping(target = "id", ignore = true)
  })
  User toUser(UserDto userDto);

  UserDto toUserDto(User user);

  List<UserDto> toUserDtoList(List<User> users);
}
