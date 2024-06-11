package twitter4j;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertNotNull(geoQuery.getLocation());
        // Add more tests for edge cases and error scenarios here
    }
}