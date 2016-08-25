package amaro.api.Model.MyAccount.ShippingInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

import amaro.api.Model.Type;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 04/05/2016.
 */
@Wither
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

  @JsonProperty("id")
  private String id;
  private String name;
  private String lastname;
  private String zip;
  private String street;
  private String number;
  private String adjunct;
  private String neighborhood;
  private String city;
  private String state;
  private String phone;
  private String cpf;
  private String uf;
  private String country;
  private String type;
  private Set<Type> types;
  private Set<Type> states;

}
