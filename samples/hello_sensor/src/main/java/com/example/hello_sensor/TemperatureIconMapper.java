package com.example.hello_sensor;

import androidx.annotation.DrawableRes;

public class TemperatureIconMapper {

    private TemperatureIconMapper(){
        // no instantiation
    }

    // Example thresholds (°C)
    //  < 10  : chilly
    //  10-22 : comfy
    //  22-30 : warm
    //  30-38 : hot
    //  >=38  : blazing!
    @DrawableRes
    public static int iconFor(float tempC) {
        if (tempC < 10f) {
            return R.drawable.cold;
        } else if (tempC < 22f) {
            return R.drawable.comfy;
        } else if (tempC < 30f) {
            return R.drawable.warm;
        } else if (tempC < 38f) {
            return R.drawable.hot;
        } else {
            return R.drawable.burning;
        }
    }
}
