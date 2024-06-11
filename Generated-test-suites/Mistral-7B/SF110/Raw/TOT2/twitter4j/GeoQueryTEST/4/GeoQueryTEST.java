package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testGetIp() {
        GeoQuery geoQuery = new GeoQuery();
        assertNull(geoQuery.getIp());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIpWithNullInput() {
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.setIp(null);
    }

    @Test
    public void testGetIpWithEmptyStringInput() {
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.setIp("");
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);

        assertTrue(query1.equals(query2));
    }

    @Test
    public void testEqualsWithSameObject() {
        GeoQuery query1 = new GeoQuery();
        assertTrue(query1.equals(query1));
    }

    @Test
    public void testEqualsWithDifferentObjectsHavingTheSameValues() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location1);

        assertTrue(query1.equals(query2));
    }

    @Test
    public void testEqualsWithDifferentObjectsHavingDifferentValues() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(3.0, 4.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);

        assertFalse(query1.equals(query2));
    }

    @Test
    public void testEqualsWithNullInput() {
        GeoQuery query1 = new GeoQuery();
        assertFalse(query1.equals(null));
    }

    @Test
    public void testHashCode() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location);
        GeoQuery query2 = new GeoQuery(location);

        assertEquals(query1.hashCode(), query2.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        assertEquals("GeoQuery{location=GeoLocation{lat=1.0, lng=2.0}, ip=null, accuracy=null, granularity=null, maxResults=0}", query.toString());
    }
}