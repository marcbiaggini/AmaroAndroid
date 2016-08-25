package amaro.amaroandroid;


import android.support.test.runner.AndroidJUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import amaro.amaroandroid.Helpers.CpfHelper;
import amaro.amaroandroid.Helpers.MainHelper;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.GenericResponse;
import amaro.api.Model.MyAccount.OrderInfo.OrdersResponse;
import amaro.api.Model.MyAccount.UserInfo.ExistUserResponse;
import amaro.api.Model.MyAccount.UserInfo.UserResponse;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;

import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Created by juan.villa on 24/05/2016.
 */
@RunWith(AndroidJUnit4.class)
public class MyAccountScenarios {

  private CpfHelper cpfHelper =new CpfHelper();
  String cpf = cpfHelper.setCPF();
  int userNumber =(int)(Math.random()*9999);

  MainHelper helper = new MainHelper();
  MultiValueMap<String, Object> loginMap = helper.createUser();


  @Before
  public void setCpf(){
    while (!cpfHelper.isValidCPF(cpf)){
      cpf = cpfHelper.setCPF();
    }
  }

  @Test
  public void loginUserEmail() {
    GenericResponse login = AmaroServices.getMyAccount().logIn(loginMap);
    assertEquals("Success.", login.getMsg());
  }

  @Test
  public void loginUserCPF() {
    MultiValueMap<String, Object> loginCpfMap = new LinkedMultiValueMap<String, Object>();
    loginCpfMap.add("email", loginMap.getFirst("cpf"));
    loginCpfMap.add("password",  loginMap.getFirst("password"));
    GenericResponse login = AmaroServices.getMyAccount().logIn(loginCpfMap);
    assertEquals("Success.", login.getMsg());
  }

  @Test
  public void loginEmptyPassword() {
    MultiValueMap<String, Object> loginEmptyPasswordMap = new LinkedMultiValueMap<String, Object>();
    loginEmptyPasswordMap.add("email", loginMap.getFirst("email"));
    loginEmptyPasswordMap.add("password", "");
    GenericResponse login = AmaroServices.getMyAccount().logIn(loginEmptyPasswordMap);
    assertEquals("Credenciais vazias.", login.getMsg());
  }

  @Test
  public void loginEmptyEmail() {
    MultiValueMap<String, Object> loginEmptyEmailMap = new LinkedMultiValueMap<String, Object>();
    loginEmptyEmailMap.add("email", "");
    loginEmptyEmailMap.add("password", "12345678");
    GenericResponse login = AmaroServices.getMyAccount().logIn(loginEmptyEmailMap);
    assertEquals("Credenciais vazias.", login.getMsg());
  }

  @Test
  public void logInWrongPassword(){
    MultiValueMap<String, Object> loginWrongPasswordMap = new LinkedMultiValueMap<String, Object>();
    loginWrongPasswordMap.add("email", loginMap.getFirst("email"));
    loginWrongPasswordMap.add("password", loginMap.getFirst("password")+"1");
    GenericResponse login = AmaroServices.getMyAccount().logIn(loginWrongPasswordMap);
    assertEquals("Senha inválida.",login.getMsg());
  }

  @Test
  public void logOut(){
    GenericResponse logOut = AmaroServices.getMyAccount().logOut();
    assertEquals("Success.",logOut.getMsg());
  }

  @Test
  public void userExistEmail() {
    ExistUserResponse existUserResponse = AmaroServices.getMyAccount().existUser((String) loginMap.getFirst("email"));
    assertEquals("true",existUserResponse.getMsg());
  }

  @Test
  public void userExistCPF() {
    ExistUserResponse existUserResponse = AmaroServices.getMyAccount().existUser((String) loginMap.getFirst("cpf"));
    assertEquals("true",existUserResponse.getMsg());
  }

  @Test
  public void userNoExist() {
    ExistUserResponse existUserResponse = AmaroServices.getMyAccount().existUser(loginMap.getFirst("email")+".br");
    assertEquals("false",existUserResponse.getMsg());
  }

  @Test
  public void signUp() {
    MultiValueMap<String, Object> signMap = new LinkedMultiValueMap<String, Object>();
    signMap.add("email", loginMap.getFirst("email"));
    signMap.add("bd_day", "06");
    signMap.add("bd_month", "10");
    GenericResponse signUp = AmaroServices.getMyAccount().signUp(signMap);
    assertEquals("Email cadastrado com sucesso.",signUp.getMsg());
  }

  @Test
  public void signUpEmptyEmail() {
    MultiValueMap<String, Object> signMap = new LinkedMultiValueMap<String, Object>();
    signMap.add("email", "");
    signMap.add("bd_day", "06");
    signMap.add("bd_month", "10");
    GenericResponse signUp = AmaroServices.getMyAccount().signUp(signMap);
    assertEquals("Email inválido.",signUp.getMsg());
  }

  @Test
  public void recoverPassword(){
    String email = (String) loginMap.getFirst("email");
    GenericResponse recoverPassword = AmaroServices.getMyAccount().recoverPassword(email);
    assertEquals("Instruções de recuperação de senha foram encaminhadas para "+email+".",recoverPassword.getMsg());
  }

  @Test
  public void createUser(){
    MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<String, Object>();
    userMap.add("email", "user"+String.valueOf(userNumber)+"@test.com");
    userMap.add("name", "User"+String.valueOf(userNumber));
    userMap.add("lastname", "Test");
    userMap.add("cpf", cpf);
    userMap.add("phone", "(11) 98433-8921");
    userMap.add("birthdate", "06/10/1983");
    userMap.add("password", "12345678");
    GenericResponse createUser = AmaroServices.getMyAccount().createUser(userMap);
    try{
      assertEquals("Success.",createUser.getMsg());
    }catch (ComparisonFailure e){
      assertEquals("Já existe uma conta cadastrada com CPF "+userMap.getFirst("cpf")+".",createUser.getMsg());
    }
  }

  @Test
  public void createUserEmptyEmail(){
    MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<String, Object>();
    userMap.add("email", "");
    userMap.add("name", "User"+String.valueOf(userNumber));
    userMap.add("lastname", "Test");
    userMap.add("cpf", cpf);
    userMap.add("phone", "(11) 98433-8921");
    userMap.add("birthdate", "06/10/1983");
    userMap.add("password", "12345678");
    GenericResponse createUser = AmaroServices.getMyAccount().createUser(userMap);
    assertEquals("O CPF "+userMap.getFirst("cpf")+" não é válido para o e-mail . Entre em contato com o nosso SAC.",createUser.getMsg());
  }

  @Test
  public void createUserWrongEmail(){
    MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<String, Object>();
    userMap.add("email", "email");
    userMap.add("name", "User"+String.valueOf(userNumber));
    userMap.add("lastname", "Test");
    userMap.add("cpf", cpf);
    userMap.add("phone", "(11) 98433-8921");
    userMap.add("birthdate", "06/10/1983");
    userMap.add("password", "12345678");
    GenericResponse createUser = AmaroServices.getMyAccount().createUser(userMap);
    assertEquals(userMap.getFirst("email")+" não é um e-mail válido!",createUser.getMsg());
  }

  @Test
  public void accountInfo(){
    UserResponse getMyAccountInfo = AmaroServices.getMyAccount().getMyAccountInfo();
    assertNotEquals("0",getMyAccountInfo.getUser().getId());
  }


}
