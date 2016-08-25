package amaro.api.Model.Product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

import amaro.api.Model.Product.Variants.ProductVariant;
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
public class ProductSearch {
  private String name;
  private String style;
  private String code_color;
  private Set<ProductVariant> variants;
  private boolean on_sale;
  private String regular_price;
  private String actual_price;
  private String discount_percentage;
  private String installments;
  private String image_url;
  private String image_url_mobile;
  private String link_url;
}
