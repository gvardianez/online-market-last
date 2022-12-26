package ru.alov.market.auth.converters;

import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.auth.entities.Role;
import ru.alov.market.auth.entities.User;

import java.util.stream.Collectors;

@Component
public class UserConverter {

  public UserProfileDto entityToDto(User user) {
    return new UserProfileDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
        user.getEmailStatus());
  }
}
