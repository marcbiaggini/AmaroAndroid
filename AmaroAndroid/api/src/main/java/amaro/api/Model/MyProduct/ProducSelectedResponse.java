package amaro.api.Model.MyProduct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import amaro.api.Model.Product.ProductSelected;
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
public class ProducSelectedResponse {
  private String code;
  private String msg;
  private ProductSelected product;
}
