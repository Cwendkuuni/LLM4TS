package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(51.5072, -0.1278);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertNotNull(params);
        assertEquals(5, params.length);

        for (HttpParameter param : params) {
            System.out.println(param.getName() + "=" + param.getValue());
        }

        // Add more assertions as needed
    }

    @Test
    public void testGeoQueryWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertNotNull(params);
        assertEquals(4, params.length);

        for (HttpParameter param : params) {
            System.out.println(param.getName() + "=" + param.getValue());
        }

        // Add more assertions as needed
    }

    @Test
    public void testGettersAndSetters() {
        GeoQuery geoQuery = new GeoQuery(null);

        geoQuery.setAccuracy("city");
        assertEquals("city", geoQuery.getAccuracy());

        geoQuery.setAccuracy(null);
        assertNull(geoQuery.getAccuracy());

        geoQuery.setGranularity("neighborhood");
        assertEquals("neighborhood", geoQuery.getGranularity());

        geoQuery.setGranularity(null);
        assertNull(geoQuery.getGranularity());

        geoQuery.setMaxResults(10);
        assertEquals(10, geoQuery.getMaxResults());

        // Add more test cases as needed
    }

    @Test
    public void testEqualsAndHashCode() {
        GeoQuery geoQuery1 = new GeoQuery(null);
        GeoQuery geoQuery2 = new GeoQuery(null);

        GeoLocation location1 = new GeoLocation(51.5072, -0.1278);
        GeoLocation location2 = new GeoLocation(51.5072, -0.1278);

        GeoQuery geoQuery3 = new GeoQuery(location1);
        GeoQuery geoQuery4 = new GeoQuery(location2);

        GeoQuery geoQuery5 = new GeoQuery("192.168.0.1");
        GeoQuery geoQuery6 = new GeoQuery("192.168.0.2");

        GeoQuery geoQuery7 = new GeoQuery(null);
        geoQuery7.setAccuracy("city");
        geoQuery7.setGranularity("neighborhood");
        geoQuery7.setMaxResults(10);

        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
        assertNotEquals(geoQuery3, geoQuery4);
        assertNotEquals(geoQuery1, geoQuery5);
        assertNotEquals(geoQuery5, geoQuery6);
        assertEquals(geoQuery7, geoQuery7);

        // Add more test cases as needed
    }
}