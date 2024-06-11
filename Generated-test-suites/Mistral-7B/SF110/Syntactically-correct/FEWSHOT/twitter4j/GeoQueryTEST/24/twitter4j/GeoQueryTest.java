package twitter4j; // make sure to import necessary classes

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

        HttpParameter[] httpParameters = geoQuery.asHttpParameterArray();
        assertEquals(5, httpParameters.length);
        for (HttpParameter httpParameter : httpParameters) {
            if (httpParameter.getName().equals("lat")) {
                assertEquals("1.0", httpParameter.getValue());
            } else if (httpParameter.getName().equals("long")) {
                assertEquals("2.0", httpParameter.getValue());
            }
        }
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());

        HttpParameter[] httpParameters = geoQuery.asHttpParameterArray();
        assertEquals(5, httpParameters.length);
        for (HttpParameter httpParameter : httpParameters) {
            if (httpParameter.getName().equals("ip")) {
                assertEquals(ip, httpParameter.getValue());
            }
        }
    }

    @Test
    public void testSetAndGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String accuracy = "coarse";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());
    }

    @Test
    public void testSetAndGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String granularity = "fine";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());
    }

    @Test
    public void testSetAndGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery geoQuery3 = new GeoQuery(location1);

        GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(1.1, 2.1));

        assertTrue(geoQuery1.equals(geoQuery3));
        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));

        assertFalse(geoQuery1.equals(null));
        assertFalse(geoQuery1.equals(new Object()));

        assertFalse(geoQuery1.equals(geoQuery4));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}