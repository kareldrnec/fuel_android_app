package com.example.fuelqualityapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Refuel {

    private String key;
    private String gasStation;
    private int tachometer;
    private float quantity;
    private String date;

    public Refuel(){
        //default constructor
    }

    public Refuel(String _key, String _gasStation, int _tachometer, float _quantity, String _date){

        this.key = _key;
        this.gasStation = _gasStation;
        this.tachometer = _tachometer;
        this.quantity = _quantity;
        this.date = _date;

    }

    public void setKey(String _key){
        this.key = _key;
    }

    public void setGasStation(String _gasStation){
        this.gasStation = _gasStation;
    }

    public void setTachometer(int _tachometer){
        this.tachometer = _tachometer;
    }

    public void setQuantity(float _quantity){
        this.quantity = _quantity;
    }

    public void setDate(String _date) {this.date = _date;}

    public String getKey(){
        return this.key;
    }

    public String getGasStation(){
        return this.gasStation;
    }

    public int getTachometer(){
        return this.tachometer;
    }

    public float getQuantity(){
        return this.quantity;
    }

    public String getDate(){ return  this.date;}

    public String toString(){
        return this.getGasStation();
    }

}
