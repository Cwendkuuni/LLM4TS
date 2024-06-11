import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {
    // 

    @Test
    public void testGeoQueryConstructionWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0); // valid latitude and longitude
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(location, actualLocation);
        assertNull(geoQuery.getIp());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length); // check that the correct number of parameters are added
        assertTrue(params[0] instanceof HttpParameter);
        assertTrue(params[0].getName().equals("lat"));
        assertTrue(params[0].getValue().equals("1.0"));

        assertTrue(params[1] instanceof HttpParameter);
        assertTrue(params[1].getName().equals("long"));
        assertTrue(params[1].getValue().equals("2.0"));

        assertNull(params[2]); // accuracy is null
        assertNull(params[3]); // granularity is null
        assertNull(params[4]); // maxResults is default value
    }

    // 
}