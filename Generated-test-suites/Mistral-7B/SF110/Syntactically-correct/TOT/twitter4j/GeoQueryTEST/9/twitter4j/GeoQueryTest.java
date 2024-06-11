import org.junit.Assert;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        // Given: Initialize a GeoQuery object with a valid location
        GeoLocation expectedLocation = new GeoLocation(51.5074, 0.0822);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);

        // When: Call the getLocation() method
        GeoLocation actualLocation = geoQuery.getLocation();

        // Then: Verify that the locations are equal
        Assert.assertEquals(expectedLocation, actualLocation);
    }
}