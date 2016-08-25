package amaro.amaroandroid.Helpers;

import android.util.Log;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import amaro.api.BuildConfig;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.GenericResponse;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;
import amaro.api.Model.MyCart.LookupZipResponse;
import amaro.api.Model.MyCart.PaymentInfo.GetPaymentResponse;

/**
 * Created by juan.villa on 30/05/2016.
 */
public class MainHelper {
  private CpfHelper cpfHelper =new CpfHelper();
  public void checkoutOrder(){
    AmaroServices.getMyCart().destroyCart();
    AmaroServices.getMyCart().delete("5807_339_0_PP");
    //Login
    MultiValueMap<String, Object> createdUser = createUser();
    MultiValueMap<String, Object> loginMap = new LinkedMultiValueMap<>();
    loginMap.add("email",createdUser.getFirst("email"));
    loginMap.add("password",createdUser.getFirst("password"));
    AmaroServices.getMyAccount().logIn(loginMap);
    AmaroServices.getMyCart().setCookie(BuildConfig.AUTENTICATION,AmaroServices.getMyAccount().getCookie(BuildConfig.AUTENTICATION));
    AmaroServices.getMyCart().setCookie(BuildConfig.SESSION,AmaroServices.getMyAccount().getCookie(BuildConfig.SESSION));
    AmaroServices.getMyCheckOut().setCookie(BuildConfig.AUTENTICATION,AmaroServices.getMyAccount().getCookie(BuildConfig.AUTENTICATION));
    AmaroServices.getMyCheckOut().setCookie(BuildConfig.SESSION,AmaroServices.getMyAccount().getCookie(BuildConfig.SESSION));


    //Add Cart
    MultiValueMap<String, Object> mvMap = new LinkedMultiValueMap<String, Object>();
    mvMap.add("sku", "5785_40130843_0_G");
    GetCartResponse addCart = AmaroServices.getMyCart().addToCart(mvMap);
    addCart = AmaroServices.getMyCart().addToCart(mvMap);
    //Look for Zip
    LookupZipResponse lookupZipResponse = AmaroServices.getMyCart().lookupZip("01254000");
    MultiValueMap<String, Object> zip = new LinkedMultiValueMap<String, Object>();
    zip.add("zip","01254000");
    AmaroServices.getMyCart().applyZip(zip);

    //Save Shiping Info
    MultiValueMap<String, Object> shippingInfoMap = new LinkedMultiValueMap<String, Object>();
    shippingInfoMap.add("name", "user");
    shippingInfoMap.add("lastname", "test");
    shippingInfoMap.add("zip", "01254000");
    shippingInfoMap.add("street", lookupZipResponse.getAddress().getStreet());
    shippingInfoMap.add("number", "326");
    shippingInfoMap.add("neighborhood", lookupZipResponse.getAddress().getNeighborhood());
    shippingInfoMap.add("city", lookupZipResponse.getAddress().getCity());
    shippingInfoMap.add("state", lookupZipResponse.getAddress().getUf());
    Log.e("apply Zip", "success");
    AmaroServices.getMyAccount().saveShippingInfo(shippingInfoMap);

    //Checkout
    GetPaymentResponse getPaymentResponse = AmaroServices.getMyCart().getPaymentMethods();
    MultiValueMap<String, Object> paymentMap = new LinkedMultiValueMap<String, Object>();
    paymentMap.add("payment-option", "11");
    paymentMap.add("cc_number", "4111111111111111");
    paymentMap.add("cc_cvv", "737");
    paymentMap.add("cc_expiration_month", "06");
    paymentMap.add("cc_expiration_year", "2016");
    paymentMap.add("cc_cardholder", "User Test");
    paymentMap.add("installments", "1");
    AmaroServices.getMyCart().getCart();
    AmaroServices.getMyCheckOut().validatePayment(paymentMap);
    AmaroServices.getMyCheckOut().confirmPurchase();
    AmaroServices.getMyCart().setCookie(BuildConfig.SESSION,AmaroServices.getMyCheckOut().getCookie(BuildConfig.SESSION));
    AmaroServices.getMyAccount().setCookie(BuildConfig.SESSION,AmaroServices.getMyCheckOut().getCookie(BuildConfig.SESSION));
  }

  public void cleanOrders(){
    //Create User
    MultiValueMap<String, Object> userCreated = createUser();
    //Login
    MultiValueMap<String, Object> loginMap = new LinkedMultiValueMap<String, Object>();
    loginMap.add("email",userCreated.getFirst("email") );
    loginMap.add("password", userCreated.getFirst("password"));
    AmaroServices.getMyAccount().logIn(loginMap);
    AmaroServices.getMyCart().setCookie(BuildConfig.AUTENTICATION,AmaroServices.getMyAccount().getCookie(BuildConfig.AUTENTICATION));
    AmaroServices.getMyCart().setCookie(BuildConfig.SESSION,AmaroServices.getMyAccount().getCookie(BuildConfig.SESSION));
    AmaroServices.getMyCheckOut().setCookie(BuildConfig.AUTENTICATION,AmaroServices.getMyAccount().getCookie(BuildConfig.AUTENTICATION));
    AmaroServices.getMyCheckOut().setCookie(BuildConfig.SESSION,AmaroServices.getMyAccount().getCookie(BuildConfig.SESSION));
  }

  public MultiValueMap<String, Object> createUser(){
    String cpf = cpfHelper.setCPF();
    while (!cpfHelper.isValidCPF(cpf)){
      cpf = cpfHelper.setCPF();
    }
    int userNumber =(int)(Math.random()*9999999);
    MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<String, Object>();
    userMap.add("email", "user"+String.valueOf(userNumber)+"@test.com");
    userMap.add("name", "User"+String.valueOf(userNumber));
    userMap.add("lastname", "Test");
    userMap.add("cpf", cpf);
    userMap.add("phone", "(11) 98433-8921");
    userMap.add("birthdate", "31/12/1990");
    userMap.add("password", "12345678");
    GenericResponse createUser = AmaroServices.getMyAccount().createUser(userMap);

    MultiValueMap<String, Object> userCreated = new LinkedMultiValueMap<String, Object>();
    if(createUser.getCode().equals("0")){
      userCreated.add("email", "user"+String.valueOf(userNumber)+"@test.com");
      userCreated.add("cpf", cpf);
      userCreated.add("password", "12345678");
      userCreated.add("name", "User"+String.valueOf(userNumber));
    }
    return userCreated;
  }
}
