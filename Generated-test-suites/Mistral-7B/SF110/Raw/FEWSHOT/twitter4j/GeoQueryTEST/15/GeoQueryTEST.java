package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        double lat = 40.7128;
        double lon = -74.0060;
        GeoLocation location = new GeoLocation(lat, lon);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] expectedParameters = new HttpParameter[]{
                new HttpParameter("lat", String.valueOf(lat)),
                new HttpParameter("long", String.valueOf(lon))
        };

        HttpParameter[] paramArray = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParameters, paramArray);
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        HttpParameter[] expectedParameters = new HttpParameter[]{
                new HttpParameter("ip", ip)
        };

        HttpParameter[] paramArray = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParameters, paramArray);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(40.7128, -74.0060);
        GeoLocation location2 = new GeoLocation(40.7128, -74.0060);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location1);
        GeoQuery geoQuery3 = new GeoQuery(location2);

        assertTrue(geoQuery1.equals(geoQuery2));
        assertTrue(geoQuery1.equals(geoQuery3));

        GeoQuery geoQuery4 = new GeoQuery();
        assertFalse(geoQuery1.equals(geoQuery4));

        GeoQuery geoQuery5 = new GeoQuery(null);
        assertFalse(geoQuery1.equals(geoQuery5));

        GeoQuery geoQuery6 = new GeoQuery(location1);
        geoQuery6.setAccuracy("accuracy1");
        assertFalse(geoQuery1.equals(geoQuery6));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery geoQuery1 = new GeoQuery(location);
        GeoQuery geoQuery2 = new GeoQuery(location);

        int hashcode1 = geoQuery1.hashCode();
        int hashcode2 = geoQuery2.hashCode();

        assertEquals(hashcode1, hashcode2);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(40.7128, -74.0060);
        GeoQuery geoQuery = new GeoQuery(location);

        String expectedString = "GeoQuery{location=" + location + ", ip='' , accuracy='' , granularity='' , maxResults=0}";
        String actualString = geoQuery.toString();

        assertEquals(expectedString, actualString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxResultsNegative() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setMaxResults(-1);
    }
}