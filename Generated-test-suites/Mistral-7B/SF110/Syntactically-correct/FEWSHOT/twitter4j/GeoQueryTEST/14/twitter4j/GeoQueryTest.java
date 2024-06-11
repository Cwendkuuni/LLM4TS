package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        assertEquals(expectedLocation, geoQuery.getLocation());
        assertNull(geoQuery.getIp());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length);
        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (HttpParameter param : params) {
            names.add(param.getName());
            values.add(param.getValue());
        }
        assertEquals(Arrays.asList("lat", Double.toString(1.0), "long", Double.toString(2.0), "max_results", Integer.toString(0), "accuracy", null, "granularity", null), names);
        assertEquals(Arrays.asList(Double.toString(1.0), Double.toString(2.0), Integer.toString(0), null, null), values);
    }

    @Test
    public void testGeoQueryWithIp() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length);
        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (HttpParameter param : params) {
            names.add(param.getName());
            values.add(param.getValue());
        }
        assertEquals(Arrays.asList("ip", ip, "max_results", Integer.toString(0), "accuracy", null, "granularity", null), names);
        assertEquals(Arrays.asList(ip, Integer.toString(0), null, null), values);
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery geoQuery = new GeoQuery(null);

        String accuracy = "5km";
        String granularity = "city";
        int maxResults = 10;

        assertEquals(null, geoQuery.getAccuracy());
        assertEquals(null, geoQuery.getGranularity());
        assertEquals(0, geoQuery.getMaxResults());

        geoQuery.accuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        geoQuery.granularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        geoQuery.maxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length);
        List<String> names = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (HttpParameter param : params) {
            names.add(param.getName());
            values.add(param.getValue());
        }
        assertEquals(Arrays.asList("max_results", Integer.toString(maxResults), "accuracy", accuracy, "granularity", granularity), names);
        assertEquals(Arrays.asList(Integer.toString(maxResults), accuracy, granularity), values);
    }

    @Test
    public void testEqualsAndHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(null);
        GeoQuery geoQuery2 = new GeoQuery(null);
        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0, 2.0));

        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);

        GeoQuery geoQuery4 = new GeoQuery(geoQuery3);
        assertEquals(geoQuery3, geoQuery4);
        assertEquals(geoQuery3.hashCode(), geoQuery4.hashCode());

        GeoQuery geoQuery5 = new GeoQuery(geoQuery3);
        geoQuery5.setGranularity("neighborhood");
        assertNotEquals(geoQuery3, geoQuery5);
        assertNotEquals(geoQuery3.hashCode(), geoQuery5.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(location);

        String expectedToString = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='192.168.1.1', accuracy=null, granularity=null, maxResults=0}";
        assertEquals(expectedToString, geoQuery.toString());

        geoQuery.setIp(ip);
        String expectedToStringWithIp = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='192.168.1.1', accuracy=null, granularity=null, maxResults=0}";
        assertEquals(expectedToStringWithIp, geoQuery.toString());

        geoQuery.setAccuracy("5km");
        String expectedToStringWithAccuracy = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='192.168.1.1', accuracy='5km', granularity=null, maxResults=0}";
        assertEquals(expectedToStringWithAccuracy, geoQuery.toString());

        geoQuery.setGranularity("city");
        String expectedToStringWithGranularity = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='192.168.1.1', accuracy='5km', granularity='city', maxResults=0}";
        assertEquals(expectedToStringWithGranularity, geoQuery.toString());

        geoQuery.setMaxResults(10);
        String expectedToStringWithMaxResults = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='192.168.1.1', accuracy='5km', granularity='city', maxResults=10}";
        assertEquals(expectedToStringWithMaxResults, geoQuery.toString());
    }
}