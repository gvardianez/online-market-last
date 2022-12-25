package ru.alov.market.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.alov.market.api.dto.JwtRequestDto;
import ru.alov.market.api.dto.JwtResponseDto;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.auth.converters.UserConverter;
import ru.alov.market.auth.entities.User;
import ru.alov.market.auth.utils.JwtTokenUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Service
@Validated
@RequiredArgsConstructor
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final JwtTokenUtil jwtTokenUtil;
  private final RedisTemplate<String, Object> redisTemplate;
  private final UserConverter userConverter;

  @Value("${utils.username.prefix}")
  private String usernamePrefix;

  public JwtResponseDto getJwtTokens(@Valid JwtRequestDto jwtRequestDto) {
    User user = authenticateUser(jwtRequestDto.getUsername(), jwtRequestDto.getPassword());
    String accessToken = jwtTokenUtil.generateAccessToken(userConverter.entityToDto(user));
    String refreshToken = jwtTokenUtil.generateRefreshToken(userConverter.entityToDto(user));
    String key = usernamePrefix + user.getUsername();
    redisTemplate.opsForValue().set(key, refreshToken);
    return new JwtResponseDto(accessToken, refreshToken);
  }

  public JwtResponseDto refreshJwtTokens(@NotBlank String refreshToken) {
    String key = usernamePrefix + jwtTokenUtil.getUsernameFromRefreshToken(refreshToken);
    if (!redisTemplate.hasKey(key)) {
      throw new ResourceNotFoundException("Вы не найдены в системе. Пожалуйста, перезайдите");
    }
    String savedRefreshToken = (String) redisTemplate.opsForValue().get(key);
    if (savedRefreshToken != null && !savedRefreshToken.equals(refreshToken)) {
      throw new IllegalStateException("Неверный токен обновления. Пожалуйста перезайдите");
    }
    UserProfileDto userProfileDto =
        new UserProfileDto(
            jwtTokenUtil.getUsernameFromRefreshToken(refreshToken),
            jwtTokenUtil.getEmailFromRefreshToken(refreshToken),
            jwtTokenUtil.getRolesFromRefreshToken(refreshToken));
    String newAccessToken = jwtTokenUtil.generateAccessToken(userProfileDto);
    String newRefreshToken = jwtTokenUtil.generateRefreshToken(userProfileDto);
    redisTemplate.opsForValue().set(key, newRefreshToken);
    return new JwtResponseDto(newAccessToken, newRefreshToken);
  }

  public User authenticateUser(@NotBlank String username, @NotBlank String password) {
    User user =
        userService
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format("Пользователь '%s' не найден", username)));
    if (!passwordEncoder.matches(password, user.getPassword()))
      throw new BadCredentialsException("Неверный пароль");
    return user;
  }
}
