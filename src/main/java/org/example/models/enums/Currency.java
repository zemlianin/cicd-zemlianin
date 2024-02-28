package org.example.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets Currency
 */
public enum Currency {
  RUB("RUB"),
    CNY("CNY"),
    EUR("EUR"),
    USD("USD"),
    GBP("GBP");

  private String value;

  Currency(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Currency fromValue(String text) {
    for (Currency b : Currency.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
