package amaro.amaroandroid.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import java.util.ArrayList;
import java.util.List;

import amaro.amaroandroid.Fragments.FilterFragment;
import amaro.amaroandroid.Fragments.Message.EntregaFragment;
import amaro.amaroandroid.R;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Helpers.Resource;
import amaro.api.Model.MySearch.Sorting.Sorting;

/**
 * Created by juan.villa on 05/08/2016.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

  List<Resource> filters;
  List<Sorting> sorting;
  private Context ctx;
  private Activity mainActivity;
  private List<FilterViewHolder> viewHolders = new ArrayList<>();
  private FilterFragment.CloseInterface mListener;
  private Dialog dialog;
  private String type = "filters";

  public FilterAdapter(List<Resource> filters, Context ctx, Activity mainActivity, FilterFragment.CloseInterface mListener, Dialog dialog, String type) {
    this.filters = filters;
    this.ctx = ctx;
    this.mainActivity = mainActivity;
    this.mListener = mListener;
    this.dialog = dialog;
    this.type = type;
  }

  @Override
  public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(ctx).inflate(R.layout.item_main_filter, parent, false);
    return new FilterViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final FilterViewHolder holder, final int position) {
    List<? extends Object> subcategoryList;
    if (type.equalsIgnoreCase("filters")) {
      final Resource resources = filters.get(position);
      holder.titleFilter.setText(resources.getName());
      subcategoryList = resources.getSubcategory();
      holder.position = position;
      viewHolders.add(holder);
    } else {
      holder.titleFilter.setText("ORDENAR");
      subcategoryList = AmaroServices.getSortings();
      holder.subCategoryList.setVisibility(View.VISIBLE);
      holder.isListVisible = true;
    }
    holder.subCategoryList.setAdapter(new GenericListAdapter((List<Object>) subcategoryList, ctx, mainActivity, mListener, dialog));
    holder.iconFilter.setImageDrawable(new IconicsDrawable(ctx)
        .icon(Ionicons.Icon.ion_ios_arrow_down)
        .color(Color.BLACK)
        .sizeDp(12).getCurrent());

    holder.mainCategoryLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (type.equalsIgnoreCase("filters")) {
          for (int i = 0; i < viewHolders.size(); i++) {
            if (i != position) {
              viewHolders.get(i).subCategoryList.setVisibility(View.GONE);
            }
          }
          if (holder.isListVisible) {
            holder.subCategoryList.setVisibility(View.GONE);
            holder.isListVisible = false;
          } else {
            holder.subCategoryList.setVisibility(View.VISIBLE);
            holder.isListVisible = true;
          }
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return filters.size();
  }

  public class FilterViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout mainCategoryLayout;
    TextView titleFilter;
    RecyclerView subCategoryList;
    ImageView iconFilter;
    boolean isListVisible = false;
    int position;

    public FilterViewHolder(View itemView) {
      super(itemView);
      iconFilter = (ImageView) itemView.findViewById(R.id.iconFilter);
      mainCategoryLayout = (RelativeLayout) itemView.findViewById(R.id.mainCategoryLayout);
      titleFilter = (TextView) itemView.findViewById(R.id.txtTitleFilter);
      subCategoryList = (RecyclerView) itemView.findViewById(R.id.subCategoryList);
      subCategoryList.setLayoutManager((new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)));
    }


  }
}
