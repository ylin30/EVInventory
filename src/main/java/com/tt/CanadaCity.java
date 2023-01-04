package com.tt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanadaCity extends City {
    public static int CANADA_CITY_RANGE = 100;

    public static Map<String, City> cityMap = new HashMap<>(60);

    static {
        cityMap.put("Vancouver", new CanadaCity("BC", "Vancouver", "V5R 2H2", -123.116226, 49.246292));
        cityMap.put("Victoria", new CanadaCity("BC", "Victoria", "V8N 0A1", -123.329773, 48.407326));
        cityMap.put("Kelowna", new CanadaCity("BC", "Kelowna", "V1P 0A1", -119.477829, 49.882114));
        cityMap.put("Calgary", new CanadaCity("AB", "Calgary", "T3B 4X3", -114.066666, 51.049999));
        cityMap.put("Edmonton", new CanadaCity("AB", "Edmonton", "T6C 2K5", -113.323975, 53.631611));
        cityMap.put("Quebec City", new CanadaCity("QC", "Quebec City", "G2M 1C6", -71.204, 46.813));
        cityMap.put("Montreal", new CanadaCity("QC", "Montreal", "H1W 3V8", -73.561668, 45.508888));
        cityMap.put("Hamilton", new CanadaCity("ON", "Hamilton", "L8L 8C5", -79.843826, 43.255203));
        cityMap.put("Toronto", new CanadaCity("ON", "Toronto", "M3C 0C1", -79.347015, 43.651070));
        cityMap.put("Ottawa", new CanadaCity("ON", "Ottawa", "K1A 0A1", -75.695000, 45.424721));
        cityMap.put("Windsor", new CanadaCity("ON", "Windsor", "N8N 0A1", -83.026772, 42.317432));
        cityMap.put("London", new CanadaCity("ON", "London", "N6A 3S9", -81.249725, 42.983612));
        cityMap.put("Mississauga", new CanadaCity("ON", "Mississauga", "L5N 4Y2", -79.640579, 43.595310));
        cityMap.put("Yellowknife", new CanadaCity("NT", "Yellowknife", "X1A 0A1", -114.371788, 62.453972));
        cityMap.put("Winnipeg", new CanadaCity("MB", "Winnipeg", "R2M 0T7", -97.138451, 49.895077));
        cityMap.put("Churchill", new CanadaCity("MB", "Churchill", "R0B 0B6", -94.164963, 58.768410));
        cityMap.put("Moncton", new CanadaCity("NB", "Moncton", "E1A 0A3", -64.790497, 46.090946));
        cityMap.put("St. John's", new CanadaCity("NL", "St. John's", "A1B 0G2", 47.560539, -52.712830));
        cityMap.put("Halifax", new CanadaCity("NS", "Halifax", "B3H 1A1", 44.651070, -63.582687));
        cityMap.put("Regina", new CanadaCity("SK", "Regina", "S4M 0A1", -104.618896, 50.445210));
        cityMap.put("Saskatoon", new CanadaCity("SK", "Saskatoon", "S7H 0A9", 52.146973, -106.647034));
        cityMap.put("Whitehorse", new CanadaCity("YT", "Whitehorse", "Y1A 0A2", -135.056839, 60.721188));
        cityMap.put("Charlottetown", new CanadaCity("PI", "Charlottetown", "C1E 2A5", -63.157, 46.268));
    }

    private String region;

    public CanadaCity(String province, String city, String zip, double lng, double lat) {
        super(province, city, zip, lng, lat);
        this.region = province;
    }

    @Override
    public int queryWithRetries(TeslaInventoryGrepper grepper, List<TeslaCar> cars, String model, String condition, String market) {
        return grepper.queryWithRetries(cars, model, condition, market, this.zip, this.lng, this.lat, this.region, CANADA_CITY_RANGE, null /* must be null for USA city */);
    }

    @Override
    public String toString() {
        return "CanadaCity{" +
            "region='" + region + '\'' +
            ", province='" + province + '\'' +
            ", city='" + city + '\'' +
            ", lng=" + lng +
            ", lat=" + lat +
            ", zip='" + zip + '\'' +
            '}';
    }
}
