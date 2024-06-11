package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeoQueryTest {

    @org.junit.Test
    public void testNewInstanceWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
    }

    @org.junit.Test
    public void testNewInstanceWithIp() {
        String ip = "192.168.0.1";
        GeoQuery query = new GeoQuery(ip);

        assertNull(query.getLocation());
        assertEquals(ip, query.getIp());
    }

    @org.junit.Test
    public void testGettersAndSetters() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        query.setAccuracy("10m");
        query.setGranularity("city");
        query.setMaxResults(10);

        assertEquals("10m", query.getAccuracy());
        assertEquals("city", query.getGranularity());
        assertEquals(10, query.getMaxResults());
    }

    @org.junit.Test
    public void testAppendParameter() {
        GeoQuery query = new GeoQuery(null);

        List<HttpParameter> params = Arrays.asList(
                new HttpParameter("lat", "1.0"),
                new HttpParameter("long", "2.0"),
                new HttpParameter("accuracy", "10m"),
                new HttpParameter("granularity", "city"),
                new HttpParameter("max_results", "10")
        );

        HttpParameter[] expectedParams = new HttpParameter[params.size()];
        for (int i = 0; i < params.size(); i++) {
            expectedParams[i] = params.get(i);
        }

        query.appendParameter("lat", 1.0, null);
        query.appendParameter("long", 2.0, null);
        query.appendParameter("accuracy", "10m", null);
        query.appendParameter("granularity", "city", null);
        query.appendParameter("max_results", 10, null);

        HttpParameter[] actualParams = query.asHttpParameterArray();

        assertArrayEquals(expectedParams, actualParams);
    }

    @org.junit.Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);

        query3 = new GeoQuery(location1);
        query4 = new GeoQuery(location2);

        GeoQuery query5 = new GeoQuery();

        assertTrue(query1.equals(query1));
        assertTrue(query1.equals(query3));
        assertTrue(query3.equals(query1));
        assertTrue(query1.equals(query2));
        assertTrue(query2.equals(query1));

        assertFalse(query1.equals(query5));
        assertFalse(query5.equals(query1));
    }

    @org.junit.Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);

        int hashCode1 = query1.hashCode();
        int hashCode2 = query2.hashCode();

        GeoQuery query3 = new GeoQuery(location1);
        int hashCode3 = query3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }

    @org.junit.Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        String expectedString = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='', maxResults=0}";
        String actualString = query.toString();

        assertEquals(expectedString, actualString);
    }
}