package ru.alov.market.auth.tests;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.alov.market.api.dto.UserProfileDto;
import ru.alov.market.auth.utils.JwtTokenUtil;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class JwtTokenUtilTest {

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Test
  public void generateTokenTest() throws InterruptedException {
    UserProfileDto user =
        new UserProfileDto(1L, "Vova Pupkin", "email", List.of("ROLE_ADMIN"), "CONFIRMED");

    String token = jwtTokenUtil.generateAccessToken(user);

    String username = jwtTokenUtil.getUsernameFromAccessToken(token);
    List<String> roleList = jwtTokenUtil.getRolesFromAccessToken(token);

    Assertions.assertEquals(username, "Vova Pupkin");
    Assertions.assertEquals(1, roleList.size());
    Assertions.assertEquals(roleList.get(0), "ROLE_ADMIN");

    Thread.sleep(1000);
    Assertions.assertThrows(
        ExpiredJwtException.class, () -> jwtTokenUtil.getUsernameFromAccessToken(token));
  }
}
