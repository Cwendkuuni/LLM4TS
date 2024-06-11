package twitter4j;

import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] httpParameters = geoQuery.asHttpParameterArray();
        List<HttpParameter> expectedParameters = new ArrayList<>();
        geoQuery.appendParameter("lat", String.valueOf(location.getLatitude()), expectedParameters);
        geoQuery.appendParameter("long", String.valueOf(location.getLongitude()), expectedParameters);

        assertEquals(expectedParameters.size(), httpParameters.length);
        for (int i = 0; i < expectedParameters.size(); i++) {
            assertEquals(expectedParameters.get(i).getName(), httpParameters[i].getName());
            assertEquals(expectedParameters.get(i).getValue(), httpParameters[i].getValue());
        }
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        HttpParameter[] httpParameters = geoQuery.asHttpParameterArray();
        List<HttpParameter> expectedParameters = new ArrayList<>();
        geoQuery.appendParameter("ip", ip, expectedParameters);

        assertEquals(expectedParameters.size(), httpParameters.length);
        for (int i = 0; i < expectedParameters.size(); i++) {
            assertEquals(expectedParameters.get(i).getName(), httpParameters[i].getName());
            assertEquals(expectedParameters.get(i).getValue(), httpParameters[i].getValue());
        }
    }

    @Test
    public void testGettersAndSetters() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));

        assertEquals(geoQuery.getLocation(), geoQuery.getLocation());
        assertEquals(geoQuery.getIp(), null);

        String accuracy = "10m";
        geoQuery.setAccuracy(accuracy);
        assertEquals(geoQuery.getAccuracy(), accuracy);

        String granularity = "city";
        geoQuery.setGranularity(granularity);
        assertEquals(geoQuery.getGranularity(), granularity);

        int maxResults = 5;
        geoQuery.setMaxResults(maxResults);
        assertEquals(geoQuery.getMaxResults(), maxResults);
    }

    @Test
    public void testEqualsAndHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        String ip = "192.168.1.1";
        String ip2 = "192.168.1.2";
        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 5;

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(ip);
        GeoQuery geoQuery4 = new GeoQuery(ip2);
        GeoQuery geoQuery5 = new GeoQuery(location1);
        GeoQuery geoQuery6 = new GeoQuery();

        GeoQuery geoQuery7 = new GeoQuery(location1).accuracy(accuracy).granularity(granularity).maxResults(maxResults);
        GeoQuery geoQuery8 = new GeoQuery(location2).accuracy(accuracy).granularity(granularity).maxResults(maxResults);

        // Testing equals
        assertTrue(geoQuery1.equals(geoQuery5));
        assertFalse(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(geoQuery3));
        assertFalse(geoQuery1.equals(geoQuery4));
        assertFalse(geoQuery1.equals(geoQuery6));
        assertFalse(geoQuery1.equals(null));
        assertTrue(geoQuery7.equals(geoQuery8));

        // Testing hashCode
        int hashCode1 = geoQuery1.hashCode();
        int hashCode5 = geoQuery5.hashCode();
        assertEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        String ip = "192.168.1.1";
        String accuracy = "10m";
        String granularity = "city";
        int maxResults = 5;

        GeoQuery geoQuery = new GeoQuery(location).accuracy(accuracy).granularity(granularity).maxResults(maxResults);

        String expectedString = "GeoQuery{location=" + location + ", ip='" + null + "''" + ", accuracy='" + accuracy + "''" + ", granularity='" + granularity + "''" + ", maxResults=5}";
        assertEquals(expectedString, geoQuery.toString());
    }
}