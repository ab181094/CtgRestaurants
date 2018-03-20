package com.csecu.amrit.ctgrestaurants.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SearchView;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.adapters.FoodAdapter;
import com.csecu.amrit.ctgrestaurants.asyncTasks.DeleteFood;
import com.csecu.amrit.ctgrestaurants.asyncTasks.GetFoods;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.listeners.RecyclerItemListener;
import com.csecu.amrit.ctgrestaurants.models.Food;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFoodsFragment extends Fragment implements AsyncResponse {
    ResAndOwner resAndOwner;
    RecyclerView recyclerView;
    GetFoods getFoods;
    FoodAdapter adapter;
    Boolean auth;
    SearchView searchView;
    DeleteFood deleteFood;
    ArrayList<Food> foodsList;

    public ViewFoodsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_foods, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        auth = sharedpreferences.getBoolean(getString(R.string.auth), false);

        getFoods = new GetFoods(getActivity());
        searchView = view.findViewById(R.id.food_searchView);
        recyclerView = view.findViewById(R.id.food_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (!auth) {
            getFoods.delegate = this;
            getFoods.execute();
        }

        deleteFood = new DeleteFood(getActivity(), getActivity().getSupportFragmentManager());
        deleteFood.delegate = this;

        recyclerView.addOnItemTouchListener(new RecyclerItemListener(getActivity(),
                recyclerView, new RecyclerItemListener.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                Food food = adapter.getItem(position);

                ToastController toastController = new ToastController(getActivity());
                FoodFragment fragment = new FoodFragment();

                if (!auth) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("food", food);
                    fragment.setArguments(bundle);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.home_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("food", food);
                    fragment.setArguments(bundle);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.owner_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void onLongClickItem(View v, int position) {
                if (auth) {
                    final Food food = adapter.getItem(position);
                    new LovelyStandardDialog(getActivity(), LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                            .setTopColorRes(R.color.colorPrimaryDark)
                            .setButtonsColorRes(R.color.colorAccent)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle(R.string.confirmation)
                            .setMessage(R.string.delete_food)
                            .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteFood.execute(food.getId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            }
        }));

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Food> filterList = new ArrayList<>();
                try {
                    for (int i = 0; i < foodsList.size(); i++) {
                        Food food = foodsList.get(i);
                        if (food.getName().contains(s) || food.getName().toLowerCase().contains(s)) {
                            filterList.add(food);
                        } else {
                            try {
                                Double price = Double.parseDouble(s);
                                Double original = Double.parseDouble(food.getPrice());

                                if (price <= original) {
                                    filterList.add(food);
                                }
                            } catch (Exception e) {
                                ToastController toastController = new ToastController(getActivity());
                                toastController.errorToast("" + e.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    ToastController toastController = new ToastController(getActivity());
                    toastController.errorToast("" + e.toString());
                }
                showList(filterList);
                return true;
            }
        });
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");

        return view;
    }

    @Override
    public void processFinish(Object output) {
        if (output == null) {
            ToastController toastController = new ToastController(getActivity());
            toastController.errorToast("Problem in retrieving data");
        } else {
            String result = output.toString().trim();
            ArrayList<Food> foodsList = parseAll(result);
            showList(foodsList);
        }
    }

    private void showList(ArrayList<Food> foodsList) {
        if (foodsList != null && foodsList.size() > 0) {
            adapter = new FoodAdapter(getActivity(), foodsList);
            recyclerView.setAdapter(adapter);
        } else {
            ToastController toastController = new ToastController(getActivity());
            toastController.errorToast("No entries found");
        }
    }

    private ArrayList<Food> parseAll(String result) {
        JSONObject jsonObject;
        JSONArray jsonArray;

        foodsList = new ArrayList<>();
        try {
            jsonObject = new JSONObject(result);
            jsonArray = jsonObject.getJSONArray("server_response");
            for (int count = 0; count < jsonArray.length(); count++) {
                JSONObject object = jsonArray.getJSONObject(count);
                int id = object.getInt("ID");
                String name = object.getString("Name");
                String price = object.getString("Price");
                String description = object.getString("Description");
                String type = object.getString("Type");
                String image = object.getString("Image");
                String restaurant = object.getString("Restaurant");
                String rating = object.getString("Rating");

                if (resAndOwner == null) {
                    Food food = new Food();
                    food.setId(id);
                    food.setName(name);
                    food.setPrice(price);
                    food.setDescription(description);
                    food.setType(type);
                    food.setImage(image);
                    food.setRestaurant(Integer.parseInt(restaurant));
                    food.setRating(Double.parseDouble(rating));

                    foodsList.add(food);
                } else {
                    int resID = Integer.parseInt(restaurant);
                    if (resID == resAndOwner.getId()) {
                        Food food = new Food();
                        food.setId(id);
                        food.setName(name);
                        food.setPrice(price);
                        food.setDescription(description);
                        food.setType(type);
                        food.setImage(image);
                        food.setRestaurant(Integer.parseInt(restaurant));
                        food.setRating(Double.parseDouble(rating));

                        foodsList.add(food);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastController toastController = new ToastController(getActivity());
            toastController.warningToast(e.toString());
        }
        return foodsList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resAndOwner = bundle.getParcelable("ResAndOwner");
            try {
                getFoods.delegate = this;
                getFoods.execute();
            } catch (Exception e) {
                ToastController toastController = new ToastController(getActivity());
                // toastController.warningToast(e.toString());
            }
        }
    }
}
