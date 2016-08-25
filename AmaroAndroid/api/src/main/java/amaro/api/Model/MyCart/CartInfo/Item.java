package amaro.api.Model.MyCart.CartInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 05/05/2016.
 */
@Wither
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

  @JsonProperty("sku")
  private String sku;
  private boolean available;
  private String name;
  private String price_num;
  private String price;
  private String color;
  private String size;
  private String quantity;
  private String weight;
  private boolean gift;
  private String thumb;
  private String credits;
}
