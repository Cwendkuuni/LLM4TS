package com.example.junit4; // replace with your package name if needed

import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

public class GeoQueryTest {

    @org.junit.Test
    public void testGeoQueryInstanceWithLocation() {
        GeoLocation location = new GeoLocation(1.0D, 2.0D);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(location, actualLocation);
        assertNull(geoQuery.getIp());
    }

    @org.junit.Test
    public void testGeoQueryInstanceWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }

    @org.junit.Test
    public void testGettersAndSetters() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(3.0D, 4.0D));

        String accuracy = "10m";
        String granularity = GeoQuery.CITY;
        int maxResults = 5;

        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @org.junit.Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(5.0D, 6.0D);
        GeoQuery geoQuery = new GeoQuery(location);

        geoQuery.setAccuracy("10m");
        geoQuery.setGranularity(GeoQuery.NEIGHBORHOOD);
        geoQuery.setMaxResults(10);

        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("lat", "5.0"));
        expectedParams.add(new HttpParameter("long", "6.0"));
        expectedParams.add(new HttpParameter("accuracy", "10m"));
        expectedParams.add(new HttpParameter("granularity", "neighborhood"));
        expectedParams.add(new HttpParameter("max_results", "10"));

        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParams.toArray(new HttpParameter[0]), actualParams);
    }

    @org.junit.Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0D, 2.0D);
        GeoLocation location2 = new GeoLocation(1.0D, 2.0D);

        String ip = "192.168.0.1";
        String accuracy = "10m";
        String granularity = GeoQuery.CITY;
        int maxResults = 5;

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location1);
        GeoQuery geoQuery3 = new GeoQuery(ip);
        GeoQuery geoQuery4 = new GeoQuery(location2);
        GeoQuery geoQuery5 = new GeoQuery();
        GeoQuery geoQuery6 = new GeoQuery(location1);

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
        geoQuery3.setIp(ip);
        geoQuery3.setLocation(location1);

        geoQuery4.setAccuracy(accuracy);
        geoQuery4.setGranularity(granularity);
        geoQuery4.setMaxResults(maxResults);
        geoQuery4.setIp(null);
        geoQuery4.setLocation(new GeoLocation(1.0D, 2.0D));

        geoQuery5.setAccuracy(accuracy);
        geoQuery5.setGranularity(granularity);
        geoQuery5.setMaxResults(maxResults);
        geoQuery5.setIp(null);
        geoQuery5.setLocation(null);

        geoQuery6.setAccuracy(accuracy);
        geoQuery6.setGranularity(granularity);
        geoQuery6.setMaxResults(maxResults);

        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));
        assertTrue(geoQuery1.equals(geoQuery3));
        assertFalse(geoQuery1.equals(geoQuery4));
        assertFalse(geoQuery1.equals(geoQuery5));
        assertTrue(geoQuery1.equals(geoQuery6));
    }

    @org.junit.Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0D, 2.0D);
        GeoLocation location2 = new GeoLocation(1.0D, 2.0D);

        String ip = "192.168.0.1";
        String accuracy = "10m";
        String granularity = GeoQuery.CITY;
        int maxResults = 5;

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location1);
        GeoQuery geoQuery3 = new GeoQuery(ip);
        GeoQuery geoQuery4 = new GeoQuery(location2);
        GeoQuery geoQuery5 = new GeoQuery();
        GeoQuery geoQuery6 = new GeoQuery(location1);

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
        geoQuery3.setIp(ip);
        geoQuery3.setLocation(location1);

        geoQuery4.setAccuracy(accuracy);
        geoQuery4.setGranularity(granularity);
        geoQuery4.setMaxResults(maxResults);
        geoQuery4.setIp(null);
        geoQuery4.setLocation(new GeoLocation(1.0D, 2.0D));

        geoQuery5.setAccuracy(accuracy);
        geoQuery5.setGranularity(granularity);
        geoQuery5.setMaxResults(maxResults);
        geoQuery5.setIp(null);
        geoQuery5.setLocation(null);

        geoQuery6.setAccuracy(accuracy);
        geoQuery6.setGranularity(granularity);
        geoQuery6.setMaxResults(maxResults);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery1.hashCode();
        int hashCode3 = geoQuery3.hashCode();
        int hashCode4 = geoQuery4.hashCode();
        int hashCode5 = geoQuery5.hashCode();
        int hashCode6 = geoQuery6.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode4);
        assertNotSame(hashCode1, hashCode5);
        assertEquals(hashCode1, hashCode6);
    }

    @org.junit.Test
    public void testToString() {
        GeoLocation location = new GeoLocation(7.0D, 8.0D);
        GeoQuery geoQuery = new GeoQuery(location);

        String accuracy = "10m";
        String granularity = GeoQuery.NEIGHBORHOOD;
        int maxResults = 10;

        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        String expectedString = "GeoQuery{location=GeoLocation{latitude=7.0, longitude=8.0}, ip='', accuracy='10m', granularity='neighborhood', maxResults=10}";
        String actualString = geoQuery.toString();

        assertEquals(expectedString, actualString);
    }
}