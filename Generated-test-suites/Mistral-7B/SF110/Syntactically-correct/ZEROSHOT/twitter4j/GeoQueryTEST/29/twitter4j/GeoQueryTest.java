package com.example.junit4; // replace with your package name if needed

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        HttpParameter[] params = query.asHttpParameterArray();
        assertEquals(5, params.length); // check number of parameters
        assertEquals("lat", params[0].getName());
        assertEquals("1.0", params[0].getValue());
        assertEquals("long", params[1].getName());
        assertEquals("2.0", params[1].getValue());

        // add more tests for other getter methods if needed
    }

    @Test
    public void testGeoQueryWithIp() {
        String ip = "192.168.1.1";
        GeoQuery query = new GeoQuery(ip);

        HttpParameter[] params = query.asHttpParameterArray();
        assertEquals(4, params.length); // check number of parameters
        assertEquals("ip", params[0].getName());
        assertEquals(ip, params[0].getValue());

        // add more tests for other getter methods if needed
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery query = new GeoQuery(null);

        query.setLocation(new GeoLocation(3.0, 4.0));
        GeoLocation location = query.getLocation();
        assertEquals(3.0, location.getLatitude());
        assertEquals(4.0, location.getLongitude());

        query.setIp("10.0.0.1");
        String ip = query.getIp();
        assertEquals("10.0.0.1", ip);

        query.setAccuracy("10m");
        String accuracy = query.getAccuracy();
        assertEquals("10m", accuracy);

        query.setGranularity("city");
        String granularity = query.getGranularity();
        assertEquals("city", granularity);

        query.setMaxResults(50);
        int maxResults = query.getMaxResults();
        assertEquals(50, maxResults);

        query.accuracy("50m");
        accuracy = query.getAccuracy();
        assertEquals("50m", accuracy);

        query.granularity("neighborhood");
        granularity = query.getGranularity();
        assertEquals("neighborhood", granularity);

        query.maxResults(10);
        maxResults = query.getMaxResults();
        assertEquals(10, maxResults);
    }

    @Test
    public void testEqualsAndHashCode() {
        GeoQuery query1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery query2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery query3 = new GeoQuery(new GeoLocation(2.0, 2.0));

        assertTrue(query1.equals(query2));
        assertFalse(query1.equals(query3));

        int hashCode1 = query1.hashCode();
        int hashCode2 = query2.hashCode();
        int hashCode3 = query3.hashCode();

        // equals objects should have the same hashCode
        assertEquals(hashCode1, hashCode2);
        // different objects should have distinct hashCodes
        assertNotSame(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(5.0, 6.0);
        GeoQuery query = new GeoQuery(location);

        String expected = "GeoQuery{location=GeoLocation(latitude=5.0, longitude=6.0), ip='', accuracy='', granularity='', maxResults=0}";
        String actual = query.toString();
        assertEquals(expected, actual);
    }
}