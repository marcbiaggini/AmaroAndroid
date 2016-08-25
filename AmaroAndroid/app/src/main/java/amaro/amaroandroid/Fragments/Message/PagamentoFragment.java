package amaro.amaroandroid.Fragments.Message;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import amaro.amaroandroid.R;
import amaro.api.Model.MyCart.PaymentInfo.GetPaymentResponse;
import amaro.api.Model.Type;
import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by juan.villa on 21/06/2016.
 */
public class PagamentoFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

  private MaterialEditText edtxtCartao, edtxtCvv, edtxtMes, edtxtAno, edtxtNome;
  private String firstYear, lastYear, selectedMonth, selectedYear, paymentMethod;
  private CloseInterface mListener;
  private View view;
  private TextView txtBoleto;
  private Dialog dialog;
  private Spinner spnParcela;
  private GetPaymentResponse paymentResponse;
  private ArrayList<String> installmentOptions = new ArrayList<>();
  private FancyButton btnContinuar, btnBoleto, btnCredito;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    view = inflater.inflate(R.layout.fragment_pagamento_dialog, null);
    txtBoleto = (TextView) view.findViewById(R.id.txtBoleto);
    setInstallmentsOptionsArray();
    setSlectedMonthYear();

    spnParcela = (Spinner) view.findViewById(R.id.spnParcelas);

    ArrayAdapter<String> adapterInstallments = new ArrayAdapter<>(getContext(),
        android.R.layout.simple_spinner_item, installmentOptions);
    adapterInstallments.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    spnParcela.setAdapter(adapterInstallments);


    edtxtCartao = (MaterialEditText) view.findViewById(R.id.edtxtCartao);
    edtxtCvv = (MaterialEditText) view.findViewById(R.id.edtxtCvv);
    edtxtMes = (MaterialEditText) view.findViewById(R.id.edtxtMes);
    edtxtAno = (MaterialEditText) view.findViewById(R.id.edtxtAno);
    edtxtNome = (MaterialEditText) view.findViewById(R.id.edtxtNome);

    if (paymentResponse.getResponse().getPaymentOptions().getCreditCard().getCard_data() != null) {
      edtxtCartao.setText(paymentResponse.getResponse().getPaymentOptions().getCreditCard().getCard_data().getNumber());
      edtxtCvv.setText(paymentResponse.getResponse().getPaymentOptions().getCreditCard().getCard_data().getCvv());
      edtxtMes.setText(selectedMonth);
      edtxtAno.setText(selectedYear);
      edtxtNome.setText(paymentResponse.getResponse().getPaymentOptions().getCreditCard().getCard_data().getCardholder());
    }

    btnBoleto = (FancyButton) view.findViewById(R.id.btnBoleto);
    btnContinuar = (FancyButton) view.findViewById(R.id.btnContinuar);
    btnCredito = (FancyButton) view.findViewById(R.id.btnCredito);
    btnBoleto.setOnClickListener(this);
    btnContinuar.setOnClickListener(this);
    btnCredito.setOnClickListener(this);

    if (paymentResponse.getResponse().getPaymentOptions().getCreditCard().isSelected()) {
      btnCredito.setBorderColor(Color.TRANSPARENT);
      btnCredito.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLightDark));
      btnCredito.setTextColor(getActivity().getResources().getColor(R.color.colorTransparentWhite));

      btnBoleto.setBorderColor(getActivity().getResources().getColor(R.color.colorLightDark));
      btnBoleto.setBackgroundColor(getActivity().getResources().getColor(R.color.colorTransparentWhite));
      btnBoleto.setTextColor(getActivity().getResources().getColor(R.color.colorLightDark));
      setChildrenVisibility(View.VISIBLE);
      txtBoleto.setVisibility(View.INVISIBLE);
      paymentMethod = "11";
    } else {
      btnBoleto.setBorderColor(Color.TRANSPARENT);
      btnBoleto.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLightDark));
      btnBoleto.setTextColor(getActivity().getResources().getColor(R.color.colorTransparentWhite));

      btnCredito.setBorderColor(getActivity().getResources().getColor(R.color.colorLightDark));
      btnCredito.setBackgroundColor(getActivity().getResources().getColor(R.color.colorTransparentWhite));
      btnCredito.setTextColor(getActivity().getResources().getColor(R.color.colorLightDark));
      setChildrenVisibility(View.INVISIBLE);
      txtBoleto.setVisibility(View.VISIBLE);
      paymentMethod = "22";
    }

    builder.setView(view);
    dialog = builder.create();
    dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, view.getHeight());
    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, view.getWidth());
    dialog.show();
    return dialog;
  }

  private void setSlectedMonthYear() {
    firstYear = "2016";
    lastYear = "2016";
    ArrayList<Type> month = new ArrayList<>();
    month.addAll(paymentResponse.getResponse().getPaymentOptions().getCreditCard().getExp_months());

    ArrayList<Type> year = new ArrayList<>();
    year.addAll(paymentResponse.getResponse().getPaymentOptions().getCreditCard().getExp_years());

    for (int i = 0; i < month.size(); i++) {
      if (month.get(i).isSelected()) {
        selectedMonth = month.get(i).getValue();
      }
    }

    for (int i = 0; i < year.size(); i++) {

      if (year.get(i).isSelected()) {
        selectedYear = year.get(i).getValue();
      }

      if (!year.get(i).getValue().equals("") && Integer.valueOf(firstYear) > Integer.valueOf(year.get(i).getValue())) {
        firstYear = year.get(i).getValue();
      }

      if (!year.get(i).getValue().equals("") && Integer.valueOf(lastYear) < Integer.valueOf(year.get(i).getValue())) {
        lastYear = year.get(i).getValue();
      }
    }
  }

  private void setInstallmentsOptionsArray() {
    ArrayList<Type> installments = new ArrayList<>();
    installments.addAll(paymentResponse.getResponse().getPaymentOptions().getCreditCard().getInstallments());

    for (int i = 0; i < installments.size(); i++) {
      installmentOptions.add(installments.get(i).getName());
    }
    Collections.sort(installmentOptions, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s1.compareToIgnoreCase(s2);
      }
    });
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

  public MaterialEditText getEdtxtCartao() {
    return edtxtCartao;
  }

  public MaterialEditText getEdtxtCvv() {
    return edtxtCvv;
  }

  public MaterialEditText getEdtxtMes() {
    return edtxtMes;
  }

  public MaterialEditText getEdtxtAno() {
    return edtxtAno;
  }

  public MaterialEditText getEdtxtNome() {
    return edtxtNome;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public Spinner getSpnParcela() {
    return spnParcela;
  }


  public void setSpnParcela(Spinner spnParcela) {
    this.spnParcela = spnParcela;
  }

  public GetPaymentResponse getPaymentResponse() {
    return paymentResponse;
  }

  public void setPaymentResponse(GetPaymentResponse paymentResponse) {
    this.paymentResponse = paymentResponse;
  }

  @Override
  public void onClick(View v) {
    if (v instanceof FancyButton) {
      switch (((FancyButton) v).getText().toString()) {
        case "CREDITO":
          btnCredito.setBorderColor(Color.TRANSPARENT);
          btnCredito.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLightDark));
          btnCredito.setTextColor(getActivity().getResources().getColor(R.color.colorTransparentWhite));

          btnBoleto.setBorderColor(getActivity().getResources().getColor(R.color.colorLightDark));
          btnBoleto.setBackgroundColor(getActivity().getResources().getColor(R.color.colorTransparentWhite));
          btnBoleto.setTextColor(getActivity().getResources().getColor(R.color.colorLightDark));
          setChildrenVisibility(View.VISIBLE);
          txtBoleto.setVisibility(View.INVISIBLE);
          paymentMethod = "11";
          mListener.onClose("selectPayment", "");
          dialog.hide();
          break;
        case "BOLETO":
          btnBoleto.setBorderColor(Color.TRANSPARENT);
          btnBoleto.setBackgroundColor(getActivity().getResources().getColor(R.color.colorLightDark));
          btnBoleto.setTextColor(getActivity().getResources().getColor(R.color.colorTransparentWhite));

          btnCredito.setBorderColor(getActivity().getResources().getColor(R.color.colorLightDark));
          btnCredito.setBackgroundColor(getActivity().getResources().getColor(R.color.colorTransparentWhite));
          btnCredito.setTextColor(getActivity().getResources().getColor(R.color.colorLightDark));
          setChildrenVisibility(View.INVISIBLE);
          txtBoleto.setVisibility(View.VISIBLE);
          paymentMethod = "22";
          mListener.onClose("selectPayment", "");
          dialog.hide();
          break;
        default:
          if (validateCardFields()) {
            mListener.onClose("validatePayment", "");
            dialog.hide();
          } else {
            setErrorMessage((AppCompatActivity) getActivity(), getActivity().getResources().getString(R.string.form_error_message) + "do cartão!");
            dialog.hide();
          }
          break;
      }
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
            dialog.show();
          }
        })
        .show();
  }

  private void setChildrenVisibility(int visibility) {
    edtxtCartao.setVisibility(visibility);
    edtxtCvv.setVisibility(visibility);
    edtxtMes.setVisibility(visibility);
    edtxtAno.setVisibility(visibility);
    edtxtNome.setVisibility(visibility);
    spnParcela.setVisibility(visibility);
  }

  public boolean validateCardFields() {
    boolean formulary = true;
    if (edtxtCartao.getText().toString().equals("")) {
      formulary = false;
    }
    if (edtxtCvv.getText().toString().equals("")) {
      formulary = false;
    }
    if (edtxtMes.getText().toString().equals("")) {
      formulary = false;
    }
    if (edtxtAno.getText().toString().equals("")) {
      formulary = false;
    }
    if (edtxtNome.getText().toString().equals("")) {
      formulary = false;
    }
    return formulary;
  }

  public interface CloseInterface extends EntregaFragment.CloseInterface {
  }
}