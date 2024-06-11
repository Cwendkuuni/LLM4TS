import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGeoQueryWithLocation() {
        // Given
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);

        // When
        GeoLocation actualLocation = query.getLocation();

        // Then
        assertEquals(location, actualLocation);
    }
}