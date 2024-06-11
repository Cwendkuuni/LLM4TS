package twitter4j;
import org.junit.Assert;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0d, 2.0d);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        Assert.assertEquals(expectedLocation.getLatitude(), actualLocation.getLatitude(), 0.001d);
        Assert.assertEquals(expectedLocation.getLongitude(), actualLocation.getLongitude(), 0.001d);
    }
}