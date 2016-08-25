package amaro.amaroandroid.Fragments.Message;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import amaro.amaroandroid.R;


/**
 * Created by juan.villa on 21/06/2016.
 */
public class LoadingFragment extends android.support.v4.app.DialogFragment {

  private Dialog dialog;
  private AVLoadingIndicatorView avloadingIndicatorView;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.fragment_loading_dialog, null);
    avloadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avloadingIndicatorView);
    builder.setView(view);
    dialog = builder.create();
    dialog.show();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    return dialog;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

}