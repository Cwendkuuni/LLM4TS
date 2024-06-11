package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        assertEquals(location, query.getLocation());
        assertEquals(null, query.getIp());
        assertNull(query.getAccuracy());
        assertNull(query.getGranularity());
        assertEquals(0, query.getMaxResults());

        HttpParameter[] params = query.asHttpParameterArray();
        assertEquals(5, params.length);
        for (HttpParameter param : params) {
            assertNotNull(param.getName());
            assertNotNull(param.getValue());
        }
        assertTrue(query.equals(query));
        assertTrue(query.hashCode() == query.hashCode());
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery query = new GeoQuery(ip);

        assertNull(query.getLocation());
        assertEquals(ip, query.getIp());
        assertNull(query.getAccuracy());
        assertNull(query.getGranularity());
        assertEquals(0, query.getMaxResults());

        HttpParameter[] params = query.asHttpParameterArray();
        assertEquals(1, params.length);
        assertEquals("ip", params[0].getName());
        assertEquals(ip, params[0].getValue());

        assertTrue(query.equals(query));
        assertTrue(query.hashCode() == query.hashCode());
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery query = new GeoQuery(null);

        String accuracy = "10m";
        query.setAccuracy(accuracy);
        assertEquals(accuracy, query.getAccuracy());

        String granularity = "country";
        query.setGranularity(granularity);
        assertEquals(granularity, query.getGranularity());

        int maxResults = 50;
        query.setMaxResults(maxResults);
        assertEquals(maxResults, query.getMaxResults());

        assertNull(query.getLocation());
        assertNull(query.getIp());

        HttpParameter[] params = query.asHttpParameterArray();
        assertEquals(5, params.length);
        for (HttpParameter param : params) {
            assertNotNull(param.getName());
            assertNotNull(param.getValue());
        }

        assertTrue(query.equals(query));
        assertTrue(query.hashCode() == query.hashCode());
    }

    @Test
    public void testAppendParameter() {
        GeoQuery query = new GeoQuery(null);

        query.appendParameter("lat", "37.7749", new ArrayList<>());
        query.appendParameter("long", "-122.4194", new ArrayList<>());
        query.appendParameter("accuracy", "50m", new ArrayList<>());
        query.appendParameter("granularity", "city", new ArrayList<>());
        query.appendParameter("max_results", "10", new ArrayList<>());

        HttpParameter[] params = query.asHttpParameterArray();
        assertEquals(5, params.length);
        for (HttpParameter param : params) {
            assertNotNull(param.getName());
            assertNotNull(param.getValue());
        }

        assertEquals("lat", params[0].getName());
        assertEquals("37.7749", params[0].getValue());

        assertEquals("long", params[1].getName());
        assertEquals("-122.4194", params[1].getValue());

        assertEquals("accuracy", params[2].getName());
        assertEquals("50m", params[2].getValue());

        assertEquals("granularity", params[3].getName());
        assertEquals("city", params[3].getValue());

        assertEquals("max_results", params[4].getName());
        assertEquals("10", params[4].getValue());

        assertTrue(query.equals(query));
        assertTrue(query.hashCode() == query.hashCode());
    }

    @Test
    public void testEqualsAndHashCode() {
        GeoQuery query1 = new GeoQuery(null);
        GeoQuery query2 = new GeoQuery(null);

        query1.setAccuracy("10m");
        query1.setGranularity("city");
        query1.setMaxResults(5);

        query2.setAccuracy("10m");
        query2.setGranularity("city");
        query2.setMaxResults(5);

        query2.appendParameter("lat", "37.7749", new ArrayList<>());
        query2.appendParameter("long", "-122.4194", new ArrayList<>());

        assertFalse(query1.equals(query2));
        assertFalse(query1.hashCode() == query2.hashCode());

        query1.appendParameter("lat", "37.7749", new ArrayList<>());
        query1.appendParameter("long", "-122.4194", new ArrayList<>());

        assertTrue(query1.equals(query2));
        assertTrue(query1.hashCode() == query2.hashCode());
    }
}