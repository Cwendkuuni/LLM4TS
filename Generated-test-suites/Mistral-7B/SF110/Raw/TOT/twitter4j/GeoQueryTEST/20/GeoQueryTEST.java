package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }
    
    @Test
    public void testGetIp() {
        GeoQuery geoQuery = new GeoQuery(null);
        assertNull(geoQuery.getIp());
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("1km");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "1.0"),
            new HttpParameter("long", "2.0"),
            new HttpParameter("accuracy", "1km"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "10")
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParams, actualParams);
    }
}