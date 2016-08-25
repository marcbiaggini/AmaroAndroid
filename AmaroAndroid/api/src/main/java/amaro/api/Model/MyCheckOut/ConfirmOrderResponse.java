package amaro.api.Model.MyCheckOut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

import amaro.api.Model.MyCart.CartInfo.Item;
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
public class ConfirmOrderResponse {

  @JsonProperty("email")
  private String email;
  private String subtotal;
  private boolean hasDiscount;
  private String discount;
  private int discountPc;
  private String shippingPayedByCustomer;
  private String totalPayed;
  private boolean boleto;
  private String paymentType;
  private String paymentMethod;
  private boolean hasCredits;
  private String paymentCredits;
  private String installments;
  private String total;
  private String shippingService;
  private String eta;
  private String trackingCode;
  private String trackingURL;
  private String addressName;
  private String street;
  private String adjunct;
  private String addressNumber;
  private String neighborhood;
  private String zip;
  private String city;
  private String state;
  private String number;
  private String date;
  private String statusPt;
  private int confirmed;
  private Set<Item> items;
}
