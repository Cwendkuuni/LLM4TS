package twitter4j;
import org.junit.Assert;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.23456789, 2.34567890);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        Assert.assertEquals(expectedLocation, actualLocation);
    }
}