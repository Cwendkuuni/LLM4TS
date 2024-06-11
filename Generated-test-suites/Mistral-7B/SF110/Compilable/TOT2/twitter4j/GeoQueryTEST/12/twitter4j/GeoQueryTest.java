package twitter4j;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(12.9715993, 77.5945627);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }
}