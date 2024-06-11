package twitter4j.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGeoQueryConstructionWithLocation() {
        GeoLocation location = new GeoLocation(1.0d, 2.0d);
        GeoQuery geoQuery = new GeoQuery(location);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(location, actualLocation);
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testGetIp() {
        GeoQuery geoQuery = new GeoQuery("123.45.67.89");
        String expectedIp = "123.45.67.89";
        String actualIp = geoQuery.getIp();
        assertEquals(expectedIp, actualIp);
        assertNull(geoQuery.getLocation());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0d, 2.0d);
        GeoLocation location2 = new GeoLocation(1.0d, 2.0d);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location1);
        GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(1.1d, 2.1d));
        GeoQuery geoQuery5 = new GeoQuery("123.45.67.89");

        assertTrue(geoQuery1.equals(geoQuery3));
        assertTrue(geoQuery1.equals(geoQuery1));
        assertFalse(geoQuery1.equals(null));
        assertFalse(geoQuery1.equals(geoQuery5));
        assertFalse(geoQuery1.equals(new Object()));
        assertFalse(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery2.equals(geoQuery4));
    }
}