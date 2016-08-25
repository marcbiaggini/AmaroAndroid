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
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import org.springframework.util.MultiValueMap;

import amaro.api.BuildConfig;
import amaro.api.Model.GenericResponse;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;
import amaro.api.Model.MyCart.LookupZipResponse;
import amaro.api.Model.MyCart.PaymentInfo.GetPaymentResponse;
import amaro.api.Model.MyCart.PaymentInfo.SelectPaymentResponse;


/**
 * Created by juan.villa on 03/05/2016.
 */
@Rest(rootUrl = BuildConfig.DEV_API_URL+"mycart", converters = {StringHttpMessageConverter.class,ResourceHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class , FormHttpMessageConverter.class})
@RequiresCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
public interface MyCart extends RestClientErrorHandling {

  @Get("/get")
  GetCartResponse getCart();

  @Get("/lookup_zip/{zip}")
  LookupZipResponse lookupZip(@Path final String zip);

  @Get("/get_payment_methods")
  GetPaymentResponse getPaymentMethods();


  @Post("/validate_availability")
  GenericResponse validateAvailability();

  @Post("/destroy")
  @SetsCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
  GetCartResponse destroyCart();

  /**
   * Add to user Cart a specific item.
   *
   * @param formParams {@link MultiValueMap}
   *      must to have the keys
   *      sku -> String
   *      quantity -> String (is optional default value is 1)
   */
  @Post("/add")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  @SetsCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
  GetCartResponse addToCart(@Body MultiValueMap formParams);

  @Post("/delete")
  @Headers(@Header(name ="Content-Type", value="application/json"))
  @SetsCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
  GetCartResponse delete(@Part String sku);


  @Post("/subtract")
  @Headers(@Header(name ="Content-Type", value="application/json"))
  @SetsCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
  GetCartResponse subtract(@Part String sku);

  /**
   * Asociate an specific adress
   * to calculate shipping value for User.
   *
   * @param formParams {@link MultiValueMap}
   *      zip -> String
   */
  @Post("/apply_zip")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  @SetsCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
  GetCartResponse applyZip(@Body MultiValueMap formParams);

  /**
   * Select payment options for User.
   *
   * @param formParams {@link MultiValueMap}
   *      must to have the keys
   *      payment_type -> String
   *      option_key -> String
   */
  @Post("/select_payment_method")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  SelectPaymentResponse selectPaymentMethod(@Body MultiValueMap formParams);

  /**
   * Select shipping options for User.
   *
   * @param formParams {@link MultiValueMap}
   *      must to have the keys
   *     shipping_service_id -> String
   */
  @Post("/select_shipping_service")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  @SetsCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
  GetCartResponse selectShippingService(@Body MultiValueMap formParams);

  /**
   * This method is used to Input payment coupon .
   *
   * @param formParams {@link MultiValueMap}
   *      must to have the keys
   *      coupon_code -> String
   */
  @Post("/apply_coupon")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  @SetsCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
  GetCartResponse applyCoupon(@Body MultiValueMap formParams);

  void setCookie(String name, String value);
  String getCookie(String name);
}
