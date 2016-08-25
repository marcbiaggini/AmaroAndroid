package amaro.api.ServiceInterfaces;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Header;
import org.androidannotations.rest.spring.annotations.Headers;
import org.androidannotations.rest.spring.annotations.Part;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresCookie;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.annotations.SetsCookie;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;

import amaro.api.BuildConfig;
import amaro.api.Model.GenericResponse;
import amaro.api.Model.MyAccount.OrderInfo.OrderResponse;
import amaro.api.Model.MyCheckOut.ConfirmOrderResponse;

/**
 * Created by juan.villa on 03/05/2016.
 */
//@SetsCookie("_aecp_dev")
@Rest(rootUrl = BuildConfig.DEV_API_URL+"mycheckout", converters = { MappingJackson2HttpMessageConverter.class , FormHttpMessageConverter.class})
@RequiresCookie({BuildConfig.SESSION,BuildConfig.AUTENTICATION})
public interface MyCheckOut {

  @Get("/confirm_order/{order_number}")
  ConfirmOrderResponse confirmOrder(@Path final String order_number);

  /**
   * Validate the choosen payment option.
   *
   * @param formParams {@link MultiValueMap}
   *      must to have the keys
   *      cc_number -> String
   *      cc_cvv -> String
   *      cc_expiration_month -> String
   *      cc_expiration_year -> String
   *      cc_cardholder -> String
   *      installments -> String
   *      cc_number -> String
   *      cc_cvv -> String
   */
  @Post("/validate_payment")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  @SetsCookie(BuildConfig.SESSION)
  GenericResponse validatePayment(@Body MultiValueMap formParams);

  @Post("/confirm_purchase")
  @Headers(@Header(name ="Content-Type", value="application/json"))
  @SetsCookie(BuildConfig.SESSION)
  GenericResponse confirmPurchase();

  void setCookie(String name, String value);
  String getCookie(String name);
}
