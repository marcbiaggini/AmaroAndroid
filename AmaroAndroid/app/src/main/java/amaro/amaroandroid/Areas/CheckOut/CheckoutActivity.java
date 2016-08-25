package amaro.amaroandroid.Areas.CheckOut;

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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import amaro.amaroandroid.Adapters.CartProductAdapter;
import amaro.amaroandroid.Fragments.Message.EntregaFragment;
import amaro.amaroandroid.Fragments.Message.LoadingFragment;
import amaro.amaroandroid.Fragments.Message.PagamentoFragment;
import amaro.amaroandroid.R;
import amaro.api.BuildConfig;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.GenericResponse;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;
import amaro.api.Model.MyCart.CartInfo.Item;
import amaro.api.Model.MyCart.CartInfo.Shipping;
import amaro.api.Model.MyCart.LookupZipResponse;
import amaro.api.Model.MyCart.PaymentInfo.GetPaymentResponse;
import amaro.api.Model.MyCart.PaymentInfo.SelectPaymentResponse;

@EActivity(R.layout.activity_checkout)
public class CheckoutActivity extends AppCompatActivity implements EntregaFragment.CloseInterface, PagamentoFragment.CloseInterface {

  EntregaFragment entregaFragment = new EntregaFragment();
  PagamentoFragment pagamentoFragment = new PagamentoFragment();
  LoadingFragment loadingFragment = new LoadingFragment();
  FragmentManager fm = getSupportFragmentManager();
  MenuItem itemPayment, itemAddress;
  @ViewById(R.id.checkoutMainLayout)
  LinearLayout checkoutMainLayout;
  @ViewById(R.id.txtSubTotalValorCheckout)
  TextView txtSubTotalValorCheckout;
  @ViewById(R.id.txtFreteTitulo)
  TextView txtFreteTitulo;
  @ViewById(R.id.txtFreteValorCheckout)
  TextView txtFreteValorCheckout;
  @ViewById(R.id.txtPagamentoCheckout)
  TextView txtPagamentoCheckout;
  @ViewById(R.id.txtTotalValorCheckout)
  TextView txtTotalValorCheckout;
  @ViewById(R.id.products_List)
  RecyclerView products_List;
  CartProductAdapter cartProductAdapter;
  private List<Item> items = new ArrayList<>();

  private String payment_type;

