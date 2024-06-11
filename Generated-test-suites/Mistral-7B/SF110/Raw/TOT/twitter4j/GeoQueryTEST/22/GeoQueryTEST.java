import org.junit.Assert;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @org.junit.Test
    public void testGeoQueryWithLocation() {
        // Given
        double latitude = 40.7128;
        double longitude = -74.0060;
        GeoLocation location = new GeoLocation(latitude, longitude);
        GeoQuery geoQuery = new GeoQuery(location);

        // When
        GeoLocation actualLocation = geoQuery.getLocation();

        // Then
        Assert.assertEquals(location, actualLocation);
        Assert.assertNull(geoQuery.getIp());
    }
}