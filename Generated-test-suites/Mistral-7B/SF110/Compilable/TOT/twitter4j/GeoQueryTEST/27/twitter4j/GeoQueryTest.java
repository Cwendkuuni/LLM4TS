package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    // Test start
    @Test
    public void testGeoQueryWithLocation() {
        // Given
        GeoLocation location = new GeoLocation(51.5074, 0.0828);
        GeoQuery query = new GeoQuery(location);

        // Then
        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
    }
    // Test end
}