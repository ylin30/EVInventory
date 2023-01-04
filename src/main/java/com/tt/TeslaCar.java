package com.tt;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TeslaCar {
    private String continent;
    private String country;
    private String countryCode;
    private String model;
    private long price;
    private long priceUSD;
    private String autopilot;
    private String city;
    private double lng;
    private double lat;
    private String currencyCode;
    private String interior;
    private long odometer;
    private String odometerType;
    private String paint;
    private String trim;
    private String wheels;
    private long year;
    private boolean isDemo;
    private String condition;
    private String vin;

    public enum Condition {
        NEW, USED;
    }

    public TeslaCar(String condition) {
        this.condition = condition;
    }

    public TeslaCar(String condition, String model, long price) {
        this.condition = condition;
        this.model = model;
        this.price = price;
    }

    public TeslaCar(String condition, Map map) {
        this.condition = condition;
        try { this.model = (String) map.get("Model"); } catch(ClassCastException e) { throw e; }
        try { this.price = (Long) map.get("Price"); } catch(ClassCastException e) { throw e; }
        System.out.println("model:" + model + " price:" + price);

        try {
            JSONArray ja = (JSONArray) map.get("AUTOPILOT");
            if (ja != null) {
                StringBuffer sb = new StringBuffer();
                Iterator itr = ja.iterator();
                while (itr.hasNext()) {
                    if (sb.length() != 0) {
                        sb.append("_");
                    }
                    String tmpStr = (String) itr.next();
                    sb.append(tmpStr);
                }
                this.autopilot = sb.toString();
            }
        } catch(ClassCastException e) { e.printStackTrace();  }

        try { this.city = (String) map.get("City"); }
        catch(ClassCastException e) { System.out.println("City:" + map.get("City")); e.printStackTrace(); }

        try {

            this.countryCode = (String) map.get("CountryCode");

            if (TeslaInventoryGrepper.smallCountries.containsKey(countryCode)) {
                this.country = TeslaInventoryGrepper.smallCountries.get(countryCode).first;
            } else {
                this.country = TeslaInventoryGrepper.countriesExcludingEU.get(countryCode);
            }
        }
        catch(ClassCastException e) { System.out.println("CountryCode:" + map.get("CountryCode")); e.printStackTrace(); }

        // geoPoints:[["47.47237,-122.21119", 8995]]
        try {
            JSONArray ja = (JSONArray) map.get("geoPoints");
            if (ja != null && !ja.isEmpty()) {
                JSONArray ja2 = (JSONArray)ja.get(0);
                if (ja2 != null && !ja2.isEmpty()) {
                    String latLng = ((String) ja2.get(0)).trim();
                    int separatorIndx = latLng.indexOf(',');
                    this.lat = Double.parseDouble(latLng.substring(0, separatorIndx));
                    this.lng = Double.parseDouble(latLng.substring(separatorIndx + 1));
                }
            }
        }
        catch(ClassCastException e) { System.out.println("geoPoints:" + map.get("geoPoints")); e.printStackTrace(); }

        try {
            this.currencyCode = (String) map.get("CurrencyCode");
            if (this.currencyCode.equalsIgnoreCase("USD")) {
                this.priceUSD = this.price;
            }
        }
        catch(ClassCastException e) { System.out.println("CurrencyCode:" + map.get("CurrencyCode")); e.printStackTrace(); }

        try {
            JSONArray ja = (JSONArray) map.get("INTERIOR");
            if (ja != null) {
                StringBuffer sb = new StringBuffer();
                Iterator itr = ja.iterator();
                while (itr.hasNext()) {
                    if (sb.length() != 0) {
                        sb.append("_");
                    }
                    String tmpStr = (String) itr.next();
                    sb.append(tmpStr);
                }
                this.interior = sb.toString();
            }
        } catch(ClassCastException e) { e.printStackTrace();  }

        try { this.isDemo = (Boolean) map.get("IsDemo"); } catch(ClassCastException e) { System.out.println("isDemo:" + map.get("IsDemo")); e.printStackTrace(); }


        try { this.odometer = (Long) map.get("Odometer"); }
        catch(ClassCastException e) { System.out.println("Odometer:" + map.get("Odometer")); e.printStackTrace(); }

        try { this.odometerType = (String) map.get("OdometerTypeShort"); }
        catch(ClassCastException e) { System.out.println("OdometerTypeShort:" + map.get("OdometerTypeShort")); e.printStackTrace(); }

        try {
            JSONArray ja = (JSONArray) map.get("PAINT");
            if (ja != null) {
                StringBuffer sb = new StringBuffer();
                Iterator itr = ja.iterator();
                while (itr.hasNext()) {
                    if (sb.length() != 0) {
                        sb.append("_");
                    }
                    String tmpStr = (String) itr.next();
                    sb.append(tmpStr);
                }
                this.paint = sb.toString();
            }
        } catch(ClassCastException e) { e.printStackTrace();  }


        try {
            JSONArray ja = (JSONArray) map.get("TRIM");
            if (ja != null) {
                StringBuffer sb = new StringBuffer();
                Iterator itr = ja.iterator();
                while (itr.hasNext()) {
                    if (sb.length() != 0) {
                        sb.append("_");
                    }
                    String tmpStr = (String) itr.next();
                    sb.append(tmpStr);
                }
                this.trim = sb.toString();
            }
        } catch(ClassCastException e) { e.printStackTrace();  }

        try {
            JSONArray ja = (JSONArray) map.get("WHEELS");
            if (ja != null) {
                StringBuffer sb = new StringBuffer();
                Iterator itr = ja.iterator();
                while (itr.hasNext()) {
                    if (sb.length() != 0) {
                        sb.append("_");
                    }
                    String tmpStr = (String) itr.next();
                    sb.append(tmpStr);
                }
                this.wheels = sb.toString();
            }
        } catch(ClassCastException e) { e.printStackTrace();  }

        try { this.year = (Long) map.get("Year"); }
        catch(ClassCastException e) { System.out.println("year:" + map.get("Year")); e.printStackTrace(); }

        try { this.vin = (String) map.get("VIN"); }
        catch(ClassCastException e) { System.out.println("vin:" + map.get("VIN")); e.printStackTrace(); }
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getAutopilot() {
        return autopilot;
    }

    public void setAutopilot(String autopilot) {
        this.autopilot = autopilot;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }

    public long getOdometer() {
        return odometer;
    }

    public void setOdometer(long odometer) {
        this.odometer = odometer;
    }

    public String getOdometerType() {
        return odometerType;
    }

    public void setOdometerType(String odometerType) {
        this.odometerType = odometerType;
    }

    public String getPaint() {
        return paint;
    }

    public void setPaint(String paint) {
        this.paint = paint;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getWheels() {
        return wheels;
    }

    public void setWheels(String wheels) {
        this.wheels = wheels;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getPriceUSD() {
        return priceUSD;
    }

    public void setPriceUSD(long priceUSD) {
        this.priceUSD = priceUSD;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeslaCar)) return false;
        TeslaCar teslaCar = (TeslaCar) o;
        return odometer == teslaCar.odometer &&
            year == teslaCar.year &&
            isDemo == teslaCar.isDemo &&
            countryCode.equals(teslaCar.countryCode) &&
            model.equals(teslaCar.model) &&
            Objects.equals(autopilot, teslaCar.autopilot) &&
            city.equals(teslaCar.city) &&
            currencyCode.equals(teslaCar.currencyCode) &&
            interior.equals(teslaCar.interior) &&
            odometerType.equals(teslaCar.odometerType) &&
            paint.equals(teslaCar.paint) &&
            trim.equals(teslaCar.trim) &&
            wheels.equals(teslaCar.wheels) &&
            condition.equals(teslaCar.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, model, autopilot, city, currencyCode, interior, odometer, odometerType, paint, trim, wheels, year, isDemo, condition);
    }

    @Override
    public String toString() {
        return "TeslaCar{" +
            "continent='" + continent + '\'' +
            ", country='" + country + '\'' +
            ", countryCode='" + countryCode + '\'' +
            ", model='" + model + '\'' +
            ", price=" + price +
            ", priceUSD=" + priceUSD +
            ", autopilot='" + autopilot + '\'' +
            ", city='" + city + '\'' +
            ", lng=" + lng + '\'' +
            ", lat=" + lat + '\'' +
            ", currencyCode='" + currencyCode + '\'' +
            ", interior='" + interior + '\'' +
            ", odometer=" + odometer +
            ", odometerType='" + odometerType + '\'' +
            ", paint='" + paint + '\'' +
            ", trim='" + trim + '\'' +
            ", wheels='" + wheels + '\'' +
            ", year=" + year +
            ", isDemo=" + isDemo +
            ", condition='" + condition + '\'' +
            ", vin='" + vin + '\'' +
            '}';
    }

    public String tagsString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" continent=" + continent);
        sb.append(" country=" + country);
        sb.append(" countryCode=" + countryCode);
        sb.append(" model=" + model);
        sb.append(" autopilot=" + autopilot);
        sb.append(" city=" + city);
        sb.append(" lng=" + lng);
        sb.append(" lat=" + lat);
        sb.append(" interior=" + interior);
        sb.append(" odometer=" + odometer);
        sb.append(" odometerType=" + odometerType);
        sb.append(" paint=" + paint);
        sb.append(" trim=" + trim);
        sb.append(" wheels=" + wheels);
        sb.append(" year=" + year);
        sb.append(" isDemo=" + isDemo);
        sb.append(" condition=" + condition);
        return sb.toString();
    }

    /**
     * [Yi-MBP EVInventory (main)]$ java -cp target/evinventory-0.1-jar-with-dependencies.jar com.tt.TeslaCar
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        String str = "{\"results\":[{\"InTransit\":false,\"ADL_OPTS\":[\"REAR_HEATED_SEATS\"],\"AUTOPILOT\":[\"AUTOPILOT\"],\"AcquisitionSubType\":null,\"AcquisitionType\":null,\"ActualGAInDate\":\"2022-10-20T17:29:10.000000\",\"AlternateCurrency\":[],\"BATTERY\":null,\"CABIN_CONFIG\":null,\"CPORefurbishmentStatus\":null," +
            "\"CPOWarrantyType\":null,\"City\":\"Greve\",\"CompositorViews\":{\"frontView\":\"STUD_3QTR\",\"sideView\":\"SIDE\",\"interiorView\":\"STUD_SEAT\"},\"CountryCode\":\"DK\",\"CountryCodes\":[\"EE\",\"FI\",\"FR\",\"RO\",\"SE\",\"SK\",\"DK\",\"HR\",\"NL\",\"PT\",\"ES\",\"BE\",\"CZ\",\"IT\",\"LT\",\"LU\",\"NO\",\"PL\",\"AT\",\"DE\",\"GR\",\"HU\",\"IS\",\"LV\",\"SI\",\"CH\"]," +
            "\"CountryOfOrigin\":\"DK\",\"CurrencyCode\":\"DKK\",\"CurrencyCodes\":\"DKK\",\"DECOR\":null,\"DRIVE\":null,\"DamageDisclosureStatus\":\"PendingReview\",\"DestinationHandlingFee\":0,\"Discount\":23100,\"DisplayWarranty\":false,\"EtaToCurrent\":\"2022-12-16T14:57:53\",\"FactoryCode\":\"GF03\",\"FactoryDepartureDate\":\"2022-10-21T01:40:51\",\"FirstRegistrationDate\":null," +
            "\"FixedAssets\":false,\"FlexibleOptionsData\":[{\"code\":\"$APF2\",\"description\":\"Fuldt selvk\\u00f8rende egenskaber\",\"group\":\"AUTOPILOT\",\"long_name\":\"Fuldt selvk\\u00f8rende egenskaber\",\"name\":\"Fuldt selvk\\u00f8rende egenskaber\",\"price\":58300}],\"ForecastedFactoryGatedDate\":null,\"HEADLINER\":null,\"HasDamagePhotos\":false,\"HasOptionCodeData\":true," +
            "\"INTERIOR\":[\"PREMIUM_WHITE\"],\"InspectionDocumentGuid\":null,\"InventoryPrice\":411390,\"IsChargingConnectorIncluded\":true,\"IsDemo\":true,\"IsLegacy\":false,\"IsPreProdWithDisclaimer\":false,\"IsTegra\":false,\"Language\":\"da\",\"Languages\":[\"da\"],\"LeaseAvailabilityDate\":null,\"LexiconDefaultOptions\":[{\"code\":\"$CPF0\",\"description\":\"Standard tilslutning\"," +
            "\"group\":\"CONNECTIVITY\",\"long_name\":\"Standard tilslutning\",\"name\":\"Standard tilslutning\"},{\"code\":\"$SC04\",\"description\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"group\":\"SUPER_CHARGER\",\"long_name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\"}]," +
            "\"ListingType\":\"public\",\"ListingTypes\":\"public\",\"ManufacturingOptionCodeList\":\"AD15,AF00,APFB,APH4,AU3D,BC3B,BTF1,RNG0,CDM0,CH15,CODK,DRLH,DV2W,FC02,FG3B,FM3B,GLCN,HL32,HM30,ID3A,IL31,LT03,MDL3,MR31,PPSW,PC30,REEU,RF3G,RS3H,S3PW,SA3P,SC04,STCP,SU3C,T3MC,TM00,TW00,UT3P,W40B,WR00,ZINV,MI02,PL30,SLR0,ST33,BG30,OSSB,AUF2,RSF1,ILF0,FGF0,CPF0,PW01,HP31,PT01,RL32,LVB2,RD05,SWF1,PP02,RA00,VC01,LS01,SG01,US00\"," +
            "\"MarketingInUseDate\":\"2022-12-15T13:50:19\",\"Model\":\"m3\",\"OdometerType\":\"Km\",\"OnConfiguratorPricePercentage\":96,\"OptionCodeData\":[{\"acceleration_unit_long\":\"kilometer i timen\",\"acceleration_unit_short\":\"km\\/t\",\"acceleration_value\":\"0-100\",\"code\":\"$MT322\",\"group\":\"SPECS_ACCELERATION\",\"price\":84810,\"unit_long\":\"sekunder\"," +
            "\"unit_short\":\"s\",\"value\":\"6.1\"},{\"code\":\"$MT322\",\"group\":\"SPECS_TOP_SPEED\",\"price\":84810,\"top_speed_label\":\"Tophastighed\",\"unit_long\":\"kilometer i timen\",\"unit_short\":\"km\\/t\",\"value\":\"225\"},{\"code\":\"$MT322\",\"group\":\"SPECS_RANGE\",\"price\":84810,\"range_label_source\":\"R\\u00e6kkevidde (WLTP)\",\"range_source\":\"WLTP\"," +
            "\"range_source_inventory_new\":\"WLTP\",\"unit_long\":\"kilometer\",\"unit_short\":\"km\",\"value\":\"491\"},{\"code\":\"$APBS\",\"description\":\"Autopilot\",\"group\":\"AUTOPILOT\",\"long_name\":\"Autopilot\",\"name\":\"Autopilot\"},{\"code\":\"$DV2W\",\"description\":\"Baghjulstr\\u00e6k\",\"group\":\"DRIVE_MODE\",\"long_name\":\"Baghjulstr\\u00e6k\"," +
            "\"name\":\"Baghjulstr\\u00e6k\"},{\"code\":\"$IBW1\",\"description\":\"Sort og hvidt interi\\u00f8r\",\"group\":\"INTERIOR\",\"long_name\":\"Sort og hvidt, delvist Premium-interi\\u00f8r\",\"name\":\"Sort og hvidt interi\\u00f8r\",\"price\":9500},{\"code\":\"$PPSW\",\"description\":\"Pearl White\",\"group\":\"PAINT\",\"long_name\":\"Pearl White lakering\"," +
            "\"name\":\"Pearl White Multi-Coat\"},{\"code\":\"$PRM30\",\"description\":\"<p>Opgrader dit interi\\u00f8r med ekstrafunktioner og premium-materialer.<\\/p><ul><li><span>Premium opvarmede s\\u00e6der og kabinematerialer inkl. tr\\u00e6-interi\\u00f8r, glastag og fire USB-porte<\\/span><\\/li><li><span>12-vejs automatisk justerbare fors\\u00e6der, rat og sidespejle, alle med tilpassede f\\u00f8rerprofiler<\\/span><\\/li><li><span>Premium lydsystem med mere kraft, diskanter, surround-h\\u00f8jtalere og subwoofer<\\/span><\\/li><\\/ul>\"," +
            "\"group\":\"PREMIUM_PACKAGE\",\"long_name\":\"Delvist Premium-interi\\u00f8r\",\"name\":\"Delvist Premium-interi\\u00f8r\"},{\"code\":\"$SC04\",\"description\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"group\":\"SUPER_CHARGER\",\"long_name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\"}," +
            "{\"code\":\"$MDL3\",\"description\":\"Leveringer vil blive p\\u00e5begyndt i USA baseret p\\u00e5 ordredato og evt. tilvalg.\",\"group\":\"MODEL\",\"long_name\":\"Model 3\",\"name\":\"Model 3\",\"price\":336000},{\"code\":\"$W40B\",\"description\":\"18\\\" Aero-f\\u00e6lge\",\"group\":\"WHEELS\",\"long_name\":\"18\\\" Aero-f\\u00e6lge\",\"name\":\"18\\\" Aero-f\\u00e6lge\"}," +
            "{\"code\":\"$MT322\",\"description\":\"Model 3 baghjulstr\\u00e6k\",\"group\":\"TRIM\",\"long_name\":\"Model 3 baghjulstr\\u00e6k\",\"name\":\"Model 3 baghjulstr\\u00e6k\",\"price\":84810},{\"code\":\"$CPF0\",\"description\":\"Standard tilslutning\",\"group\":\"CONNECTIVITY\",\"long_name\":\"Standard tilslutning\",\"name\":\"Standard tilslutning\"},{\"code\":\"$RSF1\",\"description\":\"Opvarmede bags\\u00e6der\",\"group\":\"HEATED_SEAT\",\"long_name\":\"Opvarmede bags\\u00e6der\",\"name\":\"Opvarmede bags\\u00e6der\"},{\"code\":\"$CW03\",\"description\":\"Forbedr din k\\u00f8reoplevelse med opvarmede bags\\u00e6der og opvarmet rat.\",\"group\":\"COLD_WEATHER\",\"long_name\":\"Koldtvejrsfunktion\",\"name\":\"Koldtvejrsfunktion\"}],\"OptionCodeList\":\"$APBS,$DV2W,$IBW1,$PPSW,$PRM30,$SC04,$MDL3,$W40B,$MT322,$CPF0,$RSF1,$CW03\",\"OptionCodeListDisplayOnly\":null,\"OptionCodePricing\":[{\"code\":\"$MDL3\",\"group\":\"MODEL\",\"price\":336000},{\"code\":\"$APBS\",\"group\":\"AUTOPILOT_PACKAGE\",\"price\":0},{\"code\":\"$CPF0\",\"group\":\"CONNECTIVITY\",\"price\":0},{\"code\":\"$CW03\",\"group\":\"COLD_WEATHER\",\"price\":0},{\"code\":\"$DV2W\",\"group\":\"DRIVE_MODE\",\"price\":0},{\"code\":\"$IBW1\",\"group\":\"INTERIOR\",\"price\":9500},{\"code\":\"$MT322\",\"group\":\"TRIM\",\"price\":84810},{\"code\":\"$PRM30\",\"group\":\"PREMIUM_PACKAGE\",\"price\":0},{\"code\":\"$PPSW\",\"group\":\"PAINT\",\"price\":0},{\"code\":\"$RSF1\",\"group\":\"HEATED_SEAT\",\"price\":0},{\"code\":\"$SC04\",\"group\":\"SUPER_CHARGER\",\"price\":0},{\"code\":\"$W40B\",\"group\":\"WHEELS\",\"price\":0}],\"OrderFee\":{\"type\":\"order_fee\",\"value\":0},\"OriginalDeliveryDate\":null,\"OriginalInCustomerGarageDate\":null,\"PAINT\":[\"WHITE\"],\"PlannedGADailyDate\":\"2022-10-20T00:00:00\",\"Price\":411390,\"PurchasePrice\":407210,\"ROOF\":null,\"SalesMetro\":\"Copenhagen\",\"StateProvince\":null,\"StateProvinceLongName\":null,\"TRIM\":[\"M3RWD\"],\"TaxScheme\":null,\"ThirdPartyHistoryUrl\":null,\"TitleStatus\":\"NEW\",\"TitleSubtype\":[\"DEMO\"],\"TotalPrice\":434490,\"TradeInType\":null,\"TransportFees\":{\"exemptVRL\":[],\"fees\":[],\"metro_fees\":[]},\"TrimCode\":\"$MT322\",\"TrimName\":\"Model 3 baghjulstr\\u00e6k\",\"Trt\":14850,\"TrtName\":\"Greve\",\"VIN\":\"LRW3250_6b0c231b3999c4e251516b44fb8c3d4a\",\"Variant\":null,\"VehicleRegion\":null,\"VrlName\":\"Greve\",\"WHEELS\":[\"EIGHTEEN\"],\"WarrantyBatteryExpDate\":\"2030-12-15T13:50:20Z\",\"WarrantyBatteryIsExpired\":false,\"WarrantyBatteryMile\":160000,\"WarrantyBatteryYear\":8,\"WarrantyData\":{\"UsedVehicleLimitedWarrantyMile\":0,\"UsedVehicleLimitedWarrantyYear\":0,\"WarrantyBatteryExpDate\":\"2030-12-15T13:50:20Z\",\"WarrantyBatteryIsExpired\":false,\"WarrantyBatteryMile\":160000,\"WarrantyBatteryYear\":8,\"WarrantyDriveUnitExpDate\":\"2030-12-15T13:50:20Z\",\"WarrantyDriveUnitMile\":160000,\"WarrantyDriveUnitYear\":8,\"WarrantyMile\":80000,\"WarrantyVehicleExpDate\":\"2026-12-15T13:50:20Z\",\"WarrantyVehicleIsExpired\":false,\"WarrantyYear\":4},\"WarrantyDriveUnitExpDate\":\"2030-12-15T13:50:20Z\",\"WarrantyDriveUnitMile\":160000,\"WarrantyDriveUnitYear\":8,\"WarrantyMile\":80000,\"WarrantyVehicleExpDate\":\"2026-12-15T13:50:20Z\",\"WarrantyVehicleIsExpired\":false,\"WarrantyYear\":4,\"Year\":2023,\"UsedVehicleLimitedWarrantyMile\":0,\"UsedVehicleLimitedWarrantyYear\":0,\"OdometerTypeShort\":\"km\",\"DeliveryDateDisplay\":true,\"Hash\":\"250_6b0c231b3999c4e251516b44fb8c3d4a\",\"Odometer\":10,\"VrlLocks\":[14850],\"OptionCodeSpecs\":{\"C_SPECS\":{\"code\":\"C_SPECS\",\"name\":\"Specs\",\"options\":[{\"code\":\"SPECS_ACCELERATION\",\"name\":\"6,1 s\",\"long_name\":\"6,1 sekunder\",\"description\":\"0-100 km\\/t\"},{\"code\":\"SPECS_TOP_SPEED\",\"name\":\"225 km\\/t\",\"long_name\":\"225 kilometer i timen\",\"description\":\"Tophastighed\"},{\"code\":\"SPECS_RANGE\",\"name\":\"491 km\",\"long_name\":\"491 kilometer\",\"description\":\"r\\u00e6kkevidde (WLTP)\"}]},\"C_DESIGN\":{\"code\":\"C_DESIGN\",\"name\":\"Design\",\"options\":[]},\"C_CALLOUTS\":{\"code\":\"C_CALLOUTS\",\"name\":\"Callout Features\",\"options\":[{\"code\":\"$APBS\",\"name\":\"Autopilot\",\"long_name\":\"\",\"description\":\"Autopilot\",\"group\":\"AUTOPILOT\"},{\"code\":\"PCT1M\",\"name\":\"30 dage pr\\u00f8veperiode med Premium Connectivity\",\"description\":\"30 dage pr\\u00f8veperiode med Premium Connectivity\",\"group\":\"CONNECTIVITY\",\"list\":[\"satelliteMaps\",\"musicStreaming\",\"videoStreaming\",\"caraoke\",\"internetBrowser\"],\"period\":\"30 dage\"}]},\"C_OPTS\":{\"code\":\"C_OPTS\",\"name\":\"Options\",\"options\":[{\"code\":\"$PPSW\",\"name\":\"Pearl White lakering\",\"long_name\":\"\",\"description\":\"Pearl White\"},{\"code\":\"$W40B\",\"name\":\"18\\\" Aero-f\\u00e6lge\",\"long_name\":\"\",\"description\":\"18\\\" Aero-f\\u00e6lge\"},{\"code\":\"$IBW1\",\"name\":\"Sort og hvidt, delvist Premium-interi\\u00f8r\",\"long_name\":\"\",\"description\":\"Sort og hvidt interi\\u00f8r\"},{\"code\":\"$APBS\",\"name\":\"Autopilot\",\"long_name\":\"\",\"description\":\"Autopilot\"},{\"code\":\"$RSF1\",\"name\":\"Opvarmede for- og bags\\u00e6der\",\"long_name\":\"\",\"description\":\"Opvarmede bags\\u00e6der\"}]}},\"CompositorViewsCustom\":{\"isProductWithCustomViews\":false,\"externalZoom\":{\"order\":1,\"search\":1},\"externalCrop\":{\"order\":\"1400,850,250,150\",\"search\":\"1400,850,300,130\"}},\"IsRangeStandard\":true,\"MetroName\":\"Copenhagen\",\"geoPoints\":[[\"55.586975,12.250996\",14850]],\"HasMarketingOptions\":true,\"IsFactoryGated\":true},{\"InTransit\":false,\"ADL_OPTS\":[\"REAR_HEATED_SEATS\"],\"AUTOPILOT\":[\"AUTOPILOT\"],\"AcquisitionSubType\":null,\"AcquisitionType\":null,\"ActualGAInDate\":\"2022-10-26T21:15:38.000000\",\"AlternateCurrency\":[],\"BATTERY\":null,\"CABIN_CONFIG\":null,\"CPORefurbishmentStatus\":null,\"CPOWarrantyType\":null,\"City\":\"Greve\",\"CompositorViews\":{\"frontView\":\"STUD_3QTR\",\"sideView\":\"SIDE\",\"interiorView\":\"STUD_SEAT\"},\"CountryCode\":\"DK\",\"CountryCodes\":[\"DE\",\"HU\",\"AT\",\"CH\",\"HR\",\"IT\",\"NO\",\"PL\",\"RO\",\"ES\",\"FR\",\"LV\",\"PT\",\"SK\",\"BE\",\"DK\",\"FI\",\"GR\",\"IS\",\"LT\",\"LU\",\"NL\",\"CZ\",\"EE\",\"SE\",\"SI\"],\"CountryOfOrigin\":\"DK\",\"CurrencyCode\":\"DKK\",\"CurrencyCodes\":\"DKK\",\"DECOR\":null,\"DRIVE\":null,\"DamageDisclosureStatus\":\"PendingReview\",\"DestinationHandlingFee\":0,\"Discount\":23100,\"DisplayWarranty\":false,\"EtaToCurrent\":\"2022-12-16T14:57:53\",\"FactoryCode\":\"GF03\",\"FactoryDepartureDate\":\"2022-10-27T08:10:11\",\"FirstRegistrationDate\":null,\"FixedAssets\":false,\"FlexibleOptionsData\":[{\"code\":\"$APF2\",\"description\":\"Fuldt selvk\\u00f8rende egenskaber\",\"group\":\"AUTOPILOT\",\"long_name\":\"Fuldt selvk\\u00f8rende egenskaber\",\"name\":\"Fuldt selvk\\u00f8rende egenskaber\",\"price\":58300}],\"ForecastedFactoryGatedDate\":null,\"HEADLINER\":null,\"HasDamagePhotos\":false,\"HasOptionCodeData\":true,\"INTERIOR\":[\"PREMIUM_WHITE\"],\"InspectionDocumentGuid\":null,\"InventoryPrice\":420890,\"IsChargingConnectorIncluded\":true,\"IsDemo\":true,\"IsLegacy\":false,\"IsPreProdWithDisclaimer\":false,\"IsTegra\":false,\"Language\":\"da\",\"Languages\":[\"da\"],\"LeaseAvailabilityDate\":null,\"LexiconDefaultOptions\":[{\"code\":\"$CPF0\",\"description\":\"Standard tilslutning\",\"group\":\"CONNECTIVITY\",\"long_name\":\"Standard tilslutning\",\"name\":\"Standard tilslutning\"},{\"code\":\"$SC04\",\"description\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"group\":\"SUPER_CHARGER\",\"long_name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\"}],\"ListingType\":\"public\",\"ListingTypes\":\"public\",\"ManufacturingOptionCodeList\":\"AD15,AF00,APFB,APH4,AU3D,BC3B,BTF1,RNG0,CDM0,CH15,CODK,DRLH,DV2W,FC02,FG3B,FM3B,GLCN,HL32,HM30,ID3A,IL31,LT03,MDL3,MR31,PBSB,PC30,REEU,RF3G,RS3H,S3PW,SA3P,SC04,STCP,SU3C,T3MC,TM00,TW00,UT3P,W40B,WR00,ZINV,MI02,PL30,SLR0,ST33,BG30,OSSB,AUF2,RSF1,ILF0,FGF0,CPF0,PW01,HP31,PT01,RL32,LVB2,RD05,SWF1,PP02,RA00,VC01,LS01,SG01,US00\",\"MarketingInUseDate\":\"2022-12-15T13:50:05\",\"Model\":\"m3\",\"OdometerType\":\"Km\",\"OnConfiguratorPricePercentage\":99,\"OptionCodeData\":[{\"acceleration_unit_long\":\"kilometer i timen\",\"acceleration_unit_short\":\"km\\/t\",\"acceleration_value\":\"0-100\",\"code\":\"$MT322\",\"group\":\"SPECS_ACCELERATION\",\"price\":84810,\"unit_long\":\"sekunder\",\"unit_short\":\"s\",\"value\":\"6.1\"},{\"code\":\"$MT322\",\"group\":\"SPECS_TOP_SPEED\",\"price\":84810,\"top_speed_label\":\"Tophastighed\",\"unit_long\":\"kilometer i timen\",\"unit_short\":\"km\\/t\",\"value\":\"225\"},{\"code\":\"$MT322\",\"group\":\"SPECS_RANGE\",\"price\":84810,\"range_label_source\":\"R\\u00e6kkevidde (WLTP)\",\"range_source\":\"WLTP\",\"range_source_inventory_new\":\"WLTP\",\"unit_long\":\"kilometer\",\"unit_short\":\"km\",\"value\":\"491\"},{\"code\":\"$APBS\",\"description\":\"Autopilot\",\"group\":\"AUTOPILOT\",\"long_name\":\"Autopilot\",\"name\":\"Autopilot\"},{\"code\":\"$DV2W\",\"description\":\"Baghjulstr\\u00e6k\",\"group\":\"DRIVE_MODE\",\"long_name\":\"Baghjulstr\\u00e6k\",\"name\":\"Baghjulstr\\u00e6k\"},{\"code\":\"$IBW1\",\"description\":\"Sort og hvidt interi\\u00f8r\",\"group\":\"INTERIOR\",\"long_name\":\"Sort og hvidt, delvist Premium-interi\\u00f8r\",\"name\":\"Sort og hvidt interi\\u00f8r\",\"price\":9500},{\"code\":\"$PBSB\",\"description\":\"Solid Black\",\"group\":\"PAINT\",\"long_name\":\"Solid Black lak\",\"name\":\"Solid Black\",\"price\":9500},{\"code\":\"$PRM30\",\"description\":\"<p>Opgrader dit interi\\u00f8r med ekstrafunktioner og premium-materialer.<\\/p><ul><li><span>Premium opvarmede s\\u00e6der og kabinematerialer inkl. tr\\u00e6-interi\\u00f8r, glastag og fire USB-porte<\\/span><\\/li><li><span>12-vejs automatisk justerbare fors\\u00e6der, rat og sidespejle, alle med tilpassede f\\u00f8rerprofiler<\\/span><\\/li><li><span>Premium lydsystem med mere kraft, diskanter, surround-h\\u00f8jtalere og subwoofer<\\/span><\\/li><\\/ul>\",\"group\":\"PREMIUM_PACKAGE\",\"long_name\":\"Delvist Premium-interi\\u00f8r\",\"name\":\"Delvist Premium-interi\\u00f8r\"},{\"code\":\"$SC04\",\"description\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"group\":\"SUPER_CHARGER\",\"long_name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\"},{\"code\":\"$MDL3\",\"description\":\"Leveringer vil blive p\\u00e5begyndt i USA baseret p\\u00e5 ordredato og evt. tilvalg.\",\"group\":\"MODEL\",\"long_name\":\"Model 3\",\"name\":\"Model 3\",\"price\":336000},{\"code\":\"$W40B\",\"description\":\"18\\\" Aero-f\\u00e6lge\",\"group\":\"WHEELS\",\"long_name\":\"18\\\" Aero-f\\u00e6lge\",\"name\":\"18\\\" Aero-f\\u00e6lge\"},{\"code\":\"$MT322\",\"description\":\"Model 3 baghjulstr\\u00e6k\",\"group\":\"TRIM\",\"long_name\":\"Model 3 baghjulstr\\u00e6k\",\"name\":\"Model 3 baghjulstr\\u00e6k\",\"price\":84810},{\"code\":\"$CPF0\",\"description\":\"Standard tilslutning\",\"group\":\"CONNECTIVITY\",\"long_name\":\"Standard tilslutning\",\"name\":\"Standard tilslutning\"},{\"code\":\"$RSF1\",\"description\":\"Opvarmede bags\\u00e6der\",\"group\":\"HEATED_SEAT\",\"long_name\":\"Opvarmede bags\\u00e6der\",\"name\":\"Opvarmede bags\\u00e6der\"},{\"code\":\"$CW03\",\"description\":\"Forbedr din k\\u00f8reoplevelse med opvarmede bags\\u00e6der og opvarmet rat.\",\"group\":\"COLD_WEATHER\",\"long_name\":\"Koldtvejrsfunktion\",\"name\":\"Koldtvejrsfunktion\"}],\"OptionCodeList\":\"$APBS,$DV2W,$IBW1,$PBSB,$PRM30,$SC04,$MDL3,$W40B,$MT322,$CPF0,$RSF1,$CW03\",\"OptionCodeListDisplayOnly\":null,\"OptionCodePricing\":[{\"code\":\"$MDL3\",\"group\":\"MODEL\",\"price\":336000},{\"code\":\"$APBS\",\"group\":\"AUTOPILOT_PACKAGE\",\"price\":0},{\"code\":\"$CPF0\",\"group\":\"CONNECTIVITY\",\"price\":0},{\"code\":\"$CW03\",\"group\":\"COLD_WEATHER\",\"price\":0},{\"code\":\"$DV2W\",\"group\":\"DRIVE_MODE\",\"price\":0},{\"code\":\"$IBW1\",\"group\":\"INTERIOR\",\"price\":9500},{\"code\":\"$MT322\",\"group\":\"TRIM\",\"price\":84810},{\"code\":\"$PRM30\",\"group\":\"PREMIUM_PACKAGE\",\"price\":0},{\"code\":\"$PBSB\",\"group\":\"PAINT\",\"price\":9500},{\"code\":\"$RSF1\",\"group\":\"HEATED_SEAT\",\"price\":0},{\"code\":\"$SC04\",\"group\":\"SUPER_CHARGER\",\"price\":0},{\"code\":\"$W40B\",\"group\":\"WHEELS\",\"price\":0}],\"OrderFee\":{\"type\":\"order_fee\",\"value\":0},\"OriginalDeliveryDate\":null,\"OriginalInCustomerGarageDate\":null,\"PAINT\":[\"BLACK\"],\"PlannedGADailyDate\":\"2022-10-26T00:00:00\",\"Price\":420890,\"PurchasePrice\":416710,\"ROOF\":null,\"SalesMetro\":\"Copenhagen\",\"StateProvince\":null,\"StateProvinceLongName\":null,\"TRIM\":[\"M3RWD\"],\"TaxScheme\":null,\"ThirdPartyHistoryUrl\":null,\"TitleStatus\":\"NEW\",\"TitleSubtype\":[\"DEMO\"],\"TotalPrice\":443990,\"TradeInType\":null,\"TransportFees\":{\"exemptVRL\":[],\"fees\":[],\"metro_fees\":[]},\"TrimCode\":\"$MT322\",\"TrimName\":\"Model 3 baghjulstr\\u00e6k\",\"Trt\":14850,\"TrtName\":\"Greve\",\"VIN\":\"LRW3286_346569fe7bb7491727dddcd4adfa394d\",\"Variant\":null,\"VehicleRegion\":null,\"VrlName\":\"Greve\",\"WHEELS\":[\"EIGHTEEN\"],\"WarrantyBatteryExpDate\":\"2030-12-15T13:50:05Z\",\"WarrantyBatteryIsExpired\":false,\"WarrantyBatteryMile\":160000,\"WarrantyBatteryYear\":8,\"WarrantyData\":{\"UsedVehicleLimitedWarrantyMile\":0,\"UsedVehicleLimitedWarrantyYear\":0,\"WarrantyBatteryExpDate\":\"2030-12-15T13:50:05Z\",\"WarrantyBatteryIsExpired\":false,\"WarrantyBatteryMile\":160000,\"WarrantyBatteryYear\":8,\"WarrantyDriveUnitExpDate\":\"2030-12-15T13:50:05Z\",\"WarrantyDriveUnitMile\":160000,\"WarrantyDriveUnitYear\":8,\"WarrantyMile\":80000,\"WarrantyVehicleExpDate\":\"2026-12-15T13:50:05Z\",\"WarrantyVehicleIsExpired\":false,\"WarrantyYear\":4},\"WarrantyDriveUnitExpDate\":\"2030-12-15T13:50:05Z\",\"WarrantyDriveUnitMile\":160000,\"WarrantyDriveUnitYear\":8,\"WarrantyMile\":80000,\"WarrantyVehicleExpDate\":\"2026-12-15T13:50:05Z\",\"WarrantyVehicleIsExpired\":false,\"WarrantyYear\":4,\"Year\":2023,\"UsedVehicleLimitedWarrantyMile\":0,\"UsedVehicleLimitedWarrantyYear\":0,\"OdometerTypeShort\":\"km\",\"DeliveryDateDisplay\":true,\"Hash\":\"286_346569fe7bb7491727dddcd4adfa394d\",\"Odometer\":10,\"VrlLocks\":[14850],\"OptionCodeSpecs\":{\"C_SPECS\":{\"code\":\"C_SPECS\",\"name\":\"Specs\",\"options\":[{\"code\":\"SPECS_ACCELERATION\",\"name\":\"6,1 s\",\"long_name\":\"6,1 sekunder\",\"description\":\"0-100 km\\/t\"},{\"code\":\"SPECS_TOP_SPEED\",\"name\":\"225 km\\/t\",\"long_name\":\"225 kilometer i timen\",\"description\":\"Tophastighed\"},{\"code\":\"SPECS_RANGE\",\"name\":\"491 km\",\"long_name\":\"491 kilometer\",\"description\":\"r\\u00e6kkevidde (WLTP)\"}]},\"C_DESIGN\":{\"code\":\"C_DESIGN\",\"name\":\"Design\",\"options\":[]},\"C_CALLOUTS\":{\"code\":\"C_CALLOUTS\",\"name\":\"Callout Features\",\"options\":[{\"code\":\"$APBS\",\"name\":\"Autopilot\",\"long_name\":\"\",\"description\":\"Autopilot\",\"group\":\"AUTOPILOT\"},{\"code\":\"PCT1M\",\"name\":\"30 dage pr\\u00f8veperiode med Premium Connectivity\",\"description\":\"30 dage pr\\u00f8veperiode med Premium Connectivity\",\"group\":\"CONNECTIVITY\",\"list\":[\"satelliteMaps\",\"musicStreaming\",\"videoStreaming\",\"caraoke\",\"internetBrowser\"],\"period\":\"30 dage\"}]},\"C_OPTS\":{\"code\":\"C_OPTS\",\"name\":\"Options\",\"options\":[{\"code\":\"$PBSB\",\"name\":\"Solid Black lak\",\"long_name\":\"\",\"description\":\"Solid Black\"},{\"code\":\"$W40B\",\"name\":\"18\\\" Aero-f\\u00e6lge\",\"long_name\":\"\",\"description\":\"18\\\" Aero-f\\u00e6lge\"},{\"code\":\"$IBW1\",\"name\":\"Sort og hvidt, delvist Premium-interi\\u00f8r\",\"long_name\":\"\",\"description\":\"Sort og hvidt interi\\u00f8r\"},{\"code\":\"$APBS\",\"name\":\"Autopilot\",\"long_name\":\"\",\"description\":\"Autopilot\"},{\"code\":\"$RSF1\",\"name\":\"Opvarmede for- og bags\\u00e6der\",\"long_name\":\"\",\"description\":\"Opvarmede bags\\u00e6der\"}]}},\"CompositorViewsCustom\":{\"isProductWithCustomViews\":false,\"externalZoom\":{\"order\":1,\"search\":1},\"externalCrop\":{\"order\":\"1400,850,250,150\",\"search\":\"1400,850,300,130\"}},\"IsRangeStandard\":true,\"MetroName\":\"Copenhagen\",\"geoPoints\":[[\"55.586975,12.250996\",14850]],\"HasMarketingOptions\":true,\"IsFactoryGated\":true},{\"InTransit\":false,\"ADL_OPTS\":[\"REAR_HEATED_SEATS\"],\"AUTOPILOT\":[\"AUTOPILOT\"],\"AcquisitionSubType\":null,\"AcquisitionType\":null,\"ActualGAInDate\":\"2022-10-20T06:07:01.000000\",\"AlternateCurrency\":[],\"BATTERY\":null,\"CABIN_CONFIG\":null,\"CPORefurbishmentStatus\":null,\"CPOWarrantyType\":null,\"City\":\"Aarhus\",\"CompositorViews\":{\"frontView\":\"STUD_3QTR\",\"sideView\":\"SIDE\",\"interiorView\":\"STUD_SEAT\"},\"CountryCode\":\"DK\",\"CountryCodes\":[\"PT\",\"RO\",\"SI\",\"SK\",\"IT\",\"AT\",\"CH\",\"CZ\",\"ES\",\"GR\",\"HR\",\"IS\",\"LV\",\"NL\",\"NO\",\"DK\",\"FR\",\"LT\",\"PL\",\"SE\",\"BE\",\"DE\",\"EE\",\"FI\",\"HU\",\"LU\"],\"CountryOfOrigin\":\"DK\",\"CurrencyCode\":\"DKK\",\"CurrencyCodes\":\"DKK\",\"DECOR\":null,\"DRIVE\":null,\"DamageDisclosureStatus\":\"PendingReview\",\"DestinationHandlingFee\":0,\"Discount\":23300,\"DisplayWarranty\":false,\"EtaToCurrent\":\"2022-12-03T11:00:00\",\"FactoryCode\":\"GF03\",\"FactoryDepartureDate\":\"2022-10-20T13:18:29\",\"FixedAssets\":false,\"FlexibleOptionsData\":[{\"code\":\"$APF2\",\"description\":\"Fuldt selvk\\u00f8rende egenskaber\",\"group\":\"AUTOPILOT\",\"long_name\":\"Fuldt selvk\\u00f8rende egenskaber\",\"name\":\"Fuldt selvk\\u00f8rende egenskaber\",\"price\":58300}],\"ForecastedFactoryGatedDate\":null,\"HEADLINER\":null,\"HasDamagePhotos\":false,\"HasOptionCodeData\":true,\"INTERIOR\":[\"PREMIUM_BLACK\"],\"InspectionDocumentGuid\":null,\"InventoryPrice\":484312,\"IsChargingConnectorIncluded\":true,\"IsDemo\":true,\"IsLegacy\":false,\"IsPreProdWithDisclaimer\":false,\"IsTegra\":false,\"Language\":\"da\",\"Languages\":[\"da\"],\"LeaseAvailabilityDate\":null,\"LexiconDefaultOptions\":[{\"code\":\"$CPF0\",\"description\":\"Standard tilslutning\",\"group\":\"CONNECTIVITY\",\"long_name\":\"Standard tilslutning\",\"name\":\"Standard tilslutning\"},{\"code\":\"$SC04\",\"description\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"group\":\"SUPER_CHARGER\",\"long_name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\"}],\"ListingType\":\"public\",\"ListingTypes\":\"public\",\"ManufacturingOptionCodeList\":\"AD15,AF00,APFB,APH4,AU3P,BC3B,BT43,CDM0,CH15,CODK,DRLH,DV4W,FC02,FG31,FM3B,GLCN,HL32,HM30,ID3W,IL31,LT03,MDL3,MR31,PMNG,PC30,REEU,RF3G,RS3H,S3PB,SA3P,SC04,STCP,SU3C,T3HS,TM00,TW00,UT3P,W41B,WR00,ZINV,MI02,PL30,SLR0,ST33,BG31,OSSB,AUF1,RSF1,ILF1,FGF1,CPF0,PW01,HP31,PT01,RL32,LVB2,FD01,RD05,SWF1,PP02,RA00,VC01,LS01,SG01,US00\",\"MarketingInUseDate\":\"2022-12-15T12:00:26\",\"Model\":\"m3\",\"OdometerType\":\"Km\",\"OnConfiguratorPricePercentage\":112,\"OptionCodeData\":[{\"acceleration_unit_long\":\"kilometer i timen\",\"acceleration_unit_short\":\"km\\/t\",\"acceleration_value\":\"0-100\",\"code\":\"$MT328\",\"group\":\"SPECS_ACCELERATION\",\"price\":136600,\"unit_long\":\"sekunder\",\"unit_short\":\"s\",\"value\":\"4.4\"},{\"code\":\"$MT328\",\"group\":\"SPECS_TOP_SPEED\",\"price\":136600,\"top_speed_label\":\"Tophastighed\",\"unit_long\":\"kilometer i timen\",\"unit_short\":\"km\\/t\",\"value\":\"233\"},{\"code\":\"$MT328\",\"group\":\"SPECS_RANGE\",\"price\":136600,\"range_label_source\":\"R\\u00e6kkevidde (WLTP)\",\"range_source\":\"WLTP\",\"range_source_inventory_new\":\"WLTP\",\"unit_long\":\"kilometer\",\"unit_short\":\"km\",\"value\":\"602\"},{\"code\":\"$APBS\",\"description\":\"Autopilot\",\"group\":\"AUTOPILOT\",\"long_name\":\"Autopilot\",\"name\":\"Autopilot\"},{\"code\":\"$DV4W\",\"description\":\"Dual Motor firehjulstr\\u00e6k\",\"group\":\"DRIVE_MODE\",\"long_name\":\"Dual Motor firehjulstr\\u00e6k\",\"name\":\"Dual Motor firehjulstr\\u00e6k\"},{\"code\":\"$IPB1\",\"description\":\"Sort Premium-interi\\u00f8r\",\"group\":\"INTERIOR\",\"long_name\":\"Sort Premium-interi\\u00f8r\",\"name\":\"Sort Premium-interi\\u00f8r\"},{\"code\":\"$PMNG\",\"description\":\"Midnight Silver Metallic\",\"group\":\"PAINT\",\"long_name\":\"Midnight Silver Metallic lak\",\"name\":\"Midnight Silver Metallic\",\"price\":12500},{\"code\":\"$PRM31\",\"description\":\"<p>Opgrader dit interi\\u00f8r med ekstrafunktioner og premium-materialer.<\\/p><ul><li><span>Premium opvarmede s\\u00e6der og kabinematerialer inkl. tr\\u00e6-interi\\u00f8r, glastag og fire USB-porte<\\/span><\\/li><li><span>12-vejs automatisk justerbare fors\\u00e6der, rat og sidespejle, alle med tilpassede f\\u00f8rerprofiler<\\/span><\\/li><li><span>Premium lydsystem med mere kraft, diskanter, surround-h\\u00f8jtalere og subwoofer<\\/span><\\/li><\\/ul>\",\"group\":\"PREMIUM_PACKAGE\",\"long_name\":\"Premium-interi\\u00f8r\",\"name\":\"Premium-interi\\u00f8r\"},{\"code\":\"$SC04\",\"description\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"group\":\"SUPER_CHARGER\",\"long_name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\",\"name\":\"Adgang til Supercharging-netv\\u00e6rk + Pay-As-You-Go\"},{\"code\":\"$MDL3\",\"description\":\"Leveringer vil blive p\\u00e5begyndt i USA baseret p\\u00e5 ordredato og evt. tilvalg.\",\"group\":\"MODEL\",\"long_name\":\"Model 3\",\"name\":\"Model 3\",\"price\":336000},{\"code\":\"$W41B\",\"description\":\"19\\\" Sport-f\\u00e6lge\",\"group\":\"WHEELS\",\"long_name\":\"19\\\" Sport-f\\u00e6lge\",\"name\":\"19\\\" Sport-f\\u00e6lge\",\"price\":13500},{\"code\":\"$MT328\",\"description\":\"Model 3 Long Range AWD\",\"group\":\"TRIM\",\"long_name\":\"Model 3 Long Range Dual Motor firehjulstr\\u00e6k\",\"name\":\"Long Range firehjulstr\\u00e6k\",\"price\":136600},{\"code\":\"$CPF0\",\"description\":\"Standard tilslutning\",\"group\":\"CONNECTIVITY\",\"long_name\":\"Standard tilslutning\",\"name\":\"Standard tilslutning\"},{\"code\":\"$RSF1\",\"description\":\"Opvarmede bags\\u00e6der\",\"group\":\"HEATED_SEAT\",\"long_name\":\"Opvarmede bags\\u00e6der\",\"name\":\"Opvarmede bags\\u00e6der\"},{\"code\":\"$CW03\",\"description\":\"Forbedr din k\\u00f8reoplevelse med opvarmede bags\\u00e6der og opvarmet rat.\",\"group\":\"COLD_WEATHER\",\"long_name\":\"Koldtvejrsfunktion\",\"name\":\"Koldtvejrsfunktion\"}],\"OptionCodeList\":\"$APBS,$DV4W,$IPB1,$PMNG,$PRM31,$SC04,$MDL3,$W41B,$MT328,$CPF0,$RSF1,$CW03\",\"OptionCodeListDisplayOnly\":null,\"OptionCodePricing\":[{\"code\":\"$MDL3\",\"group\":\"MODEL\",\"price\":336000},{\"code\":\"$APBS\",\"group\":\"AUTOPILOT_PACKAGE\",\"price\":0},{\"code\":\"$CPF0\",\"group\":\"CONNECTIVITY\",\"price\":0},{\"code\":\"$CW03\",\"group\":\"COLD_WEATHER\",\"price\":0},{\"code\":\"$DV4W\",\"group\":\"DRIVE_MODE\",\"price\":0},{\"code\":\"$IPB1\",\"group\":\"INTERIOR\",\"price\":0},{\"code\":\"$MT328\",\"group\":\"TRIM\",\"price\":136600},{\"code\":\"$PRM31\",\"group\":\"PREMIUM_PACKAGE\",\"price\":0},{\"code\":\"$PMNG\",\"group\":\"PAINT\",\"price\":12500},{\"code\":\"$RSF1\",\"group\":\"HEATED_SEAT\",\"price\":0},{\"code\":\"$SC04\",\"group\":\"SUPER_CHARGER\",\"price\":0},{\"code\":\"$W41B\",\"group\":\"WHEELS\",\"price\":13500}],\"OrderFee\":{\"type\":\"order_fee\",\"value\":0},\"OriginalDeliveryDate\":null,\"OriginalInCustomerGarageDate\":null,\"PAINT\":[\"GRAY\"],\"PlannedGADailyDate\":\"2022-10-20T00:00:00\",\"Price\":484312,\"PurchasePrice\":475300,\"ROOF\":null,\"SalesMetro\":\"Aarhus\",\"StateProvince\":null,\"StateProvinceLongName\":null,\"TRIM\":[\"LRAWD\"],\"TaxScheme\":null,\"ThirdPartyHistoryUrl\":null,\"TitleStatus\":\"NEW\",\"TitleSubtype\":[\"DEMO\"],\"TotalPrice\":507612,\"TradeInType\":null,\"TransportFees\":{\"exemptVRL\":[],\"fees\":[],\"metro_fees\":[]},\"TrimCode\":\"$MT328\",\"TrimName\":\"Model 3 Long Range Dual Motor firehjulstr\\u00e6k\",\"Trt\":11814,\"TrtName\":\"Bielefeld\",\"VIN\":\"LRW3242_58ceb36387f756a96ad968078dfe5d94\",\"Variant\":null,\"VehicleRegion\":null,\"VrlName\":\"Aarhus\",\"WHEELS\":[\"NINETEEN\"],\"WarrantyBatteryExpDate\":\"2030-12-15T12:00:26Z\",\"WarrantyBatteryIsExpired\":false,\"WarrantyBatteryMile\":192000,\"WarrantyBatteryYear\":8,\"WarrantyData\":{\"UsedVehicleLimitedWarrantyMile\":0,\"UsedVehicleLimitedWarrantyYear\":0,\"WarrantyBatteryExpDate\":\"2030-12-15T12:00:26Z\",\"WarrantyBatteryIsExpired\":false,\"WarrantyBatteryMile\":192000,\"WarrantyBatteryYear\":8,\"WarrantyDriveUnitExpDate\":\"2030-12-15T12:00:26Z\",\"WarrantyDriveUnitMile\":192000,\"WarrantyDriveUnitYear\":8,\"WarrantyMile\":80000,\"WarrantyVehicleExpDate\":\"2026-12-15T12:00:26Z\",\"WarrantyVehicleIsExpired\":false,\"WarrantyYear\":4},\"WarrantyDriveUnitExpDate\":\"2030-12-15T12:00:26Z\",\"WarrantyDriveUnitMile\":192000,\"WarrantyDriveUnitYear\":8,\"WarrantyMile\":80000,\"WarrantyVehicleExpDate\":\"2026-12-15T12:00:26Z\",\"WarrantyVehicleIsExpired\":false,\"WarrantyYear\":4,\"Year\":2023,\"UsedVehicleLimitedWarrantyMile\":0,\"UsedVehicleLimitedWarrantyYear\":0,\"OdometerTypeShort\":\"km\",\"DeliveryDateDisplay\":true,\"Hash\":\"242_58ceb36387f756a96ad968078dfe5d94\",\"Odometer\":10,\"VrlLocks\":[326],\"OptionCodeSpecs\":{\"C_SPECS\":{\"code\":\"C_SPECS\",\"name\":\"Specs\",\"options\":[{\"code\":\"SPECS_ACCELERATION\",\"name\":\"4,4 s\",\"long_name\":\"4,4 sekunder\",\"description\":\"0-100 km\\/t\"},{\"code\":\"SPECS_TOP_SPEED\",\"name\":\"233 km\\/t\",\"long_name\":\"233 kilometer i timen\",\"description\":\"Tophastighed\"},{\"code\":\"SPECS_RANGE\",\"name\":\"602 km\",\"long_name\":\"602 kilometer\",\"description\":\"r\\u00e6kkevidde (WLTP)\"}]},\"C_DESIGN\":{\"code\":\"C_DESIGN\",\"name\":\"Design\",\"options\":[]},\"C_CALLOUTS\":{\"code\":\"C_CALLOUTS\",\"name\":\"Callout Features\",\"options\":[{\"code\":\"$APBS\",\"name\":\"Autopilot\",\"long_name\":\"\",\"description\":\"Autopilot\",\"group\":\"AUTOPILOT\"},{\"code\":\"PCT1M\",\"name\":\"30 dage pr\\u00f8veperiode med Premium Connectivity\",\"description\":\"30 dage pr\\u00f8veperiode med Premium Connectivity\",\"group\":\"CONNECTIVITY\",\"list\":[\"satelliteMaps\",\"musicStreaming\",\"videoStreaming\",\"caraoke\",\"internetBrowser\"],\"period\":\"30 dage\"}]},\"C_OPTS\":{\"code\":\"C_OPTS\",\"name\":\"Options\",\"options\":[{\"code\":\"$PMNG\",\"name\":\"Midnight Silver Metallic lak\",\"long_name\":\"\",\"description\":\"Midnight Silver Metallic\"},{\"code\":\"$W41B\",\"name\":\"19\\\" Sport-f\\u00e6lge\",\"long_name\":\"\",\"description\":\"19\\\" Sport-f\\u00e6lge\"},{\"code\":\"$IPB1\",\"name\":\"Sort Premium-interi\\u00f8r\",\"long_name\":\"\",\"description\":\"Sort Premium-interi\\u00f8r\"},{\"code\":\"$APBS\",\"name\":\"Autopilot\",\"long_name\":\"\",\"description\":\"Autopilot\"},{\"code\":\"$RSF1\",\"name\":\"Opvarmede for- og bags\\u00e6der\",\"long_name\":\"\",\"description\":\"Opvarmede bags\\u00e6der\"}]}},\"CompositorViewsCustom\":{\"isProductWithCustomViews\":false,\"externalZoom\":{\"order\":1,\"search\":1},\"externalCrop\":{\"order\":\"1400,850,250,150\",\"search\":\"1400,850,300,130\"}},\"IsRangeStandard\":true,\"MetroName\":\"Aarhus\",\"geoPoints\":[[\"56.178032,10.139133\",326]],\"HasMarketingOptions\":true,\"IsFactoryGated\":true}],\"total_matches_found\":\"3\"}";
        List<TeslaCar> cars = new LinkedList<>();

        TeslaInventoryGrepper grepper = new TeslaInventoryGrepper();
        grepper.parse("new", str, cars);
        for(Iterator<TeslaCar> it = cars.iterator(); it.hasNext();) {
            TeslaCar car = (TeslaCar)it.next();
            System.out.println(car);
        }
    }
}
