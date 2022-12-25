package ru.alov.market.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alov.market.api.enums.RoleStatus;
import ru.alov.market.api.exception.ResourceNotFoundException;
import ru.alov.market.auth.entities.Role;
import ru.alov.market.auth.repositories.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;

  public Role getUserRole() {
    return roleRepository
        .findByName(RoleStatus.ROLE_USER.toString())
        .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
  }

  public Role getAdminRole() {
    return roleRepository
        .findByName(RoleStatus.ROLE_ADMIN.toString())
        .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
  }
}
