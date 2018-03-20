package com.csecu.amrit.ctgrestaurants.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.adapters.CustomListAdapter;
import com.csecu.amrit.ctgrestaurants.adapters.RestaurantAdapter;
import com.csecu.amrit.ctgrestaurants.asyncTasks.GetBooking;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.Booking;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewBookingFragment extends Fragment implements AsyncResponse {
    ExpandableListView listView;
    ArrayList<Booking> bookList;
    CustomListAdapter adapter;
    ResAndOwner resAndOwner;

    public ViewBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_booking, container, false);
        listView = view.findViewById(R.id.expanded_list_view);

        GetBooking getBooking = new GetBooking(getActivity());
        getBooking.delegate = this;
        getBooking.execute();

        return view;
    }

    @Override
    public void processFinish(Object output) {
        if (output == null) {
            ToastController toastController = new ToastController(getActivity());
            toastController.errorToast("Problem in retrieving data");
        } else {
            String result = output.toString().trim();
            bookList = parseAll(result);
            showList(bookList);
        }
    }

    private void showList(ArrayList<Booking> resList) {
        if (resList != null && resList.size() > 0) {
            adapter = new CustomListAdapter(getActivity(), bookList);
            listView.setAdapter(adapter);
        } else {
            ToastController toastController = new ToastController(getActivity());
            toastController.errorToast("No entries found");
        }
    }

    private ArrayList<Booking> parseAll(String result) {
        JSONObject jsonObject;
        JSONArray jsonArray;

        ArrayList<Booking> resList = new ArrayList<>();
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
                String phone = object.getString("Phone");
                String occasion = object.getString("Occasion");
                String person = object.getString("Person");
                String date = object.getString("Date");
                String time = object.getString("Time");
                String item = object.getString("Item");
                String req = object.getString("Req");
                String restaurant = object.getString("Restaurant");
                String token = object.getString("Token");

                Booking booking = new Booking();
                booking.setId(id);
                booking.setName(name);
                booking.setPhone(phone);
                booking.setOccassion(occasion);
                booking.setPerson(person);
                booking.setDate(date);
                booking.setTime(time);
                booking.setItem(item);
                booking.setReq(req);
                booking.setRestaurant(restaurant);
                booking.setToken(token);

                if (resAndOwner.getId() == Integer.parseInt(restaurant)) {
                    resList.add(booking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("CTGRESTAURANTS", e.toString());
            ToastController toastController = new ToastController(getActivity());
            toastController.warningToast(e.toString());
        }
        return resList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resAndOwner = bundle.getParcelable("ResAndOwner");
        }
    }
}
