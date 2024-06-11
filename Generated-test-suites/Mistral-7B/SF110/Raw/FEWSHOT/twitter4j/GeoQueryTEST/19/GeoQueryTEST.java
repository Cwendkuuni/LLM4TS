package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testEquality() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);

        String ip = "192.168.1.1";
        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location1);
        GeoQuery query3 = new GeoQuery(ip);
        GeoQuery query4 = new GeoQuery(ip);
        GeoQuery query5 = new GeoQuery();

        // Test equality with same values
        assertEquals(query1, query2);
        assertEquals(query3, query4);

        // Test equality with different instances but same values
        query1.setAccuracy(accuracy);
        query2.setAccuracy(accuracy);
        query1.setGranularity(granularity);
        query2.setGranularity(granularity);
        query1.setMaxResults(maxResults);
        query2.setMaxResults(maxResults);

        assertEquals(query1, query2);

        // Test inequality with different values
        query1.setAccuracy("50m");
        assertNotEquals(query1, query2);

        query1.setGranularity(NEIGHBORHOOD);
        assertNotEquals(query1, query2);

        query1.setMaxResults(5);
        assertNotEquals(query1, query2);

        // Test inequality with null values
        query1.setLocation(null);
        assertNotEquals(query1, query2);

        query2.setLocation(null);
        assertEquals(query1, query2);

        query1.setIp(null);
        assertNotEquals(query1, query2);

        query2.setIp(null);
        assertEquals(query1, query2);

        // Test inequality with different classes
        String obj = new Object();
        assertNotEquals(query1, obj);
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);

        String ip = "192.168.1.1";
        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location1);
        GeoQuery query3 = new GeoQuery(ip);
        GeoQuery query4 = new GeoQuery(ip);
        GeoQuery query5 = new GeoQuery();

        int hashCode1 = query1.hashCode();
        int hashCode2 = query2.hashCode();
        int hashCode3 = query3.hashCode();
        int hashCode4 = query4.hashCode();
        int hashCode5 = query5.hashCode();

        // Test same values, same order, same hash codes
        assertEquals(hashCode1, hashCode2);
        assertEquals(hashCode3, hashCode4);

        // Test same values, different order, same hash codes
        query1.setAccuracy(accuracy);
        query2.setAccuracy(accuracy);
        query1.setGranularity(granularity);
        query2.setGranularity(granularity);
        query1.setMaxResults(maxResults);
        query2.setMaxResults(maxResults);

        assertEquals(hashCode1, hashCode2);

        // Test different values, different hash codes
        query1.setAccuracy("50m");
        int newHashCode1 = query1.hashCode();
        assertNotEquals(newHashCode1, hashCode1);

        query1.setGranularity(NEIGHBORHOOD);
        int newHashCode2 = query1.hashCode();
        assertNotEquals(newHashCode2, hashCode1);

        query1.setMaxResults(5);
        int newHashCode3 = query1.hashCode();
        assertNotEquals(newHashCode3, hashCode1);

        // Test null values, same hash codes
        query1.setLocation(null);
        assertEquals(hashCode1, hashCode5);

        query2.setLocation(null);
        assertEquals(hashCode1, hashCode5);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);
        query.setAccuracy("10m");
        query.setGranularity("city");
        query.setMaxResults(10);

        String expected = "GeoQuery{location=" + location + ", ip='' " +
                ", accuracy='10m' " +
                ", granularity='city' " +
                ", maxResults=10}";

        assertEquals(expected, query.toString());
    }

    @Test
    public void testSetterGetters() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        String ip = "192.168.1.1";
        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery query = new GeoQuery();

        // Test setter for location
        query.setLocation(location);
        GeoLocation actualLocation = query.getLocation();
        assertEquals(location, actualLocation);

        // Test setter for ip
        query.setIp(ip);
        String actualIp = query.getIp();
        assertEquals(ip, actualIp);

        // Test setter for accuracy
        query.setAccuracy(accuracy);
        String actualAccuracy = query.getAccuracy();
        assertEquals(accuracy, actualAccuracy);

        // Test setter for granularity
        query.setGranularity(granularity);
        String actualGranularity = query.getGranularity();
        assertEquals(granularity, actualGranularity);

        // Test setter for maxResults
        query.setMaxResults(maxResults);
        int actualMaxResults = query.getMaxResults();
        assertEquals(maxResults, actualMaxResults);
    }

    @Test
    public void testAppendParameter() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery query = new GeoQuery();

        // Test appendParameter for String value
        query.appendParameter("lat", "1.0", params);
        HttpParameter param1 = params.get(0);
        assertEquals("lat", param1.getName());
        assertEquals("1.0", param1.getValue());

        // Test appendParameter for int value
        query.appendParameter("max_results", 10, params);
        HttpParameter param2 = params.get(1);
        assertEquals("max_results", param2.getName());
        assertEquals("10", param2.getValue());

        // Test appendParameter for double value
        query.appendParameter("long", 2.0, params);
        HttpParameter param3 = params.get(2);
        assertEquals("long", param3.getName());
        assertEquals("2.0", param3.getValue());

        // Test appendParameter for null value
        query.appendParameter("accuracy", null, params);
        assertEquals(0, params.size());
    }

    @Test
    public void testAsHttpParameterArray() {
        List<HttpParameter> params = new ArrayList<>();
        GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));

        // Test asHttpParameterArray with location
        query.appendParameter("lat", "1.0", params);
        query.appendParameter("long", "2.0", params);
        HttpParameter[] paramArray = query.asHttpParameterArray();
        assertEquals(2, paramArray.length);
        assertEquals("lat", paramArray[0].getName());
        assertEquals("1.0", paramArray[0].getValue());
        assertEquals("long", paramArray[1].getName());
        assertEquals("2.0", paramArray[1].getValue());

        // Test asHttpParameterArray with ip
        query.setIp("192.168.1.1");
        paramArray = query.asHttpParameterArray();
        assertEquals(1, paramArray.length);
        assertEquals("ip", paramArray[0].getName());
        assertEquals("192.168.1.1", paramArray[0].getValue());

        // Test asHttpParameterArray with accuracy
        query.setAccuracy("10m");
        paramArray = query.asHttpParameterArray();
        assertEquals(1, paramArray.length);
        assertEquals("accuracy", paramArray[0].getName());
        assertEquals("10m", paramArray[0].getValue());

        // Test asHttpParameterArray with granularity
        query.setGranularity("city");
        paramArray = query.asHttpParameterArray();
        assertEquals(1, paramArray.length);
        assertEquals("granularity", paramArray[0].getName());
        assertEquals("city", paramArray[0].getValue());

        // Test asHttpParameterArray with maxResults
        query.setMaxResults(10);
        paramArray = query.asHttpParameterArray();
        assertEquals(1, paramArray.length);
        assertEquals("max_results", paramArray[0].getName());
        assertEquals("10", paramArray[0].getValue());
    }
}