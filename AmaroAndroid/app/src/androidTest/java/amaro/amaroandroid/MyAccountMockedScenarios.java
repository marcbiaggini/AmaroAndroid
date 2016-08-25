package amaro.amaroandroid;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import amaro.amaroandroid.Helpers.MainHelper;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.GenericResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Created by juan.villa on 03/06/2016.
 */
@RunWith(AndroidJUnit4.class)
public class MyAccountMockedScenarios {

  @Mock
  GenericResponse loginSucess, loginEmpty,logInWrongPassword;
  @Mock
  AmaroServices amaroServices = new AmaroServices();
  public MultiValueMap<String, Object> loginMap = new LinkedMultiValueMap<>();

  MainHelper helper = new MainHelper();

  @Before
  public void setMocks() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(loginSucess.getMsg()).thenReturn("Success.");
    Mockito.when(loginEmpty.getMsg()).thenReturn("Credenciais vazias.");
    Mockito.when(logInWrongPassword.getMsg()).thenReturn("Senha inválida.");
    loginMap.clear();
  }

  @Test
  public void loginSucess()  {
    try{
      loginMap = helper.createUser();
      amaroServices.getMyAccount().logIn(loginMap);
    }catch (Exception e){

    }
    String mesage = loginSucess.getMsg();
    Mockito.verify(loginSucess).getMsg();
  }

  @Test
  public void loginEmptyEmail(){
    loginMap.add("email", "");
    loginMap.add("password", "12345678");
    try{
      amaroServices.getMyAccount().logIn(loginMap);
    }catch (Exception e){

    }
    String mesage = loginEmpty.getMsg();
    Mockito.verify(loginEmpty).getMsg();
  }

  @Test
  public void loginEmptyPassword(){
    loginMap.add("email", "email@test.com");
    loginMap.add("password", "");
    try{
      amaroServices.getMyAccount().logIn(loginMap);
    }catch (Exception e){

    }
    String mesage = loginEmpty.getMsg();
    Mockito.verify(loginEmpty).getMsg();
  }


  @Test
  public void loginUserCPF() {
    try{
      loginMap = helper.createUser();
      MultiValueMap<String, Object> loginCpfMap = new LinkedMultiValueMap<>();
      loginCpfMap.add("email", loginMap.getFirst("cpf"));
      loginCpfMap.add("password",  loginMap.getFirst("password"));
      amaroServices.getMyAccount().logIn(loginCpfMap);
    }catch (Exception e){

    }
    String mesage = loginSucess.getMsg();
    Mockito.verify(loginSucess).getMsg();
  }

  @Test
  public void loginWrongPassword(){
    try{
      loginMap = helper.createUser();
      MultiValueMap<String, Object> loginWrongPassword = new LinkedMultiValueMap<>();
      loginWrongPassword.add("email", loginMap.getFirst("email"));
      loginWrongPassword.add("password",  loginMap.getFirst("password")+"1");
      amaroServices.getMyAccount().logIn(loginWrongPassword);
    }catch (Exception e){

    }
    String mesage =logInWrongPassword.getMsg();
    Mockito.verify(logInWrongPassword).getMsg();
  }

  @Test
  public void logOut(){
    try {
      amaroServices.getMyAccount().logOut();
    }catch (Exception e){

    }
    String mesage = loginSucess.getMsg();
    Mockito.verify(loginSucess).getMsg();
  }
}
