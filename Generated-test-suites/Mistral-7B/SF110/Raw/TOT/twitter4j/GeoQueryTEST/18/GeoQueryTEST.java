import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGeoQueryWithValidLocation() {
        GeoLocation location = new GeoLocation(51.507351, -0.127758);
        GeoQuery geoQuery = new GeoQuery(location);
        assertNull(geoQuery.getIp());
        assertNotNull(geoQuery.getLocation());
        // Add more assertions here as needed
    }
}