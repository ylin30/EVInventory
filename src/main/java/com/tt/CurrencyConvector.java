package com.tt;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConvector {
    private Map<String, Double> currencyCodeToUSD = new HashMap<>();
    public long toUSD(long price, String currencyCode) throws HttpClient.HttpException, ParseException {
        if (!currencyCodeToUSD.containsKey(currencyCode)) {
            int i=0, maxRetries = 10;
            while (i < maxRetries) {
                try {
                    double rate = queryRate(currencyCode);
                    currencyCodeToUSD.put(currencyCode, rate);
                    i = maxRetries; // Forced to break;
                } catch (HttpClient.HttpException | ParseException e) {
                    i++;
                    if (i == maxRetries) {
                        throw e;
                    }
                }
            }
        }
        double rate = currencyCodeToUSD.get(currencyCode);

        return (long)(price * rate);
    }

    /**
     * curl -v -H "Authorization: Basic bG9kZXN0YXI6eWZtTlB1U0RMbDVzZ0o0NFZKNThWNGpudWRjdmh1N3I=" 'https://www.xe.com/api/protected/statistics/?from=EUR&to=USD'
     * @param currencyCode
     * @return
     * @throws HttpClient.HttpException
     * @throws ParseException
     */
    public double queryRate(String currencyCode) throws HttpClient.HttpException, ParseException {
        // Query XE.com
        String url = "https://www.xe.com/api/protected/statistics/?from="+currencyCode+"&to=USD";
        HttpClient.Pair res = HttpClient.execGet(url, "Basic bG9kZXN0YXI6eWZtTlB1U0RMbDVzZ0o0NFZKNThWNGpudWRjdmh1N3I=");

        // Parse the result.
        return parse(res.msg);
    }

    public double parse(String jsonStr) throws ParseException {
        // System.out.println(jsonStr);
        // parsing json string
        Object obj = new JSONParser().parse(jsonStr);

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        Map last1Day = (Map) jo.get("last1Days");
        return (Double)last1Day.get("average");
    }

    public static void main(String[] args) {
        String currencyCode = args[0].toUpperCase();

        CurrencyConvector convector = new CurrencyConvector();
        try {
            double rate = convector.queryRate(currencyCode);
            System.out.println("Rate:" + rate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
