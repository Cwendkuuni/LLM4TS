package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullLocation() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.getLocation();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLocationWithEmptyConstructor() {
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.getLocation();
    }

    @Test
    public void testGetLocationWithOtherProperties() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation).accuracy("high").granularity("country").maxResults(10);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }
}