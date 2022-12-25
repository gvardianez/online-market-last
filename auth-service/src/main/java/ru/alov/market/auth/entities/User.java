package ru.alov.market.auth.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Setter
@Getter
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotBlank
  @Column(name = "username")
  private String username;

  @NotNull
  @Pattern(
      regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
      message =
          "Пароль должен содержать минимум одну цифру, хотя бы одну заглавную и одну прописную латинскую букву, хотя бы один спец символ(@#$%^&+=), не содержит пробелов и должен быть длиной не меннее 8 символов")
  @Column(name = "password")
  private String password;

  @NotNull
  @Email
  @Column(name = "email")
  private String email;

  @NotBlank
  @Column(name = "email_status")
  private String emailStatus;

  @NotNull
  @Column(name = "subscriber")
  private Boolean subscriber;

  @NotEmpty
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Collection<Role> roles;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public enum UserStatus {
    MAIL_CONFIRMED,
    MAIL_NOT_CONFIRMED
  }
}
