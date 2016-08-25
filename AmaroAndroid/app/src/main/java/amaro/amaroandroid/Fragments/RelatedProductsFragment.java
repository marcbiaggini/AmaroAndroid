package amaro.amaroandroid.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import amaro.amaroandroid.Adapters.ProductGridAdapter;
import amaro.amaroandroid.R;
import amaro.api.Model.Product.Related.RelatedProduct;
import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by juan.villa on 21/06/2016.
 */
public class RelatedProductsFragment extends android.support.v4.app.DialogFragment {

  private Dialog dialog;
  private RecyclerView productsRelated, productsRecomended;
  private ProductGridAdapter productRelatedAdapter, productRecomendedAdapter;
  private List<RelatedProduct> itemsRelated = new ArrayList<>();
  private List<RelatedProduct> itemsRecomended = new ArrayList<>();
  private FancyButton btnCart, btnContinue;
  private CloseRelatedMessage relatedFragmentListener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.fragment_related_products_dialog, null);
    view.setVerticalScrollBarEnabled(true);

    btnCart = (FancyButton) view.findViewById(R.id.btnIrCart);
    btnContinue = (FancyButton) view.findViewById(R.id.btnContinuaCompra);

    productsRelated = (RecyclerView) view.findViewById(R.id.products_related);
    productsRecomended = (RecyclerView) view.findViewById(R.id.products_recomended);

    productRelatedAdapter = new ProductGridAdapter(itemsRelated, getActivity(), "related");
    productRecomendedAdapter = new ProductGridAdapter(itemsRecomended, getActivity(), "related");

    productsRelated.setLayoutManager((new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)));
    productsRecomended.setLayoutManager((new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)));

    productsRelated.setAdapter(productRelatedAdapter);
    productsRecomended.setAdapter(productRecomendedAdapter);

    builder.setView(view);
    dialog = builder.create();
    dialog.show();
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());

    WindowManager wm = (WindowManager) dialog.getContext().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int height = (4 * size.y) / 5;
    dialog.getWindow().setLayout(lp.width, height);

    setClicklistener();
    return dialog;
  }

  @Override
  public void onAttach(Activity activity) {
    relatedFragmentListener = (CloseRelatedMessage) activity;
    super.onAttach(activity);
  }

  @Override
  public void onDetach() {
    relatedFragmentListener = null;
    super.onDetach();
  }

  public void setItemsRelated(List<RelatedProduct> itemsRelated) {
    this.itemsRelated = itemsRelated;
  }

  public void setItemsRecomended(List<RelatedProduct> itemsRecomended) {
    this.itemsRecomended = itemsRecomended;
  }

  public void setClicklistener() {
    btnCart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        relatedFragmentListener.onClose("cart");
        dialog.dismiss();
      }
    });
    btnContinue.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        relatedFragmentListener.onClose("continue");
        dialog.dismiss();
      }
    });
  }

  public interface CloseRelatedMessage {
    void onClose(String goTo);
  }

}