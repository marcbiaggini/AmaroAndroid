package amaro.amaroandroid.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.urbanairship.UAirship;

import java.util.ArrayList;
import java.util.List;

import amaro.amaroandroid.Areas.Products.ProductDetailsActivity_;
import amaro.amaroandroid.R;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.Product.ProductSearch;
import amaro.api.Model.Product.Related.RelatedProduct;

/**
 * Created by juan.villa on 29/04/2016.
 */

public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.ProductViewHolder> {
  private Context ctx;
  private List<ProductSearch> items;
  private List<RelatedProduct> itemsRelated;
  private Activity mainActivity;
  private String function = "product";


  public ProductGridAdapter(List<ProductSearch> items, Context ctx, Activity mainActivity) {
    this.ctx = ctx;
    this.items = items;
    this.mainActivity = mainActivity;
  }


  public ProductGridAdapter(List<RelatedProduct> itemsRelated, Activity mainActivity, String function) {
    this.itemsRelated = itemsRelated;
    this.mainActivity = mainActivity;
    this.ctx = mainActivity.getApplicationContext();
    this.function = function;
  }

  @Override
  public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    if (function.equals("related")) {
      view = LayoutInflater.from(ctx).inflate(R.layout.item_related_product, parent, false);
    } else {
      view = LayoutInflater.from(ctx).inflate(R.layout.item_product, parent, false);
    }
    return new ProductViewHolder(view, mainActivity);
  }

  @Override
  public void onBindViewHolder(ProductViewHolder holder, int position) {

    if (function.equals("related")) {
      final RelatedProduct relatedProduct = itemsRelated.get(position);
      holder.name.setText(relatedProduct.getName());
      holder.price.setText(relatedProduct.getActual_price());
      holder.instalment.setText(relatedProduct.getInstallments());
      //holder.codeColor=relatedProduct.getCode_color();
      Picasso.with(mainActivity.getBaseContext()).load(relatedProduct.getImage_url_mobile()).into(holder.image);
    } else {
      final ProductSearch productSearch = items.get(position);
      holder.name.setText(productSearch.getName());
      holder.price.setText(productSearch.getActual_price());
      holder.instalment.setText(productSearch.getInstallments());
      holder.codeColor = productSearch.getCode_color();
      Picasso.with(mainActivity.getBaseContext()).load(productSearch.getImage_url_mobile()).into(holder.image);
    }
  }

  @Override
  public int getItemCount() {
    if (function.equals("related")) {
      return itemsRelated.size();
    } else {
      return items.size();
    }
  }

  public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView name, price, instalment;
    public ImageView image;
    public int position;
    public String codeColor;
    private Activity mainActivity;

    public ProductViewHolder(View itemView, Activity mainActivity) {
      super(itemView);
      itemView.setOnLongClickListener(this);
      itemView.setOnClickListener(this);
      name = (TextView) itemView.findViewById(R.id.txtCartProductName);
      price = (TextView) itemView.findViewById(R.id.txtPrice);
      instalment = (TextView) itemView.findViewById(R.id.instalment);
      image = (ImageView) itemView.findViewById(R.id.image);
      this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {
      //Click Events
      if (!function.equals("related")) {
        ProductDetailsActivity_.intent(mainActivity.getBaseContext()).extra("code_color", codeColor).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
      }
    }

    @Override
    public boolean onLongClick(View v) {
      if (!function.equals("related")) {
        // Created a new Dialog
        Dialog dialog = new Dialog(mainActivity);

        // inflate the layout
        dialog.setContentView(R.layout.item_product);

        // Set the title
        dialog.setTitle(name.getText().toString());
        dialog.show();
        int tagNumber = (int) (Math.random() * 9);
        UAirship.shared().getPushManager().editTags()
            .addTag("tag" + String.valueOf(tagNumber))
            .apply();
      }
      return false;
    }
  }

}
