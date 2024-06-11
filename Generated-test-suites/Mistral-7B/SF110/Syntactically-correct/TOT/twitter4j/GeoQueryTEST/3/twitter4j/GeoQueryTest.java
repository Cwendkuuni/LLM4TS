import org.junit.Assert;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation expectedLocation = location;
        String expectedIp = null;
        String expectedAccuracy = null;
        String expectedGranularity = null;
        int expectedMaxResults = GeoQuery.DEFAULT_MAX_RESULTS;

        GeoLocation actualLocation = geoQuery.getLocation();
        String actualIp = geoQuery.getIp();
        String actualAccuracy = geoQuery.getAccuracy();
        String actualGranularity = geoQuery.getGranularity();
        int actualMaxResults = geoQuery.getMaxResults();

        Assert.assertEquals(expectedLocation, actualLocation);
        Assert.assertNull(actualIp);
        Assert.assertNull(actualAccuracy);
        Assert.assertNull(actualGranularity);
        Assert.assertEquals(expectedMaxResults, actualMaxResults);
    }

    
}