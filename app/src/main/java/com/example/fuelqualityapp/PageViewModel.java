package com.example.fuelqualityapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class PageViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Refuel>> mRefuels = new MutableLiveData<>();

    public void addRefuel(ArrayList<Refuel> arrayList){
        mRefuels.setValue(arrayList);
    }

    public LiveData<ArrayList<Refuel>> getRefuel(){
        return mRefuels;
    }
}
