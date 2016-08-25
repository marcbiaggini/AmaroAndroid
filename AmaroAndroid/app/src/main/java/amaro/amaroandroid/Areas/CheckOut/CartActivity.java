package amaro.amaroandroid.Areas.CheckOut;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import amaro.amaroandroid.Adapters.CartProductAdapter;
import amaro.amaroandroid.Areas.Login.LoginActivity_;
import amaro.amaroandroid.Fragments.Message.CepFragment;
import amaro.amaroandroid.Fragments.Message.LoadingFragment;
import amaro.amaroandroid.R;
import amaro.api.BuildConfig;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;
import amaro.api.Model.MyCart.CartInfo.Item;
import amaro.api.Model.MyCart.CartInfo.Shipping;

@EActivity(R.layout.activity_cart)
public class CartActivity extends AppCompatActivity implements CepFragment.CloseInterface {

  @ViewById(R.id.products_List)
  RecyclerView products_List;
  @ViewById(R.id.txtSubTotalValor)
  TextView txtSubTotalValor;
  @ViewById(R.id.txtTotalValor)
  TextView txtTotalValor;
  @ViewById(R.id.txtFreteValor)
  TextView txtFreteValor;
  @ViewById(R.id.txtEmptyMessage)
  TextView txtEmptyMessage;
  CartProductAdapter cartProductAdapter;
  MenuItem itemCep;
  String CEP;
  FragmentManager fm = getSupportFragmentManager();
  CepFragment cepMessageFragment = new CepFragment();
  LoadingFragment loadingFragment = new LoadingFragment();
  private List<Item> items = new ArrayList<>();
  private Set<Shipping> shippingOptions;


  @AfterViews
  public void init() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    loadingFragment.setCancelable(false);
    loadingFragment.show(fm, "fragment_loading_dialog");
    getCart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    txtEmptyMessage.setVisibility(View.INVISIBLE);
    if (loadingFragment.getDialog() == null) {
      loadingFragment.setCancelable(false);
      loadingFragment.show(fm, "fragment_loading_dialog");
      items.clear();
      getCart();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_cart, menu);
    itemCep = menu.findItem(R.id.cep);
    itemCep.setIcon(new IconicsDrawable(this)
        .icon(MaterialDesignIconic.Icon.gmi_truck)
        .color(Color.BLACK)
        .sizeDp(22).getCurrent());

    final Drawable bakcIcon = new IconicsDrawable(this)
        .icon(Ionicons.Icon.ion_ios_arrow_left)
        .color(Color.parseColor("#FF000000"))
        .sizeDp(18).getCurrent();
    bakcIcon.setColorFilter(Color.parseColor("#FF000000"), PorterDuff.Mode.SRC_ATOP);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(bakcIcon);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.cep:
        showCepDialog();
        return true;
      default:
        finish();
    }
    return super.onOptionsItemSelected(item);
  }

  private void showCepDialog() {
    if (cepMessageFragment.getCEP() == null || cepMessageFragment.getCEP().isEmpty() || cepMessageFragment.getCEP().equals("")) {
      cepMessageFragment.setCEP(CEP);
    }
    cartProductAdapter.setShippinigMessage(cepMessageFragment);
    cepMessageFragment.show(fm, "fragment_cep_dialog");
  }

  @Click(R.id.btnFinalizarCompra)
  public void checkOut() {
    if (AmaroServices.getMyAccount().getCookie(BuildConfig.AUTENTICATION) == "") {
      LoginActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
    } else if (items.size() > 0) {
      CheckoutActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
      finish();
    } else {
      setErrorMessage(this, getResources().getString(R.string.cart_empty_message));
    }
  }

  @Background
  public void getCart() {
    getCart(AmaroServices.getMyCart().getCart());
  }

  @UiThread
  public void getCart(GetCartResponse getCartResponse) {
    loadingFragment.dismiss();
    if (getCartResponse.getCode().equals("0")) {
      AmaroServices.setCart(getCartResponse.getCart());
      items.addAll(getCartResponse.getCart().getItems());
      if (items.size() <= 0) {
        txtEmptyMessage.setVisibility(View.VISIBLE);
      }
      shippingOptions = getCartResponse.getCart().getShipping_options();
      CEP = getCartResponse.getCart().getApplied_zip();
      cepMessageFragment.setShippingOptions(shippingOptions);
      txtSubTotalValor.setText(getCartResponse.getCart().getSubtotal());
      txtTotalValor.setText(getCartResponse.getCart().getTotal());
      txtFreteValor.setText(getCartResponse.getCart().getShipping_total());
      txtFreteValor.setVisibility(View.VISIBLE);
      cartProductAdapter = new CartProductAdapter(getBaseContext(), items, this);
      cartProductAdapter.setShippinigMessage(cepMessageFragment);
      products_List.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)));
      products_List.setAdapter(cartProductAdapter);
    }
  }

  @Override
  public void onClose(Boolean isClosed, String total, String frete, Set<Shipping> shippingOptions) {
    if (isClosed && !frete.equals("")) {
      txtFreteValor.setText(frete);
      txtTotalValor.setText(total);
      this.shippingOptions = shippingOptions;
      cepMessageFragment.setShippingOptions(shippingOptions);
    }
  }

  public void setErrorMessage(AppCompatActivity activity, String message) {

    new LovelyStandardDialog(activity)
        .setTopColorRes(R.color.colorAppRed)
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
}
