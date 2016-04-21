package com.rockspoon.automationtest.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by juancamilovilladuarte on 3/14/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  @JsonProperty("name")
  String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
