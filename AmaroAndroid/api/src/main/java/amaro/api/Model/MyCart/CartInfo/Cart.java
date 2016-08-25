package amaro.api.Model.MyCart.CartInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

import amaro.api.Model.MyAccount.ShippingInfo.Address;
import amaro.api.Model.MyCart.PaymentInfo.SelectedPaymentOption;
import amaro.api.Model.MyAccount.UserInfo.User;
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
public class Cart {

  @JsonProperty("user")
  private Set<User> userSet;
  private Set<Item> items;
  private String subtotal;
  private String shipping_total;
  private SelectedPaymentOption selected_payment_option;
  private boolean has_credits;
  private boolean has_giveaway;
  private boolean has_discount;
  private boolean has_promotion;
  private String discount;
  private String total;
  private Double total_num;
  private Double total_due_num;
  private boolean wait;
  private int num_items;
  private String checksum;
  private Set<CouponPromotions> coupon_promotions;
  private Address shipping_address;
  private Set<Shipping> shipping_options;
  private Shipping selected_shipping_option;
  private Set<String> cart_promotions;
  private Double subtotal_num;
  private int shipping_total_num;
  private Double discount_num;
  private Double discount_pc;
  private String total_due;
  private String applied_zip;
}
