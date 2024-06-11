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
    public void testGeoQueryConstructionWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testGeoQueryConstructionWithIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetters() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());

        String accuracy = "10m";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        String granularity = "city";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        int maxResults = 5;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testSettingValuesWithMethods() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String accuracy = "10m";
        geoQuery = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        String granularity = "city";
        geoQuery = geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        int maxResults = 5;
        geoQuery = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testSettingValuesWithSetters() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String accuracy = "10m";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        String granularity = "city";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        int maxResults = 5;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("10m");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(5);

        List<HttpParameter> expectedParameters = new ArrayList<>();
        expectedParameters.add(new HttpParameter("lat", "1.0"));
        expectedParameters.add(new HttpParameter("long", "2.0"));
        expectedParameters.add(new HttpParameter("accuracy", "10m"));
        expectedParameters.add(new HttpParameter("granularity", "city"));
        expectedParameters.add(new HttpParameter("max_results", "5"));

        HttpParameter[] actualParameters = geoQuery.asHttpParameterArray();
        assertEquals(expectedParameters.size(), actualParameters.length);
        assertArrayEquals(expectedParameters.toArray(), actualParameters);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location1);

        GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(2.0, 2.0));
        GeoQuery geoQuery5 = new GeoQuery("127.0.0.1");

        assertTrue(geoQuery1.equals(geoQuery3));
        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2)); // should be true due to equals of location

        assertFalse(geoQuery1.equals(null));
        assertFalse(geoQuery1.equals(""));
        assertFalse(geoQuery1.equals(geoQuery4));
        assertFalse(geoQuery1.equals(geoQuery5));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(1.0, 2.1));

        int hashCode1 = geoQuery.hashCode();
        int hashCode2 = geoQuery2.hashCode();
        int hashCode3 = geoQuery3.hashCode();
        int hashCode4 = geoQuery4.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String expectedToString = "GeoQuery{location=" + location + ", ip='null' , accuracy='null' , granularity='null' , maxResults=0}";
        String actualToString = geoQuery.toString();

        assertEquals(expectedToString, actualToString);

        geoQuery.setAccuracy("10m");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(5);

        expectedToString = "GeoQuery{location=" + location + ", ip='null' , accuracy='10m' , granularity='city' , maxResults=5}";
        actualToString = geoQuery.toString();

        assertEquals(expectedToString, actualToString);
    }
}