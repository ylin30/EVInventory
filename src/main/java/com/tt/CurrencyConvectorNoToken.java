package com.tt;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConvectorNoToken {
    private Map<String, Double> currencyCodeToUSD = new HashMap<>();

    public CurrencyConvectorNoToken() {
    }

    public long toUSD(long price, String currencyCode) throws HttpClient.HttpException, ParseException {
        if (currencyCode.equalsIgnoreCase("USD")) {
            return price;
        }
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
     * curl 'https://fxds-public-exchange-rates-api.oanda.com/cc-api/currencies?base=GBP&quote=USD&data_type=general_currency_pair&start_date=2023-01-03&end_date=2023-01-04'
     *
     * Results:
     * {"response":[{"base_currency":"GBP","quote_currency":"USD","close_time":"2023-01-03T23:59:59Z","average_bid":"1.19991","average_ask":"1.20016","high_bid":"1.20837","high_ask":"1.20854","low_bid":"1.19000","low_ask":"1.19020"}]}
     * @param currencyCode
     * @return
     * @throws HttpClient.HttpException
     * @throws ParseException
     */
    public double queryRate(String currencyCode) throws HttpClient.HttpException, ParseException {
        // Query oanda.com
        String baseUrl = "https://fxds-public-exchange-rates-api.oanda.com/cc-api/currencies?base="+currencyCode+"&quote=USD&data_type=general_currency_pair";

        // Prepare e.g., &start_date=2023-01-03&end_date=2023-01-04
        Calendar currCal = Calendar.getInstance();
        Date date = currCal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currDateStr = dateFormat.format(date);

        currCal.add(Calendar.DATE, -1);
        Date prevDate = currCal.getTime();
        String prevDateStr = dateFormat.format(prevDate);

        String url = "&start_date=" + prevDateStr +"&end_date=" + currDateStr;
        url = baseUrl + url;

        System.out.println(url);

        HttpClient.Pair res = HttpClient.execGet(url);

        // Parse the result.
        try {
            return parse(res.msg);
        } catch (ParseException e) {
            System.out.println("Fail to parse queryRate msg:" + res.msg);
            throw e;
        }
    }

    /**
     * To parse:
     * {"response":[{"base_currency":"GBP","quote_currency":"USD","close_time":"2023-01-03T23:59:59Z","average_bid":"1.19991","average_ask":"1.20016","high_bid":"1.20837","high_ask":"1.20854","low_bid":"1.19000","low_ask":"1.19020"}]}
     * @param jsonStr
     * @return
     * @throws ParseException
     */
    public double parse(String jsonStr) throws ParseException {
        System.out.println(jsonStr);
        // parsing json string
        Object obj = new JSONParser().parse(jsonStr);

        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) obj;

        JSONArray ja = (JSONArray) jo.get("response");

        Map map = (Map)ja.get(0);

        return Double.parseDouble((String)map.get("average_bid"));
    }

    public static void main(String[] args) {
        String currencyCode = args[0].toUpperCase();

        CurrencyConvectorNoToken convector = new CurrencyConvectorNoToken();
        try {
            double rate = convector.queryRate(currencyCode);
            System.out.println("Rate:" + rate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}