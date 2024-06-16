package org.example.models.dao;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.example.models.enums.Currency;
import org.springframework.validation.annotation.Validated;

/**
 * RatesResposne
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-02-27T23:37:12.135063400+03:00[Europe/Moscow]")


public class RatesResponse {
  @JsonProperty("base")
  private Currency base = null;

  @JsonProperty("rates")
  private Map<String, BigDecimal> rates = null;

  public RatesResponse base(Currency base) {
    this.base = base;
    return this;
  }

  /**
   * Get base
   * @return base
   **/
  @Schema(description = "")

    public Currency getBase() {
    return base;
  }

  public void setBase(Currency base) {
    this.base = base;
  }

  public RatesResponse rates(Map<String, BigDecimal> rates) {
    this.rates = rates;
    return this;
  }

  public RatesResponse putRatesItem(String key, BigDecimal ratesItem) {
    if (this.rates == null) {
      this.rates = new HashMap<String, BigDecimal>();
    }
    this.rates.put(key, ratesItem);
    return this;
  }

  /**
   * Get rates
   * @return rates
   **/
    public Map<String, BigDecimal> getRates() {
    return rates;
  }

  public void setRates(Map<String, BigDecimal> rates) {
    this.rates = rates;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatesResponse ratesResponse = (RatesResponse) o;
    return Objects.equals(this.base, ratesResponse.base) &&
        Objects.equals(this.rates, ratesResponse.rates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(base, rates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatesResposne {\n");
    
    sb.append("    base: ").append(toIndentedString(base)).append("\n");
    sb.append("    rates: ").append(toIndentedString(rates)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
