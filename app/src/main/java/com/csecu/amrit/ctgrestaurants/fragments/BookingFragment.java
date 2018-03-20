package com.csecu.amrit.ctgrestaurants.fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.asyncTasks.AddBooking;
import com.csecu.amrit.ctgrestaurants.asyncTasks.GetFoods;
import com.csecu.amrit.ctgrestaurants.controllers.ToastController;
import com.csecu.amrit.ctgrestaurants.interfaces.AsyncResponse;
import com.csecu.amrit.ctgrestaurants.models.Booking;
import com.csecu.amrit.ctgrestaurants.models.Food;
import com.csecu.amrit.ctgrestaurants.models.ResAndOwner;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment implements AsyncResponse {
    EditText etName, etPhone, etOccasion, etPerson, etReq;
    Button btDate, btTime, btSubmit;
    Spinner spFood;
    TextView tvDate, tvTime;
    TagContainerLayout tagContainerLayout;
    int day = 0, month = 0, year = 0;
    ResAndOwner resAndOwner;
    ArrayList<Food> foodsList;
    ArrayList<String> foodItems;
    ArrayList<String> selectedItems;

    public BookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        etName = view.findViewById(R.id.booking_et_name);
        etPhone = view.findViewById(R.id.booking_et_phone);
        etOccasion = view.findViewById(R.id.booking_et_occasion);
        etPerson = view.findViewById(R.id.booking_et_person);
        etReq = view.findViewById(R.id.booking_et_request);
        btDate = view.findViewById(R.id.booking_bt_date);
        btTime = view.findViewById(R.id.booking_bt_time);
        btSubmit = view.findViewById(R.id.booking_bt_submit);
        spFood = view.findViewById(R.id.booking_sp_items);
        tvDate = view.findViewById(R.id.booking_tv_date);
        tvTime = view.findViewById(R.id.booking_tv_time);
        tagContainerLayout = view.findViewById(R.id.booking_tag);

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);
                DatePickerDialog dialog =
                        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                tvDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                tvDate.setVisibility(View.VISIBLE);
                            }
                        }, year, month, day);
                dialog.setTitle("Select Date");
                dialog.show();
            }
        });

        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String start = selectedHour + ":" + selectedMinute;
                        tvTime.setText(start);
                        tvTime.setVisibility(View.VISIBLE);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                name = encodeString(name);
                String occasion = etOccasion.getText().toString().trim();
                occasion = encodeString(occasion);
                String phone = etPhone.getText().toString().trim();
                phone = encodeString(phone);
                String person = etPerson.getText().toString().trim();
                person = encodeString(person);
                String req = etReq.getText().toString().trim();
                req = encodeString(req);

                String date = "", time = "", item = "";
                if (tvDate.getText().toString() != null) {
                    date = encodeString(tvDate.getText().toString().trim());
                }

                if (tvTime.getText().toString() != null) {
                    time = encodeString(tvTime.getText().toString().trim());
                }

                selectedItems = new ArrayList<>();
                selectedItems = (ArrayList<String>) tagContainerLayout.getTags();

                for (int i = 0; i < selectedItems.size(); i++) {
                    item = item + " " + selectedItems.get(i);
                    if (i != (selectedItems.size() - 1)) {
                        item = item + ",";
                    }
                }

                item = encodeString(item);

                Log.d("ItemTobeServed", item);

                String token = FirebaseInstanceId.getInstance().getToken();

                if (name.length() == 0 || name == null) {
                    etName.setError("Fill this field");
                    View focusView = etName;
                    focusView.requestFocus();
                } else {
                    if (phone.length() == 0 || phone == null) {
                        etPhone.setError("Fill this field");
                        View focusView = etPhone;
                        focusView.requestFocus();
                    } else {
                        if (person.length() == 0 || person == null) {
                            etPerson.setError("Fill this field");
                            View focusView = etPerson;
                            focusView.requestFocus();
                        } else {
                            Booking booking = new Booking(name, phone, occasion, person, date, time,
                                    item, req, String.valueOf(resAndOwner.getId()), token);
                            AddBooking addBooking = new AddBooking(getActivity());
                            addBooking.execute(booking);
                        }
                    }
                }
            }
        });

        return view;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resAndOwner = bundle.getParcelable("ResAndOwner");
            try {
                GetFoods getFoods = new GetFoods(getActivity());
                getFoods.delegate = this;
                getFoods.execute();
            } catch (Exception e) {
                ToastController toastController = new ToastController(getActivity());
                toastController.warningToast(e.toString());
            }
        }
    }

    @Override
    public void processFinish(Object output) {
        if (output == null) {
            ToastController toastController = new ToastController(getActivity());
            toastController.errorToast("Problem in retrieving data");
        } else {
            String result = output.toString().trim();
            foodsList = parseAll(result);

            foodItems = new ArrayList<>();
            for (int i = 0; i < foodsList.size(); i++) {
                foodItems.add(decodeString(foodsList.get(i).getName()));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, foodItems);
            spFood.setAdapter(adapter);

            selectedItems = new ArrayList<>();

            spFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    /*if (!selectedItems.contains(foodItems.get(i))) {
                        selectedItems.add(foodItems.get(i));
                    }*/

                    tagContainerLayout.addTag(foodItems.get(i));
                    tagContainerLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text) {

                }

                @Override
                public void onTagLongClick(int position, String text) {

                }

                @Override
                public void onTagCrossClick(int position) {
                    tagContainerLayout.removeTag(position);
                }
            });
        }
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
}
