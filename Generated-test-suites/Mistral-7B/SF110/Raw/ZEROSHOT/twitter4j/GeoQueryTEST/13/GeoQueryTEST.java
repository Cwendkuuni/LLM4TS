import org.junit.Before;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeoQueryTest {

    private GeoQuery geoQuery;

    @Before
    public void setUp() {
        double latitude = 37.42199535;
        double longitude = -122.08405635;
        GeoLocation location = new GeoLocation(latitude, longitude);
        this.geoQuery = new GeoQuery(location);
    }

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation expectedLocation = new GeoLocation(37.42199535, -122.08405635);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(expectedLocation.getLatitude(), actualLocation.getLatitude(), 0.0);
        assertEquals(expectedLocation.getLongitude(), actualLocation.getLongitude(), 0.0);
    }

    @Test
    public void testGeoQueryWithIp() {
        String ip = "192.168.1.1";
        geoQuery = new GeoQuery(ip);
        assertNotNull(geoQuery.getIp());
        assertEquals(geoQuery.getIp(), ip);
    }

    @Test
    public void testGettersAndSetters() {
        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 10;

        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testAsHttpParameterArray() {
        List<HttpParameter> expectedParameters = new ArrayList<>();
        expectedParameters.add(new HttpParameter("lat", Double.toString(geoQuery.getLocation().getLatitude())));
        expectedParameters.add(new HttpParameter("long", Double.toString(geoQuery.getLocation().getLongitude())));

        HttpParameter[] actualParameters = geoQuery.asHttpParameterArray();
        assertEquals(expectedParameters.size(), actualParameters.length);

        for (int i = 0; i < expectedParameters.size(); i++) {
            assertEquals(expectedParameters.get(i).getName(), actualParameters[i].getName());
            assertEquals(expectedParameters.get(i).getValue(), actualParameters[i].getValue());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxResults() {
        int maxResults = -1;
        geoQuery.setMaxResults(maxResults);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLocation() {
        geoQuery = new GeoQuery(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullIp() {
        geoQuery = new GeoQuery(null);
    }
}