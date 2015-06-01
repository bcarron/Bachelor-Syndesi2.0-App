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
            return R.drawable.sensor_light;
        }
    },
    curtain{
        @Override
        public int getIcon(String status){
            if(status.equals("on")){
                return R.drawable.node_curtain_on;
            }else{
                return R.drawable.node_curtain_off;
            }
        }
    };

    public int getIcon(String status){
        return 0;
    }
}
