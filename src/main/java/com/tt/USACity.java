package com.tt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class USACity extends City {
    private String region;
    public USACity(String region, String city, String zip, double lng, double lat) {
        super(region, city, zip, lng, lat);
        this.region = region;
    }

    public static Map<String, City> cityMap = new HashMap<>(60);
    static {
        cityMap.put("Seattle", new USACity("WA", "Seattle", "98101", -122.336407,47.6084921));
        cityMap.put("Montgomery", new USACity("AL", "Montgomery", "36104", -86.3224638,32.3989318));
        cityMap.put("Juneau", new USACity("AK", "Juneau", "99801", -134.4202956,58.3018264));
        cityMap.put("Phoenix", new USACity("AZ", "Phoenix", "85001", -112.0702225,33.4499672));
        cityMap.put("LittleRock", new USACity("AR", "LittleRock", "72201", -92.28520139999999,34.7499657));
        cityMap.put("Sacramento", new USACity("CA", "Sacramento", "95814", -121.4941738, 38.5777528));
        cityMap.put("SanFrancisco", new USACity("CA", "SanFrancisco", "94102", -122.4212424,37.7786871));
        cityMap.put("Fresno", new USACity("CA", "Fresno", "93701", -119.7802409,36.7509383));
        cityMap.put("LosAngeles", new USACity("CA", "LosAngeles", "90001", -118.2468148, 33.9697897));
        cityMap.put("SanDiego", new USACity("CA", "SanDiego", "92039", -117.2727893, 32.8470987));
        cityMap.put("Denver", new USACity("CO", "Denver", "80202", -105.0002242, 39.7541032));
        cityMap.put("Hartford", new USACity("CT", "Hartford", "06103", -72.6757236, 41.7665502));
        cityMap.put("Dover", new USACity("DE", "Dover", "19901", -75.45777749999999, 39.2081349));
        cityMap.put("Jacksonville", new USACity("FL", "Jacksonville", "32201", -81.6541918,30.3431292));
        cityMap.put("Miami", new USACity("FL", "Miami", "33101", -80.1990136, 25.7783254));
        cityMap.put("Orlando", new USACity("FL", "Orlando", "32701", -81.3729372, 28.6659654));
        cityMap.put("FortMyers", new USACity("FL", "FortMyers", "33901", -81.87706709999999,26.6235485));
        cityMap.put("Atlanta", new USACity("GA", "Atlanta", "30303", -84.3883717, 33.755711));
        cityMap.put("Honolulu", new USACity("HI", "Honolulu", "96813", -157.8480364, 21.3136151));
        cityMap.put("Boise", new USACity("ID", "Boise", "83702", -116.1630431, 43.6624385));
        cityMap.put("Springfield", new USACity("IL", "Springfield", "62701", -89.6498742, 39.7994917));
        cityMap.put("Indianapolis", new USACity("IN", "Indianapolis", "46225", -86.1590444, 39.7372606));
        cityMap.put("DesMoines", new USACity("IA", "DesMoines", "50309", -93.6243133, 41.5816456));
        cityMap.put("Topeka", new USACity("KS", "Topeka", "66603", -95.6747209, 39.0555831));
        cityMap.put("Frankfort", new USACity("KY", "Frankfort", "40601", -84.8984775, 38.2481018));
        cityMap.put("BatonRouge", new USACity("LA", "BatonRouge", "70802", -91.17850159999999, 30.4362298));
        cityMap.put("Augusta", new USACity("ME", "Augusta", "04330", -69.692376, 44.3520015));
        cityMap.put("Annapolis", new USACity("MD", "Annapolis", "21401", -76.4919706, 38.979434));
        cityMap.put("WashingtonDC", new USACity("DC", "WashingtonDC", "20001", -77.0190228,38.912068));
        cityMap.put("Boston", new USACity("MA", "Boston", "02201", -71.0585053, 42.358158));
        cityMap.put("Lansing", new USACity("MI", "Lansing", "48933", -84.55628779999999, 42.7371075));
        cityMap.put("St.Paul", new USACity("MN", "St.Paul", "55102", -93.1127413, 44.9343827));
        cityMap.put("Jackson", new USACity("MS", "Jackson", "39205", -90.1529468, 32.3503996));
        cityMap.put("JeffersonCity", new USACity("MO", "JeffersonCity", "65101", -92.1646722, 38.528107));
        cityMap.put("Helena", new USACity("MT", "Helena", "59623", -112.0408432, 46.5899699));
        cityMap.put("Lincoln", new USACity("NE", "Lincoln", "68502", -96.7012424, 40.7895543));
        cityMap.put("Las Vegas", new USACity("NV", "Las Vegas", "88901", -115.14, 36.18));
        cityMap.put("Concord", new USACity("NH", "Concord", "03301", -71.5486562, 43.2086343));
        cityMap.put("Trenton", new USACity("NJ", "Trenton", "08608", -74.76735459999999, 40.2187428));
        cityMap.put("SantaFe", new USACity("NM", "SantaFe", "87501", -105.8407722, 35.7135437));
        cityMap.put("Albany", new USACity("NY", "Albany", "12207", -73.7463147, 42.6568958));
        cityMap.put("Syracuse", new USACity("NY", "Syracuse", "13201", -76.15272759999999,43.0452574));
        cityMap.put("Buffalo", new USACity("NY", "Buffalo", "14201", -78.8863847,42.89676739999999));
        cityMap.put("NewYork", new USACity("NY", "NewYork", "10001", -73.9991637, 40.75368539999999));
        cityMap.put("Raleigh", new USACity("NC", "Raleigh", "27601", -78.6313624, 35.7744301));
        cityMap.put("Charlotte", new USACity("NC", "Charlotte", "28201", -80.9247346,35.2294139));
        cityMap.put("Bismarck", new USACity("ND", "Bismarck", "58501", -100.659753, 46.8243438));
        cityMap.put("Columbus", new USACity("OH", "Columbus", "43215", -83.0092803, 39.9602601));
        cityMap.put("OklahomaCity", new USACity("OK", "OklahomaCity", "73102", -97.51660199999999, 35.4711808));
        cityMap.put("Portland", new USACity("OR", "Portland", "97201", -122.6882145, 45.505603));
        cityMap.put("Harrisburg", new USACity("PA", "Harrisburg", "17101", -76.88650849999999, 40.2584515));
        cityMap.put("Pittsburgh", new USACity("PA", "Pittsburgh", "15201", -79.9612368,40.4737114));
        cityMap.put("Philadelphia", new USACity("PA", "Philadelphia", "19101", -75.1625181,39.9522665));
        cityMap.put("Providence", new USACity("RI", "Providence", "02903", -71.40915629999999, 41.816736));
        cityMap.put("Columbia", new USACity("SC", "Columbia", "29217", -81.03578569999999,34.0090267));
        cityMap.put("Pierre", new USACity("SD", "Pierre", "57501", -100.3510108,44.3683222));
        cityMap.put("Nashville", new USACity("TN", "Nashville", "37219", -86.7831085,36.1671314));
        cityMap.put("Austin", new USACity("TX", "Austin", "78701", -97.74438630000002, 30.2729209));
        cityMap.put("Houston", new USACity("TX", "Houston", "77001", -95.36999999999999,29.77));
        cityMap.put("SanAntonio", new USACity("TX", "SanAntonio", "78201", -98.5226706,29.46357));
        cityMap.put("Dallas", new USACity("TX", "Dallas", "78201", -96.8297403,32.9654778));
        cityMap.put("SaltLakeCity", new USACity("UT", "SaltLakeCity", "84111", -111.8836874,40.7569389));
        cityMap.put("Montpelier", new USACity("VT", "Montpelier", "05602", -72.61950759999999,44.28092729999999));
        cityMap.put("Richmond", new USACity("VA", "Richmond", "23219", -77.43864289999999,37.5410261));
        cityMap.put("Charleston", new USACity("WV", "Charleston", "25301", -81.63364740000002, 38.3517112));
        cityMap.put("Madison", new USACity("WI", "Madison", "53703", -89.3724769,43.0833196));
        cityMap.put("Cheyenne", new USACity("WY", "Cheyenne", "82001", -104.8201078,41.1400096));
    }

    @Override
    public int queryWithRetries(TeslaInventoryGrepper grepper, List<TeslaCar> cars, String model, String condition, String market) {
        return grepper.queryWithRetries(cars, model, condition, market, this.zip, this.lng, this.lat, this.region, City.RANGE, null /* must be null for USA city */);
    }

    @Override
    public String toString() {
        return "USACity{" +
            "region='" + region + '\'' +
            ", city='" + city + '\'' +
            ", lng=" + lng +
            ", lat=" + lat +
            ", zip='" + zip + '\'' +
            '}';
    }
}
