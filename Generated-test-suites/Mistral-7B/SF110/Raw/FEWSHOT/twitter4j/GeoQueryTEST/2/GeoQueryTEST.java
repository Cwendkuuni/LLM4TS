package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location1);
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(2.0, 3.0));

        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='', maxResults=5}";
        String actual = geoQuery.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        String latName = "lat";
        String longName = "long";

        assertEquals(2, params.length);
        HttpParameter latParam = params[0];
        HttpParameter longParam = params[1];

        assertEquals(latName, latParam.getName());
        assertEquals(Double.toString(location.getLatitude()), latParam.getValue());

        assertEquals(longName, longParam.getName());
        assertEquals(Double.toString(location.getLongitude()), longParam.getValue());
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        String ipName = "ip";

        assertEquals(1, params.length);
        HttpParameter ipParam = params[0];

        assertEquals(ipName, ipParam.getName());
        assertEquals(ip, ipParam.getValue());
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery geoQuery = new GeoQuery(null);

        String accuracy = "accurate";
        String granularity = "city";
        int maxResults = 5;

        assertEquals(null, geoQuery.getLocation());
        assertEquals(null, geoQuery.getIp());
        assertEquals(null, geoQuery.getAccuracy());
        assertEquals(null, geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());

        geoQuery.accuracy("accurate");
        assertEquals(accuracy, geoQuery.getAccuracy());

        geoQuery.granularity("city");
        assertEquals(granularity, geoQuery.getGranularity());

        geoQuery.maxResults(5);
        assertEquals(maxResults, geoQuery.getMaxResults());

        geoQuery.setLocation(new GeoLocation(1.0, 2.0));
        GeoLocation location = geoQuery.getLocation();
        assertEquals(1.0, location.getLatitude(), 0.01);
        assertEquals(2.0, location.getLongitude(), 0.01);

        geoQuery.setIp("192.168.1.1");
        assertEquals("192.168.1.1", geoQuery.getIp());
    }
}