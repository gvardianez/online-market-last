package ru.alov.market.analytics.repositories.projections;

import java.math.BigDecimal;

public interface AnalyticalProductsQuantityAndCostRating {
  Long getProductId();

  Integer getQuantity();

  BigDecimal getCost();
}
