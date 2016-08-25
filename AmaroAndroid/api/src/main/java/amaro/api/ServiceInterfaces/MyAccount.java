package amaro.api.ServiceInterfaces;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Header;
import org.androidannotations.rest.spring.annotations.Headers;
import org.androidannotations.rest.spring.annotations.Part;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresCookie;
import org.androidannotations.rest.spring.annotations.RequiresCookieInUrl;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.annotations.SetsCookie;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import org.springframework.util.MultiValueMap;;
import amaro.api.BuildConfig;
import amaro.api.Model.MyAccount.UserInfo.ExistUserResponse;
import amaro.api.Model.GenericResponse;
import amaro.api.Model.MyAccount.OrderInfo.OrderResponse;
import amaro.api.Model.MyAccount.OrderInfo.OrdersResponse;
import amaro.api.Model.MyAccount.ShippingInfo.ShippingAddressResponse;
import amaro.api.Model.MyAccount.UserInfo.UserResponse;
import lombok.experimental.NonFinal;

/**
 * Created by juan.villa on 02/05/2016.
 */
@NonFinal
@Rest(rootUrl = BuildConfig.DEV_API_URL+"myaccount", converters = { MappingJackson2HttpMessageConverter.class , FormHttpMessageConverter.class})
@RequiresCookie({BuildConfig.SESSION,BuildConfig.AUTENTICATION})
public interface MyAccount extends RestClientErrorHandling {

  @Get("/get")
  UserResponse getMyAccountInfo();

  @Get("/get_user")
  UserResponse getUserInfo();

  @Get("/get_orders")
  OrdersResponse getOrders();

  @Get("/get_order/{order_id}")
  OrderResponse getOrder(@Path final String order_id);

  @Get("/get_shipping_addresses")
  ShippingAddressResponse getShippingAddresses();

  /**
   * Login inside user account.
   *
   * @param formParams {@link MultiValueMap}
   *      must to have the keys
   *      email/cpf
   *      password
   */
  @Post("/login")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  @SetsCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
  GenericResponse logIn(@Body MultiValueMap formParams);

  @Post("/logout")
  @SetsCookie({"_aecp_dev_user","_aecp_dev"})
  GenericResponse logOut();

  /**
   * Create a new User.
   *
   * @param formParams {@link MultiValueMap}
   *      must to have the keys
   *      email
   *      name
   *      lastname
   *      cpf
   *      phone (xx) xxxxx-xxxx
   *      birthdate xx/xx/xxxx
   *      password
   */
  @Post("/create")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  @SetsCookie({BuildConfig.AUTENTICATION,BuildConfig.SESSION})
  GenericResponse createUser(@Body MultiValueMap formParams);

  @Post("/recover_password")
  GenericResponse recoverPassword(@Part String email);

  /**
   * Sign Up into news and novelties.
   *
   * @param formParams {@link MultiValueMap}
   *      must to have the keys
   *      email
   *      bd_day [optional]
   *      bd_month [optional]
   */
  @Post("/signup")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  GenericResponse signUp(@Body MultiValueMap formParams);

  /**
   * Used for add a new shipping address everything is generated from a CEP code .
   *   that comes from lookup_zip + apply_zip(for shipping estimates in cart area)
   *
   * @param formParams {@link MultiValueMap}
   *      must to have the keys
   *      name
   *      lastname
   *      zip
   *      street
   *      number
   *      adjunct
   *      neighborhood
   *      city
   *      state
   *      shipping_state
   */
  @Post("/save_shipping_info")
  @Headers(@Header(name ="Content-Type", value="application/x-www-form-urlencoded"))
  GenericResponse saveShippingInfo(@Body MultiValueMap formParams);

  @Post("/exists")
  @Headers(@Header(name ="Content-Type", value="application/json"))
  ExistUserResponse existUser(@Part String email);

  void setCookie(String name, String value);
  String getCookie(String name);

}
