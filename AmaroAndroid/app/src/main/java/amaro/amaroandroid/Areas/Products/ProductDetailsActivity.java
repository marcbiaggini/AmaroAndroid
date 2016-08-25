package amaro.amaroandroid.Areas.Products;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lid.lib.LabelImageView;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import amaro.amaroandroid.Adapters.ColorSpinerAdapter;
import amaro.amaroandroid.Areas.CheckOut.CartActivity_;
import amaro.amaroandroid.Fragments.Message.LoadingFragment;
import amaro.amaroandroid.Fragments.RelatedProductsFragment;
import amaro.amaroandroid.Model.ViewModels.ProductColor;
import amaro.amaroandroid.R;
import amaro.api.BuildConfig;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.MyCart.CartInfo.GetCartResponse;
import amaro.api.Model.MyProduct.ProducSelectedResponse;
import amaro.api.Model.Product.Image.Image;
import amaro.api.Model.Product.Image.ImageType;
import amaro.api.Model.Product.Related.RelatedProduct;
import amaro.api.Model.Product.Variants.ProductSize;
import amaro.api.Model.Product.Variants.ProductVariant;

@EActivity(R.layout.activity_product_details)
public class ProductDetailsActivity extends AppCompatActivity implements RelatedProductsFragment.CloseRelatedMessage {

  @ViewById(R.id.toolbar)
  Toolbar toolbar;
  @ViewById(R.id.spnColor)
  Spinner spnColor;
  @ViewById(R.id.spnSize)
  Spinner spnSize;
  @Extra("code_color")
  String code_color;
  @ViewById(R.id.txtRealPrice)
  TextView txtRealPrice;
  @ViewById(R.id.txtPrice)
  TextView txtPrice;
  @ViewById(R.id.txtinstallment)
  TextView txtinstallment;
  @ViewById(R.id.txtCartProductName)
  TextView txtCartProductName;
  @ViewById(R.id.txtDescount)
  TextView txtDescount;
  @ViewById(R.id.imageLabel)
  LabelImageView imageLabel;
  @ViewById(R.id.imageProduct)
  LabelImageView imageProduct;
  @ViewById(R.id.avLoadingPictureView)
  AVLoadingIndicatorView avLoadingPictureView;
  @ViewById(R.id.mainImageLayout)
  RelativeLayout mainImageLayout;

  MenuItem itemBag, itemShare, itemInfo;
  LoadingFragment loadingFragment = new LoadingFragment();
  FragmentManager fm = getSupportFragmentManager();
  List<ProductColor> productColors = new ArrayList<>();
  List<ProductVariant> productVariants = new ArrayList<>();

  MultiValueMap<String, List<String>> listSizes = new LinkedMultiValueMap<>();
  MultiValueMap<String, List<String>> productsImage = new LinkedMultiValueMap<>();
  MultiValueMap<String, String> productsSku = new LinkedMultiValueMap<>();
  float startXValue = 1;
  int imagePointer = 0;
  String color_slug;
  @ViewById(R.id.count)
  TextView count;
  private List<RelatedProduct> itemsRelated = new ArrayList<>();
  private List<RelatedProduct> itemsRecomended = new ArrayList<>();
  private String link_url = "0";
  private int productItemSelected;

  @AfterViews
  public void init() {
    setSupportActionBar(toolbar);
    count.setText(String.valueOf(imagePointer));
    setSpinnerSelectItemListener();
    setSwipeGesture();
    getProductDetails();
  }

