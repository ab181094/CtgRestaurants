package com.csecu.amrit.ctgrestaurants.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.csecu.amrit.ctgrestaurants.R;
import com.csecu.amrit.ctgrestaurants.models.Booking;

import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by Amrit on 2/2/2018.
 */

public class CustomListAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<Booking> transactionsList;

    public CustomListAdapter(Context context, ArrayList<Booking> transactionsList) {
        this.context = context;
        this.transactionsList = transactionsList;
    }

    @Override
    public int getGroupCount() {
        return transactionsList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return transactionsList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return transactionsList.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Booking transactions = (Booking) getGroup(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_expanded_group, null);
        }

        TextView tvName = view.findViewById(R.id.view_book_tv_name);
        TextView tvDate = view.findViewById(R.id.view_book_tv_date);

        tvName.setText(decodeString(transactions.getName()));
        tvDate.setText(decodeString(transactions.getDate()));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Booking transactions = (Booking) getChild(i, i1);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_expanded_item, null);
        }
        TextView tvPhone = view.findViewById(R.id.view_book_tv_phone);
        TextView tvOccasion = view.findViewById(R.id.view_book_tv_occasion);
        TextView tvPerson = view.findViewById(R.id.view_book_tv_person);
        TextView tvTime = view.findViewById(R.id.view_book_tv_time);
        TextView tvItem = view.findViewById(R.id.view_book_tv_item);
        TextView tvReq = view.findViewById(R.id.view_book_tv_req);

        tvPhone.setText(decodeString(transactions.getPhone()));
        tvOccasion.setText(decodeString(transactions.getOccassion()));
        tvPerson.setText(decodeString(String.valueOf(transactions.getPerson())));
        tvTime.setText(decodeString(String.valueOf(transactions.getTime())));
        tvItem.setText(decodeString(String.valueOf(transactions.getItem())));
        tvReq.setText(decodeString(String.valueOf(transactions.getReq())));
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private String decodeString(String value) {
        try {
            value = URLDecoder.decode(value, "UTF-8");
            return value;
        } catch (Exception e) {
            return value;
        }
    }
}
