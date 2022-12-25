package ru.alov.market.auth.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.UserProfileDto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtil {

  @Value("${jwt.access.secret}")
  private String accessTokenSecret;

  @Value("${jwt.access.lifetime}")
  private Integer accessTokenLifetime;

  @Value("${jwt.refresh.secret}")
  private String refreshTokenSecret;

  @Value("${jwt.refresh.lifetime}")
  private Integer refreshTokenLifetime;

  public String generateAccessToken(UserProfileDto userProfileDto) {
    return getToken(userProfileDto, accessTokenLifetime, accessTokenSecret);
  }

  public String generateRefreshToken(UserProfileDto userProfileDto) {
    return getToken(userProfileDto, refreshTokenLifetime, refreshTokenSecret);
  }

  private String getToken(
      UserProfileDto userProfileDto, Integer tokenLifetime, String tokenSecret) {
    Map<String, Object> claims = new HashMap<>();
    List<String> rolesList = userProfileDto.getRoles();
    claims.put("roles", rolesList);
    claims.put("email", userProfileDto.getEmail());

    Date issuedDate = new Date();
    Date expiredDate = new Date(issuedDate.getTime() + tokenLifetime);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(userProfileDto.getUsername())
        .setIssuedAt(issuedDate)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS256, tokenSecret)
        .compact();
  }

  private Claims getAllClaimsFromToken(String token, String secret) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  public String getEmailFromAccessToken(String token) {
    return getAllClaimsFromToken(token, accessTokenSecret).get("email", String.class);
  }

  public String getUsernameFromAccessToken(String token) {
    return getAllClaimsFromToken(token, accessTokenSecret).getSubject();
  }

  public List<String> getRolesFromAccessToken(String token) {
    return getAllClaimsFromToken(token, accessTokenSecret).get("roles", List.class);
  }

  public String getEmailFromRefreshToken(String token) {
    return getAllClaimsFromToken(token, refreshTokenSecret).get("email", String.class);
  }

  public String getUsernameFromRefreshToken(String token) {
    return getAllClaimsFromToken(token, refreshTokenSecret).getSubject();
  }

  public List<String> getRolesFromRefreshToken(String token) {
    return getAllClaimsFromToken(token, refreshTokenSecret).get("roles", List.class);
  }
}
