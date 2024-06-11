package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testConstructorWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testConstructorWithIp() {
        GeoQuery geoQuery = new GeoQuery("1.2.3.4");
        assertNull(geoQuery.getLocation());
        assertEquals("1.2.3.4", geoQuery.getIp());
    }

    @Test
    public void testGettersAndSetters() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertEquals(location, geoQuery.getLocation());

        String accuracy = "accurate";
        String granularity = "granular";
        int maxResults = 5;

        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(6, params.length);

        for (HttpParameter param : params) {
            if (param.getName().equals("lat")) {
                assertEquals("lat", param.getName());
                assertEquals(String.valueOf(location.getLatitude()), param.getValue());
            } else if (param.getName().equals("long")) {
                assertEquals("long", param.getName());
                assertEquals(String.valueOf(location.getLongitude()), param.getValue());
            }
            // Add more assertions for other parameters if needed
        }
    }

    @Test
    public void testEqualsAndHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery geoQuery3 = new GeoQuery(location1);
        GeoQuery geoQuery4 = new GeoQuery(null);
        GeoQuery geoQuery5 = new GeoQuery("123.45.67.890");

        assertTrue(geoQuery1.equals(geoQuery3));
        assertTrue(geoQuery1.equals(geoQuery1));
        assertFalse(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(geoQuery4));
        assertFalse(geoQuery1.equals(geoQuery5));

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();
        int hashCode3 = geoQuery3.hashCode();
        int hashCode4 = geoQuery4.hashCode();
        int hashCode5 = geoQuery5.hashCode();

        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode4);
        assertNotSame(hashCode1, hashCode5);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='', maxResults=0}";
        assertEquals(expected, geoQuery.toString());
    }
}