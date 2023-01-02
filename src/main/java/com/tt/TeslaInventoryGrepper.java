package com.tt;

import okhttp3.HttpUrl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;

public class TeslaInventoryGrepper {
    private static String url="https://www.tesla.com/inventory/api/v1/inventory-results";

    public static String TOTAL_NUMBER_METRIC = "car_number";
    public static String PRICE_USD_METRIC = "price_USD";
    public static String PRICE_LOCAL_CURRENCY_METRIC = "price_";

    public static String[] models = {"m3", "my", "mx", "ms"};
    public static String[] conditions = {"new", "used"};

    public static Map<String, String> EUCountries = new HashMap<>(24);
    static {
        EUCountries.put("BE", "Belgium");
        EUCountries.put("CZ", "Czech");
        EUCountries.put("DE", "Germany");
        EUCountries.put("DK", "Denmark");
        EUCountries.put("GR", "Greek");
        EUCountries.put("ES", "Spain");
        EUCountries.put("FR", "France");
        EUCountries.put("HR", "Croatia");
        EUCountries.put("HU", "Hungary");
        EUCountries.put("IE", "Ireland");
        EUCountries.put("IS", "Iceland");
        EUCountries.put("IT", "Italy");
        EUCountries.put("LU", "Luxembourg");
        EUCountries.put("NL", "Netherlands");
        EUCountries.put("NO", "Norway");
        EUCountries.put("AT", "Austria");
        EUCountries.put("PL", "Poland");
        EUCountries.put("PT", "Portugal");
        EUCountries.put("RO", "Romania");
        EUCountries.put("SI", "Slovenia");
        EUCountries.put("SE", "Sweden");
        EUCountries.put("CH", "Switzerland");
        EUCountries.put("FI", "Finland");
        EUCountries.put("GB", "England");
    }

    public static Map<String, String> countriesExcludingEU = new HashMap<>(20);
    static {
        countriesExcludingEU.put("US", "USA");
    }

    private Set<String> carVINs;
    public TeslaInventoryGrepper() {
        this.carVINs = new HashSet<>();
    }

    private static String language(String country) {
        if (country.equalsIgnoreCase("FR") ||
            country.equalsIgnoreCase("BE") ||
            country.equalsIgnoreCase("LU") ||
            country.equalsIgnoreCase("CH")) { // France, Belgium, Luxembourg, Switzerland
            return "fr";
        } else if (country.equalsIgnoreCase("FI")) { // Finland
            return "fi";
        } else if (country.equalsIgnoreCase("SE")) { // Sweden
            return "sv";
        } else if (country.equalsIgnoreCase("SI")) { // Slovenia
            return "sl";
        } else if (country.equalsIgnoreCase("RO")) { // Romania
            return "ro";
        } else if (country.equalsIgnoreCase("PT")) { // Portugal
            return "pt";
        } else if (country.equalsIgnoreCase("PL")) { // Poland
            return "pl";
        } else if (country.equalsIgnoreCase("NO")) { // Norway
            return "no";
        } else if (country.equalsIgnoreCase("NL")) { // Netherlands
            return "nl";
        } else if (country.equalsIgnoreCase("IT")) { // Italy
            return "it";
        } else if (country.equalsIgnoreCase("IS")) { // Iceland
            return "is";
        } else if (country.equalsIgnoreCase("HU")) { // Hungary
            return "hu";
        } else if (country.equalsIgnoreCase("HR")) { // Croatia
            return "hr";
        } else if (country.equalsIgnoreCase("ES")) { // Spain
            return "es";
        } else if (country.equalsIgnoreCase("GR")) { // GREEK
            return "el";
        } else if (country.equalsIgnoreCase("DE") ||
            country.equalsIgnoreCase("AT")) { // Germany, Austria
            return "de";
        } else if (country.equalsIgnoreCase("DK")) { // Denmark
            return "da";
        } else if (country.equalsIgnoreCase("CZ")) { // Czech Republic
            return "cs";
        } else if (country.equalsIgnoreCase("CA") ||
            country.equalsIgnoreCase("IE") ||
            country.equalsIgnoreCase("GB") ||
            country.equalsIgnoreCase("US")) { // Canada, Ireland, England
            return "en";
        } else {
            return "en";
        }
    }

