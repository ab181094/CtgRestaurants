package com.csecu.amrit.ctgrestaurants.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.asyncTasks.AddFood;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.models.Food;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFoodFragment extends Fragment {
    EditText etName, etPrice, etDescription;
    Spinner spType;
    Button btSave;
    int restaurant;
    double rating = 0.0;
    String op;
    Food food;

    public AddFoodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_food, container, false);

        etName = view.findViewById(R.id.add_food_et_name);
        etPrice = view.findViewById(R.id.add_food_et_price);
        etDescription = view.findViewById(R.id.add_food_et_description);
        spType = view.findViewById(R.id.add_food_sp_type);
        btSave = view.findViewById(R.id.add_food_bt_Save);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastController toastController = new ToastController(getActivity());

                String name = etName.getText().toString().trim();
                name = encodeString(name);
                String price = etPrice.getText().toString().trim();
                price = encodeString(price);
                String description = etDescription.getText().toString().trim();
                description = encodeString(description);
                String type = (String) spType.getSelectedItem();
                type = encodeString(type);

                if (name.length() == 0 || name == null) {
                    toastController.errorToast("Fill this field");
                    View focusView = etName;
                    focusView.requestFocus();
                } else {
                    if (price.length() == 0 || price == null) {
                        toastController.errorToast("Fill this field");
                        View focusView = etPrice;
                        focusView.requestFocus();
                    } else {
                        if (description.length() == 0 || description == null) {
                            toastController.errorToast("Fill this field");
                            View focusView = etDescription;
                            focusView.requestFocus();
                        } else {
                            if (op == null) {
                                Food food = new Food(name, price, description, type, restaurant, rating);

                                ImageFragment fragment = new ImageFragment();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("food", food);
                                fragment.setArguments(bundle);
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.owner_container, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            } else {
                                food.setName(name);
                                food.setPrice(price);
                                food.setDescription(description);
                                food.setType(type);

                                ImageFragment fragment = new ImageFragment();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("food", food);
                                bundle.putString("op", "update");
                                fragment.setArguments(bundle);
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.owner_container, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ResAndOwner resAndOwner = bundle.getParcelable("ResAndOwner");
            if (resAndOwner != null) {
                restaurant = resAndOwner.getId();
            }

            food = bundle.getParcelable("food");
            op = bundle.getString("op");

            if (op != null) {
                etName.setText(decodeString(food.getName()));
                etPrice.setText(decodeString(food.getPrice()));
                etDescription.setText(decodeString(food.getDescription()));

                String[] types = getResources().getStringArray(R.array.type_array);

                for (int i = 0; i<types.length;i++) {
                    if (types[i].equals(food.getType())) {
                        spType.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    private String encodeString(String s) {
        ToastController toastController = new ToastController(getActivity());
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            toastController.errorToast(e.toString());
        }
        return s;
    }

    private String decodeString(String s) {
        ToastController toastController = new ToastController(getActivity());
        try {
            s = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            toastController.errorToast(e.toString());
        }
        return s;
    }
}
