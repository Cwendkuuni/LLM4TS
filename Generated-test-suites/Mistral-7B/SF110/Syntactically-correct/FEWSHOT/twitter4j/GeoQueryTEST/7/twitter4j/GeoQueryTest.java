package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import twitter4j.internal.http.HttpParameter;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

import java.util.ArrayList;
import java.util.List;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery = new GeoQuery(location);

        List<HttpParameter> httpParameters = geoQuery.asHttpParameterArray();
        assertEquals(5, httpParameters.size());
        assertEquals("lat", httpParameters.get(0).getName());
        assertEquals("51.5074", httpParameters.get(0).getValue());
        assertEquals("long", httpParameters.get(1).getName());
        assertEquals("-0.1278", httpParameters.get(1).getValue());
        assertNull(httpParameters.get(2).getName());
        assertNull(httpParameters.get(2).getValue());
        assertEquals("accuracy", httpParameters.get(3).getName());
        assertNull(httpParameters.get(3).getValue());
        assertEquals("granularity", httpParameters.get(4).getName());
        assertNull(httpParameters.get(4).getValue());
        assertEquals("max_results", httpParameters.get(5).getName());
        assertEquals("" + GeoQuery.MAX_RESULTS, httpParameters.get(5).getValue());
    }

    @Test
    public void testGeoQueryWithIP() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");

        List<HttpParameter> httpParameters = geoQuery.asHttpParameterArray();
        assertEquals(6, httpParameters.size());
        assertNull(httpParameters.get(0).getName());
        assertNull(httpParameters.get(0).getValue());
        assertEquals("ip", httpParameters.get(1).getName());
        assertEquals("192.168.1.1", httpParameters.get(1).getValue());
        assertNull(httpParameters.get(2).getName());
        assertNull(httpParameters.get(2).getValue());
        assertEquals("accuracy", httpParameters.get(3).getName());
        assertNull(httpParameters.get(3).getValue());
        assertEquals("granularity", httpParameters.get(4).getName());
        assertNull(httpParameters.get(4).getValue());
        assertEquals("max_results", httpParameters.get(5).getName());
        assertEquals("" + GeoQuery.MAX_RESULTS, httpParameters.get(5).getValue());
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery geoQuery = new GeoQuery(null);

        String accuracy = "10m";
        String granularity = GeoQuery.NEIGHBORHOOD;
        int maxResults = 20;

        geoQuery.setAccuracy(accuracy);
        geoQuery.granularity(granularity);
        geoQuery.maxResults(maxResults);

        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(maxResults, geoQuery.getMaxResults());

        List<HttpParameter> httpParameters = geoQuery.asHttpParameterArray();
        assertEquals("accuracy", httpParameters.get(3).getName());
        assertEquals(accuracy, httpParameters.get(3).getValue());
        assertEquals("granularity", httpParameters.get(4).getName());
        assertEquals(granularity, httpParameters.get(4).getValue());
        assertEquals("max_results", httpParameters.get(5).getName());
        assertEquals("" + maxResults, httpParameters.get(5).getValue());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(51.5074, -0.1278);
        GeoLocation location2 = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(null);
        GeoQuery geoQuery4 = new GeoQuery("192.168.1.1");

        assertTrue(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(geoQuery3));
        assertFalse(geoQuery1.equals(geoQuery4));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(51.5074, -0.1278);
        GeoLocation location2 = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(null);
        GeoQuery geoQuery4 = new GeoQuery("192.168.1.1");

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
        assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
        assertNotEquals(geoQuery1.hashCode(), geoQuery4.hashCode());
    }
}