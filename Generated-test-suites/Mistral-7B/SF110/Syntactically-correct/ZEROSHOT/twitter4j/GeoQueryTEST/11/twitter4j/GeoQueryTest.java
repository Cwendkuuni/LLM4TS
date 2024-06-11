package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0D, 2.0D);
        GeoQuery query = new GeoQuery(location);

        HttpParameter[] params = query.asHttpParameterArray();
        List<String> expectedParams = new ArrayList<>();
        expectedParams.add("lat=1.0");
        expectedParams.add("long=2.0");

        assertArrayEquals(expectedParams.toArray(new String[expectedParams.size()]), getNamesAndValues(params));
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "127.0.0.1";
        GeoQuery query = new GeoQuery(ip);

        HttpParameter[] params = query.asHttpParameterArray();
        List<String> expectedParams = new ArrayList<>();
        expectedParams.add("ip=127.0.0.1");

        assertArrayEquals(expectedParams.toArray(new String[expectedParams.size()]), getNamesAndValues(params));
    }

    @Test
    public void testGeoQueryWithAllFields() {
        GeoLocation location = new GeoLocation(1.0D, 2.0D);
        String ip = "127.0.0.1";
        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 5;

        GeoQuery query = new GeoQuery(location)
                .accuracy(accuracy)
                .granularity(granularity)
                .maxResults(maxResults);

        HttpParameter[] params = query.asHttpParameterArray();
        List<String> expectedParams = new ArrayList<>();
        expectedParams.add("lat=1.0");
        expectedParams.add("long=2.0");
        expectedParams.add("ip=127.0.0.1");
        expectedParams.add("accuracy=10m");
        expectedParams.add("granularity=city");
        expectedParams.add("max_results=5");

        assertArrayEquals(expectedParams.toArray(new String[expectedParams.size()]), getNamesAndValues(params));
    }

    private static String[] getNamesAndValues(HttpParameter[] params) {
        String[] result = new String[params.length * 2];
        int index = 0;
        for (HttpParameter param : params) {
            result[index++] = param.getName();
            result[index++] = param.getValue();
        }
        return result;
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0D, 2.0D);
        GeoLocation location2 = new GeoLocation(1.0D, 2.0D);

        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);

        GeoQuery query3 = new GeoQuery(location1);

        assertTrue(query1.equals(query1));
        assertTrue(query1.equals(query3));
        assertTrue(query1.equals(query2));

        query2.setMaxResults(10);
        assertFalse(query1.equals(query2));

        query2.setMaxResults(5);
        assertTrue(query1.equals(query2));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0D, 2.0D);
        GeoLocation location2 = new GeoLocation(2.0D, 3.0D);

        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);

        int hashCode1 = query1.hashCode();
        int hashCode2 = query2.hashCode();

        GeoQuery query3 = new GeoQuery(location1);
        int hashCode3 = query3.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0D, 2.0D);
        String ip = "127.0.0.1";
        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 5;

        GeoQuery query = new GeoQuery(location)
                .accuracy(accuracy)
                .granularity(granularity)
                .maxResults(maxResults);

        String expectedString = "GeoQuery{location=" + location + ", ip='" + ip + "', accuracy='"
                + accuracy + "', granularity='" + granularity + "', maxResults=5}";

        assertEquals(expectedString, query.toString());
    }
}