import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithValidLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);
        assertEquals(location, query.getLocation());
        assertNull(query.getIp());
        //More tests can be added here
    }

    @Test
    public void testGeoQueryWithNullLocation() {
        GeoQuery query = new GeoQuery(null);
        assertNull(query.getLocation());
        assertNull(query.getIp());
    }

    //Add more test cases here
}