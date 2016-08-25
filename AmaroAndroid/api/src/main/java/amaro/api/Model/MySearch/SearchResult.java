package amaro.api.Model.MySearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Set;

import amaro.api.Model.MySearch.Filter.Filter;
import amaro.api.Model.MySearch.Pagination.Pagination;
import amaro.api.Model.Product.ProductSearch;
import amaro.api.Model.MySearch.Sorting.Sorting;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 01/08/2016.
 */
@Wither
@NonFinal
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {

  @JsonProperty("response_code")
  private String response_code;
  private Collection<ProductSearch> products;
  private Filter filters;
  private Set<Sorting> sorting;
  private Pagination pagination;
}
