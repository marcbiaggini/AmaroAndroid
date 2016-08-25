package amaro.api.Model.MyAccount.ShippingInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 04/05/2016.
 */
@Wither
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingAddressResponse {

  @JsonProperty("code")
  private String code;
  private String msg;
  @JsonProperty("response")
  private ShippingAddressCollection response;

}
