package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import twitter4j.GeoLocation;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testNewInstanceWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(location, actualLocation);
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testNewInstanceWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGettersAndSetters() {
        GeoQuery geoQuery = new GeoQuery(null);

        String accuracy = "1km";
        String granularity = "city";
        int maxResults = 10;

        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAppendParameter() {
        GeoQuery geoQuery = new GeoQuery(null);
        List<HttpParameter> params = new ArrayList<>();

        String accuracy = "1km";
        String granularity = "city";
        int maxResults = 10;

        geoQuery.appendParameter("accuracy", accuracy, params);
        assertEquals(1, params.size());
        HttpParameter actualParam = params.get(0);
        assertEquals("accuracy", actualParam.getName());
        assertEquals(accuracy, actualParam.getValue());

        geoQuery.appendParameter("granularity", granularity, params);
        assertEquals(2, params.size());
        actualParam = params.get(1);
        assertEquals("granularity", actualParam.getName());
        assertEquals(granularity, actualParam.getValue());

        geoQuery.appendParameter("max_results", String.valueOf(maxResults), params);
        assertEquals(3, params.size());
        actualParam = params.get(2);
        assertEquals("max_results", actualParam.getName());
        assertEquals(String.valueOf(maxResults), actualParam.getValue());
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(null);
        GeoQuery geoQuery2 = new GeoQuery(null);

        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);

        String accuracy1 = "1km";
        String accuracy2 = "1km";
        String granularity1 = "city";
        String granularity2 = "city";
        String ip1 = "192.168.0.1";
        String ip2 = "192.168.0.1";

        geoQuery1.setLocation(location1);
        geoQuery1.setAccuracy(accuracy1);
        geoQuery1.setGranularity(granularity1);
        geoQuery1.setIp(ip1);

        geoQuery2.setLocation(location2);
        geoQuery2.setAccuracy(accuracy2);
        geoQuery2.setGranularity(granularity2);
        geoQuery2.setIp(ip2);

        HttpParameter[] params1 = geoQuery1.asHttpParameterArray();
        HttpParameter[] params2 = geoQuery2.asHttpParameterArray();

        assertArrayEquals(params1, params2);

        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(null);
        GeoQuery geoQuery2 = new GeoQuery(null);

        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);

        String accuracy1 = "1km";
        String accuracy2 = "1km";
        String granularity1 = "city";
        String granularity2 = "city";
        String ip1 = "192.168.0.1";
        String ip2 = "192.168.0.1";

        geoQuery1.setLocation(location1);
        geoQuery1.setAccuracy(accuracy1);
        geoQuery1.setGranularity(granularity1);
        geoQuery1.setIp(ip1);

        geoQuery2.setLocation(location2);
        geoQuery2.setAccuracy(accuracy2);
        geoQuery2.setGranularity(granularity2);
        geoQuery2.setIp(ip2);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(null);

        GeoLocation location = new GeoLocation(1.0, 2.0);
        String accuracy = "1km";
        String granularity = "city";
        String ip = "192.168.0.1";
        int maxResults = 10;

        geoQuery.setLocation(location);
        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setIp(ip);
        geoQuery.setMaxResults(maxResults);

        String expected = String.format("GeoQuery{location=%s, ip='%s', accuracy='%s', granularity='%s', maxResults=%d}", location, ip, accuracy, granularity, maxResults);
        assertEquals(expected, geoQuery.toString());
    }
}