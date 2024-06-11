package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        assertEquals(expectedLocation, geoQuery.getLocation());
        assertNull(geoQuery.getIp());

        GeoQuery geoQueryWithSameLocation = new GeoQuery(location);
        assertEquals(geoQuery, geoQueryWithSameLocation);

        GeoQuery geoQueryWithDifferentLocation = new GeoQuery(new GeoLocation(3.0, 4.0));
        assertNotEquals(geoQuery, geoQueryWithDifferentLocation);
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());

        GeoQuery geoQueryWithSameIP = new GeoQuery(ip);
        assertEquals(geoQuery, geoQueryWithSameIP);

        GeoQuery geoQueryWithDifferentIP = new GeoQuery("192.168.1.2");
        assertNotEquals(geoQuery, geoQueryWithDifferentIP);
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery geoQuery = new GeoQuery(null);

        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 5;

        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(3.0, 4.0));
        GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(3.0, 4.0));

        GeoQuery ipQuery1 = new GeoQuery("192.168.1.1");
        GeoQuery ipQuery2 = new GeoQuery("192.168.1.2");

        GeoQuery geoQueryWithSameLocationAndIP = new GeoQuery(location1, "192.168.1.1");
        GeoQuery geoQueryWithSameLocationAndDifferentIP = new GeoQuery(location1, "192.168.1.2");

        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));
        assertTrue(geoQuery2.equals(geoQuery1));

        assertFalse(geoQuery1.equals(geoQuery3));
        assertFalse(geoQuery1.equals(geoQuery4));
        assertFalse(geoQuery3.equals(geoQuery4));

        assertFalse(geoQuery1.equals(ipQuery1));
        assertFalse(geoQuery1.equals(ipQuery2));
        assertFalse(ipQuery1.equals(ipQuery2));

        assertFalse(geoQuery1.equals(null));
        assertFalse(geoQuery1.equals(""));

        assertTrue(geoQueryWithSameLocationAndIP.equals(geoQueryWithSameLocationAndIP));
        assertFalse(geoQueryWithSameLocationAndIP.equals(geoQueryWithSameLocationAndDifferentIP));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        int hashCode1 = geoQuery.hashCode();
        GeoQuery geoQueryWithSameLocation = new GeoQuery(location);

        int hashCode2 = geoQueryWithSameLocation.hashCode();

        assertEquals(hashCode1, hashCode2);

        GeoQuery geoQueryWithDifferentLocation = new GeoQuery(new GeoLocation(3.0, 4.0));
        int hashCode3 = geoQueryWithDifferentLocation.hashCode();

        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String expectedString = "GeoQuery{location=" + location + ", ip='' , accuracy='' , granularity='' , maxResults=0}";
        assertEquals(expectedString, geoQuery.toString());
    }
}