    // Example: france, model y, inventory of new vehicle
    // {
    // "query":{
    //  "model":"my",
    //  "condition":"new",
    //  "options":{},
    //  "arrangeby":"Price",
    //  "order":"asc",
    //  "market":"FR",
    //  "language":"fr",
    //  "super_region":"north america",
    //  "lng":"",
    //  "lat":"",
    //  "zip":"",
    //  "range":0
    //  },
    // "offset":0,
    // "count":50,
    // "outsideOffset":0,
    // "outsideSearch":false
    // }
    private String buildQueryParams(String model, String condition, String market, String zip, int offset) {
        JSONObject JO = new JSONObject();

        // "query":{...}
        Map queryJO = new LinkedHashMap(11);
        queryJO.put("model", model);
        queryJO.put("condition", condition);
        queryJO.put("arrangeby", "Price");
        queryJO.put("order", "asc");
        queryJO.put("market", market);
        queryJO.put("language", language(market));
        queryJO.put("super_region","north america");
        queryJO.put("lng", "");
        queryJO.put("lat", "");
        queryJO.put("zip", zip);
        queryJO.put("range", 0);
        JO.put("query", queryJO);

        // "offset":0
        JO.put("offset", offset);

        // "count":50
        JO.put("count", 50);

        // "outsideOffset": 0
        JO.put("outsideOffset", 0);

        // "outsideSearch":false
        JO.put("outsideSearch", false);

        return JO.toJSONString();
    }

    /**
     * For US. Must specify lng, lat, region.
     * @param model
     * @param condition
     * @param market
     * @param zip
     * @param lng
     * @param lat
     * @param region
     * @param offset
     * @return
     */
    private String buildQueryParams(String model, String condition, String market, String zip, double lng, double lat, String region, int range, int offset) {
        JSONObject JO = new JSONObject();

        // "query":{...}
        Map queryJO = new LinkedHashMap(11);
        queryJO.put("model", model);
        queryJO.put("condition", condition);
        queryJO.put("arrangeby", "Price");
        queryJO.put("order", "asc");
        queryJO.put("market", market);
        queryJO.put("language", language(market));
        queryJO.put("super_region","north america");
        queryJO.put("lng", lng);
        queryJO.put("lat", lat);
        queryJO.put("zip", zip);
        queryJO.put("range", range);
        queryJO.put("region", region);
        JO.put("query", queryJO);

        // "offset":0
        JO.put("offset", offset);

        // "count":50
        JO.put("count", 50);

        // "outsideOffset": 0
        JO.put("outsideOffset", 0);

        // "outsideSearch":false
        JO.put("outsideSearch", false);

        return JO.toJSONString();
    }

    /**
     * Build full url with parameters.
     * There is only parameter, "query". Its value is a Json string.
     * @param model
     * @param condition
     * @param market
     * @param offset
     * @return
     */
    public HttpClient.Pair queryByGet(String model, String condition, String market, int offset) throws HttpClient.HttpException {
        return queryByGet(model, condition, market, "", 0, 0, "", 0, offset);
    }

    public HttpClient.Pair queryByGet(String model, String condition, String market, String zip, double lng, double lat, String region, int range, int offset) throws HttpClient.HttpException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        // Just one parameter query={"query":{...}, offset:0,...,outsideSearch:false}
        String queryJsonStr = buildQueryParams(model, condition, market, zip, lng, lat, region, range, offset);
        urlBuilder.addQueryParameter("query", queryJsonStr);

        String url = urlBuilder.build().toString();

        System.out.println("To query GET Url=\'" + url + "\'");

