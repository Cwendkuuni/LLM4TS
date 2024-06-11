package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import twitter4j.GeoLocation;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0d, 2.0d);
        GeoQuery query = new GeoQuery(location);
        HttpParameter[] params = query.asHttpParameterArray();
        assertEquals(5, params.length);
        HttpParameter param;
        for (int i = 0; i < params.length; i++) {
            param = params[i];
            switch (i) {
                case 0:
                    assertEquals("lat", param.getName());
                    assertEquals("" + 1.0d, param.getValue());
                    break;
                case 1:
                    assertEquals("long", param.getName());
                    assertEquals("" + 2.0d, param.getValue());
                    break;
                case 2:
                    assertNull(param.getValue());
                    break;
                case 3:
                    assertEquals("accuracy", param.getName());
                    assertNull(param.getValue());
                    break;
                case 4:
                    assertEquals("granularity", param.getName());
                    assertNull(param.getValue());
                    break;
                default:
                    fail("Unexpected parameter index: " + i);
            }
        }
    }

    @Test
    public void testGeoQueryWithIp() {
        GeoQuery query = new GeoQuery("123.45.67.89");
        HttpParameter[] params = query.asHttpParameterArray();
        assertEquals(5, params.length);
        HttpParameter param;
        for (int i = 0; i < params.length; i++) {
            param = params[i];
            switch (i) {
                case 0:
                    assertNull(param.getName());
                    assertEquals("123.45.67.89", param.getValue());
                    break;
                case 1:
                    assertNull(param.getName());
                    assertNull(param.getValue());
                    break;
                case 2:
                    assertEquals("accuracy", param.getName());
                    assertNull(param.getValue());
                    break;
                case 3:
                    assertEquals("granularity", param.getName());
                    assertNull(param.getValue());
                    break;
                case 4:
                    assertEquals("max_results", param.getName());
                    assertEquals("0", param.getValue());
                    break;
                default:
                    fail("Unexpected parameter index: " + i);
            }
        }
    }

    @Test
    public void testGettersAndSetters() {
        GeoQuery query = new GeoQuery(null);
        GeoLocation location = new GeoLocation(1.0d, 2.0d);

        query.setLocation(location);
        assertEquals(location, query.getLocation());

        query.setIp("123.45.67.89");
        assertEquals("123.45.67.89", query.getIp());

        query.setAccuracy("10m");
        assertEquals("10m", query.getAccuracy());

        query.setGranularity("city");
        assertEquals("city", query.getGranularity());

        query.setMaxResults(5);
        assertEquals(5, query.getMaxResults());
    }

    @Test
    public void testEqualsAndHashCode() {
        GeoLocation location1 = new GeoLocation(1.0d, 2.0d);
        GeoLocation location2 = new GeoLocation(1.0d, 2.0d);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery(null);
        GeoQuery query4 = new GeoQuery("123.45.67.89");

        assertEquals(query1, query1);
        assertEquals(query1, query1);
        assertEquals(query1, query2);
        assertNotEquals(query1, query3);
        assertNotEquals(query1, query4);
        assertEquals(query2, query2);
        assertEquals(query2, query1);
        assertNotEquals(query2, query3);
        assertNotEquals(query2, query4);
        assertEquals(query3, query3);
        assertEquals(query3, query3);
        assertNotEquals(query3, query1);
        assertNotEquals(query3, query2);
        assertNotEquals(query3, query4);
        assertEquals(query4, query4);
        assertEquals(query4, query4);
        assertNotEquals(query4, query1);
        assertNotEquals(query4, query2);
        assertNotEquals(query4, query3);

        int hash1 = query1.hashCode();
        int hash2 = query1.hashCode();
        int hash3 = query3.hashCode();
        int hash4 = query4.hashCode();

        assertEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
        assertNotEquals(hash1, hash4);
        assertEquals(hash2, hash2);
        assertEquals(hash3, hash3);
        assertNotEquals(hash3, hash1);
        assertNotEquals(hash3, hash2);
        assertNotEquals(hash3, hash4);
        assertEquals(hash4, hash4);
        assertEquals(hash4, hash4);
        assertNotEquals(hash4, hash1);
        assertNotEquals(hash4, hash2);
        assertNotEquals(hash4, hash3);
    }
}