package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals(location, geoQuery.getLocation());
        assertNull(geoQuery.getIp());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("lat", "1.0"));
        expectedParams.add(new HttpParameter("long", "2.0"));
        assertArrayEquals(expectedParams.toArray(new HttpParameter[expectedParams.size()]), params);
    }

    @Test
    public void testGeoQueryWithIp() {
        String ip = "127.0.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        assertNull(geoQuery.getLocation());
        assertEquals(ip, geoQuery.getIp());

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        List<HttpParameter> expectedParams = new ArrayList<>();
        expectedParams.add(new HttpParameter("ip", ip));
        assertArrayEquals(expectedParams.toArray(new HttpParameter[expectedParams.size()]), params);
    }

    @Test
    public void testSettersAndGetters() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));

        String accuracy = "accurate";
        geoQuery.setAccuracy(accuracy);
        assertEquals(accuracy, geoQuery.getAccuracy());

        String granularity = "granular";
        geoQuery.setGranularity(granularity);
        assertEquals(granularity, geoQuery.getGranularity());

        int maxResults = 5;
        geoQuery.setMaxResults(maxResults);
        assertEquals(maxResults, geoQuery.getMaxResults());
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(3.0, 4.0));
        GeoQuery geoQuery4 = new GeoQuery(null);

        assertEquals(geoQuery1, geoQuery1);
        assertEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
        assertNotEquals(geoQuery1, geoQuery4);

        geoQuery1.setAccuracy("accurate1");
        geoQuery2.setAccuracy("accurate2");
        assertNotEquals(geoQuery1, geoQuery2);

        geoQuery1.setGranularity("granular1");
        geoQuery2.setGranularity("granular2");
        assertNotEquals(geoQuery1, geoQuery2);

        geoQuery1.setIp("192.168.1.1");
        geoQuery2.setIp("192.168.1.2");
        assertNotEquals(geoQuery1, geoQuery2);

        geoQuery1.setLocation(location1);
        geoQuery2.setLocation(location2);
        assertEquals(geoQuery1, geoQuery2);
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(3.0, 4.0));
        GeoQuery geoQuery4 = new GeoQuery(null);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery1.hashCode();
        assertEquals(hashCode1, hashCode2);

        geoQuery1.setAccuracy("accurate1");
        int hashCode3 = geoQuery1.hashCode();
        assertNotEquals(hashCode1, hashCode3);

        geoQuery1.setGranularity("granular1");
        int hashCode4 = geoQuery1.hashCode();
        assertNotEquals(hashCode1, hashCode4);

        geoQuery1.setIp("192.168.1.1");
        int hashCode5 = geoQuery1.hashCode();
        assertNotEquals(hashCode1, hashCode5);

        geoQuery1.setLocation(location2);
        int hashCode6 = geoQuery1.hashCode();
        assertNotEquals(hashCode1, hashCode6);

        assertEquals(hashCode1, geoQuery2.hashCode());
        assertNotEquals(hashCode1, geoQuery3.hashCode());
        assertNotEquals(hashCode1, geoQuery4.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        assertEquals("GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='', maxResults=0}", geoQuery.toString());

        geoQuery.setAccuracy("accurate");
        assertEquals("GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='accurate', granularity='', maxResults=0}", geoQuery.toString());

        geoQuery.setGranularity("granular");
        assertEquals("GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='', granularity='granular', maxResults=0}", geoQuery.toString());

        geoQuery.setIp("127.0.0.1");
        assertEquals("GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='127.0.0.1', accuracy='', granularity='', maxResults=0}", geoQuery.toString());

        geoQuery.setMaxResults(5);
        assertEquals("GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='127.0.0.1', accuracy='', granularity='', maxResults=5}", geoQuery.toString());
    }
}