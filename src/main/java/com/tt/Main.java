package com.tt;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Parameters length must be 3.");
            System.err.println("Usgae: java com.tt.Main <TT host (e.g., localhost)> <TT http port(e.g., 6182)> <XE Token>");
            return;
        }
        String TThost = args[0];
        String TTHttpPort = args[1];
        String XEToken = args[2];
        String writeURL = "http://" + TThost + ":" + TTHttpPort + "/api/put";
        TeslaInventoryGrepper grepper = new TeslaInventoryGrepper();
        grepper.grepEUCountries(XEToken, writeURL);

        grepper.grepCarsInCities(XEToken, writeURL, "NA", "US", "USA", USACity.cityMap);
        grepper.grepCarsInCities(XEToken, writeURL, "NA", "CA", "Canada", CanadaCity.cityMap);

        grepper.setUrl(TeslaInventoryGrepper.urlCn);
        grepper.grepChina(XEToken, writeURL);
    }
}
