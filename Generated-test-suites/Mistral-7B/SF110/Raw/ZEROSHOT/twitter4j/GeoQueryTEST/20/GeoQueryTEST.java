package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

public class GeoQueryTest {

    @Test
    public void testNewInstanceWithLocation() {
        GeoLocation location = new GeoLocation(1.0D, 2.0D);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(location, actualLocation);
        assertNull(geoQuery.getIp());
    }

    @Test
    public void testNewInstanceWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGettersAndSetters() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0D, 2.0D));

        String accuracy = "10m";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        String granularity = "city";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        int maxResults = 10;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testSettersReturnThis() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0D, 2.0D));

        GeoQuery accuracySetter = geoQuery.accuracy("50m");
        assertEquals(geoQuery, accuracySetter);
        assertEquals("50m", accuracySetter.getAccuracy());

        GeoQuery granularitySetter = accuracySetter.granularity("country");
        assertEquals(accuracySetter, granularitySetter);
        assertEquals("country", granularitySetter.getGranularity());

        GeoQuery maxResultsSetter = granularitySetter.maxResults(5);
        assertEquals(granularitySetter, maxResultsSetter);
        assertEquals(5, maxResultsSetter.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0D, 2.0D));
        geoQuery.setAccuracy("10m").setGranularity("city").setMaxResults(5);

        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("lat", "1.0"));
        expectedParams.add(new HttpParameter("long", "2.0"));
        expectedParams.add(new HttpParameter("accuracy", "10m"));
        expectedParams.add(new HttpParameter("granularity", "city"));
        expectedParams.add(new HttpParameter("max_results", "5"));

        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParams.toArray(new HttpParameter[expectedParams.size()]), actualParams);
    }

    @Test
    public void testEquals() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0D, 2.0D));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0D, 2.0D));
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0D, 3.0D));

        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(geoQuery3));
    }

    @Test
    public void testHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0D, 2.0D));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0D, 2.0D));
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0D, 3.0D));

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();
        int hashCode3 = geoQuery3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0D, 2.0D));
        geoQuery.setAccuracy("10m").setGranularity("city").setMaxResults(5);

        String expectedString = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='', accuracy='10m', granularity='city', maxResults=5}";
        String actualString = geoQuery.toString();
        assertEquals(expectedString, actualString);
    }
}