package com.example.buddha.mess_management;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BUDDHA on 2/16/2017.
 */

public class ItemAdapter extends ArrayAdapter {

    List list=new ArrayList();
    public ItemAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(Item object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row;
        row=convertView;
        ItemHolder itemHolder;
        if(row == null)
        {
            LayoutInflater layoutInflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflator.inflate(R.layout.raw_layout, parent, false);
            itemHolder = new ItemHolder();
            itemHolder.tx_address = (TextView) row.findViewById(R.id.text_address);
            itemHolder.tx_rent = (TextView) row.findViewById(R.id.text_rent);
            itemHolder.tx_numberofseat = (TextView) row.findViewById(R.id.text_numberofseat);
           // itemHolder.tx_contractnumber = (TextView) row.findViewById(R.id.text_contractnumber);
           // itemHolder.tx_description = (TextView) row.findViewById(R.id.text_description);
            row.setTag(itemHolder);
        }
        else
        {
            itemHolder=(ItemHolder)row.getTag();
        }

        Item item=(Item)this.getItem(position);
        itemHolder.tx_address.setText("Address "+item.getAddress());
        itemHolder.tx_rent.setText("Rent :"+item.getRent());
        itemHolder.tx_numberofseat.setText("Number of available seat :"+item.getNumberofseat());
    //    itemHolder.tx_contractnumber.setText("Contract number :"+item.getContractnumber());
    //    itemHolder.tx_description.setText("Description :"+item.getDescription());
        return row;
    }


    static class ItemHolder
    {
        TextView tx_address,tx_rent,tx_numberofseat,tx_contractnumber,tx_description;
    }
}
