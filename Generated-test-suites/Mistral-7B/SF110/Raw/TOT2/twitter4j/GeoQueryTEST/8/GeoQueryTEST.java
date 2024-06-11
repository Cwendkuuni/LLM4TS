import org.junit.Assert;
import twitter4j.GeoLocation;

public class GeoQueryTest {

    @org.junit.Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        Assert.assertEquals(expectedLocation, actualLocation);
    }

    // Edge case: Testing with null location
    @org.junit.Test(expected = NullPointerException.class)
    public void testGetLocationNullLocation() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.getLocation();
    }
}