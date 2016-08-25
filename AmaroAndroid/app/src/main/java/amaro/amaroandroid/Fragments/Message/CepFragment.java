package amaro.amaroandroid.Fragments.Message;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashSet;
import java.util.Set;

import amaro.amaroandroid.Adapters.ShippingOptionsAdapter;
import amaro.amaroandroid.R;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;
import amaro.api.Model.MyCart.CartInfo.Shipping;


/**
 * Created by juan.villa on 21/06/2016.
 */
public class CepFragment extends android.support.v4.app.DialogFragment {

  public boolean isClosed = false;
  public ShippingOptionsAdapter shippingOptionsAdapter;
  RecyclerView cepOptions;
  String CEP = new String();
  MaterialEditText edtxtCep;
  LoadingFragment loadingFragment = new LoadingFragment();
  FragmentManager fm;
  private Dialog dialog;
  private CloseInterface mListener;
  private Set<Shipping> shippingOptions = new HashSet<Shipping>();

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.fragment_cep_dialog, null);
    fm = getFragmentManager();

    cepOptions = (RecyclerView) view.findViewById(R.id.option_List);
    edtxtCep = (MaterialEditText) view.findViewById(R.id.edtxtCep);
    edtxtCep.setText(CEP);
    edtxtCep.clearFocus();
    setEdtxtListener();
    shippingOptionsAdapter = new ShippingOptionsAdapter(getContext(), shippingOptions, (AppCompatActivity) getActivity());
    shippingOptionsAdapter.setmListener(mListener);
    builder.setView(view);
    dialog = builder.create();
    shippingOptionsAdapter.setDialog(dialog);
    cepOptions.setLayoutManager((new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)));
    cepOptions.setAdapter(shippingOptionsAdapter);
    dialog.show();


    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());
    WindowManager wm = (WindowManager) dialog.getContext().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int height = size.y / 2;

    dialog.getWindow().setLayout(lp.width, height);
    return dialog;
  }

  @Override
  public void onAttach(Activity activity) {
    mListener = (CloseInterface) activity;
    super.onAttach(activity);
  }

  @Override
  public void onDetach() {
    mListener = null;
    super.onDetach();
  }

  private void setEdtxtListener() {
    edtxtCep.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {
      }

      @Override
      public void onTextChanged(final CharSequence s, int start, int before,
                                int count) {
      }

      @Override
      public void afterTextChanged(final Editable s) {
        //avoid triggering event when text is too short
        if (s.length() == 8) {
          loadingFragment.setCancelable(false);
          loadingFragment.show(fm, "fragment_loading_dialog");
          dialog.hide();
          UpdateShippingAddressTask task = new UpdateShippingAddressTask();
          task.execute(s.toString());
        }
      }
    });
  }

  public String getCEP() {
    return CEP;
  }

  public void setCEP(String CEP) {
    this.CEP = CEP;
  }

  public Set<Shipping> getShippingOptions() {
    return shippingOptions;
  }

  public void setShippingOptions(Set<Shipping> shippingOptions) {
    this.shippingOptions = shippingOptions;
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

  public interface CloseInterface {
    void onClose(Boolean isClosed, String total, String frete, Set<Shipping> shippingOptions);
  }

  //Inner Class to update Shipping options
  class UpdateShippingAddressTask extends AsyncTask<String, Void, GetCartResponse> {

    String zip;

    @Override
    protected GetCartResponse doInBackground(String... params) {
      zip = params[0];
      MultiValueMap<String, Object> shippingMap = new LinkedMultiValueMap<String, Object>();
      shippingMap.add("zip", zip);
      return AmaroServices.getMyCart().applyZip(shippingMap);
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
        CEP = zip;
        mListener.onClose(true, result.getCart().getTotal(), result.getCart().getShipping_total(), result.getCart().getShipping_options());
        dialog.dismiss();
      } else {
        setErrorMessage((AppCompatActivity) getActivity(), result.getMsg());
      }
    }
  }

}