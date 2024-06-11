import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        // create a GeoQuery object with a valid location
        GeoLocation expectedLocation = new GeoLocation(12.971598, 77.594562);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);

        // call the getLocation() method and check if it returns the expected location
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(expectedLocation.getLatitude(), actualLocation.getLatitude());
        assertEquals(expectedLocation.getLongitude(), actualLocation.getLongitude());
    }
}