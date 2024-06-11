package test;

import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        double latitude = 37.4219983;
        double longitude = -122.0840677;
        GeoLocation location = new GeoLocation(latitude, longitude);
        GeoQuery geoQuery = new GeoQuery(location);

        List<HttpParameter> expectedParams = Arrays.asList(
                new HttpParameter("lat", String.valueOf(latitude)),
                new HttpParameter("long", String.valueOf(longitude))
        );

        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();

        assertEquals("Number of parameters does not match", expectedParams.size(), actualParams.length);

        for (int i = 0; i < expectedParams.size(); i++) {
            assertEquals("Parameter name does not match", expectedParams.get(i).getName(), actualParams[i].getName());
            assertEquals("Parameter value does not match", expectedParams.get(i).getValue(), actualParams[i].getValue());
        }
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        List<HttpParameter> expectedParams = Arrays.asList(new HttpParameter("ip", ip));

        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();

        assertEquals("Number of parameters does not match", expectedParams.size(), actualParams.length);

        for (HttpParameter param : expectedParams) {
            HttpParameter actualParam = Arrays.stream(actualParams)
                    .filter(p -> p.getName().equals(param.getName()))
                    .findFirst()
                    .get();
            assertEquals("Parameter value does not match", param.getValue(), actualParam.getValue());
        }
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 1.0));
        String accuracy = "10km";
        String granularity = "city";
        int maxResults = 5;

        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        assertEquals("Incorrect accuracy value", accuracy, geoQuery.getAccuracy());
        assertEquals("Incorrect granularity value", granularity, geoQuery.getGranularity());
        assertEquals("Incorrect maxResults value", maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 1.0);
        GeoLocation location2 = new GeoLocation(1.0, 1.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoLocation location3 = new GeoLocation(2.0, 2.0);
        GeoQuery geoQuery3 = new GeoQuery(location3);

        String ip = "192.168.1.1";
        GeoQuery geoQuery4 = new GeoQuery(ip);

        List<HttpParameter> params1 = Arrays.asList(
                new HttpParameter("lat", "1.0"),
                new HttpParameter("long", "1.0")
        );
        List<HttpParameter> params2 = Arrays.asList(
                new HttpParameter("lat", "1.0"),
                new HttpParameter("long", "1.0")
        );

        List<HttpParameter> params3 = Arrays.asList(
                new HttpParameter("ip", ip)
        );

        assertTrue("GeoQueries with the same location should be equal", geoQuery1.equals(geoQuery2));
        assertTrue("GeoQueries with the same IP should be equal", geoQuery4.equals(geoQuery4));
        assertFalse("GeoQueries with different locations should not be equal", geoQuery1.equals(geoQuery3));
        assertFalse("GeoQueries with different IPs should not be equal", geoQuery1.equals(geoQuery4));
        assertFalse("GeoQueries with different parameters should not be equal", geoQuery1.equals(new Object()));
        assertEquals("Hashcodes of equal objects should be equal", geoQuery1.hashCode(), geoQuery2.hashCode());
        assertNotEquals("Hashcodes of different objects should be different", geoQuery1.hashCode(), geoQuery3.hashCode());
        assertNotEquals("Hashcodes of different objects should be different", geoQuery1.hashCode(), new Object().hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 1.0);
        String expectedString = "GeoQuery{location=" + location + ", ip='' city='' maxResults=0, accuracy='', granularity=''}"
                + System.lineSeparator();

        GeoQuery geoQuery = new GeoQuery(location);

        String actualString = geoQuery.toString();

        assertEquals("ToString method does not return the correct string", expectedString, actualString);
    }
}