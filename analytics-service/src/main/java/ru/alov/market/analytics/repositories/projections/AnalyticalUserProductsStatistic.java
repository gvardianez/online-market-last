package ru.alov.market.analytics.repositories.projections;

import java.math.BigDecimal;

public interface AnalyticalUserProductsStatistic {
  String getUsername();

  Long getProductId();

  Integer getQuantity();

  BigDecimal getCost();
}
