import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGeoQueryWithLocation() {
        double latitude = 37.4220;
        double longitude = -122.0841;
        GeoLocation location = new GeoLocation(latitude, longitude);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(location, actualLocation);
        assertNull(geoQuery.getIp());
    }
}