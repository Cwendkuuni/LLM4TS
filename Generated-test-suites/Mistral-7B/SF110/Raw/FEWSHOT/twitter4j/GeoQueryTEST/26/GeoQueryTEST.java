package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location1);
        GeoQuery query3 = new GeoQuery(new GeoLocation(2.0, 2.0));
        GeoQuery query4 = new GeoQuery("123.123.123.123");

        assertTrue(query1.equals(query1));
        assertTrue(query1.equals(query2));
        assertFalse(query1.equals(query3));
        assertFalse(query1.equals(query4));
        assertFalse(query3.equals(query4));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location1);
        GeoQuery query3 = new GeoQuery(new GeoLocation(2.0, 2.0));
        GeoQuery query4 = new GeoQuery("123.123.123.123");

        int hashCode1 = query1.hashCode();
        int hashCode2 = query2.hashCode();
        int hashCode3 = query3.hashCode();
        int hashCode4 = query4.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode2, hashCode3);
        assertNotEquals(hashCode2, hashCode4);
        assertNotEquals(hashCode3, hashCode4);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        String actual = query.toString();
        String expected = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='', accuracy='', granularity='', maxResults=0}";

        assertEquals(expected, actual);
    }

    @Test
    public void testAppendParameter() {
        List<HttpParameter> params = new ArrayList<>();
        GeoLocation location = new GeoLocation(1.0, 2.0);

        query.appendParameter("lat", location.getLatitude(), params);
        query.appendParameter("long", location.getLongitude(), params);
        query.appendParameter("ip", "123.123.123.123", params);
        query.appendParameter("accuracy", "10m", params);
        query.appendParameter("granularity", "city", params);
        query.appendParameter("max_results", 5, params);

        HttpParameter[] expected = new HttpParameter[]{
            new HttpParameter("lat", "1.0"),
            new HttpParameter("long", "2.0"),
            new HttpParameter("ip", "123.123.123.123"),
            new HttpParameter("accuracy", "10m"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "5")
        };

        HttpParameter[] actual = query.asHttpParameterArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testConstructors() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        String ip = "123.123.123.123";

        GeoQuery query1 = new GeoQuery(location);
        GeoQuery query2 = new GeoQuery(ip);

        assertNull(query1.getIp());
        assertEquals(location, query1.getLocation());
        assertNull(query1.getAccuracy());
        assertNull(query1.getGranularity());
        assertEquals(0, query1.getMaxResults());

        assertNull(query2.getLocation());
        assertEquals(ip, query2.getIp());
        assertNull(query2.getAccuracy());
        assertNull(query2.getGranularity());
        assertEquals(0, query2.getMaxResults());
    }

    @Test
    public void testGettersAndSetters() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 5;

        GeoQuery query = new GeoQuery(location);

        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
        assertNull(query.getAccuracy());
        assertNull(query.getGranularity());
        assertEquals(0, query.getMaxResults());

        query.setAccuracy(accuracy);
        query.setGranularity(granularity);
        query.setMaxResults(maxResults);

        assertEquals(location, query.getLocation());
        assertEquals(accuracy, query.getAccuracy());
        assertEquals(granularity, query.getGranularity());
        assertEquals(maxResults, query.getMaxResults());

        GeoQuery queryWithIp = new GeoQuery("123.123.123.123");
        assertEquals("123.123.123.123", queryWithIp.getIp());
        assertNull(queryWithIp.getLocation());
        assertNull(queryWithIp.getAccuracy());
        assertNull(queryWithIp.getGranularity());
        assertEquals(0, queryWithIp.getMaxResults());

        // Testing setters with invalid values
        try {
            query.setMaxResults(-1);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            query.setAccuracy(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            query.setGranularity(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            query.setLocation(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}