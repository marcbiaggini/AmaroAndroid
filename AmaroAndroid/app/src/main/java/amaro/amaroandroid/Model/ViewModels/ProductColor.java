package amaro.amaroandroid.Model.ViewModels;

import android.widget.ImageView;

/**
 * Created by juan.villa on 12/08/2016.
 */
public class ProductColor {
  private ImageView image;
  private String color_slug;
  private boolean available;
  private String name;
  private boolean selected;

  public ImageView getImage() {
    return image;
  }

  public void setImage(ImageView image) {
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getColor_slug() {
    return color_slug;
  }

  public void setColor_slug(String color_slug) {
    this.color_slug = color_slug;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }
}
