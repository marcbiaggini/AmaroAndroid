package amaro.api.Model.Product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.Set;

import amaro.api.Model.Product.Image.Image;
import amaro.api.Model.Product.Related.RelatedProduct;
import amaro.api.Model.Product.Variants.ProductVariant;
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
public class ProductSelected {
  private String style;
  private String category;
  private String name;
  private String name_slug;
  private String description;
  private String details;
  private String color;
  private String color_slug;
  private String size;
  private boolean on_sale;
  private String regular_price;
  private String actual_price;
  private String discount_percentage;
  private String installments;
  private String image_url;
  private String image_url_mobile;
  private String link_url;
  private Collection<RelatedProduct> related_products;
  private Collection<RelatedProduct> recommended_products;
  private Set<ProductVariant> variants;
  private Collection<Image> images;
}

