package twitter4j;

import org.junit.*;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

@RunWith(JUnit4.class)
public class GeoQueryTest {
    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        GeoLocation returnedLocation = geoQuery.getLocation();
        assertEquals(location, returnedLocation);
    }

    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        String accuracy = "10m";
        GeoQuery geoQueryWithAccuracy = geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQueryWithAccuracy.getAccuracy());
    }

    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        int maxResults = 10;
        GeoQuery geoQueryWithMaxResults = geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQueryWithMaxResults.getMaxResults());
    }

    @Test
    public void testSetLocation() {
        GeoLocation originalLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(originalLocation);
        GeoLocation newLocation = new GeoLocation(3.0, 4.0);
        GeoQuery geoQueryWithNewLocation = geoQuery.setLocation(newLocation);
        GeoLocation returnedLocation = geoQueryWithNewLocation.getLocation();
        assertEquals(newLocation, returnedLocation);
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("10m");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(10);

        HttpParameter[] expectedParamArray = new HttpParameter[]{
                new HttpParameter("lat", "1.0"),
                new HttpParameter("long", "2.0"),
                new HttpParameter("accuracy", "10m"),
                new HttpParameter("granularity", "city"),
                new HttpParameter("max_results", "10")
        };

        HttpParameter[] actualParamArray = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParamArray, actualParamArray);
    }

    // Other test cases for methods like setGranularity(), equals(), and hashCode() here...
}