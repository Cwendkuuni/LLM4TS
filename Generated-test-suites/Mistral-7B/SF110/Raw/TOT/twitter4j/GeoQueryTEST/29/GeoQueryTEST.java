import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(12.971598d, 77.594562d); // valid latitude and longitude values
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(expectedLocation.getLatitude(), expectedLocation.getLongitude()));
        GeoLocation actualLocation = geoQuery.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }
}