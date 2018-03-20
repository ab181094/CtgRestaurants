package com.csecu.amrit.ctgrestaurants.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.adapters.RestaurantAdapter;
import com.csecu.amrit.ctgrestaurants.asyncTasks.GetRestaurants;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.listeners.RecyclerItemListener;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRestaurantsFragment extends Fragment implements AsyncResponse {
    RecyclerView recyclerView;
    GetRestaurants getRestaurants;
    RestaurantAdapter adapter;
    SearchView searchView;
    ArrayList<ResAndOwner> resList;

    public ViewRestaurantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_restaurants, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getRestaurants = new GetRestaurants(getActivity());
        searchView = view.findViewById(R.id.res_searchView);
        recyclerView = view.findViewById(R.id.res_recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        getRestaurants.delegate = this;
        getRestaurants.execute();

        recyclerView.addOnItemTouchListener(new RecyclerItemListener(getActivity(),
                recyclerView, new RecyclerItemListener.RecyclerTouchListener() {
            @Override
            public void onClickItem(View v, int position) {
                ResAndOwner resAndOwner = adapter.getItem(position);
                ViewFoodsFragment fragment = new ViewFoodsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("ResAndOwner", resAndOwner);
                fragment.setArguments(bundle);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onLongClickItem(View v, int position) {
                ResAndOwner resAndOwner = adapter.getItem(position);
                ResDetailsFragment fragment = new ResDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("ResAndOwner", resAndOwner);
                fragment.setArguments(bundle);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.home_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
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
                try {
                    ArrayList<ResAndOwner> filterList = new ArrayList<>();
                    for (int i = 0; i < resList.size(); i++) {
                        ResAndOwner resAndOwner = resList.get(i);
                        if (resAndOwner.getName().contains(s) || resAndOwner.getName().toLowerCase().contains(s)) {
                            filterList.add(resAndOwner);
                        }
                    }
                    showList(filterList);
                    return true;
                } catch (Exception e) {
                    ToastController toastController = new ToastController(getActivity());
                    toastController.errorToast(e.toString());
                }
                return false;
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
            ArrayList<ResAndOwner> resList = parseAll(result);
            showList(resList);
        }
    }

    private void showList(ArrayList<ResAndOwner> resList) {
        if (resList != null && resList.size() > 0) {
            adapter = new RestaurantAdapter(getActivity(), resList);
            recyclerView.setAdapter(adapter);
        } else {
            ToastController toastController = new ToastController(getActivity());
            toastController.errorToast("No entries found");
        }
    }

    private ArrayList<ResAndOwner> parseAll(String result) {
        JSONObject jsonObject;
        JSONArray jsonArray;

        resList = new ArrayList<>();
        try {
            jsonObject = new JSONObject(result);
            jsonArray = jsonObject.getJSONArray("server_response");
            for (int count = 0; count < jsonArray.length(); count++) {
                JSONObject object = jsonArray.getJSONObject(count);
                int id = 0;
                try {
                    id = object.getInt("ID");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String name = object.getString("Name");
                String owner = object.getString("Owner");
                String phone = object.getString("Phone");
                String password = object.getString("Password");
                String capacity = object.getString("Capacity");
                String description = object.getString("Description");
                String latitude = object.getString("Latitude");
                String longitude = object.getString("Longitude");
                String token = object.getString("Token");
                String image = object.getString("Image");

                ResAndOwner resAndOwner = new ResAndOwner();
                resAndOwner.setId(id);
                resAndOwner.setName(name);
                resAndOwner.setOwner(owner);
                resAndOwner.setPhone(phone);
                resAndOwner.setPassword(password);
                resAndOwner.setCapacity(capacity);
                resAndOwner.setDescription(description);
                resAndOwner.setLatitude(latitude);
                resAndOwner.setLongitude(longitude);
                resAndOwner.setToken(token);
                resAndOwner.setImage(image);

                resList.add(resAndOwner);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("CTGRESTAURANTS", e.toString());
            ToastController toastController = new ToastController(getActivity());
            toastController.warningToast(e.toString());
        }
        return resList;
    }
}
