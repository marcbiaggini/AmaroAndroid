package amaro.amaroandroid.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import amaro.amaroandroid.Fragments.FilterFragment;
import amaro.amaroandroid.R;
import amaro.api.Helpers.Resource;
import amaro.api.Model.MySearch.Sorting.Sorting;

/**
 * Created by juan.villa on 05/08/2016.
 */
public class GenericListAdapter extends RecyclerView.Adapter<GenericListAdapter.GenericViewHolder> {

  String resource;
  private List<Object> items;
  private Context ctx;
  private Activity mainActivity;
  private FilterFragment.CloseInterface mListener;
  private Dialog dialog;


  public GenericListAdapter(List<Object> items, Context ctx, Activity mainActivity, FilterFragment.CloseInterface mListener, Dialog dialog) {
    this.items = items;
    this.ctx = ctx;
    this.mainActivity = mainActivity;
    this.mListener = mListener;
    this.dialog = dialog;
  }

  @Override
  public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(ctx).inflate(R.layout.item_generic_list, parent, false);
    return new GenericViewHolder(view);
  }

  @Override
  public void onBindViewHolder(GenericViewHolder holder, int position) {
    Object object = items.get(position);
    if (object instanceof Resource) {
      holder.type = "filter";
      holder.txtGenericTitle.setText(((Resource) object).getName());
      holder.query = ((Resource) object).getQuery();
    }
    if (object instanceof Sorting) {
      holder.type = "sorting";
      holder.txtGenericTitle.setText(((Sorting) object).getName());
      holder.query = ((Sorting) object).getQuery();
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView txtGenericTitle;
    String query = new String();
    String type = new String();

    public GenericViewHolder(View itemView) {
      super(itemView);
      txtGenericTitle = (TextView) itemView.findViewById(R.id.txtGenericTitle);
      txtGenericTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      mListener.onClose(type, query);
      dialog.dismiss();
    }

  }

}
