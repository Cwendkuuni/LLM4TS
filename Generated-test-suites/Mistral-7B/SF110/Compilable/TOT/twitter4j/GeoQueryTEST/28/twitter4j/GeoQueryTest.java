package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }
}