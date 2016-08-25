package amaro.api.Model.MySearch.Filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;

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
public class FilterType {
  private String name;
  private Set<FilterValue> values;
}
