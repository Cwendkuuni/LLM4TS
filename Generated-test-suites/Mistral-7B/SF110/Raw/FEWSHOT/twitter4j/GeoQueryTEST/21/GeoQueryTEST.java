package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import twitter4j.GeoLocation;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {
    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length);
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                assertEquals("lat", params[i].getName());
                assertEquals("1.0", params[i].getValue());
            } else if (i == 1) {
                assertEquals("long", params[i].getName());
                assertEquals("2.0", params[i].getValue());
            } else if (i == 2) {
                assertNull(params[i]);
            } else if (i == 3) {
                assertEquals("accuracy", params[i].getName());
                assertNull(params[i].getValue());
            } else if (i == 4) {
                assertEquals("granularity", params[i].getName());
                assertNull(params[i].getValue());
            }
        }
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length);
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                assertNull(params[i]);
            } else if (i == 1) {
                assertEquals("ip", params[i].getName());
                assertEquals(ip, params[i].getValue());
            } else if (i == 2) {
                assertNull(params[i].getValue());
                assertEquals("location", params[i].getName());
            } else if (i == 3) {
                assertEquals("accuracy", params[i].getName());
                assertNull(params[i].getValue());
            } else if (i == 4) {
                assertEquals("granularity", params[i].getName());
                assertNull(params[i].getValue());
            }
        }
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        assertEquals(1.0, geoQuery.getLocation().getLatitude(), 0.0);
        assertEquals(2.0, geoQuery.getLocation().getLongitude(), 0.0);
        assertNull(geoQuery.getIp());
        geoQuery.setAccuracy("accurate");
        assertEquals("accurate", geoQuery.getAccuracy());
        geoQuery.setGranularity("city");
        assertEquals("city", geoQuery.getGranularity());
        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0, 2.1));
        GeoQuery geoQuery4 = new GeoQuery();

        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(geoQuery3));
        assertFalse(geoQuery1.equals(geoQuery4));

        geoQuery1.setAccuracy("accurate");
        geoQuery2.setAccuracy("precise");
        assertFalse(geoQuery1.equals(geoQuery2));

        geoQuery1.setGranularity("city");
        geoQuery2.setGranularity("neighborhood");
        assertFalse(geoQuery1.equals(geoQuery2));

        geoQuery1.setIp("192.168.1.1");
        geoQuery2.setIp("192.168.1.2");
        assertFalse(geoQuery1.equals(geoQuery2));

        geoQuery1.setLocation(null);
        geoQuery2.setLocation(null);
        assertTrue(geoQuery1.equals(geoQuery2));

        geoQuery1.setLocation(location1);
        geoQuery2.setLocation(location2);
        assertFalse(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0, 2.1));
        GeoQuery geoQuery4 = new GeoQuery();

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery1.hashCode();
        assertEquals(hashCode1, hashCode2);

        geoQuery1.setAccuracy("accurate");
        int hashCode3 = geoQuery1.hashCode();
        assertEquals(hashCode1, hashCode3);

        geoQuery1.setGranularity("city");
        int hashCode4 = geoQuery1.hashCode();
        assertNotEquals(hashCode1, hashCode4);

        geoQuery1.setIp("192.168.1.1");
        int hashCode5 = geoQuery1.hashCode();
        assertNotEquals(hashCode1, hashCode5);

        geoQuery1.setLocation(null);
        int hashCode6 = geoQuery1.hashCode();
        assertEquals(hashCode1, hashCode6);

        geoQuery1.setLocation(location1);
        int hashCode7 = geoQuery1.hashCode();
        assertNotEquals(hashCode1, hashCode7);

        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());

        assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
        assertNotEquals(geoQuery1.hashCode(), geoQuery4.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        String expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='', maxResults=}";
        assertEquals(expected, geoQuery.toString());
    }
}