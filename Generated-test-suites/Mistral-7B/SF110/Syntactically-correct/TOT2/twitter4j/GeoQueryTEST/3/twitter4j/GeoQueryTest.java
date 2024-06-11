
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
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
        String expectedIp = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(expectedIp);
        String actualIp = geoQuery.getIp();
        assertEquals(expectedIp, actualIp);
    }
    
    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0), "192.168.1.1", "high", "city", 5);
        HttpParameter[] expectedParams = {
            new HttpParameter("lat", "1.0"),
            new HttpParameter("long", "2.0"),
            new HttpParameter("accuracy", "high"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "5")
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParams, actualParams);
    }
    
    @Test
    public void testGetLocation_whenNoLocationSet() {
        GeoQuery geoQuery = new GeoQuery();
        assertNull(geoQuery.getLocation());
    }
    
    @Test
    public void testGetIp_whenNoIpSet() {
        GeoQuery geoQuery = new GeoQuery();
        assertNull(geoQuery.getIp());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAsHttpParameterArray_whenInvalidLocation() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.asHttpParameterArray();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAsHttpParameterArray_whenInvalidIp() {
        GeoQuery geoQuery = new GeoQuery("invalid ip");
        geoQuery.asHttpParameterArray();
    }
}
