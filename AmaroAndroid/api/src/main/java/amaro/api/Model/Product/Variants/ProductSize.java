package amaro.api.Model.Product.Variants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 01/08/2016.
 */
@Wither
@NonFinal
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSize {
  private boolean available;
  private String size;
  private String sku;
  private boolean on_sale;
  private String regular_price;
  private String actual_price;
  private String discount_percentage;
  private String installments;
}
