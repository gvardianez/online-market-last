package ru.alov.market.core.converters;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.alov.market.api.dto.PageDto;

@Component
public class PageConverter {

  public <T> PageDto<T> entityToDto(Page<T> page) {
    return new PageDto<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getNumberOfElements(),
        page.getTotalElements(),
        page.getTotalPages());
  }
}
