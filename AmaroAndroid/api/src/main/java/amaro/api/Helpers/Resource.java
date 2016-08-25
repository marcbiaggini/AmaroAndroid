package amaro.api.Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by juan.villa on 05/08/2016.
 */
public class Resource {

  private String name;
  private List<Resource> subcategory;
  private String query;

  public Resource() {
    subcategory = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Resource> getSubcategory() {
    return subcategory;
  }

  public void setSubcategory(List<Resource> subcategory) {
    this.subcategory = subcategory;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }
}
