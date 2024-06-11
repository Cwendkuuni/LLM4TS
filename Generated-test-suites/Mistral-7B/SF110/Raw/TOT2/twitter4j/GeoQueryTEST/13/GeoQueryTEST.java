package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

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

        String ip = "192.168.1.1";
        GeoQuery geoQueryWithIp = new GeoQuery(ip);
        assertEquals(ip, geoQueryWithIp.getIp());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        String ip = "192.168.1.1";
        String accuracy = "accurate";
        String granularity = "city";
        int maxResults = 10;

        // Test equal objects
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0, 2.0));
        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));
        assertTrue(geoQuery1.equals(geoQuery3));

        // Test null object
        assertFalse(geoQuery1.equals(null));

        // Test different classes
        assertFalse(geoQuery1.equals(new Object()));

        // Test different maxResults
        GeoQuery geoQuery4 = new GeoQuery(location1).maxResults(11);
        assertFalse(geoQuery1.equals(geoQuery4));

        // Test different accuracy
        GeoQuery geoQuery5 = new GeoQuery(location1).accuracy("lessAccurate");
        assertFalse(geoQuery1.equals(geoQuery5));

        // Test different granularity
        GeoQuery geoQuery6 = new GeoQuery(location1).granularity("neighborhood");
        assertFalse(geoQuery1.equals(geoQuery6));

        // Test different ip
        GeoQuery geoQuery7 = new GeoQuery(ip);
        assertFalse(geoQuery1.equals(geoQuery7));
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        String ip = "192.168.1.1";
        String accuracy = "accurate";
        String granularity = "city";
        int maxResults = 10;
        GeoQuery geoQuery = new GeoQuery(location).accuracy(accuracy).granularity(granularity).maxResults(maxResults);
        HttpParameter[] expectedParams = geoQuery.asHttpParameterArray();
        HttpParameter[] actualParams = new HttpParameter[5];
        actualParams[0] = new HttpParameter("lat", "1.0");
        actualParams[1] = new HttpParameter("long", "2.0");
        actualParams[2] = new HttpParameter("accuracy", accuracy);
        actualParams[3] = new HttpParameter("granularity", granularity);
        actualParams[4] = new HttpParameter("max_results", Integer.toString(maxResults));
        assertArrayEquals(expectedParams, actualParams);
    }
}