package amaro.amaroandroid.Fragments.Message;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import amaro.amaroandroid.R;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.MyCart.CartInfo.Shipping;
import mehdi.sakout.fancybuttons.FancyButton;


/**
 * Created by juan.villa on 21/06/2016.
 */
public class EntregaFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener, MaterialEditText.OnTouchListener, MaterialEditText.OnFocusChangeListener {

  FancyButton btnContinuar;
  ArrayList<String> shippingOptions = new ArrayList<String>();
  MaterialEditText edtxtNome, edtxtSobrenome, edtxtCep, edtxtEndereco, edtxtNumero, edtxtComplemento, edtxtBairro, edtxtCidade;
  int shipSelected = 0;
  boolean isUserSelection = false;
  boolean isUserType = false;
  ArrayList<Shipping> shipOtions = new ArrayList<>();
  RadioButton radBtnComercial, radBtnResidential;
  ArrayAdapter<CharSequence> adapterUf;
  private Dialog dialog;
  private Spinner spnEstado, spnEnvio;
  private CloseInterface mListener;
  private String lastCep;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.fragment_entrega_dialog, null);
    setChildrenListeners(view);

    radBtnComercial = (RadioButton) view.findViewById(R.id.radBtnComercial);
    radBtnResidential = (RadioButton) view.findViewById(R.id.radBtnResidential);

    edtxtNome = (MaterialEditText) view.findViewById(R.id.edtxtNome);

    edtxtSobrenome = (MaterialEditText) view.findViewById(R.id.edtxtSobrenome);

    edtxtCep = (MaterialEditText) view.findViewById(R.id.edtxtCep);

    edtxtEndereco = (MaterialEditText) view.findViewById(R.id.edtxtEndereco);

    edtxtNumero = (MaterialEditText) view.findViewById(R.id.edtxtNumero);

    edtxtComplemento = (MaterialEditText) view.findViewById(R.id.edtxtComplemento);

    edtxtBairro = (MaterialEditText) view.findViewById(R.id.edtxtBairro);

    edtxtCidade = (MaterialEditText) view.findViewById(R.id.edtxtCidade);
    if (AmaroServices.getCart().getShipping_address().getType() != null) {
      if (AmaroServices.getCart().getShipping_address().getType().equalsIgnoreCase("residential")) {
        radBtnResidential.setChecked(true);
      } else {
        radBtnComercial.setChecked(true);
      }
      edtxtNome.setText(AmaroServices.getCart().getShipping_address().getName());
      edtxtSobrenome.setText(AmaroServices.getCart().getShipping_address().getLastname());
      edtxtCep.setText(AmaroServices.getCart().getApplied_zip());
      lastCep = AmaroServices.getCart().getApplied_zip();
      edtxtEndereco.setText(AmaroServices.getCart().getShipping_address().getStreet());
      edtxtNumero.setText(AmaroServices.getCart().getShipping_address().getNumber());
      edtxtComplemento.setText(AmaroServices.getCart().getShipping_address().getAdjunct());
      edtxtBairro.setText(AmaroServices.getCart().getShipping_address().getNeighborhood());
      edtxtCidade.setText(AmaroServices.getCart().getShipping_address().getCity());

    } else {
      edtxtEndereco.setText(AmaroServices.getLookupZipResponse().getAddress().getStreet());
      edtxtComplemento.setText(AmaroServices.getLookupZipResponse().getAddress().getAdjunct());
      edtxtBairro.setText(AmaroServices.getLookupZipResponse().getAddress().getNeighborhood());
      edtxtCidade.setText(AmaroServices.getLookupZipResponse().getAddress().getCity());
    }

    btnContinuar = (FancyButton) view.findViewById(R.id.btnContinuar);

    btnContinuar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fieldValidator()) {
          mListener.onClose("saveShippingInfo", "");
          dialog.hide();
        }
      }
    });

    spnEnvio = (Spinner) view.findViewById(R.id.spnEnvio);
    spnEstado = (Spinner) view.findViewById(R.id.spnEstado);

    adapterUf = ArrayAdapter.createFromResource(getContext(),
        R.array.ufs, android.R.layout.simple_spinner_item);
    adapterUf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    initShippingOtions();

    ArrayAdapter<String> adapterShipping = new ArrayAdapter<>(getContext(),
        android.R.layout.simple_spinner_item, shippingOptions);
    adapterShipping.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    spnEstado.setAdapter(adapterUf);
    if (AmaroServices.getCart().getShipping_address().getState() != null) {
      if (!AmaroServices.getCart().getShipping_address().getState().equals(null)) {
        int spinnerPosition = adapterUf.getPosition(AmaroServices.getCart().getShipping_address().getState());
        spnEstado.setSelection(spinnerPosition);
      }
    } else {
      if (!AmaroServices.getLookupZipResponse().getAddress().getState().equals(null)) {
        int spinnerPosition = adapterUf.getPosition(AmaroServices.getLookupZipResponse().getAddress().getState());
        spnEstado.setSelection(spinnerPosition);
      }
    }


    spnEnvio.setAdapter(adapterShipping);
    spnEnvio.setSelection(shipSelected);

    spnEnvio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        String optionSelected;
        if (isUserSelection) {
          for (int i = 0; i < shipOtions.size(); i++) {
            optionSelected = shipOtions.get(i).getName() + " (" + shipOtions.get(i).getRate() + " - " + shipOtions.get(i).getDelivery_time() + ")";
            if (optionSelected.equals(selectedItem)) {
              mListener.onClose("shipping", shipOtions.get(i).getId());
              dialog.hide();
              break;
            }
          }
        } else {
          isUserSelection = true;
        }
      }

      // to close the onItemSelected
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    setEdtxtListener();
    builder.setView(view);
    dialog = builder.create();
    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, view.getHeight());
    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, view.getWidth());
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

  private void initShippingOtions() {
    shipOtions.addAll(AmaroServices.getCart().getShipping_options());
    for (int i = 0; i < shipOtions.size(); i++) {
      if (!shipOtions.get(i).getChecked().equals("")) {
        shipSelected = i;
      }
      shippingOptions.add(shipOtions.get(i).getName() + " (" + shipOtions.get(i).getRate() + " - " + shipOtions.get(i).getDelivery_time() + ")");
    }
  }

  @Override
  public void onFocusChange(View view, boolean hasFocus) {
    if (((MaterialEditText) view).getKeyListener() == null) {
      InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

  @Override
  public boolean onTouch(View view, MotionEvent event) {
    if (view instanceof MaterialEditText) {
      view.setOnFocusChangeListener(this);
    }
    return false;
  }


  private void setEdtxtListener() {
    edtxtCep.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {
      }

      @Override
      public void onTextChanged(final CharSequence s, int start, int before,
                                int count) {
        isUserType = true;
      }

      @Override
      public void afterTextChanged(final Editable s) {
        //avoid triggering event when text is too short
        if (s.length() == 8) {
          if (isUserType) {
            mListener.onClose("cep", s.toString());
            edtxtNumero.setText("");
            edtxtComplemento.setText("");
            dialog.hide();
          } else {
            isUserType = true;
          }
        }
      }
    });
  }


  private ArrayList<View> getAllChildren(View v) {

    if (!(v instanceof ViewGroup)) {
      ArrayList<View> viewArrayList = new ArrayList<View>();
      viewArrayList.add(v);
      return viewArrayList;
    }

    ArrayList<View> result = new ArrayList<View>();

    ViewGroup vg = (ViewGroup) v;
    for (int i = 0; i < vg.getChildCount(); i++) {

      View child = vg.getChildAt(i);

      ArrayList<View> viewArrayList = new ArrayList<View>();
      viewArrayList.add(v);
      viewArrayList.addAll(getAllChildren(child));

      result.addAll(viewArrayList);
    }
    return result;
  }

  private void setChildrenListeners(View view) {
    ArrayList<View> allViewsWithinMyTopView = getAllChildren(view);
    for (View child : allViewsWithinMyTopView) {
      if (child instanceof MaterialEditText) {
        child.setOnTouchListener(this);
        if (!((MaterialEditText) child).getHint().equals("CEP") && !((MaterialEditText) child).getHint().equals("NUM") && !((MaterialEditText) child).getHint().equals("COMP")) {
          ((MaterialEditText) child).setKeyListener(null);
        }
      }
      if (child instanceof RadioButton) {
        child.setOnClickListener(this);
      }
    }
  }

  @Override
  public void onClick(View view) {
    if (view instanceof RadioButton) {
      if (((RadioButton) view).getHint().equals("r")) {
        radBtnComercial.setChecked(false);
        radBtnResidential.setChecked(true);
      } else {
        radBtnComercial.setChecked(true);
        radBtnResidential.setChecked(false);
      }
    }
  }

  public String getLastCep() {
    return lastCep;
  }

  public MaterialEditText getEdtxtNome() {
    return edtxtNome;
  }

  public MaterialEditText getEdtxtSobrenome() {
    return edtxtSobrenome;
  }

  public MaterialEditText getEdtxtCep() {
    return edtxtCep;
  }

  public MaterialEditText getEdtxtEndereco() {
    return edtxtEndereco;
  }

  public MaterialEditText getEdtxtNumero() {
    return edtxtNumero;
  }

  public MaterialEditText getEdtxtComplemento() {
    return edtxtComplemento;
  }

  public MaterialEditText getEdtxtBairro() {
    return edtxtBairro;
  }

  public MaterialEditText getEdtxtCidade() {
    return edtxtCidade;
  }

  public ArrayList<String> getShippingOptions() {
    return shippingOptions;
  }

  public Spinner getSpnEstado() {
    return spnEstado;
  }

  public Spinner getSpnEnvio() {
    return spnEnvio;
  }

  public ArrayAdapter<CharSequence> getAdapterUf() {
    return adapterUf;
  }

  public void setUserSelection(boolean userSelection) {
    isUserSelection = userSelection;
  }

  public void setUserType(boolean userType) {
    isUserType = userType;
  }

  public boolean fieldValidator() {
    boolean formfilled = true;
    if (edtxtCep.getText().toString().equals("")) {
      formfilled = false;
      edtxtCep.setHint("Obrigatorio");
    }
    if (edtxtEndereco.getText().toString().equals("")) {
      formfilled = false;
      edtxtEndereco.setHint("Obrigatorio");
    }
    if (edtxtNumero.getText().toString().equals("")) {
      formfilled = false;
      edtxtNumero.setHint("Obrigatorio");
    }
    return formfilled;
  }

  public interface CloseInterface {
    void onClose(String callServiceString, String params);
  }
}