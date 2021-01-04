package com.example.fuelqualityapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterRefuel extends ArrayAdapter<Refuel> {

    private Context mContext;
    private List<Refuel> refuelList = new ArrayList<>();


    public AdapterRefuel(@NonNull Context context,
                         @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Refuel> list) {
        super(context, 0, list);
        mContext = context;
        refuelList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        Refuel currentRefuel = refuelList.get(position);

        ImageView imageView = (ImageView)listItem.findViewById(R.id.iconItem);
        imageView.setBackgroundResource(R.drawable.ic_baseline_local_gas_station_40);

        TextView gasStation = (TextView)listItem.findViewById(R.id.liGasStation);
        gasStation.setText(currentRefuel.getGasStation());
        TextView quantity = (TextView)listItem.findViewById(R.id.liQuantity);
        quantity.setText("Quantity: " + String.valueOf(currentRefuel.getQuantity()));
        TextView tachometer = (TextView)listItem.findViewById(R.id.liTachometer);
        tachometer.setText("Tachometer: " + String.valueOf(currentRefuel.getTachometer()));
        TextView date = (TextView)listItem.findViewById(R.id.liDate);
        String oldDate = currentRefuel.getDate();
        String[] part = oldDate.split("/");
        String newDate = part[1] + "/" + part[0] + "/" + part[2];
        date.setText("Date: " + newDate);

        return listItem;
    }

    public View getViewByPosition(int pos, ListView listView){
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
