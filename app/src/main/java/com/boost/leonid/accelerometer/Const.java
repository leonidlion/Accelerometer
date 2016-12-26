package com.boost.leonid.accelerometer;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Const {
    public static final String CHILD_USERS  = "users";
    public static final String CHILD_DATA   = "data_coordinates";

    public static final String PUT_MAP_TIME = "time";
    public static final String PUT_MAP_X    = "x";
    public static final String PUT_MAP_Y    = "y";
    public static final String PUT_MAP_Z    = "z";

    public static final String PUT_MAP_START_DATE       = "startDate";
    public static final String PUT_MAP_START_TIME       = "startTime";
    public static final String PUT_MAP_INTERVAL         = "interval";
    public static final String PUT_MAP_DEVICE_MODEL     = "deviceModel";
    public static final String PUT_MAP_COORDINATES      = "coordinates";

    public static DatabaseReference getUserReference(){
        return FirebaseDatabase.getInstance().getReference().child(CHILD_USERS);
    }

    public static DatabaseReference getDataCoordinatesOfUserReference(String userId){
        return FirebaseDatabase.getInstance().getReference().child(CHILD_DATA).child(userId);
    }

    public static String getPathToDataForMapInsert(String userId, String sessionId){
        return "/" + CHILD_DATA + "/" + userId + "/" + sessionId + "/";
    }
}
