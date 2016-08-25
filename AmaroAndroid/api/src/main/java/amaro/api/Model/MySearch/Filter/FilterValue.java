package amaro.api.Model.MySearch.Filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class FilterValue {
  private String key;
  private String name;
  private String attr;
  private String resource;
  private String query;
  private String remove;
  private String query_remove;
  private String count;
  private String selected;
}
