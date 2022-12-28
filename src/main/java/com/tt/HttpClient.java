package com.tt;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;

public class HttpClient {
    protected static MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
    protected static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");

    protected static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient().newBuilder()
        .readTimeout(500000, TimeUnit.MILLISECONDS)
        .connectTimeout(500000, TimeUnit.MILLISECONDS)
        .writeTimeout(500000, TimeUnit.MILLISECONDS)
        .build();

    private static OkHttpClient getOkHttpClient() {
        return OK_HTTP_CLIENT;
    }

    private static Pair exeOkHttpRequest(Request request) throws HttpException {
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
            throw new HttpException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public static Pair execPost(String reqURL, String query) throws HttpException{
        Request request = new Request.Builder()
            .url(reqURL)
            .post(RequestBody.create(query, MEDIA_TYPE_TEXT))
            .build();
        return exeOkHttpRequest(request);
    }

    public static Pair execGet(String reqURLWithParameters) throws HttpException{
        Request request = new Request.Builder()
            .url(reqURLWithParameters)
            .build();
        return exeOkHttpRequest(request);
    }

    public static Pair execGet(String reqURLWithParameters, String token) throws HttpException{
        Request request = new Request.Builder()
            .header("Authorization", token)
            .url(reqURLWithParameters)
            .build();
        return exeOkHttpRequest(request);
    }

    static class Pair {
        int code;
        String msg;

        Pair(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    static class HttpException extends Exception {
        public HttpException(String msg) {
            super(msg);
        }
    }
}

