package ru.alov.market.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.ChangePasswordRequestDto;
import ru.alov.market.api.dto.RegisterUserDto;
import ru.alov.market.api.enums.RoleStatus;
import ru.alov.market.api.exception.FieldValidationException;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.auth.entities.User;
import ru.alov.market.auth.repositories.UserRepository;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;

  public Optional<User> findByUsername(@NotBlank String username) {
    return userRepository.findByUsername(username);
  }

  public Optional<User> findUserByEmail(@NotNull @Email String email) {
    return userRepository.findUserByEmail(email);
  }

  public Mono<User> findMonoByUsername(String username) {
    return Mono.just(getUser(username));
  }

  @Transactional
  public User createNewUser(@Valid RegisterUserDto registerUserDto) {
    if (!registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword())) {
      throw new ResourceNotFoundException("Введеные пароли не совпадают");
    }
    if (findByUsername(registerUserDto.getUsername()).isPresent())
      throw new IllegalStateException("Имя пользователя уже используется");
    if (findUserByEmail(registerUserDto.getEmail()).isPresent())
      throw new IllegalStateException("Email уже используется");
    User user = new User();
    user.setUsername(registerUserDto.getUsername());
    user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
    user.setEmail(registerUserDto.getEmail());
    user.setSubscriber(true);
    user.setRoles(List.of(roleService.getUserRole()));
    user.setEmailStatus(User.UserStatus.MAIL_NOT_CONFIRMED.toString());
    return userRepository.save(user);
  }

  public void confirmUserEmail(@NotBlank String username, @NotNull @Email String email) {
    User user = getUser(username);
    if (!user.getEmail().equals(email)) {
      throw new IllegalStateException(
          "Email пользователя не совпадает с указанным при регистрации");
    }
    user.setEmailStatus(User.UserStatus.MAIL_CONFIRMED.toString());
    userRepository.save(user);
  }

  public void changePassword(
      @NotBlank String username, @Valid ChangePasswordRequestDto changePasswordRequestDto) {
    if (!changePasswordRequestDto
        .getNewPassword()
        .equals(changePasswordRequestDto.getConfirmNewPassword())) {
      throw new FieldValidationException("Введеные пароли не совпадают");
    }
    User user = getUser(username);
    if (!passwordEncoder.matches(changePasswordRequestDto.getOldPassword(), user.getPassword()))
      throw new BadCredentialsException("Неверно указан старый пароль");
    user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getNewPassword()));
    userRepository.save(user);
  }

  public String recoverPassword(@NotBlank String username) {
    User user = getUser(username);
    if (user.getEmailStatus().equals(User.UserStatus.MAIL_NOT_CONFIRMED.toString()))
      throw new IllegalStateException("Для восстановления пароля необходимо подтвредить Email");
    String newPassword = String.valueOf((int) (Math.random() * (10000000)));
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    return newPassword;
  }

  public List<String> getSubscribersEmails(@NotBlank String role) {
    if (role.equals(RoleStatus.ROLE_ADMIN.toString())) {
      return userRepository.findAllBySubscriberTrue().stream()
          .map(User::getEmail)
          .collect(Collectors.toList());
    } else throw new SecurityException("Недостаточно прав доступа");
  }

  private User getUser(String username) {
    return findByUsername(username)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("Пользователь '%s' не найден", username)));
  }
}
