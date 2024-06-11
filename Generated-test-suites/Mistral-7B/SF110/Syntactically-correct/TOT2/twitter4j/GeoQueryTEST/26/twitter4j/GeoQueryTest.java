package twitter4j.test;

import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        Assert.assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testEmptyConstructor() {
        GeoQuery geoQuery = new GeoQuery();
        Assert.assertNull(geoQuery.getLocation());
        Assert.assertNull(geoQuery.getIp());
        Assert.assertNull(geoQuery.getAccuracy());
        Assert.assertNull(geoQuery.getGranularity());
        Assert.assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String accuracy = "high";
        geoQuery.setAccuracy(accuracy);
        Assert.assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] expectedParams = new HttpParameter[]{
            new HttpParameter("lat", "1.0"),
            new HttpParameter("long", "2.0"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "5")
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        Assert.assertArrayEquals(expectedParams, actualParams);
    }

    // Test edge cases and exception handling for asHttpParameterArray() method
}