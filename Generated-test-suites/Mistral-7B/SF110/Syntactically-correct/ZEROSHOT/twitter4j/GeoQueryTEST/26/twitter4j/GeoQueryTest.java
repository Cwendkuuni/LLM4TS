package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeoQueryTest {

    @Test
    public void testGeoQueryInstanceWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        assertEquals(expectedLocation, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testGeoQueryInstanceWithIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGettersAndSetters() {
        String accuracy = "1km";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testSettersReturnSelf() {
        String accuracy = "1km";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery expectedGeoQuery = geoQuery.accuracy(accuracy).granularity(granularity).maxResults(maxResults);

        assertEquals(expectedGeoQuery, geoQuery);
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy("1km");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        List<HttpParameter> expectedParams = Arrays.asList(
                new HttpParameter("lat", "1.0"),
                new HttpParameter("long", "2.0"),
                new HttpParameter("accuracy", "1km"),
                new HttpParameter("granularity", "city"),
                new HttpParameter("max_results", "10")
        );

        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        List<HttpParameter> actualParamsList = Arrays.asList(actualParams);

        assertEquals(expectedParams, actualParamsList);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoLocation location3 = new GeoLocation(2.0, 3.0);
        String ip = "127.0.0.1";
        String accuracy = "1km";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(ip);
        GeoQuery geoQuery4 = new GeoQuery(location1);
        GeoQuery geoQuery5 = new GeoQuery();

        geoQuery1.setAccuracy(accuracy);
        geoQuery1.setGranularity(granularity);
        geoQuery1.setMaxResults(maxResults);

        geoQuery2.setAccuracy(accuracy);
        geoQuery2.setGranularity(granularity);
        geoQuery2.setMaxResults(maxResults);
        geoQuery2.setIp(ip);

        geoQuery3.setAccuracy(accuracy);
        geoQuery3.setGranularity(granularity);
        geoQuery3.setMaxResults(maxResults);
        geoQuery3.setLocation(location1);

        geoQuery4.setAccuracy(accuracy);
        geoQuery4.setGranularity(granularity);
        geoQuery4.setMaxResults(maxResults);

        // Test equal objects
        assertTrue(geoQuery1.equals(geoQuery4));
        assertTrue(geoQuery1.equals(geoQuery2));
        assertTrue(geoQuery1.equals(geoQuery3));

        // Test not equal objects
        assertFalse(geoQuery1.equals(geoQuery5));
        assertFalse(geoQuery1.equals(new Object()));
        assertFalse(geoQuery1.equals(null));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoLocation location3 = new GeoLocation(2.0, 3.0);
        String ip = "127.0.0.1";
        String accuracy = "1km";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(ip);
        GeoQuery geoQuery4 = new GeoQuery(location1);
        GeoQuery geoQuery5 = new GeoQuery();

        geoQuery1.setAccuracy(accuracy);
        geoQuery1.setGranularity(granularity);
        geoQuery1.setMaxResults(maxResults);

        geoQuery2.setAccuracy(accuracy);
        geoQuery2.setGranularity(granularity);
        geoQuery2.setMaxResults(maxResults);
        geoQuery2.setIp(ip);

        geoQuery3.setAccuracy(accuracy);
        geoQuery3.setGranularity(granularity);
        geoQuery3.setMaxResults(maxResults);
        geoQuery3.setLocation(location1);

        geoQuery4.setAccuracy(accuracy);
        geoQuery4.setGranularity(granularity);
        geoQuery4.setMaxResults(maxResults);

        int expectedHashCode1 = location1.hashCode() + ip.hashCode() + accuracy.hashCode().hashCode() + granularity.hashCode().hashCode() + maxResults;
        int expectedHashCode2 = location1.hashCode() + location2.hashCode() + accuracy.hashCode().hashCode() + granularity.hashCode().hashCode() + maxResults;
        int expectedHashCode3 = location1.hashCode() + ip.hashCode() + accuracy.hashCode().hashCode() + granularity.hashCode().hashCode() + maxResults;
        int expectedHashCode4 = location1.hashCode() + location2.hashCode() + accuracy.hashCode().hashCode() + granularity.hashCode().hashCode() + maxResults;
        int expectedHashCode5 = 0;

        int actualHashCode1 = geoQuery1.hashCode();
        int actualHashCode2 = geoQuery2.hashCode();
        int actualHashCode3 = geoQuery3.hashCode();
        int actualHashCode4 = geoQuery4.hashCode();
        int actualHashCode5 = geoQuery5.hashCode();

        assertEquals(expectedHashCode1, actualHashCode1);
        assertEquals(expectedHashCode2, actualHashCode2);
        assertEquals(expectedHashCode3, actualHashCode3);
        assertEquals(expectedHashCode4, actualHashCode4);
        assertEquals(expectedHashCode5, actualHashCode5);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        String ip = "127.0.0.1";
        String accuracy = "1km";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);
        geoQuery.setIp(ip);

        String expectedString = "GeoQuery{location=" + location + ", ip='" + ip + "\''"
                + ", accuracy='" + accuracy + "\''"
                + ", granularity='" + granularity + "\''"
                + ", maxResults=" + maxResults + "}";

        String actualString = geoQuery.toString();

        assertEquals(expectedString, actualString);
    }
}