package com.tt;

import okhttp3.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class HttpClientExample {
    protected String writeURL = "/api/put";
    protected String queryURL = "/api/query";
    protected String METRIC = "test.metric";
    protected String FARM_TAG = "farm";
    protected String DEVICE_TAG = "device";
    protected String SENSOR_TAG = "sensor";

    private long timestamp = 1614735960L;

    private void initConnect(String ip, String port) {
        writeURL = "http://" + ip + ":" + port + "" + writeURL;
        queryURL = "http://" + ip + ":" + port + "" + queryURL;
    }

    /**
     * insert data point in plain format (default and recommended)
     */
    private int insertPlainData() throws HttpClient.HttpException {
        StringBuilder putReqSB = new StringBuilder();

        int numDataPoints = 2;
        for(int i=1; i <= numDataPoints; i++) {
            putReqSB.append("put " + METRIC);
            putReqSB.append(" " + timestamp);
            putReqSB.append(" " + 1.0);
            putReqSB.append(" " + FARM_TAG + "=f" + i);
            putReqSB.append(" " + DEVICE_TAG + "=d" + i);
            putReqSB.append(" " + SENSOR_TAG + "=s" + i);
            putReqSB.append(System.lineSeparator());
        }

        System.out.println("To insert:");
        System.out.println(putReqSB.toString());
        return HttpClient.execPost(writeURL, putReqSB.toString()).code;
    }

    /**
     * Insert data point in Json format
     */
    private int insertJsonData() throws HttpClient.HttpException {
        // putting data to JSONObject
        JSONArray ja = new JSONArray();

        int numDataPoints = 2;
        for(int i=1; i <= numDataPoints; i++) {
            Map pointMap = new LinkedHashMap(4);
            pointMap.put("metric", METRIC);
            pointMap.put("timestamp", timestamp);
            pointMap.put("value", i);
            Map tagMap = new LinkedHashMap(3);
            tagMap.put(FARM_TAG, "f"+i);
            tagMap.put(DEVICE_TAG, "d"+i);
            tagMap.put(SENSOR_TAG, "s"+i);
            pointMap.put("tags", tagMap);
            ja.add(pointMap);
        }

        return HttpClient.execPost(writeURL, ja.toJSONString()).code;
    }

    public HttpClient.Pair queryInJson(long start, long end) throws HttpClient.HttpException {
        JSONObject jo = new JSONObject();

        jo.put("start", start);
        jo.put("end", end);

        // Add a list of queries
        JSONArray ja = new JSONArray();

        Map subQuery = new LinkedHashMap(4);
        subQuery.put("aggregator", "avg");
        subQuery.put("downsample", "1m-avg");
        subQuery.put("metric", METRIC);
        Map subTag = new LinkedHashMap(3);
        subTag.put(FARM_TAG, "f1");
        subTag.put(DEVICE_TAG, "d1");
        subTag.put(SENSOR_TAG, "s1");
        subQuery.put("tags", subTag);

        ja.add(subQuery);

        // Add to json object
        jo.put("queries", ja);

        String json = jo.toJSONString();
        System.out.println("To query json:");
        System.out.println(json);

        return HttpClient.execPost(queryURL, json);
    }

    /**
     * <url>/api/query?start=1600000000&end=1633412176&m=avg:1m-avg:test.metric{farm=f1,device=d1,sensor=s1}
     * @param start
     * @param end
     * @return
     */
    public HttpClient.Pair queryByGet(long start, long end) throws HttpClient.HttpException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(queryURL).newBuilder();
        urlBuilder.addQueryParameter("start", Long.toString(start));
        urlBuilder.addQueryParameter("end", Long.toString(end));

        StringBuilder mb = new StringBuilder();
        mb.append("avg");
        mb.append(":");
        mb.append("1m-avg");
        mb.append(":");
        mb.append(METRIC);
        mb.append("{");
        mb.append(FARM_TAG);
        mb.append("=f1");
        mb.append(",");

        mb.append(DEVICE_TAG);
        mb.append("=d1");
        mb.append(",");

        mb.append(SENSOR_TAG);
        mb.append("=s1");
        mb.append("}");

        urlBuilder.addQueryParameter("m", mb.toString());
        String url = urlBuilder.build().toString();

        System.out.println("To query GET Url=\'"+url+"\'");

        return HttpClient.execGet(url);
    }

    public static void main(String[] args) throws HttpClient.HttpException {
        // Usgae: java HttpClientExample <host> <port>
        HttpClientExample a=new HttpClientExample();
        String host=args[0];
        String port=args[1];
	    a.initConnect(host, port);

	    // By default, Ticktock accepts data points in plain format(http.request.format=plain in config).
	    a.insertPlainData();

	    // You need to change TickTock config, http.request.format=json
        // a.insertJsonData();

        HttpClient.Pair resp = a.queryInJson(a.timestamp, 1614739000L);
        System.out.println("QueryInJson Return code:"+resp.code);
        System.out.println("QueryInJson Return msg:"+resp.msg);

        resp = a.queryByGet(a.timestamp, 1614739000L);
        System.out.println("QueryByGet Return code:"+resp.code);
        System.out.println("QueryByGet Return msg:"+resp.msg);
    }
}

