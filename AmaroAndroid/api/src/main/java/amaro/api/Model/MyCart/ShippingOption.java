package amaro.api.Model.MyCart;

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
public class ShippingOption {

  @JsonProperty("quote_id")
  private String quote_id;
  private String id;
  private String name;
  private String method_name;
  private String rate;
  private String std_rate;
  private String rate_num;
  private String cost_num;
  private String std_rate_num;
  private String delivery_time;
  private String days;
  private String checked;

}
