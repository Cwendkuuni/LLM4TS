package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(51.5072F, -0.1278F);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] parameters = geoQuery.asHttpParameterArray();
        assertEquals(5, parameters.length);

        for (HttpParameter parameter : parameters) {
            System.out.println(parameter.getName() + "=" + parameter.getValue());
        }

        assertEquals("lat", parameters[0].getName());
        assertEquals("51.5072", parameters[0].getValue());
        assertEquals("long", parameters[1].getName());
        assertEquals("-0.1278", parameters[1].getValue());
        assertNull(parameters[2]);
        assertNull(parameters[3]);
        assertEquals("max_results", parameters[4].getName());
        assertEquals("", parameters[4].getValue());
    }

    @Test
    public void testGeoQueryWithIP() {
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");

        HttpParameter[] parameters = geoQuery.asHttpParameterArray();
        assertEquals(2, parameters.length);

        for (HttpParameter parameter : parameters) {
            System.out.println(parameter.getName() + "=" + parameter.getValue());
        }

        assertEquals("ip", parameters[0].getName());
        assertEquals("192.168.1.1", parameters[0].getValue());
        assertNull(parameters[1]);
    }

    @Test
    public void testGeoQueryWithLocationAndIP() {
        GeoLocation location = new GeoLocation(51.5072F, -0.1278F);
        GeoQuery geoQuery = new GeoQuery("192.168.1.1");
        geoQuery.maxResults(5);

        HttpParameter[] parameters = geoQuery.asHttpParameterArray();
        assertEquals(5, parameters.length);

        for (HttpParameter parameter : parameters) {
            System.out.println(parameter.getName() + "=" + parameter.getValue());
        }

        assertEquals("ip", parameters[0].getName());
        assertEquals("192.168.1.1", parameters[0].getValue());
        assertEquals("lat", parameters[1].getName());
        assertEquals("51.5072", parameters[1].getValue());
        assertEquals("long", parameters[2].getName());
        assertEquals("-0.1278", parameters[2].getValue());
        assertEquals("max_results", parameters[4].getName());
        assertEquals("5", parameters[4].getValue());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(51.5072F, -0.1278F);
        GeoLocation location2 = new GeoLocation(51.5072F, -0.1278F);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location1);

        assertTrue(geoQuery1.equals(geoQuery1));
        assertTrue(geoQuery1.equals(geoQuery3));
        assertTrue(geoQuery1.equals(geoQuery2));

        GeoQuery geoQuery4 = new GeoQuery(location1);
        geoQuery4.setMaxResults(6);
        assertFalse(geoQuery1.equals(geoQuery4));

        GeoQuery geoQuery5 = new GeoQuery("192.168.1.1");
        assertFalse(geoQuery1.equals(geoQuery5));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(51.5072F, -0.1278F);
        GeoLocation location2 = new GeoLocation(51.5072F, -0.1278F);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();

        assertEquals(hashCode1, hashCode2);

        GeoQuery geoQuery3 = new GeoQuery(location1);
        geoQuery3.setMaxResults(6);
        assertNotEquals(hashCode1, geoQuery3.hashCode());

        GeoQuery geoQuery4 = new GeoQuery("192.168.1.1");
        assertNotEquals(hashCode1, geoQuery4.hashCode());
    }
}