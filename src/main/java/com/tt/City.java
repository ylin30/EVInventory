package com.tt;

import java.util.List;

public class City {
    // Used in inventory query to specify range of query.
    public static int RANGE = 50;

    String province;
    String city;
    double lng;
    double lat;
    String zip;

    public City(String province, String city, String zip, double lng, double lat) {
        this.province = province;
        this.city = city;
        this.lng = lng;
        this.lat = lat;
        this.zip = zip;
    }

    public int queryWithRetries(TeslaInventoryGrepper grepper, List<TeslaCar> cars, String model, String condition, String market) {
        return grepper.queryWithRetries(cars, model, condition, market, this.zip, this.lng, this.lat, this.province, City.RANGE, this.province);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "City{" +
            "province='" + province + '\'' +
            ", city='" + city + '\'' +
            ", lng=" + lng +
            ", lat=" + lat +
            ", zip='" + zip + '\'' +
            '}';
    }
}
