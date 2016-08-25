package amaro.api.Model.Product.Variants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

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
public class ProductVariant {
  private boolean available;
  private boolean selected;
  private String color_slug;
  private String color;
  private boolean on_sale;
  private String regular_price;
  private String actual_price;
  private String discount_percentage;
  private String installments;
  private String swatch_url;//Estampa do produto
  private String video_code;
  private  ProductImage image_desktop;
  private  ProductImage image_mobile;
  private Collection<ProductSize> sizes;
}