  @AfterViews
  public void init() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    loadingFragment.setCancelable(false);
    loadingFragment.show(fm, "fragment_loading_dialog");
    getCart();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_checkout, menu);
    itemAddress = menu.findItem(R.id.address);
    itemAddress.setIcon(new IconicsDrawable(this)
        .icon(Ionicons.Icon.ion_android_pin)
        .color(Color.BLACK)
        .sizeDp(22).getCurrent());
    itemPayment = menu.findItem(R.id.paymentMethod);
    itemPayment.setIcon(new IconicsDrawable(this)
        .icon(Ionicons.Icon.ion_card)
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
      case R.id.address:
        checkoutMainLayout.setVisibility(View.GONE);
        entregaFragment.setUserType(false);
        entregaFragment.setUserSelection(false);
        if (entregaFragment.getDialog() == null) {
          entregaFragment.show(fm, "fragment_entrega_dialog");
        } else {
          entregaFragment.getDialog().show();
        }
        return true;
      case R.id.paymentMethod:
        if (pagamentoFragment.getDialog() == null) {
          pagamentoFragment.show(fm, "fragment_pagamento_dialog");
        } else {
          pagamentoFragment.getDialog().show();
        }
        checkoutMainLayout.setVisibility(View.GONE);
        return true;
      default:
        finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClose(String callServiceString, String params) {
    checkoutMainLayout.setVisibility(View.VISIBLE);
    loadingFragment.setCancelable(false);
    loadingFragment.show(fm, "fragment_loading_dialog");
    switch (callServiceString) {
      case "shipping":
        checkoutMainLayout.setVisibility(View.INVISIBLE);
        selectShippingService(params);
        break;
      case "saveShippingInfo":
        checkoutMainLayout.setVisibility(View.INVISIBLE);
        saveShippingInfo();
        break;
      case "validatePayment":
        checkoutMainLayout.setVisibility(View.INVISIBLE);
        validatePaymentMethod();
        break;
      case "selectPayment":
        checkoutMainLayout.setVisibility(View.INVISIBLE);
        selectPaymentMethod();
        break;
      default:
        checkoutMainLayout.setVisibility(View.INVISIBLE);
        lookUpZip(params);
        break;
    }
  }

  @Click(R.id.btnComprar)
  public void purchase() {
    loadingFragment.setCancelable(false);
    loadingFragment.show(fm, "fragment_loading_dialog");
    loadCart();
  }

  @Background
  public void saveShippingInfo() {
    MultiValueMap<String, Object> shippingInfoMap = new LinkedMultiValueMap<String, Object>();
    shippingInfoMap.add("name", entregaFragment.getEdtxtNome().getText().toString());
    shippingInfoMap.add("lastname", entregaFragment.getEdtxtSobrenome().getText().toString());
    shippingInfoMap.add("zip", entregaFragment.getEdtxtCep().getText().toString());
    shippingInfoMap.add("street", entregaFragment.getEdtxtEndereco().getText().toString());
    shippingInfoMap.add("number", entregaFragment.getEdtxtNumero().getText().toString());
    shippingInfoMap.add("adjunct", entregaFragment.getEdtxtComplemento().getText().toString());
    shippingInfoMap.add("neighborhood", entregaFragment.getEdtxtBairro().getText().toString());
    shippingInfoMap.add("city", entregaFragment.getEdtxtCidade().getText().toString());
    shippingInfoMap.add("state", entregaFragment.getSpnEstado().getSelectedItem().toString());
    saveShippingInfo(AmaroServices.getMyAccount().saveShippingInfo(shippingInfoMap));
  }

  @UiThread
  public void saveShippingInfo(GenericResponse response) {
    if (response.getCode().equals("0")) {
      getPaymentMethods();
    } else {
      setErrorMessage(this, response.getMsg());
    }
  }

  @Background
  public void loadCart() {
    loadCart(AmaroServices.getMyCart().getCart());
  }

  @UiThread
  public void loadCart(GetCartResponse response) {
    if (response.getCode().equals("0")) {
      confirmPurchase();
    } else {
      setErrorMessage(this, response.getMsg());
    }
  }

  @Background
  public void confirmPurchase() {
    confirmPurchase(AmaroServices.getMyCheckOut().confirmPurchase());
  }

  @UiThread
  public void confirmPurchase(GenericResponse response) {
    loadingFragment.dismiss();
    if (response.getCode().equals("0")) {
      AmaroServices.getMyCart().setCookie(BuildConfig.SESSION, AmaroServices.getMyCheckOut().getCookie(BuildConfig.SESSION));
      AmaroServices.getMyAccount().setCookie(BuildConfig.SESSION, AmaroServices.getMyCheckOut().getCookie(BuildConfig.SESSION));
      setSucessMessage(this, "Parabéns, Seu Pedido Foi Realizado Com Sucesso!");
    } else {
      setErrorMessage(this, response.getMsg() + "Volte para " + response.getGoto_step());
    }

  }

  @Background
  public void applyZip(String parms) {
    MultiValueMap<String, Object> zip = new LinkedMultiValueMap<String, Object>();
    zip.add("zip", parms);
    applyZip(AmaroServices.getMyCart().applyZip(zip));
  }

  @UiThread
  public void applyZip(GetCartResponse response) {
    loadingFragment.dismiss();
    if (response.getCode().equals("0")) {
      AmaroServices.setCart(response.getCart());
      AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.AUTENTICATION));
      AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.SESSION));
      AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.AUTENTICATION));
      AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.SESSION));
      entregaFragment.getShippingOptions().clear();
      ArrayList<Shipping> shipOtions = new ArrayList<>();
      shipOtions.addAll(AmaroServices.getCart().getShipping_options());
      int shipSelected = 0;
      for (int i = 0; i < shipOtions.size(); i++) {
        if (!shipOtions.get(i).getChecked().equals("")) {
          shipSelected = i;
        }
        entregaFragment.getShippingOptions().add(shipOtions.get(i).getName() + " (" + shipOtions.get(i).getRate() + " - " + shipOtions.get(i).getDelivery_time() + ")");
      }
      ArrayAdapter<String> adapterShipping = new ArrayAdapter<>(entregaFragment.getContext(),
          android.R.layout.simple_spinner_item, entregaFragment.getShippingOptions());
      adapterShipping.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      entregaFragment.getSpnEnvio().setAdapter(adapterShipping);
      entregaFragment.setUserSelection(false);
      entregaFragment.getSpnEnvio().setSelection(shipSelected);
      entregaFragment.getDialog().show();
    }
  }

  @Background
  public void getCart() {
    genericResponse(AmaroServices.getMyCart().getCart());
  }

  @UiThread
  public void genericResponse(GetCartResponse response) {
    loadingFragment.dismiss();
    if (response.getCode().equals("0")) {
      AmaroServices.setCart(response.getCart());
      items.clear();
      items.addAll(response.getCart().getItems());
      txtSubTotalValorCheckout.setText(response.getCart().getSubtotal());
      txtFreteTitulo.setText("Frete" + " - " + response.getCart().getSelected_shipping_option().getName());//+ " - " + response.getCart().getSelected_shipping_option().getDelivery_time());
      txtTotalValorCheckout.setText(response.getCart().getTotal());
      txtFreteValorCheckout.setText(response.getCart().getShipping_total());
      txtFreteValorCheckout.setVisibility(View.VISIBLE);
      cartProductAdapter = new CartProductAdapter(getBaseContext(), items, this);
      cartProductAdapter.setCheckout(true);
      products_List.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)));
      products_List.setAdapter(cartProductAdapter);
      checkoutMainLayout.setVisibility(View.GONE);
      if (entregaFragment.getDialog() == null) {
        entregaFragment.show(fm, "fragment_entrega_dialog");
      } else {
        entregaFragment.getDialog().show();
      }

    } else {
      setErrorMessage(this, response.getMsg());
    }
  }

  @Background
  public void getPaymentMethods() {
    getPaymentMethods(AmaroServices.getMyCart().getPaymentMethods());
  }

  @UiThread
  public void getPaymentMethods(GetPaymentResponse response) {
    loadingFragment.dismiss();
    if (response.getCode().equals("0")) {
      pagamentoFragment.setPaymentResponse(response);
      AmaroServices.setCart(response.getCart());
      if (response.getResponse().getPaymentOptions().getCreditCard().isSelected()) {
        txtPagamentoCheckout.setText(response.getResponse().getPaymentOptions().getCreditCard().getType());
      } else {
        txtPagamentoCheckout.setText(response.getResponse().getPaymentOptions().getBoleto().getType());
      }
      checkoutMainLayout.setVisibility(View.GONE);
      if (pagamentoFragment.getDialog() == null) {
        pagamentoFragment.show(fm, "fragment_pagamento_dialog");
      } else {
        pagamentoFragment.getDialog().show();
      }
    } else {
      setErrorMessage(this, response.getMsg());
    }
  }

  @Background
  public void lookUpZip(String params) {
    lookUpZip(AmaroServices.getMyCart().lookupZip(params), params);
  }

  @UiThread
  public void lookUpZip(LookupZipResponse response, String params) {
    if (response.getCode().equals("0")) {
      AmaroServices.setLookupZipResponse(response);
      entregaFragment.getEdtxtEndereco().setText(response.getAddress().getStreet());
      entregaFragment.getEdtxtBairro().setText(response.getAddress().getNeighborhood());
      entregaFragment.getEdtxtCidade().setText(response.getAddress().getCity());
      int spinnerPosition = entregaFragment.getAdapterUf().getPosition(response.getAddress().getUf());
      entregaFragment.getSpnEstado().setSelection(spinnerPosition);
      applyZip(params);
    } else {
      loadingFragment.dismiss();
      checkoutMainLayout.setVisibility(View.VISIBLE);
      setErrorMessage(this, response.getMsg());
    }
  }

  @Background
  public void selectShippingService(String params) {
    MultiValueMap<String, Object> shippingOptionMap = new LinkedMultiValueMap<String, Object>();
    shippingOptionMap.add("shipping_service_id", params);
    genericResponse(AmaroServices.getMyCart().selectShippingService(shippingOptionMap));
  }

  @Background
  public void validatePaymentMethod() {
    MultiValueMap<String, Object> paymentMap = new LinkedMultiValueMap<String, Object>();
    paymentMap.add("payment-option", pagamentoFragment.getPaymentMethod());
    paymentMap.add("cc_number", pagamentoFragment.getEdtxtCartao().getText().toString());
    paymentMap.add("cc_cvv", pagamentoFragment.getEdtxtCvv().getText().toString());
    paymentMap.add("cc_expiration_month", pagamentoFragment.getEdtxtMes().getText().toString());
    paymentMap.add("cc_expiration_year", pagamentoFragment.getEdtxtAno().getText().toString());
    paymentMap.add("cc_cardholder", pagamentoFragment.getEdtxtNome().getText().toString());
    paymentMap.add("installments", String.valueOf(pagamentoFragment.getSpnParcela().getSelectedItemPosition() + 1));
    validatePaymentMethod(AmaroServices.getMyCheckOut().validatePayment(paymentMap));
  }

  @UiThread
  public void validatePaymentMethod(GenericResponse response) {
    loadingFragment.dismiss();
    checkoutMainLayout.setVisibility(View.VISIBLE);
    txtPagamentoCheckout.setText(payment_type);
    if (!response.getCode().equals("0")) {
      setErrorMessage(this, response.getMsg());
    }
  }

  @Background
  public void selectPaymentMethod() {
    MultiValueMap<String, Object> selectPaymentMap = new LinkedMultiValueMap<String, Object>();

    if (pagamentoFragment.getPaymentMethod().equals("11")) {
      payment_type = "credit-card";
    } else {
      payment_type = "boleto";
    }
    selectPaymentMap.add("option_key", pagamentoFragment.getPaymentMethod());
    selectPaymentMap.add("payment_type", payment_type);
    selectPaymentMethod(AmaroServices.getMyCart().selectPaymentMethod(selectPaymentMap));
  }

  @UiThread
  public void selectPaymentMethod(SelectPaymentResponse response) {
    loadingFragment.dismiss();
    if (response.getCode().equals("0")) {
      txtPagamentoCheckout.setText(payment_type);
      pagamentoFragment.getDialog().show();
    } else {
      setErrorMessage(this, response.getMsg());
    }
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

  public void setSucessMessage(AppCompatActivity activity, String message) {

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
            finish();
          }
        })
        .show();
  }
}
