package amaro.api.Model.Product.Image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.Wither;

/**
 * Created by juan.villa on 12/08/2016.
 */
@Wither
@NonFinal
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
  private String color_slug;
  private String video_code;
  private Collection<ImageType> images_desktop;
  private Collection<ImageType> images_mobile;
}
