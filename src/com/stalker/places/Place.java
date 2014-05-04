package com.stalker.places;

import java.io.Serializable;

import com.google.api.client.util.Key;
import com.stalker.DBHelper.Todo;

public class Place {
	@Key
    public String id;
     
    @Key
    public String name;
     
    @Key
    public String reference;
     
    @Key
    public String icon;
     
    @Key
    public String vicinity;
     
    @Key
    public Geometry geometry;
     
    @Key
    public String formatted_address;
     
    @Key
    public String formatted_phone_number;
    
    public Todo associatedTODO;
 
    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }
     
    public static class Geometry implements Serializable
    {
        @Key
        public Location location;
    }
     
    public static class Location implements Serializable
    {
        @Key
        public double lat;
         
        @Key
        public double lng;
    }
}
