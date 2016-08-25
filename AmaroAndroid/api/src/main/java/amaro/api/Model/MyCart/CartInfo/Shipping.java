package amaro.api.Model.MyCart.CartInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 07/07/2016.
 */
@Wither
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Shipping {
  @JsonProperty("quote_id")
  private String quote_id;
  private String id;
  private String name;
  private String method_name;
  private String rate;
  private String std_rate;
  private String delivery_time;
  private String checked;
}
