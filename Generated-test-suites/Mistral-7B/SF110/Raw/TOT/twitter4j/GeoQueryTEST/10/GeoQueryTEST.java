
package twitter4j; // make sure to import all necessary classes and dependencies

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class GeoQueryTest {

    GeoLocation location;

    @Before
    public void setUp() {
        location = new GeoLocation(51.5072, -0.1278); // valid latitude and longitude values
    }

    @After
    public void tearDown() {
        location = null;
    }

    @Test
    public void testGeoQueryWithValidLocation() {
        GeoQuery geoQuery = new GeoQuery(location);
        assertNotNull(geoQuery.getLocation()); // the location should not be null
    }

    @Test
    public void testGetAndSetIp() {
        GeoQuery geoQuery = new GeoQuery();
        String expectedIp = "192.168.1.1";
        geoQuery.setIp(expectedIp);
        String actualIp = geoQuery.getIp();
        assertNotNull(actualIp);
        assertEquals(expectedIp, actualIp);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(51.5072, -0.1278);
        GeoLocation location2 = new GeoLocation(51.5072, -0.1278);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery();

        assertFalse(query1.equals(query3)); // different instances
        assertTrue(query1.equals(query1)); // same instance
        assertTrue(query1.equals(query2)); // same location
        GeoLocation location3 = new GeoLocation(51.5073, -0.1278);
        assertFalse(query1.equals(new GeoQuery(location3))); // different location
    }
}
