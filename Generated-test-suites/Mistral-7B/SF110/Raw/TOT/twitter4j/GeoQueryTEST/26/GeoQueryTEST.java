import org.junit.Assert;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    @Test
    public void testGeoQueryWithLocation() {
        // Given
        GeoLocation location = new GeoLocation(51.5074, -0.1278);
        GeoQuery geoQuery = new GeoQuery(location);
        
        // Then
        Assert.assertNotNull(geoQuery.getLocation());
        Assert.assertNull(geoQuery.getIp());
        
        // Verify that the GeoQuery object was constructed correctly
        HttpParameter[] httpParams = geoQuery.asHttpParameterArray();
        Assert.assertEquals(5, httpParams.length);
        Assert.assertEquals("lat", httpParams[0].getName());
        Assert.assertEquals("51.5074", httpParams[0].getValue());
        Assert.assertEquals("long", httpParams[1].getName());
        Assert.assertEquals("-0.1278", httpParams[1].getValue());
        Assert.assertNull(httpParams[2]);
        Assert.assertNull(httpParams[3]);
        Assert.assertEquals("max_results", httpParams[4].getName());
        Assert.assertTrue(String.valueOf(geoQuery.getMaxResults()).equals(httpParams[4].getValue()));
    }
}