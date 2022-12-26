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
    protected MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
    protected static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private long timestamp = 1614735960L;

    protected static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient().newBuilder()
        .readTimeout(500000, TimeUnit.MILLISECONDS)
        .connectTimeout(500000, TimeUnit.MILLISECONDS)
        .writeTimeout(500000, TimeUnit.MILLISECONDS)
        .build();

    private static OkHttpClient getOkHttpClient() {
        return OK_HTTP_CLIENT;
    }

    private Pair exeOkHttpRequest(Request request) {
        Response response = null;
        OkHttpClient client = getOkHttpClient();
        try {
            response = client.newCall(request).execute();
            int code = response.code();
            String body = response.body().string();

            if (!response.isSuccessful()) {
                System.out.println("Fail with code " + code);
            } else {
                System.out.println("Succeed with code " + code);
            }
            //System.out.println(body);

            return new Pair(code, body);
        } catch (Exception e) {
            e.printStackTrace();
            return new Pair(500, e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private Pair execPost(String reqURL, String query) {
        Request request = new Request.Builder()
            .url(reqURL)
            .post(RequestBody.create(query, MEDIA_TYPE_TEXT))
            .build();
        return exeOkHttpRequest(request);
    }

    private Pair execGet(String reqURLWithParameters) {
        Request request = new Request.Builder()
            .url(reqURLWithParameters)
            .build();
        return exeOkHttpRequest(request);
    }

    private void initConnect(String ip, String port) {
        writeURL = "http://" + ip + ":" + port + "" + writeURL;
        queryURL = "http://" + ip + ":" + port + "" + queryURL;
    }

    /**
     * insert data point in plain format (default and recommended)
     */
    private int insertPlainData() {
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
        return execPost(writeURL, putReqSB.toString()).code;
    }

    /**
     * Insert data point in Json format
     */
    private int insertJsonData() {
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

        return execPost(writeURL, ja.toJSONString()).code;
    }

    public Pair queryInJson(long start, long end) {
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

        return execPost(queryURL, json);
    }

    /**
     * <url>/api/query?start=1600000000&end=1633412176&m=avg:1m-avg:test.metric{farm=f1,device=d1,sensor=s1}
     * @param start
     * @param end
     * @return
     */
    public Pair queryByGet(long start, long end) {
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

        return execGet(url);
    }

    class Pair {
        int code;
        String msg;

        Pair(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    public static void main(String[] args) {
        // Usgae: java HttpClientExample <host> <port>
        HttpClientExample a=new HttpClientExample();
        String host=args[0];
        String port=args[1];
	    a.initConnect(host, port);

	    // By default, Ticktock accepts data points in plain format(http.request.format=plain in config).
	    a.insertPlainData();

	    // You need to change TickTock config, http.request.format=json
        // a.insertJsonData();

        Pair resp = a.queryInJson(a.timestamp, 1614739000L);
        System.out.println("QueryInJson Return code:"+resp.code);
        System.out.println("QueryInJson Return msg:"+resp.msg);

        resp = a.queryByGet(a.timestamp, 1614739000L);
        System.out.println("QueryByGet Return code:"+resp.code);
        System.out.println("QueryByGet Return msg:"+resp.msg);
    }
}

