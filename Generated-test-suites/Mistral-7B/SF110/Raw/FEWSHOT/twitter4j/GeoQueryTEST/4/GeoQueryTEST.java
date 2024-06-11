package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

public class GeoQueryTest {

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery("1.2.3.4");

        // equals with same object
        assertEquals(query1, query1);

        // equals with same value
        assertEquals(query1, query2);

        // equals with null location
        GeoQuery query4 = new GeoQuery(null);
        assertEquals(query1, query4);

        // equals with different location
        GeoLocation location3 = new GeoLocation(3.0, 4.0);
        GeoQuery query5 = new GeoQuery(location3);
        assertNotEquals(query1, query5);

        // equals with different ip
        GeoQuery query6 = new GeoQuery("5.6.7.8");
        assertNotEquals(query1, query6);

        // equals with different accuracy
        GeoQuery query7 = new GeoQuery(location1).accuracy("meter");
        assertNotEquals(query1, query7);

        // equals with different granularity
        GeoQuery query8 = new GeoQuery(location1).granularity("country");
        assertNotEquals(query1, query8);

        // equals with different maxResults
        GeoQuery query9 = new GeoQuery(location1).maxResults(10);
        assertNotEquals(query1, query9);
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery query1 = new GeoQuery(location1);
        GeoQuery query2 = new GeoQuery(location2);
        GeoQuery query3 = new GeoQuery("1.2.3.4");

        // hashcode with same object
        int hashCode1 = query1.hashCode();
        assertEquals(hashCode1, query1.hashCode());

        // hashcode with same value
        int hashCode2 = query1.hashCode();
        assertEquals(hashCode2, query2.hashCode());

        // hashcode with null location
        GeoQuery query4 = new GeoQuery(null);
        assertEquals(hashCode1, query4.hashCode());

        // hashcode with different location
        GeoLocation location3 = new GeoLocation(3.0, 4.0);
        GeoQuery query5 = new GeoQuery(location3);
        assertNotEquals(hashCode1, query5.hashCode());

        // hashcode with different ip
        GeoQuery query6 = new GeoQuery("5.6.7.8");
        assertNotEquals(hashCode1, query6.hashCode());

        // hashcode with different accuracy
        GeoQuery query7 = new GeoQuery(location1).accuracy("meter");
        assertNotEquals(hashCode1, query7.hashCode());

        // hashcode with different granularity
        GeoQuery query8 = new GeoQuery(location1).granularity("country");
        assertNotEquals(hashCode1, query8.hashCode());

        // hashcode with different maxResults
        GeoQuery query9 = new GeoQuery(location1).maxResults(10);
        assertNotEquals(hashCode1, query9.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);
        String expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='', maxResults=}";
        assertEquals(expected, query.toString());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery query = new GeoQuery(location);
        List<HttpParameter> params = new ArrayList<>();
        query.asHttpParameterArray(params);
        HttpParameter[] expectedParams = new HttpParameter[] {
                new HttpParameter("lat", String.valueOf(location.getLatitude())),
                new HttpParameter("long", String.valueOf(location.getLongitude()))
        };
        assertArrayEquals(expectedParams, params.toArray(expectedParams));
    }
}