package com.example.fuelqualityapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PageViewModel pageViewModel;
    private ArrayList<Refuel> arrayList;
    private View v;
    private Button showButton;
    private GraphView graph;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public tab3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab3.
     */
    // TODO: Rename and change types and number of parameters
    public static tab3 newInstance(String param1, String param2) {
        tab3 fragment = new tab3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        pageViewModel = ViewModelProviders.of(requireActivity()).get(PageViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tab3, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageViewModel.getRefuel().observe(requireActivity(), new Observer<ArrayList<Refuel>>() {
            @Override
            public void onChanged(ArrayList<Refuel> refuels) {
                arrayList = refuels;
            }
        });
        graph = (GraphView) v.findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);
        showButton = v.findViewById(R.id.btnShow);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(),v);
                popupMenu.getMenuInflater().inflate(R.menu.graph_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String itemSt = item.getTitle().toString();
                        if(arrayList.size() > 1){
                            graph.setVisibility(View.VISIBLE);
                            displayGraph(itemSt);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.notEnoughInputData), 0).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }



    public void displayGraph(String itemSt){
        float average_fuel_consumption = 0;
        int number_of_km = 0;
        Toast.makeText(getContext(), itemSt, 0).show();
        graph.getViewport().setMaxY(30);
        graph.getViewport().setYAxisBoundsManual(true);
        if(arrayList.size() == 2){
            number_of_km = arrayList.get(arrayList.size() - 1).getTachometer() - arrayList.get(0).getTachometer();
            average_fuel_consumption = getAverageFuelConsumption(2, number_of_km);
            graph.getViewport().setMaxX(1);
            graph.getViewport().setXAxisBoundsManual(true);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(0, average_fuel_consumption),
                    new DataPoint(1, average_fuel_consumption)
            });
            graph.addSeries(series);
        } else if (arrayList.size() > 2) {
            float[] values = new float[arrayList.size() - 1];
            number_of_km = 0;
            for (int i = 0; i < arrayList.size() - 1; i++) {
                number_of_km = arrayList.get(i+1).getTachometer() - arrayList.get(i).getTachometer();
                values[i] = getAverageFuelConsumption(arrayList.get(i).getQuantity(), number_of_km);
                Log.d("Hodnota", Float.toString(values[i]));
            }
            if(itemSt.equals("Last 5")){
                if(arrayList.size() > 5){
                    DataPoint[] array = new DataPoint[5];
                    int index = 0;
                    for (int i = values.length - 5; i <= values.length - 1 ; i++) {
                        array[index] = new DataPoint(i+2, values[i]);
                        index++;
                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(array);
                    graph.addSeries(series);
                } else if(arrayList.size() <= 5){
                    DataPoint[] array = new DataPoint[arrayList.size() - 1];
                    for (int i = 0; i < values.length; i++) {
                        array[i] = new DataPoint(i+1, values[i]);
                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(array);
                    graph.addSeries(series);
                    graph.getViewport().setMaxX(arrayList.size() - 1);
                    graph.getViewport().setXAxisBoundsManual(true);
                }

            } else if(itemSt.equals("Last 10")){
                if(arrayList.size() > 10){
                    DataPoint[] array = new DataPoint[5];
                    int index = 0;
                    for (int i = values.length - 10; i <= values.length ; i++) {
                        array[index] = new DataPoint(i+2, values[i]);
                        index++;
                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(array);
                    graph.addSeries(series);
                } else if(arrayList.size() <= 10){
                    DataPoint[] array = new DataPoint[arrayList.size() - 1];
                    for (int i = 0; i < values.length; i++) {
                        array[i] = new DataPoint(i+1, values[i]);
                    }
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(array);
                    graph.addSeries(series);
                    graph.getViewport().setMaxX(arrayList.size() - 1);
                    graph.getViewport().setXAxisBoundsManual(true);
                }


            }
        }
    }

    public float getAverageFuelConsumption(float quantity, int number_of_km){ ;
        return (quantity/ (float) number_of_km) * 100;
    }
}