package amaro.amaroandroid.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import amaro.amaroandroid.Fragments.Message.CepFragment;
import amaro.amaroandroid.Fragments.Message.LoadingFragment;
import amaro.amaroandroid.R;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;
import amaro.api.Model.MyCart.CartInfo.Shipping;

/**
 * Created by juan.villa on 07/07/2016.
 */
public class ShippingOptionsAdapter extends RecyclerView.Adapter<ShippingOptionsAdapter.ShippingOtionsViewHolder> {


  private static CheckBox lastChecked;
  private static int lastCheckedPos = 0;
  LoadingFragment loadingFragment = new LoadingFragment();
  FragmentManager fm;
  private Context ctx;
  private List<Shipping> shippingOptions = new ArrayList<>();
  private AppCompatActivity mainActivity;
  private CepFragment.CloseInterface mListener;
  private Dialog dialog;


  public ShippingOptionsAdapter(Context ctx, Set<Shipping> shippingOptions, AppCompatActivity mainActivity) {
    this.ctx = ctx;
    this.shippingOptions.addAll(shippingOptions);
    this.mainActivity = mainActivity;
    fm = mainActivity.getSupportFragmentManager();
  }

  @Override
  public ShippingOptionsAdapter.ShippingOtionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(ctx).inflate(R.layout.item_cep_list, parent, false);
    return new ShippingOtionsViewHolder(view, mainActivity);
  }

  @Override
  public void onBindViewHolder(final ShippingOptionsAdapter.ShippingOtionsViewHolder holder, final int position) {
    final Shipping shipping = shippingOptions.get(position);
    holder.checkBoxOption.setText(shipping.getName() + " (" + shipping.getRate() + " - " + shipping.getDelivery_time() + ")");
    if (shipping.getChecked().equalsIgnoreCase("checked")) {
      holder.checkBoxOption.setChecked(true);
      lastChecked = holder.checkBoxOption;
      lastCheckedPos = position;
    }

    holder.checkBoxOption.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        CheckBox cb = (CheckBox) v;
        int clickedPos = position;

        if (cb.isChecked()) {
          if (lastChecked != null) {
            lastChecked.setChecked(false);
            //shippingOptions.get(lastCheckedPos).setSelected(false);
          }
          lastChecked = cb;
          lastCheckedPos = clickedPos;
        } else {
          lastChecked = null;
          //shippingOptions.get(clickedPos).setSelected(cb.isChecked());
        }
        loadingFragment.setCancelable(false);
        loadingFragment.show(fm, "fragment_loading_dialog");
        dialog.hide();
        UpdateShippingTask task = new UpdateShippingTask();
        task.execute(shippingOptions.get(position).getId());
      }
    });
  }

  @Override
  public int getItemCount() {
    return shippingOptions.size();
  }

  public void setDialog(Dialog dialog) {
    this.dialog = dialog;
  }

  public void setmListener(CepFragment.CloseInterface mListener) {
    this.mListener = mListener;
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

  public class ShippingOtionsViewHolder extends RecyclerView.ViewHolder {

    CheckBox checkBoxOption;

    public ShippingOtionsViewHolder(View itemView, AppCompatActivity mainActivity) {
      super(itemView);
      checkBoxOption = (CheckBox) itemView.findViewById(R.id.checkBoxOption);
    }
  }

  class UpdateShippingTask extends AsyncTask<String, Void, GetCartResponse> {

    String id;

    @Override
    protected GetCartResponse doInBackground(String... params) {
      id = params[0];
      MultiValueMap<String, Object> shippingOptionMap = new LinkedMultiValueMap<String, Object>();
      shippingOptionMap.add("shipping_service_id", id);
      return AmaroServices.getMyCart().selectShippingService(shippingOptionMap);
    }

    @Override
    protected void onPostExecute(GetCartResponse result) {
      if (result.getCode().equals("0")) {
        AmaroServices.setCart(result.getCart());
        AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.AUTENTICATION));
        AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.SESSION));
        AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.AUTENTICATION));
        AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.SESSION));
        loadingFragment.dismiss();
        mListener.onClose(true, result.getCart().getTotal(), result.getCart().getShipping_total(), result.getCart().getShipping_options());
        dialog.dismiss();
      } else {
        setErrorMessage(mainActivity, result.getMsg());
      }
    }
  }

}
