package com.tt;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Parameters length must be 2.");
            System.err.println("Usgae: java com.tt.Main <TT host (e.g., localhost)> <TT http port(e.g., 6182)>");
            return;
        }
        String TThost = args[0];
        String TTHttpPort = args[1];
        String writeURL = "http://" + TThost + ":" + TTHttpPort + "/api/put";
        TeslaInventoryGrepper grepper = new TeslaInventoryGrepper();
        grepper.grepSmallCountries(writeURL);

        grepper.grepCarsInCities(writeURL, "NA", "US", "USA", USACity.cityMap);
        grepper.grepCarsInCities(writeURL, "NA", "CA", "Canada", CanadaCity.cityMap);

        grepper.setUrl(TeslaInventoryGrepper.urlCn);
        grepper.grepChina(writeURL);
    }
}
