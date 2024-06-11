package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        List<HttpParameter> expectedParameters = new ArrayList<>();
        expectedParameters.add(new HttpParameter("lat", "1.0"));
        expectedParameters.add(new HttpParameter("long", "2.0"));

        HttpParameter[] actualParameters = geoQuery.asHttpParameterArray();

        assertArrayEquals(expectedParameters.toArray(new HttpParameter[0]), actualParameters);
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        List<HttpParameter> expectedParameters = new ArrayList<>();
        expectedParameters.add(new HttpParameter("ip", ip));

        HttpParameter[] actualParameters = geoQuery.asHttpParameterArray();

        assertArrayEquals(expectedParameters.toArray(new HttpParameter[0]), actualParameters);
    }

    @Test
    public void testGeoQueryWithBothLocationAndIP() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setIp(ip);

        List<HttpParameter> expectedParameters = new ArrayList<>();
        expectedParameters.add(new HttpParameter("lat", "1.0"));
        expectedParameters.add(new HttpParameter("long", "2.0"));
        expectedParameters.add(new HttpParameter("ip", ip));

        HttpParameter[] actualParameters = geoQuery.asHttpParameterArray();

        assertArrayEquals(expectedParameters.toArray(new HttpParameter[0]), actualParameters);
    }

    @Test
    public void testGettersAndSetters() {
        GeoQuery geoQuery = new GeoQuery(null);

        String accuracy = "accurate";
        String granularity = "granular";
        int maxResults = 10;

        geoQuery.setAccuracy(accuracy);
        geoQuery.setGranularity(granularity);
        geoQuery.setMaxResults(maxResults);

        assertEquals(accuracy, geoQuery.getAccuracy());
        assertEquals(granularity, geoQuery.getGranularity());
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testHashCodeAndEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery geoQuery3 = new GeoQuery(null);

        GeoQuery geoQuery4 = new GeoQuery(location1);
        geoQuery4.setIp("127.0.0.1");

        assertEquals(geoQuery1, geoQuery1);
        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
        assertNotEquals(geoQuery1, geoQuery4);
    }
}