package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testConstructors() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery queryWithLocation = new GeoQuery(location);
        assertNull(queryWithLocation.getIp());
        assertEquals(location, queryWithLocation.getLocation());
        assertNull(queryWithLocation.getAccuracy());
        assertNull(queryWithLocation.getGranularity());
        assertEquals(0, queryWithLocation.getMaxResults());

        String ipAddress = "192.168.0.1";
        GeoQuery queryWithIp = new GeoQuery(ipAddress);
        assertNull(queryWithIp.getLocation());
        assertEquals(ipAddress, queryWithIp.getIp());
        assertNull(queryWithIp.getAccuracy());
        assertNull(queryWithIp.getGranularity());
        assertEquals(0, queryWithIp.getMaxResults());
    }

    @Test
    public void testGettersAndSetters() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery();
        query.setLocation(location);
        query.setAccuracy("10m");
        query.setGranularity("city");
        query.setMaxResults(5);

        assertEquals(location, query.getLocation());
        assertEquals("10m", query.getAccuracy());
        assertEquals("city", query.getGranularity());
        assertEquals(5, query.getMaxResults());

        query.setLocation(null);
        query.setIp("192.168.0.1");
        assertNull(query.getLocation());
        assertEquals("192.168.0.1", query.getIp());

        query.setAccuracy(null);
        query.setGranularity(null);
        query.setMaxResults(0);
        assertNull(query.getAccuracy());
        assertNull(query.getGranularity());
        assertEquals(0, query.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("10m");
        query.setGranularity("city");
        query.setMaxResults(5);

        HttpParameter[] params = query.asHttpParameterArray();
        assertEquals(5, params.length);
        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (HttpParameter param : params) {
            names.add(param.getName());
            values.add(param.getValue());
        }

        // Check for location parameters
        assertTrue(names.contains("lat"));
        assertTrue(names.contains("long"));
        assertEquals(Double.toString(location.getLatitude()), values.get(names.indexOf("lat")));
        assertEquals(Double.toString(location.getLongitude()), values.get(names.indexOf("long")));

        // Check for other parameters
        assertTrue(names.contains("accuracy"));
        assertEquals("10m", values.get(names.indexOf("accuracy")));
        assertTrue(names.contains("granularity"));
        assertEquals("city", values.get(names.indexOf("granularity")));
        assertTrue(names.contains("max_results"));
        assertEquals(Integer.toString(query.getMaxResults()), values.get(names.indexOf("max_results")));

        query.setIp("192.168.0.1");
        params = query.asHttpParameterArray();
        names.clear();
        values.clear();
        for (HttpParameter param : params) {
            names.add(param.getName());
            values.add(param.getValue());
        }

        // Check for ip parameter
        assertTrue(names.contains("ip"));
        assertEquals("192.168.0.1", values.get(names.indexOf("ip")));

        // Check for other parameters
        assertTrue(names.contains("accuracy"));
        assertNull(values.get(names.indexOf("accuracy")));
        assertTrue(names.contains("granularity"));
        assertNull(values.get(names.indexOf("granularity")));
        assertTrue(names.contains("max_results"));
        assertEquals(Integer.toString(query.getMaxResults()), values.get(names.indexOf("max_results")));
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        String ipAddress1 = "192.168.0.1";
        String ipAddress2 = "192.168.0.1";
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery(ipAddress1);
        GeoQuery query4 = new GeoQuery(ipAddress1);
        GeoQuery query5 = new GeoQuery();

        // Check for equal GeoQueries
        assertTrue(query1.equals(query1));
        assertTrue(query1.equals(query2));

        // Check for equal GeoQueries with different maxResults
        query2.setMaxResults(6);
        assertFalse(query1.equals(query2));

        // Check for equal GeoQueries with different accuracy
        query1.setAccuracy("20m");
        assertFalse(query1.equals(query2));

        // Check for equal GeoQueries with different granularity
        query1.setGranularity("neighborhood");
        assertFalse(query1.equals(query2));

        // Check for equal GeoQueries with different location
        query1.setLocation(new GeoLocation(1.1, 2.1));
        assertFalse(query1.equals(query2));

        // Check for equal GeoQueries with different ip
        query1.setIp("192.168.0.2");
        assertFalse(query1.equals(query2));

        // Check for equal GeoQueries with null location
        query2.setLocation(null);
        assertFalse(query1.equals(query2));

        // Check for equal GeoQueries with null ip
        query1.setIp(null);
        assertTrue(query1.equals(query2));

        // Check for equal GeoQueries with same ip
        query1.setIp(ipAddress1);
        assertTrue(query1.equals(query3));
        assertTrue(query1.equals(query4));

        // Check for not equal GeoQueries
        assertFalse(query1.equals(query3));
        assertFalse(query1.equals(query5));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        String ipAddress1 = "192.168.0.1";
        String ipAddress2 = "192.168.0.2";
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery(ipAddress1);
        GeoQuery query4 = new GeoQuery(ipAddress1);
        GeoQuery query5 = new GeoQuery();

        int hashCode1 = query1.hashCode();
        int hashCode2 = query1.hashCode();

        // Check for equal GeoQueries
        assertTrue(hashCode1 == hashCode2);

        // Check for equal GeoQueries with different maxResults
        query2.setMaxResults(6);
        assertNotEquals(hashCode1, hashCode2);

        // Check for equal GeoQueries with different accuracy
        query1.setAccuracy("20m");
        assertNotEquals(hashCode1, hashCode2);

        // Check for equal GeoQueries with different granularity
        query1.setGranularity("neighborhood");
        assertNotEquals(hashCode1, hashCode2);

        // Check for equal GeoQueries with different location
        query1.setLocation(new GeoLocation(1.1, 2.1));
        assertNotEquals(hashCode1, hashCode2);

        // Check for equal GeoQueries with different ip
        query1.setIp("192.168.0.2");
        assertNotEquals(hashCode1, hashCode2);

        // Check for equal GeoQueries with null location
        query2.setLocation(null);
        assertNotEquals(hashCode1, hashCode2);

        // Check for equal GeoQueries with null ip
        query1.setIp(null);
        assertEquals(hashCode1, hashCode2);

        // Check for equal GeoQueries with same ip
        query1.setIp(ipAddress1);
        assertEquals(hashCode1, hashCode3.hashCode());
        assertEquals(hashCode1, hashCode4.hashCode());

        // Check for not equal GeoQueries
        assertNotEquals(hashCode1, hashCode5.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("10m");
        query.setGranularity("city");
        query.setMaxResults(5);

        String expected = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='', accuracy='10m', granularity='city', maxResults=5}";
        assertEquals(expected, query.toString());

        query.setIp("192.168.0.1");
        expected = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='192.168.0.1', accuracy=null, granularity=null, maxResults=5}";
        assertEquals(expected, query.toString());

        query.setAccuracy(null);
        query.setGranularity(null);
        query.setMaxResults(0);
        expected = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='192.168.0.1', accuracy=null, granularity=null, maxResults=0}";
        assertEquals(expected, query.toString());
    }
}