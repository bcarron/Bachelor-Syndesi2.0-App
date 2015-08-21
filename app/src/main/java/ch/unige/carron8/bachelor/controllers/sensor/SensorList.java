package ch.unige.carron8.bachelor.controllers.sensor;

import android.hardware.Sensor;

import java.util.ArrayList;

import ch.unige.carron8.bachelor.R;

/**
 * Created by Blaise on 27.05.2015.
 */
public class SensorList {
    public static Integer[] sensorUsed = new Integer[]
            {Sensor.TYPE_LIGHT, Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_PRESSURE, Sensor.TYPE_RELATIVE_HUMIDITY, Sensor.TYPE_PROXIMITY};

    public static String getStringType(int sensorType){
        String stringType;
        switch (sensorType){
            case Sensor.TYPE_LIGHT: stringType = "LIGHT"; break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE: stringType = "TEMPERATURE"; break;
            case Sensor.TYPE_PRESSURE: stringType = "PRESSURE"; break;
            case Sensor.TYPE_RELATIVE_HUMIDITY: stringType = "HUMIDITY"; break;
            case Sensor.TYPE_PROXIMITY: stringType = "PROXIMITY"; break;
            default: stringType = "UNDEFINED"; break;
        }
        return stringType;
    }

    public static String getStringUnit(int sensorType){
        String stringType;
        switch (sensorType){
            case Sensor.TYPE_LIGHT: stringType = "lx"; break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE: stringType = "°C"; break;
            case Sensor.TYPE_PRESSURE: stringType = "hPa"; break;
            case Sensor.TYPE_RELATIVE_HUMIDITY: stringType = "%"; break;
            case Sensor.TYPE_PROXIMITY: stringType = "cm"; break;
            default: stringType = "Undefined"; break;
        }
        return stringType;
    }

    public static int getIcon(int sensorType){
        int icon;
        switch (sensorType){
            case Sensor.TYPE_LIGHT: icon = R.drawable.sensor_light; break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE: icon = R.drawable.sensor_temperature; break;
            case Sensor.TYPE_PRESSURE: icon = R.drawable.sensor_pressure; break;
            case Sensor.TYPE_RELATIVE_HUMIDITY: icon = R.drawable.sensor_humidity; break;
            case Sensor.TYPE_PROXIMITY: icon = R.drawable.sensor_proximity; break;
            default: icon = R.drawable.sensor; break;
        }
        return icon;
    }
}
