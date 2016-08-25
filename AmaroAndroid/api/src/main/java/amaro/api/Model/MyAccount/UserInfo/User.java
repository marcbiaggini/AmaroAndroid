package amaro.api.Model.MyAccount.UserInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 02/05/2016.
 */
@Wither
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  @JsonProperty("id")
  private String id;
  @JsonProperty("email")
  private String email;
  private String birthdate;
  private String cpf;
  private String name;
  private String lastname;
  private String phone;
  private String credits;
  private String password;
  private boolean logged_in;
}
