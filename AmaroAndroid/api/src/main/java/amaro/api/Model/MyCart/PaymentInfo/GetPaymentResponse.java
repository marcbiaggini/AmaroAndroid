package amaro.api.Model.MyCart.PaymentInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import amaro.api.Model.MyCart.CartInfo.Cart;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 09/05/2016.
 */
@Wither
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPaymentResponse {

  private String code;
  private String msg;
  private ResponsePayment response;
  private Cart cart;
}