  private void setSwipeGesture() {
    mainImageLayout.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        float endXValue = 0;
        float x1 = event.getAxisValue(MotionEvent.AXIS_Y);
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
          case (MotionEvent.ACTION_DOWN):
            startXValue = event.getAxisValue(MotionEvent.AXIS_Y);
            break;
          case (MotionEvent.ACTION_UP):
            if (productsImage.size() > 0) {

              endXValue = event.getAxisValue(MotionEvent.AXIS_Y);
              avLoadingPictureView.setVisibility(View.VISIBLE);
              if (endXValue > startXValue) {
                if (endXValue - startXValue > 100) {
                  Log.e("Gesture", "down");
                  imagePointer = imagePointer + 1;
                  if (imagePointer >= productsImage.getFirst(color_slug).size()) {
                    imagePointer = 0;
                  }
                }
              } else {
                if (startXValue - endXValue > 100) {
                  Log.e("Gesture", "up");
                  imagePointer = imagePointer - 1;
                  if (imagePointer < 0) {
                    imagePointer = productsImage.getFirst(color_slug).size() - 1;
                  }
                }
              }
              count.setText(String.valueOf(imagePointer));
              Picasso.with(getBaseContext()).load(productsImage.getFirst(color_slug).get(imagePointer)).into(imageProduct, new Callback() {
                @Override
                public void onSuccess() {
                  avLoadingPictureView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                }
              });
            }
            break;
          default:

        }
        return true;
      }

    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_product_details, menu);

    itemBag = menu.findItem(R.id.bag);
    itemShare = menu.findItem(R.id.share);
    itemInfo = menu.findItem(R.id.info);

    itemShare.setIcon(new IconicsDrawable(this)
        .icon(Ionicons.Icon.ion_android_share_alt)
        .color(Color.BLACK)
        .sizeDp(18).getCurrent());

    itemInfo.setIcon(new IconicsDrawable(this)
        .icon(Ionicons.Icon.ion_ios_information_outline)
        .color(Color.BLACK)
        .sizeDp(18).getCurrent());

    itemBag.setIcon(new IconicsDrawable(this)
        .icon(FontAwesome.Icon.faw_shopping_bag)
        .color(Color.BLACK)
        .sizeDp(18).getCurrent());

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
      case R.id.bag:
        CartActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        return true;
      case R.id.share:
        if (!link_url.equals("0")) {
          Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
          sharingIntent.setType("text/plain");
          sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, txtCartProductName.getText().toString());
          sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, BuildConfig.SHARE_URL + link_url);
          startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
        }
        return true;
      case R.id.info:
        return true;
      default:
        finish();
    }
    return super.onOptionsItemSelected(item);
  }


  @Background
  public void getProductDetails() {
    getProductDetails(AmaroServices.getMyProduct().getProductSelected(code_color));
  }

  @UiThread
  public void getProductDetails(ProducSelectedResponse response) {
    if (response.getCode().equals("0")) {
      link_url = response.getProduct().getLink_url();
      txtCartProductName.setText(response.getProduct().getName());
      itemsRelated.addAll(response.getProduct().getRelated_products());
      itemsRecomended.addAll(response.getProduct().getRecommended_products());
      if (response.getProduct().isOn_sale()) {
        txtRealPrice.setText(response.getProduct().getRegular_price());
        txtRealPrice.setPaintFlags(txtRealPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtRealPrice.setVisibility(View.VISIBLE);
        txtDescount.setVisibility(View.VISIBLE);
      }
      txtPrice.setText(response.getProduct().getActual_price());
      txtDescount.setText(response.getProduct().getDiscount_percentage());
      txtinstallment.setText((response.getProduct().getInstallments()));
      productVariants.addAll(response.getProduct().getVariants());


      for (int i = 0; i < productVariants.size(); i++) {
        if (productVariants.get(i).isSelected()) {
          productItemSelected = i;
        }
        ProductColor productColor = new ProductColor();
        productColor.setImage(new ImageView(getBaseContext()));
        productColor.setSelected(productVariants.get(i).isSelected());
        productColor.setColor_slug(productVariants.get(i).getColor_slug());
        productColor.setAvailable(productVariants.get(i).isAvailable());
        productColor.setName(productVariants.get(i).getColor());
        Picasso.with(getBaseContext()).load(productVariants.get(i).getSwatch_url()).into(productColor.getImage());
        // if(productColor.isAvailable()) { This is temporary unavailable cuz there is a bug in Endpoint from Server
        productColors.add(productColor);
        //Set List of Sizes
        List<String> sizes = new ArrayList<>();
        sizes.add(0, "Selecione Tamanho");
        for (Iterator<ProductSize> it = productVariants.get(i).getSizes().iterator(); it.hasNext(); ) {
          ProductSize size = it.next();
          if (size.isAvailable()) {
            sizes.add(size.getSize());
            productsSku.add(productColor.getColor_slug() + size.getSize(), size.getSku());
          }
        }
        listSizes.add(productColor.getColor_slug(), sizes);

        //}
      }

      Picasso.with(getBaseContext()).load(response.getProduct().getImage_url_mobile()).into(imageProduct);
      imageLabel.bringToFront();

      color_slug = response.getProduct().getColor_slug();
      /*Get All Images*/
      for (Iterator<Image> iteratorImages = response.getProduct().getImages().iterator(); iteratorImages.hasNext(); ) {
        Image imageInstance = iteratorImages.next();
        List<String> productSpecificImages = new ArrayList<>();
        for (Iterator<ImageType> iteratorLinks = imageInstance.getImages_mobile().iterator(); iteratorLinks.hasNext(); ) {
          ImageType imageTypeInstance = iteratorLinks.next();
          productSpecificImages.add(imageTypeInstance.getCatalog());
        }
        productsImage.add(imageInstance.getColor_slug(), productSpecificImages);
      }
      /*Get All Images*/
      ArrayAdapter<String> adapterSize = new ArrayAdapter<>(getBaseContext(),
          android.R.layout.simple_spinner_item, listSizes.getFirst(color_slug));
      adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spnSize.setAdapter(adapterSize);
      ColorSpinerAdapter adapterColor = new ColorSpinerAdapter(getBaseContext(), productColors);
      spnColor.setAdapter(adapterColor);
      spnColor.setSelection(productItemSelected);
    } else {
      Log.e("Error: ", response.getMsg());
    }

    /*Cart Counting*/
    if (AmaroServices.getCart().getItems().size() > 0) {
      ActionItemBadge.update(this, itemBag, new IconicsDrawable(this)
          .icon(FontAwesome.Icon.faw_shopping_bag)
          .color(Color.BLACK)
          .sizeDp(18).getCurrent(), ActionItemBadge.BadgeStyles.RED, AmaroServices.getCart().getItems().size());
    } else {
      ActionItemBadge.hide(itemBag);
      itemBag.setVisible(true);
    }
    /*Cart Counting*/
  }

  @Background
  public void addToCart(MultiValueMap<String, Object> addProductMap) {
    addToCart(AmaroServices.getMyCart().addToCart(addProductMap));
  }

  @UiThread
  public void addToCart(GetCartResponse response) {
    loadingFragment.dismiss();
    if (response.getCode().equals("0")) {
      AmaroServices.setCart(response.getCart());
      AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.AUTENTICATION));
      AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.SESSION));
      AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.AUTENTICATION));
      AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyCart().getCookie(amaro.api.BuildConfig.SESSION));
      RelatedProductsFragment relatedProductsFragment = new RelatedProductsFragment();
      relatedProductsFragment.setItemsRelated(itemsRelated);
      relatedProductsFragment.setItemsRecomended(itemsRecomended);
      relatedProductsFragment.show(fm, "fragment_related_products_dialog");
      //finish();
    } else {
      setErrorMessage(this, response.getMsg());
    }
  }

  @Click(R.id.btnComprar)
  public void btnComprar() {
    if (spnSize.getCount() > 1) {
      if (spnSize.getSelectedItemPosition() == 0) {
        spnSize.performClick();
      } else {
        String size = spnSize.getSelectedItem().toString();
        if (spnColor.getSelectedItem() instanceof ProductColor) {
          String colorSlug = ((ProductColor) spnColor.getSelectedItem()).getColor_slug();
          MultiValueMap<String, Object> addProductMap = new LinkedMultiValueMap<String, Object>();
          addProductMap.add("sku", productsSku.getFirst(colorSlug + size));
          loadingFragment.setCancelable(false);
          loadingFragment.show(fm, "fragment_loading_dialog");
          addToCart(addProductMap);
        }
      }
    }
  }

  public void setSpinnerSelectItemListener() {
    spnColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      /**
       * Called when a new item is selected (in the Spinner)
       */
      public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        ProductColor productColor = (ProductColor) parent.getAdapter().getItem(pos);
        color_slug = productColor.getColor_slug();

        //Set New Sizes for View
        ArrayAdapter<String> adapterSize = new ArrayAdapter<>(getBaseContext(),
            android.R.layout.simple_spinner_item, listSizes.getFirst(color_slug));
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSize.setAdapter(adapterSize);
        if (spnSize.getAdapter().getCount() == 1) {
          spnSize.setEnabled(false);
        } else {
          spnSize.setEnabled(true);
        }

        imageProduct.setVisibility(View.INVISIBLE);
        avLoadingPictureView.setVisibility(View.VISIBLE);

        Picasso.with(getBaseContext()).load(productsImage.getFirst(color_slug).get(0)).into(imageProduct, new Callback() {
          @Override
          public void onSuccess() {
            imageProduct.setVisibility(View.VISIBLE);
            avLoadingPictureView.setVisibility(View.INVISIBLE);
          }

          @Override
          public void onError() {
          }
        });
      }

      public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing, just another required interface callback
      }

    });
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

  @Override
  public void onClose(String goTo) {
    switch (goTo) {
      case "cart":
        CartActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        finish();
        break;
      case "continue":
        finish();
        break;
      default:
        break;
    }
  }
}
