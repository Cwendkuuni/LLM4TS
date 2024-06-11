package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length);
        for (HttpParameter param : params) {
            System.out.println("Name: " + param.getName() +", Value: " + param.getValue());
        }
    }

    @Test
    public void testGeoQueryWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(4, params.length);
        for (HttpParameter param : params) {
            System.out.println("Name: " + param.getName() +", Value: " + param.getValue());
        }
    }

    @Test
    public void testSetAndGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(null);
        String accuracy = "10m";
        geoQuery.accuracy(accuracy);

        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testSetAndGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(null);
        String granularity = "city";
        geoQuery.granularity(granularity);

        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testSetAndGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(null);
        int maxResults = 5;
        geoQuery.maxResults(maxResults);

        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(51.5074, -0.1278);
        GeoLocation location2 = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location1);
        GeoQuery geoQuery4 = new GeoQuery(null);

        assertTrue(geoQuery1.equals(geoQuery3));
        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(geoQuery4));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);
        GeoQuery geoQuery3 = new GeoQuery(null);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();
        int hashCode3 = geoQuery3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }
}