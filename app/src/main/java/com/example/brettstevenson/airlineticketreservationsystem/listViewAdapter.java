package com.example.brettstevenson.airlineticketreservationsystem;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.ArrayList;

public class listViewAdapter extends BaseAdapter {

    private ArrayList<Flight> flightList;
    public  ArrayList<String> itemIds;
    public  ArrayList<Integer> seatCounts;
    Activity activity;

    public listViewAdapter(Activity activity, ArrayList<Flight> flightList, ArrayList<String> itemIds,
                           ArrayList<Integer> seatCounts) {
        super();
        this.activity = activity;
        this.flightList = flightList;
        this.itemIds = itemIds;
        if(seatCounts.size() == 0)
            for (int i = 0; i < flightList.size(); i++)
                seatCounts.add(1);
        this.seatCounts = seatCounts;
    }
    public listViewAdapter(Activity activity, ArrayList<Flight> flightList, ArrayList<String> itemIds) {
        this(activity, flightList, itemIds, new ArrayList<Integer>());
    }
    public listViewAdapter(Activity activity, ArrayList<Flight> flightList) {
        this(activity, flightList, new ArrayList<String>());
    }

//    public listViewAdapter(Activity activity, ArrayList<Reservation> reservationList) {
//        super();
//        this.activity = activity;
//        this.reservationList = reservationList;
//    }

    @Override
    public int getCount() { return flightList.size(); }

    @Override
    public Object getItem(int position) { return flightList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    private class ViewHolder {
        TextView mHiddenId;
        TextView mId;
        TextView mDeparture;
        TextView mArrival;
        TextView mTime;
        TextView mPrice;
    }

//    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.mHiddenId = convertView.findViewById(R.id.hidden_id);
            holder.mId = convertView.findViewById(R.id.list_id);
            holder.mDeparture = convertView.findViewById(R.id.list_departure);
            holder.mArrival = convertView.findViewById(R.id.list_arrival);
            holder.mTime = convertView.findViewById(R.id.list_time);
            holder.mPrice = convertView.findViewById(R.id.list_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Flight flight = flightList.get(position);
        String hiddenId = itemIds.get(position);
        int seatCount = seatCounts.get(position);
        holder.mHiddenId.setText(hiddenId);
        holder.mId.setText(flight.getId());
        holder.mDeparture.setText(flight.getDeparture());
        holder.mArrival.setText(flight.getArrival());
        holder.mTime.setText(flight.getTime());
        holder.mPrice.setText(NumberFormat.getCurrencyInstance().format(flight.getPrice()*seatCount));
        return convertView; 
    }
}
