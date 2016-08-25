package amaro.api.Model.Product.Related;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 12/08/2016.
 */
@Wither
@NonFinal
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedProduct {
  private String name;
  private String color;
  private String on_sale;
  private String regular_price;
  private String actual_price;
  private String discount_percentage;
  private String installments;
  private String image_url;
  private String image_url_mobile;
  private String link_url;
}
