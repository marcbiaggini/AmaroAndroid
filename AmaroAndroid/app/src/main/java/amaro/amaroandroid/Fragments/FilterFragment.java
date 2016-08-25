package amaro.amaroandroid.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import amaro.amaroandroid.Adapters.FilterAdapter;
import amaro.amaroandroid.Fragments.Message.EntregaFragment;
import amaro.amaroandroid.R;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Helpers.Resource;


/**
 * Created by juan.villa on 21/06/2016.
 */
public class FilterFragment extends android.support.v4.app.DialogFragment {

  private RecyclerView filterOptionList;
  private Dialog dialog;
  private CloseInterface mListener;
  private String type;


  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.fragment_filter_dialog, null);
    filterOptionList = (RecyclerView) view.findViewById(R.id.filterOptionList);
    filterOptionList.setLayoutManager((new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false)));
    builder.setView(view);
    dialog = builder.create();
    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, view.getHeight());
    if (type.equalsIgnoreCase("filters")) {
      filterOptionList.setAdapter(new FilterAdapter(AmaroServices.getFilters(), getContext(), getActivity(), mListener, dialog, type));
    } else {
      Resource resource = new Resource();
      List<Resource> sorting = new ArrayList<>();
      sorting.add(resource);
      filterOptionList.setAdapter(new FilterAdapter(sorting, getContext(), getActivity(), mListener, dialog, type));
    }
    dialog.show();
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public interface CloseInterface extends EntregaFragment.CloseInterface {
  }
}