        return HttpClient.execGet(url);
    }

    /**
     * Query inventory of an EU country. No zip needed.
     * @param cars
     * @param model
     * @param condition
     * @param market
     */
    public int queryOneEUCountry(List<TeslaCar> cars, String model, String condition, String market) {
        return queryOneCountry(cars, model, condition, market);
    }

    /**
     * Query inventory of a country.
     * total matched found may be larger than count (50). So we need to issue another call until all cars are retrieved.
     * @param cars
     * @param model
     * @param condition
     * @param market
=     */
    public int queryOneCountry(List<TeslaCar> cars, String model, String condition, String market) {
        return queryWithRetries(cars, model, condition, market, "", 0, 0, "", 0);
    }

    /**
     * Since each queryByGet only return limit number of rows, we need to issue more queries with new offset if the returned
     * rows is less than total_matches_found.
     *
     * @param cars
     * @param model
     * @param condition
     * @param market
     * @param zip
     * @param lng
     * @param lat
     * @param region
     * @param range
     * @return
     */
    public int queryWithRetries(List<TeslaCar> cars, String model, String condition, String market, String zip, double lng, double lat, String region, int range) {
        int total_matches_found = 0;
        int offset = 0;
        int maxRetries = 5;
        int i = 0;
        do {
            try {
                HttpClient.Pair resp = queryByGet(model, condition, market, zip, lng, lat, region, range, offset);
                total_matches_found = parse(condition, resp.msg, cars);
            } catch (HttpClient.HttpException he) {
                System.out.println(String.format("Fail to query %s, %s, %s, %s, %d", model, condition, market, offset));
                he.printStackTrace();
                try {
                    sleep(10000);
                } catch (InterruptedException ie) {
                    // do nothing
                }
                i++;
            } catch (ParseException e) {
                System.out.println(String.format("Exception in parse result of %s, %s, %s, %d" , model, condition, market, offset));
                e.printStackTrace();
                try {
                    sleep(10000);
                } catch (InterruptedException ie) {
                    // do nothing
                }
                i++;
            }
            offset = cars.size();
        } while (offset < total_matches_found && i < maxRetries);
        return total_matches_found;
    }

    /**
     * Query inventory of an USA city. Zip, region(state), lng and lat must be speicified.
     * @param cars
     * @param model
     * @param condition
     * @param city
     */
    private int queryOneUSACity(List<TeslaCar> cars, String model, String condition, USACity city) {
        return queryWithRetries(cars, model, condition, "US", city.zip, city.lng, city.lat, city.region, USACity.RANGE);
    }

    public static int parse(String condition, String jsonResult, List<TeslaCar> cars) throws ParseException {
        // parsing json string
        Object obj = new JSONParser().parse(jsonResult);

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        // getting total_matches_found
        int total_matches_found = 0;
        try {
            total_matches_found = Integer.parseInt((String) jo.get("total_matches_found"));
        } catch (ClassCastException cce) {
            // In case of the value is 0, not a string e.g., "3".
            // Try again to verify it is 0.
            total_matches_found = ((Long) jo.get("total_matches_found")).intValue();
        }

        if (total_matches_found > 0) {
            JSONArray ja = (JSONArray) jo.get("results");

            // iterating phoneNumbers
            Iterator itr = ja.iterator();
            while (itr.hasNext()) {
                Map tmpStrMap = (Map) itr.next();
                TeslaCar car = new TeslaCar(condition, tmpStrMap);

                // Add to list
                cars.add(car);
            }
        }
        return total_matches_found;
    }

    private static void testParse1() {
        try {
            String js = "{\"results\":{\"exact\":[],\"approximate\":[],\"approximateOutside\":[]},\"total_matches_found\":0}";
            List<TeslaCar> cars = new LinkedList<>();
            parse("new", js, cars);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loop all EU countries.
     * Query all models and all conditions in each country.
     */
    public void grepEUCountries(String XEToken, String writeURL) {
        CurrencyConvector convector = new CurrencyConvector(XEToken);

        long currSec = System.currentTimeMillis() / 1000;
        for (Map.Entry<String, String> entry : EUCountries.entrySet()) {
            String countryCode = entry.getKey();
            String country = entry.getValue();
            for(int i = 0; i < models.length; i++) {
                String model = models[i];
                for(int j = 0; j < conditions.length; j++) {
                    String condition = conditions[j];
                    List<TeslaCar> cars = new LinkedList<>();

                    int total_number = queryOneEUCountry(cars, model, condition, countryCode);

                    for (TeslaCar car : cars) {
                        // convert local price to USD, and set it in the object.
                        try {
                            long priceUSD = convector.toUSD(car.getPrice(), car.getCurrencyCode());
                            car.setPriceUSD(priceUSD);

                            car.setContinent("EU");
                        } catch (Exception e) {
                            System.out.println("Fail to convert price to USD for car " + car);
                            e.printStackTrace();
                        }
                    }

                    System.out.println(String.format("Country:%s(%s), model:%s, condition:%s, total_matches_found:%d", country, countryCode, model, condition, cars.size()));
                    writeTotalNumberToTT(writeURL, currSec, total_number, model, condition, "EU", country, countryCode, null);

                    writeMetricsToTT(cars, writeURL, currSec);
                    try {
                        sleep(5000);
                    } catch(InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Loop all EU countries.
     * Query all models and all conditions in each country.
     */
    public void grepUSACities(String writeURL) {
        // Some cities might be too closed and their results have overlapped.
        // Don't count repeated inventory.
        Set<String> knownVINs = new HashSet<>();

        long currSec = System.currentTimeMillis() / 1000;
        String market = "US";
        for (Map.Entry<String, USACity> entry : USACity.cityMap.entrySet()) {
            String city = entry.getKey();
            USACity cityObj = entry.getValue();
            for(int i = 0; i < models.length; i++) {
                String model = models[i];
                for(int j = 0; j < conditions.length; j++) {
                    String condition = conditions[j];
                    List<TeslaCar> cars = new LinkedList<>();

                    int totalNumber = queryOneUSACity(cars, model, condition, cityObj);
                    System.out.println(String.format("%s(%s), model:%s, condition:%s, total_matches_found:%d, cars:%d", entry.getValue(), market, model, condition, totalNumber, cars.size()));

                    int numKnowns = removeRedundantCars(cars, knownVINs);

                    System.out.println(String.format("After removed redundant, numRemoved:%d, cars:%d", numKnowns, cars.size()));

                    // No matter if zero inventory or not, write total car number to TT.
                    writeTotalNumberToTT(writeURL, currSec, totalNumber - numKnowns, model, condition, "NA", "USA", market, city);

                    if (cars.size() > 0) {
                        for (TeslaCar car : cars) {
                            car.setContinent("NA");
                        }
                        writeMetricsToTT(cars, writeURL, currSec);
                    }
                    try {
                        sleep(1000);
                    } catch(InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }
    }

    private int removeRedundantCars(List<TeslaCar> cars, Set<String> knownVINs) {
        //System.out.println(Arrays.toString(knownVINs.toArray()));
        int numRemoved = 0;
        Iterator<TeslaCar> it = cars.iterator();
        while(it.hasNext()) {
            TeslaCar car = it.next();
            if (knownVINs.contains(car.getVin())) {
                //System.out.println("Car " + car.getVin() + " is in Set. Removed!");
                it.remove();
                numRemoved++;
            } else {
                knownVINs.add(car.getVin());
            }
        }
        return numRemoved;
    }

    /**
     * Generate put requests of metrics (car_number, price_USA, price_[localCurrencyCode]) for each car.
     * Send them to TT in a batch once put requests reach a certain threshold (numDataPoints=10)
     * @param cars
     * @param writeURL
     * @param currSec
     */
    private void writeMetricsToTT(List<TeslaCar> cars, String writeURL, long currSec) {
        int numDataPoints = 10;
        StringBuffer putReqSB = new StringBuffer();
        for(int i=0; i < cars.size(); i++) {

            putReqSB.append(generatePutRequests(cars.get(i), currSec));

            if (i%numDataPoints == 9) {
                System.out.println("To write to " + writeURL + " " + putReqSB.toString());
                try {
                    HttpClient.execPost(writeURL, putReqSB.toString());
                } catch (HttpClient.HttpException e) {
                    System.out.println("Fail to write " + putReqSB.toString());
                    e.printStackTrace();
                }

                putReqSB = new StringBuffer();
            }
        }

        if (putReqSB.length() > 0) {
            System.out.println("To write to " + writeURL + " " + putReqSB.toString());
            try {
                HttpClient.execPost(writeURL, putReqSB.toString());
            } catch (HttpClient.HttpException e) {
                System.out.println("Fail to write " + putReqSB.toString());
                e.printStackTrace();
            }
        }
    }

    private void writeTotalNumberToTT(String writeURL, long currMs, int total_number, String model, String condition, String continent, String country, String countryCode, String city) {
        StringBuffer putReqSB = new StringBuffer();
        putReqSB.append("put " + TOTAL_NUMBER_METRIC);
        putReqSB.append(" " + currMs);
        putReqSB.append(" " + total_number);
        putReqSB.append(" model=" + model);
        putReqSB.append(" condition=" + condition);
        putReqSB.append(" country=" + country);
        putReqSB.append(" countryCode=" + countryCode);
        putReqSB.append(" continent=" + continent);
        if (city != null && !city.isEmpty()) {
            putReqSB.append(" city=" + city);
        }
        putReqSB.append(System.lineSeparator());

        try {
            HttpClient.execPost(writeURL, putReqSB.toString());
        } catch (HttpClient.HttpException e) {
            System.out.println("Fail to write " + putReqSB.toString());
            e.printStackTrace();
        }
    }

    /**
     * put
     * @param car
     * @return
     */
    private String generatePutRequests(TeslaCar car, long currSec) {
        StringBuffer putReqSB = new StringBuffer();

        // PUT price_<CurrencyCode> currMs <tag1=v1 tag2=v2 ...>
        putReqSB.append("put " + PRICE_LOCAL_CURRENCY_METRIC + car.getCurrencyCode());
        putReqSB.append(" " + currSec);
        putReqSB.append(" " + car.getPrice());
        putReqSB.append(car.tagsString());
        putReqSB.append(System.lineSeparator());

        if (!car.getCurrencyCode().equalsIgnoreCase("USD")) {
            // PUT price_USD currMs <tag1=v1 tag2=v2 ...>
            putReqSB.append("put " + PRICE_USD_METRIC);
            putReqSB.append(" " + currSec);
            putReqSB.append(" " + car.getPriceUSD());
            putReqSB.append(car.tagsString());
            putReqSB.append(System.lineSeparator());
        }

        return putReqSB.toString();
    }

    public static void main(String[] args) {
        // Usgae: java TeslaInventoryGrepper <model(m3, my, mx, ms)> <condition(new, used)> <market(fr...)> [zip(98004...)]
        HttpClientExample a = new HttpClientExample();

        if (args.length < 3 || args.length > 4) {
            System.err.println("Parameters length must be 3 or 4.");
            System.err.println("Usgae: java TeslaInventoryGrepper <model(m3, my, mx, ms)> <condition(new, used)> <market(fr...)> [zip(98004...)]");
            return;
        }
        String model = args[0].toLowerCase();
        if (!model.equalsIgnoreCase("m3") &&
            !model.equalsIgnoreCase("my") &&
            !model.equalsIgnoreCase("mx") &&
            !model.equalsIgnoreCase("ms")) {
            System.err.println("model " + model + " is not one of (m3, my, mx, ms)");
            return;
        }
        String condition = args[1].toLowerCase();
        if (!condition.equalsIgnoreCase("new") && !condition.equalsIgnoreCase("used")) {
            System.err.println("condition " + condition + " is not one of (new, used)");
            return;
        }

        String market = args[2].toUpperCase();
        String zip = args.length > 3 ? args[4] : "";

        TeslaInventoryGrepper grepper = new TeslaInventoryGrepper();
        try {
            HttpClient.Pair resp = grepper.queryByGet(model, condition, market, 0);
            System.out.println("QueryByGet Return code:" + resp.code);
            System.out.println("QueryByGet Return msg:" + resp.msg);
        } catch(HttpClient.HttpException e) {
            e.printStackTrace();
        }
    }
}
