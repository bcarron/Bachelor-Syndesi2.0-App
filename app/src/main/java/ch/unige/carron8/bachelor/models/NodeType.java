package ch.unige.carron8.bachelor.models;

import ch.unige.carron8.bachelor.R;

/**
 * Created by Blaise on 31.05.2015.
 */
public enum NodeType {
    bulb{
        @Override
        public int getIcon(String status){
            if(status.equals("on")){
                return R.drawable.node_bulb_on;
            }else{
                return R.drawable.node_bulb_off;
            }
        }
    },
    light{
        @Override
        public int getIcon(String status){
            if(status.equals("on")){
                return R.drawable.node_light_on;
            }else{
                return R.drawable.node_light_off;
            }
        }
    },
    curtain{
        @Override
        public int getIcon(String status){
            if(status.equals("up")){
                return R.drawable.node_curtain_on;
            }else{
                return R.drawable.node_curtain_off;
            }
        }
        public String getStatus(String status){
            if(status.equals("up")){
                return "up";
            }else{
                return "down";
            }
        }
        public String getToggleStatus(String status){
            if(status.equals("up")){
                return "down";
            }else{
                return "up";
            }
        }
    },
    coffee{
        @Override
        public int getIcon(String status){
            if(status.equals("up")){
                return R.drawable.node_coffee_on;
            }else{
                return R.drawable.node_coffee_off;
            }
        }
    },
    alarm{
        @Override
        public int getIcon(String status){
            if(status.equals("up")){
                return R.drawable.node_alarm_on;
            }else{
                return R.drawable.node_alarm_off;
            }
        }
    },
    door{
        @Override
        public int getIcon(String status){
            if(status.equals("up")){
                return R.drawable.node_door_on;
            }else{
                return R.drawable.node_door_off;
            }
        }
    },
    heater{
        @Override
        public int getIcon(String status){
            if(status.equals("on")){
                return R.drawable.node_heater_on;
            }else{
                return R.drawable.node_heater_off;
            }
        }
    },
    fan{
        @Override
        public int getIcon(String status){
            if(status.equals("on")){
                return R.drawable.node_fan_on;
            }else{
                return R.drawable.node_fan_off;
            }
        }
    },
    generic;

    public static NodeType getType(String device){
        if (device.contains("bulb")) {
            return NodeType.bulb;
        } else if (device.contains("curtain")) {
            return NodeType.curtain;
        } else if (device.contains("light")) {
            return NodeType.light;
        } else if (device.contains("coffee")) {
            return NodeType.coffee;
        } else if (device.contains("alarm")) {
            return NodeType.alarm;
        }else if (device.contains("door")) {
            return NodeType.door;
        }else if (device.contains("fan")) {
            return NodeType.fan;
        }else if (device.contains("heater")) {
            return NodeType.heater;
        }else{
            return NodeType.generic;
        }
    }

    public static String parseResponse(String response){
        if(response.contains("ON")) {
            return "on";
        }else if(response.contains("OFF")){
            return "off";
        }else if(response.contains("UP")){
            return "up";
        }else if(response.contains("DOWN")){
            return "down";
        }else if(response.contains("li")){
            return "on";
        }else{
            return "off";
        }
    }

    //Adapt default on/off status to the required status from telosb
    public String getStatus(String status){
        if(status.equals("on")){
            return "on";
        }else{
            return "off";
        }
    }

    public String getToggleStatus(String status){
        if(status.equals("on")){
            return "off";
        }else{
            return "on";
        }
    }

    public int getIcon(String status){
        if(status.equals("on")){
            return R.drawable.node_on;
        }else{
            return R.drawable.node_off;
        }
    }
}
