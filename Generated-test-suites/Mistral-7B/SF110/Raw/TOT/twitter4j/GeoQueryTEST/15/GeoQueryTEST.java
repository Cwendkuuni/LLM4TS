package twitter4j;

import org.junit.Assert;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {
    
    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0); // Latitude and Longitude
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        Assert.assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testGetIp() {
        String expectedIp = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(null).ip(expectedIp);
        String actualIp = geoQuery.getIp();
        Assert.assertEquals(expectedIp, actualIp);
    }

    @Test
    public void testAsHttpParameterArray() {
        String accuracy = "100m";
        String granularity = "city";
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(null)
                .accuracy(accuracy)
                .granularity(granularity)
                .maxResults(maxResults);
        HttpParameter[] expectedParams = new HttpParameter[] {
                new HttpParameter("accuracy", accuracy),
                new HttpParameter("granularity", granularity),
                new HttpParameter("max_results", String.valueOf(maxResults))
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        Assert.assertArrayEquals(expectedParams, actualParams);
    }
}