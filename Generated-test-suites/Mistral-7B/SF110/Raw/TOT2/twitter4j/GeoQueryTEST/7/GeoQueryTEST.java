package twitter4j.test;

import static org.junit.Assert.*; import org.junit.Test; import twitter4j.*; import twitter4j.internal.http.*;

public class GeoQueryTest {

    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertNotNull(actualLocation);
        assertEquals(expectedLocation, actualLocation);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidLocationAndIpConstructor() {
        GeoQuery geoQuery = new GeoQuery(null);
    }

    @Test
    public void testEmptyAccuracyAndGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy("");
        geoQuery.setGranularity("");
        assertNotNull(geoQuery.getAccuracy());
        assertNotNull(geoQuery.getGranularity());
        assertEquals("", geoQuery.getAccuracy());
        assertEquals("", geoQuery.getGranularity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setMaxResults(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setAccuracy("unsupported_accuracy");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        geoQuery.setGranularity("unsupported_granularity");
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        HttpParameter[] httpParameters = geoQuery.asHttpParameterArray();
        assertEquals(6, httpParameters.length);
        for (HttpParameter parameter : httpParameters) {
            assertNotNull(parameter.getName());
            assertNotNull(parameter.getValue());
        }
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(3.0, 3.0));
        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
    }
}