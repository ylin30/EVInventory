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
    private String url = urlCom;
    public static String urlCom="https://www.tesla.com/inventory/api/v1/inventory-results";
    public static String urlCn="https://www.tesla.cn/inventory/api/v1/inventory-results";

    public static String TOTAL_NUMBER_METRIC = "car_number";
    public static String PRICE_USD_METRIC = "price_USD";
    public static String PRICE_LOCAL_CURRENCY_METRIC = "price_";

    public static String[] models = {"m3", "my", "mx", "ms"};
    public static String[] conditions = {"new", "used"};

    /**
     * Small countries can be queried with countryCode, without specifying region/province.
     * No space allowed in country name
     */
    public static Map<String, Pair<String>> smallCountries = new HashMap<>(24);
    static {
        smallCountries.put("BE", new Pair<String>("Belgium", "EU"));
        smallCountries.put("CZ", new Pair<String>("Czech", "EU"));
        smallCountries.put("DE", new Pair<String>("Germany", "EU"));
        smallCountries.put("DK", new Pair<String>("Denmark", "EU"));
        smallCountries.put("GR", new Pair<String>("Greek", "EU"));
        smallCountries.put("ES", new Pair<String>("Spain", "EU"));
        smallCountries.put("FR", new Pair<String>("France", "EU"));
        smallCountries.put("HR", new Pair<String>("Croatia", "EU"));
        smallCountries.put("HU", new Pair<String>("Hungary", "EU"));
        smallCountries.put("IE", new Pair<String>("Ireland", "EU"));
        smallCountries.put("IS", new Pair<String>("Iceland", "EU"));
        smallCountries.put("IT", new Pair<String>("Italy", "EU"));
        smallCountries.put("LU", new Pair<String>("Luxembourg", "EU"));
        smallCountries.put("NL", new Pair<String>("Netherlands", "EU"));
        smallCountries.put("NO", new Pair<String>("Norway", "EU"));
        smallCountries.put("AT", new Pair<String>("Austria", "EU"));
        smallCountries.put("PL", new Pair<String>("Poland", "EU"));
        smallCountries.put("PT", new Pair<String>("Portugal", "EU"));
        smallCountries.put("RO", new Pair<String>("Romania", "EU"));
        smallCountries.put("SI", new Pair<String>("Slovenia", "EU"));
        smallCountries.put("SE", new Pair<String>("Sweden", "EU"));
        smallCountries.put("CH", new Pair<String>("Switzerland", "EU"));
        smallCountries.put("FI", new Pair<String>("Finland", "EU"));
        smallCountries.put("GB", new Pair<String>("England", "EU"));
        smallCountries.put("PR", new Pair<String>("PuertoRico", "NA"));
        smallCountries.put("MX", new Pair<String>("Mexico", "NA"));
        smallCountries.put("HK", new Pair<String>("HongKong", "Asia"));
        smallCountries.put("MO", new Pair<String>("Macau", "Asia"));
        smallCountries.put("SG", new Pair<String>("Singapore", "Asia"));
        smallCountries.put("KR", new Pair<String>("SouthKorea", "Asia"));
        smallCountries.put("JP", new Pair<String>("Japan", "Asia"));
        smallCountries.put("AE", new Pair<String>("UAE", "MiddleEast"));
        smallCountries.put("IL", new Pair<String>("Israel", "MiddleEast"));
        smallCountries.put("AU", new Pair<String>("Australia", "Australia"));
        smallCountries.put("NZ", new Pair<String>("NewZealand", "Australia"));
    }

    public static Map<String, String> countriesExcludingEU = new HashMap<>(20);
    static {
        countriesExcludingEU.put("US", "USA");
        countriesExcludingEU.put("CN", "China");
        countriesExcludingEU.put("CA", "Canada");
    }

    private Set<String> carVINs;
    public TeslaInventoryGrepper() {
        this.carVINs = new HashSet<>();
    }
    public TeslaInventoryGrepper(String url) {
        this.url = url;
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
        } else if (country.equalsIgnoreCase("ES") || country.equalsIgnoreCase("MX")) { // Spain, Mexico
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
        } else if (country.equalsIgnoreCase("KR")) { // Korea
            return "ko";
        } else if (country.equalsIgnoreCase("JP")) { // Japan
            return "ja";
        } else if (country.equalsIgnoreCase("IL")) { // Israel
            return "he";
        } else if (country.equalsIgnoreCase("CA") ||
            country.equalsIgnoreCase("IE") ||
            country.equalsIgnoreCase("GB") ||
            country.equalsIgnoreCase("US") ||
            country.equalsIgnoreCase("HK") ||
            country.equalsIgnoreCase("MO") ||
            country.equalsIgnoreCase("SG") ||
            country.equalsIgnoreCase("AE") ||
            country.equalsIgnoreCase("AU") ||
            country.equalsIgnoreCase("NZ")) { // Canada, Ireland, England
            return "en";
        } else if (country.equalsIgnoreCase("CN")) { // China
            return "zh";
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
     *
     * For EU countries. Only need to specify countryCode
     *
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
     *
     * For China cities. Need to specify Option:{"Province", "FleetSalesRegions"}, e.g.,
     * "options":{"Province":["广东"],"FleetSalesRegions":["Dong Guan 东莞","Fo Shan 佛山","Guang Zhou 广州","Hui Zhou 惠州","Shen Zhen 深圳","Zhong Shan 中山","Zhu Hai 珠海","Jiang Men 江门","Shan Tou 汕头"]}
     * "options":{"Province":["上海"],"FleetSalesRegions":["Shang Hai 上海"]}
     *
     * In fact, we only need to specify Province:["Beijing"]. The query will return all the cars in China.
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
    private String buildQueryParams(String model, String condition, String market, String zip, double lng, double lat, String region, int range, String province, int offset) {
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
        if (province !=null && !province.isEmpty()) {
            // It is for China province. No region and range.
            JSONArray ja = new JSONArray();
            ja.add(province);
            // for address data, first create LinkedHashMap
            Map prov = new LinkedHashMap(2);
            prov.put("Province", ja);

            queryJO.put("options", prov);
        } else {
            queryJO.put("region", region);
        }
        queryJO.put("range", range);

        // Top level parameters continues...
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
    public HttpClient.Pair queryByGet(String model, String condition, String market, String province, int offset) throws HttpClient.HttpException {
        return queryByGet(model, condition, market, "", 0, 0, "", 0, province, offset);
    }

    public HttpClient.Pair queryByGet(String model, String condition, String market, String zip, double lng, double lat, String region, int range, String province, int offset) throws HttpClient.HttpException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        // Just one parameter query={"query":{...}, offset:0,...,outsideSearch:false}
        String queryJsonStr = buildQueryParams(model, condition, market, zip, lng, lat, region, range, province, offset);
        urlBuilder.addQueryParameter("query", queryJsonStr);

        String url = urlBuilder.build().toString();

        System.out.println("To query GET Url=\'" + url + "\'");

        return HttpClient.execGet(url);
    }

    /**
     * Query inventory of a country. No zip needed.
     * @param cars
     * @param model
     * @param condition
     * @param market
     */
    public int queryOneCountry(List<TeslaCar> cars, String model, String condition, String market) {
        return queryWithRetries(cars, model, condition, market, "", 0, 0, "", 0, null);
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
    public int queryWithRetries(List<TeslaCar> cars, String model, String condition, String market, String zip, double lng, double lat, String region, int range, String province) {
        int total_matches_found = 0;
        int offset = 0;
        int maxRetries = 5;
        int i = 0;
        do {
            try {
                HttpClient.Pair resp = queryByGet(model, condition, market, zip, lng, lat, region, range, province, offset);
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
    private int queryCity(TeslaInventoryGrepper grepper, List<TeslaCar> cars, String market, String model, String condition, City city) {
        return queryWithRetries(cars, model, condition, market, city.zip, city.lng, city.lat, city.province /* used as region */, City.RANGE, null /* must be null */);
    }


    /**
     * Query inventory of a China city. Zip, region(state), lng and lat must be speicified.
     * @param cars
     * @param model
     * @param condition
     * @param province
     */
    private int queryOneChinaProvince(List<TeslaCar> cars, String model, String condition, String province) {
        return queryWithRetries(cars, model, condition, "CN", "", 0, 0, "", 0, province);
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
     * Loop all countries which can be queried with countryCode only.
     * Query all models and all conditions in each country.
     */
    public void grepSmallCountries(String writeURL) {
        CurrencyConvectorNoToken convector = new CurrencyConvectorNoToken();

        long currSec = System.currentTimeMillis() / 1000;
        for (Map.Entry<String, Pair<String>> entry : smallCountries.entrySet()) {
            String countryCode = entry.getKey();
            Pair<String> countryContinent = entry.getValue();
            String country = countryContinent.first;
            String continent = countryContinent.second;
            for(int i = 0; i < models.length; i++) {
                String model = models[i];
                for(int j = 0; j < conditions.length; j++) {
                    String condition = conditions[j];
                    List<TeslaCar> cars = new LinkedList<>();

                    int total_number = queryOneCountry(cars, model, condition, countryCode);

                    for (TeslaCar car : cars) {
                        // convert local price to USD, and set it in the object.
                        try {
                            if (!car.getCurrencyCode().equalsIgnoreCase("USD")) {
                                long priceUSD = convector.toUSD(car.getPrice(), car.getCurrencyCode());
                                car.setPriceUSD(priceUSD);
                            }

                            car.setContinent(continent);
                        } catch (Exception e) {
                            System.out.println("Fail to convert price to USD for car " + car);
                            e.printStackTrace();
                        }
                    }

                    System.out.println(String.format("Country:%s(%s), model:%s, condition:%s, total_matches_found:%d", country, countryCode, model, condition, cars.size()));
                    writeTotalNumberToTT(writeURL, currSec, total_number, model, condition, continent, country, countryCode, null, null);

                    writeMetricsToTT(cars, writeURL, currSec);
                    try {
                        sleep(1000);
                    } catch(InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Loop all USA cities.
     * Query all models and all conditions in each city. Removed redundant cars (by VINs) if necessary.
     */
    public void grepCarsInCities(String writeURL, String continent, String countryCode, String country, Map<String, City> cityNameToObjMap) {
        CurrencyConvectorNoToken convector = new CurrencyConvectorNoToken();

        // Some cities might be too closed and their results have overlapped.
        // Don't count repeated inventory.
        Set<String> knownVINs = new HashSet<>();

        long currSec = System.currentTimeMillis() / 1000;
        for (Map.Entry<String, City> entry : cityNameToObjMap.entrySet()) {
            String city = entry.getKey();
            City cityObj = entry.getValue();
            for(int i = 0; i < models.length; i++) {
                String model = models[i];
                for(int j = 0; j < conditions.length; j++) {
                    String condition = conditions[j];
                    List<TeslaCar> cars = new LinkedList<>();

                    int totalNumber = queryCity(this, cars, countryCode, model, condition, cityObj);
                    System.out.println(String.format("%s(%s), model:%s, condition:%s, total_matches_found:%d, cars:%d", entry.getValue(), countryCode, model, condition, totalNumber, cars.size()));

                    int numKnowns = removeRedundantCars(cars, knownVINs);

                    System.out.println(String.format("After removed redundant, numRemoved:%d, cars:%d", numKnowns, cars.size()));

                    // No matter if zero inventory or not, write total car number to TT.
                    writeTotalNumberToTT(writeURL, currSec, totalNumber - numKnowns, model, condition, continent, country, countryCode, cityObj.province, city);

                    if (cars.size() > 0) {
                        for (TeslaCar car : cars) {
                            // convert local price to USD, and set it in the object.
                            try {
                                if (!car.getCurrencyCode().equalsIgnoreCase("USD")) {

                                    long priceUSD = convector.toUSD(car.getPrice(), car.getCurrencyCode());
                                    car.setPriceUSD(priceUSD);
                                }
                                car.setContinent(continent);
                            } catch (Exception e) {
                                System.out.println("Fail to convert price to USD for car " + car);
                                e.printStackTrace();
                            }
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

    /**
     * Loop all Chinese cities.
     * Query all models and all conditions in each Chinese city.
     */
    public void grepChina(String writeURL) {
        CurrencyConvectorNoToken convector = new CurrencyConvectorNoToken();

        // Some cities might be too closed and their results have overlapped.
        // Don't count repeated inventory.
        Set<String> knownVINs = new HashSet<>();

        long currSec = System.currentTimeMillis() / 1000;
        String market = "CN";

        for (int i = 0; i < models.length; i++) {
            String model = models[i];
            for (int j = 0; j < conditions.length; j++) {
                String condition = conditions[j];
                List<TeslaCar> cars = new LinkedList<>();

                // Specify "Province":["Beijing"] will be enough to return all cars in China.
                int totalNumber = queryOneChinaProvince(cars, model, condition, "Beijing");
                System.out.println(String.format("%s(%s), model:%s, condition:%s, total_matches_found:%d, cars:%d", "China", market, model, condition, totalNumber, cars.size()));

                Map<String, Integer> cityToNum = new HashMap<>();
                summarizeNum(cars, cityToNum);

                for(Map.Entry<String, Integer> entry : cityToNum.entrySet()) {
                    String city = entry.getKey();
                    int num = entry.getValue();
                    // No matter if zero inventory or not, write total car number to TT.
                    writeTotalNumberToTT(writeURL, currSec, num, model, condition, "ASIA", "China", market, null, city);
                }

                if (cars.size() > 0) {
                    for (TeslaCar car : cars) {
                        // convert local price in CNY to USD, and set it in the object.
                        try {
                            long priceUSD = convector.toUSD(car.getPrice(), car.getCurrencyCode());
                            car.setPriceUSD(priceUSD);

                            car.setContinent("ASIA");
                        } catch (Exception e) {
                            System.out.println("Fail to convert price to USD for car " + car);
                            e.printStackTrace();
                        }
                    }
                    writeMetricsToTT(cars, writeURL, currSec);
                }
                try {
                    sleep(1000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
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
     * Given a list of cars, summarize how many cars in a city.
     * @param cars
     * @param cityToNum
     */
    private void summarizeNum(List<TeslaCar> cars, Map<String, Integer> cityToNum) {
        Iterator<TeslaCar> it = cars.iterator();
        while(it.hasNext()) {
            TeslaCar car = it.next();
            if (!cityToNum.containsKey(car.getCity())) {
                //System.out.println("Car " + car.getVin() + " is in Set. Removed!");
                cityToNum.put(car.getCity(), 0);
            }
            int currNum = cityToNum.get(car.getCity());
            cityToNum.put(car.getCity(), currNum + 1);
        }
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

    private void writeTotalNumberToTT(String writeURL, long currMs, int total_number, String model, String condition, String continent, String country, String countryCode, String stateCode, String city) {
        StringBuffer putReqSB = new StringBuffer();
        putReqSB.append("put " + TOTAL_NUMBER_METRIC);
        putReqSB.append(" " + currMs);
        putReqSB.append(" " + total_number);
        putReqSB.append(" model=" + model);
        putReqSB.append(" condition=" + condition);
        if (country != null && !country.isEmpty()) putReqSB.append(" country=" + country.replace(" ", "_"));
        putReqSB.append(" countryCode=" + countryCode);
        putReqSB.append(" continent=" + continent);
        if (city != null && !city.isEmpty()) {
            putReqSB.append(" city=" + city.replace(" ", "_"));
        }
        if (stateCode != null && !stateCode.isEmpty()) {
            putReqSB.append(" state=" + stateCode);
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

    public void setUrl(String url) {
        this.url = url;
    }

    public static void main(String[] args) {
        // Usgae: java TeslaInventoryGrepper <model(m3, my, mx, ms)> <condition(new, used)> <market(fr, cn...)> [Province(上海)]
        HttpClientExample a = new HttpClientExample();

        if (args.length < 3 || args.length > 4) {
            System.err.println("Parameters length must be 3 or 4.");
            System.err.println("Usgae: java TeslaInventoryGrepper <model(m3, my, mx, ms)> < (new, used)> <market(fr, cn...)> [Province(Shanghai)]");
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
        String province = args.length > 3 ? args[3] : null;

        TeslaInventoryGrepper grepper = new TeslaInventoryGrepper(
            province!=null ? TeslaInventoryGrepper.urlCn : TeslaInventoryGrepper.urlCom
        );
        try {
            HttpClient.Pair resp = grepper.queryByGet(model, condition, market, province, 0);
            System.out.println("QueryByGet Return code:" + resp.code);
            System.out.println("QueryByGet Return msg:" + resp.msg);
        } catch(HttpClient.HttpException e) {
            e.printStackTrace();
        }
    }
}
