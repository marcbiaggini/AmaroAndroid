package amaro.api.Model.MySearch.Pagination;

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
public class Pagination {
  private String previous;
  private String previous_query;
  private String next;
  private String next_query;
  private String num_results;
  private String total_num_results;
  private String index_start;
  private String index_end;
}
