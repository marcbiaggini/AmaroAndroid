package amaro.amaroandroid.Areas.Account;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import amaro.amaroandroid.Areas.CheckOut.CartActivity_;
import amaro.amaroandroid.R;
import amaro.api.BuildConfig;
import amaro.api.Helpers.AmaroServices;
import mehdi.sakout.fancybuttons.FancyButton;

@EActivity(R.layout.activity_account)
public class AccountActivity extends AppCompatActivity {

  @ViewById(R.id.btnCadastro)
  FancyButton btnCadastro;
  @ViewById(R.id.btnPedidos)
  FancyButton btnPedidos;
  @ViewById(R.id.btnCreditos)
  FancyButton btnCreditos;
  @ViewById(R.id.btnSair)
  FancyButton btnSair;
  @ViewById(R.id.toolbar)
  Toolbar  toolbar;

  MenuItem itemBag, itemHelp;


  @AfterViews
  public void init(){
    setSupportActionBar(toolbar);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_account, menu);

    itemBag = menu.findItem(R.id.bag);
    itemHelp = menu.findItem(R.id.help);

    itemHelp.setIcon(new IconicsDrawable(this)
        .icon(Ionicons.Icon.ion_ios_help_outline)
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

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.bag:
        CartActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
        return true;
      case R.id.help:
        return true;
      default:
        finish();
    }
    return super.onOptionsItemSelected(item);
  }


}
