package amaro.amaroandroid.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Iterator;
import java.util.List;

import amaro.amaroandroid.Fragments.Message.CepFragment;
import amaro.amaroandroid.Fragments.Message.LoadingFragment;
import amaro.amaroandroid.R;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;
import amaro.api.Model.MyCart.CartInfo.Item;

/**
 * Created by juan.villa on 30/06/2016.
 */
public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder> {

  TextView txtTotalValor, txtSubTotalValor, txtFreteValor, txtEmptyMessage;
  CepFragment cepMessageFragment;
  LoadingFragment loadingFragment = new LoadingFragment();
  FragmentManager fm;
  private Context ctx;
  private List<Item> items;
  private AppCompatActivity mainActivity;
  private String service;
  private boolean isCheckout;


  public CartProductAdapter(Context ctx, List<Item> items, AppCompatActivity mainActivity) {
    this.ctx = ctx;
    this.items = items;
    this.mainActivity = mainActivity;
    txtSubTotalValor = (TextView) mainActivity.findViewById(R.id.txtSubTotalValor);
    txtTotalValor = (TextView) mainActivity.findViewById(R.id.txtTotalValor);
    txtFreteValor = (TextView) mainActivity.findViewById(R.id.txtFreteValor);
    txtEmptyMessage = (TextView) mainActivity.findViewById(R.id.txtEmptyMessage);
    cepMessageFragment = new CepFragment();
    fm = mainActivity.getSupportFragmentManager();
  }

  @Override
  public CartProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(ctx).inflate(R.layout.item_cart_product, parent, false);
    return new CartProductViewHolder(view, mainActivity);
  }

  @Override
  public void onBindViewHolder(final CartProductViewHolder holder, final int position) {
    final Item product = items.get(position);

    holder.txtProductName.setText(product.getName());
    holder.txtPrice.setText(product.getPrice());
    holder.txtSize.setText(product.getSize());
    holder.txtColor.setText(product.getColor());
    holder.itemNumber.setText(product.getQuantity());
    Picasso.with(mainActivity.getBaseContext()).load("https://d2odcms9up7saa.cloudfront.net" + product.getThumb()).into(holder.imageItemCart);

    if (isCheckout) {
      holder.txtProductName.setText(product.getQuantity() + "x " + product.getName());
      holder.addBtnCartItem.setVisibility(View.INVISIBLE);
      holder.substractBtnCartItem.setVisibility(View.INVISIBLE);
      holder.deleteProduct.setVisibility(View.INVISIBLE);
      holder.itemNumber.setVisibility(View.INVISIBLE);
    }

    holder.substractBtnCartItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        int item = Integer.valueOf(holder.itemNumber.getText().toString());
        if (item != 1) {
          loadingFragment.setCancelable(false);
          loadingFragment.show(fm, "fragment_loading_dialog");
          service = "substract";
          UpdateCartTask taskSubstract = new UpdateCartTask();
          taskSubstract.execute(service, String.valueOf(position), items.get(position).getSku());
        } else {
          service = "delete";
          deleteMessage(mainActivity, position);
        }
      }
    });

    holder.addBtnCartItem.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        loadingFragment.setCancelable(false);
        loadingFragment.show(fm, "fragment_loading_dialog");
        service = "add";
        UpdateCartTask taskAdd = new UpdateCartTask();
        taskAdd.execute(service, String.valueOf(position), items.get(position).getSku());

      }
    });

    holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        service = "delete";
        deleteMessage(mainActivity, position);
      }
    });
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  private void deleteMessage(AppCompatActivity activity, final int position) {

    new LovelyStandardDialog(activity)
        .setTopColorRes(R.color.colorAccent)
        .setButtonsColorRes(R.color.colorLightDark)
        .setIcon(new IconicsDrawable(activity.getBaseContext())
            .icon(Ionicons.Icon.ion_android_alert)
            .color(Color.parseColor("#FFFFFF"))
            .sizeDp(32).getCurrent())
        .setTitle(R.string.delete_item_title)
        .setMessage(R.string.delete_item_message)
        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            loadingFragment.setCancelable(false);
            loadingFragment.show(fm, "fragment_loading_dialog");
            UpdateCartTask task = new UpdateCartTask();
            task.execute(service, String.valueOf(position), "");
          }
        })
        .setNegativeButton(android.R.string.no, new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
        })
        .show();
  }

  public void setCheckout(boolean checkout) {
    isCheckout = checkout;
  }

  public void setShippinigMessage(CepFragment cepMessageFragment) {
    this.cepMessageFragment = cepMessageFragment;
  }

  public void setErrorMessage(AppCompatActivity activity, String message) {

    new LovelyStandardDialog(activity)
        .setTopColorRes(R.color.colorAccent)
        .setButtonsColorRes(R.color.colorLightDark)
        .setIcon(new IconicsDrawable(activity.getBaseContext())
            .icon(Ionicons.Icon.ion_android_alert)
            .color(Color.parseColor("#FFFFFF"))
            .sizeDp(32).getCurrent())
        .setTitle(R.string.delete_item_title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
        })
        .show();
  }

  //InnerClass RecyclerView//
  public class CartProductViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

    public TextView itemNumber, txtProductName, txtColor, txtSize, txtPrice;
    public ImageView deleteBtnProduct, imageItemCart;
    Button addBtnCartItem, substractBtnCartItem;
    MaterialRippleLayout deleteProduct;
    private Activity mainActivity;


    public CartProductViewHolder(View itemView, final Activity mainActivity) {
      super(itemView);
      itemView.setOnLongClickListener(this);
      this.mainActivity = mainActivity;
      deleteProduct = (MaterialRippleLayout) itemView.findViewById(R.id.deleteProduct);
      txtProductName = (TextView) itemView.findViewById(R.id.txtCartProductName);
      txtColor = (TextView) itemView.findViewById(R.id.txtColor);
      txtSize = (TextView) itemView.findViewById(R.id.txtSize);
      txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
      imageItemCart = (ImageView) itemView.findViewById(R.id.imageItemCart);
      substractBtnCartItem = (Button) itemView.findViewById(R.id.substractBtnCartItem);
      itemNumber = (TextView) itemView.findViewById(R.id.itemNumber);
      addBtnCartItem = (Button) itemView.findViewById(R.id.addBtnCartItem);
      deleteBtnProduct = (ImageView) itemView.findViewById(R.id.deleteBtnProduct);
      deleteBtnProduct.setImageDrawable(new IconicsDrawable(this.mainActivity.getBaseContext())
          .icon(Ionicons.Icon.ion_ios_close_empty)
          .color(Color.parseColor("#000000"))
          .sizeDp(12).getCurrent());
    }


    @Override
    public boolean onLongClick(View v) {
      return false;
    }
  }

  //InnerTask
  class UpdateCartTask extends AsyncTask<String, Void, GetCartResponse> {

    String callServiceString;
    String sku;
    private int position;

    @Override
    protected GetCartResponse doInBackground(String... params) {
      callServiceString = params[0];
      position = Integer.valueOf(params[1]);
      sku = params[2];
      switch (callServiceString) {
        case "add":
          MultiValueMap<String, Object> addProductMap = new LinkedMultiValueMap<String, Object>();
          addProductMap.add("sku", sku);
          return AmaroServices.getMyCart().addToCart(addProductMap);
        case "substract":
          return AmaroServices.getMyCart().subtract(sku);
        default:
          return AmaroServices.getMyCart().delete(items.get(position).getSku());
      }
    }

    @Override
    protected void onPostExecute(GetCartResponse result) {
      loadingFragment.dismiss();
      if (result.getCode().equals("0")) {
        AmaroServices.setCart(result.getCart());
        AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.AUTENTICATION));
        AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.SESSION));
        AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.AUTENTICATION));
        AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.SESSION));
        cepMessageFragment.setShippingOptions(result.getCart().getShipping_options());
        switch (callServiceString) {
          case "delete":
            txtSubTotalValor.setText(result.getCart().getSubtotal());
            txtTotalValor.setText(result.getCart().getTotal());
            txtFreteValor.setText(result.getCart().getShipping_total());
            items.remove(position);
            notifyDataSetChanged();
            if (items.size() == 0) {
              txtEmptyMessage.setVisibility(View.VISIBLE);
            }
            break;
          default:
            txtSubTotalValor.setText(result.getCart().getSubtotal());
            txtTotalValor.setText(result.getCart().getTotal());
            txtFreteValor.setText(result.getCart().getShipping_total());
            for (Iterator<Item> it = result.getCart().getItems().iterator(); it.hasNext(); ) {
              Item item = it.next();
              if ((item.getSku().equals(items.get(position).getSku())) && !(item.getQuantity().equals(items.get(position).getQuantity()))) {
                items.set(position, item);
                notifyDataSetChanged();
              }
            }
            break;
        }
      } else {
        setErrorMessage(mainActivity, result.getMsg());
      }

    }

  }
}
