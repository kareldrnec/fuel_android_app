package com.example.fuelqualityapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tab2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tab2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView textViewValue;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private PageViewModel pageViewModel;
    private ArrayList<Refuel> arrayList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;

    public tab2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tab2.
     */
    // TODO: Rename and change types and number of parameters
    public static tab2 newInstance(String param1, String param2) {
        tab2 fragment = new tab2();
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



        v = inflater.inflate(R.layout.fragment_tab2, container, false);


      //  SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
       // float value = sharedPreferences.getFloat("value",0);
       // textViewValue = v.findViewById(R.id.averageFuelConsumptionValueTV);
       // textViewValue.setText(df.format(value) + " l / 100km");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pageViewModel.getRefuel().observe(requireActivity(), new Observer<ArrayList<Refuel>>() {
            @Override
            public void onChanged(ArrayList<Refuel> refuels) {
                textViewValue = v.findViewById(R.id.averageFuelConsumptionValueTV);
                arrayList = refuels;
                Hashtable<String, Integer> my_dict = new Hashtable<String, Integer>();
                if(arrayList.size() >= 2){
                    float total_quantity = 0;
                    float average_consumption = 0;
                    int number_of_km = arrayList.get(arrayList.size() - 1).getTachometer() - arrayList.get(0).getTachometer();
                    for (int i = 0; i < arrayList.size() - 1; i++) {
                        total_quantity = total_quantity + arrayList.get(i).getQuantity();

                    }
                    average_consumption = (total_quantity / (float) number_of_km) * 100;
                    textViewValue.setTextSize(35);
                    textViewValue.setText(Float.toString(average_consumption));

                } else {
                    textViewValue.setTextSize(15);
                    textViewValue.setText(getString(R.string.notEnoughInputData));
                }

            }
        });

    }
}