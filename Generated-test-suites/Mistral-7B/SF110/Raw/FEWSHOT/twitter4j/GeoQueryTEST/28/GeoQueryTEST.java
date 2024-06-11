package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals("location=1.0,2.0", Arrays.toString(params[0].getName() + "=" + params[0].getValue()));

        GeoQuery newGeoQuery = new GeoQuery(geoQuery.getLocation());
        assertEquals(geoQuery, newGeoQuery);
    }

    @Test
    public void testGeoQueryWithIP() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals("ip=192.168.1.1", Arrays.toString(params[0].getName() + "=" + params[0].getValue()));

        GeoQuery newGeoQuery = new GeoQuery(geoQuery.getIp());
        assertEquals(geoQuery, newGeoQuery);
    }

    @Test
    public void testAccuracy() {
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.accuracy("high");

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals("accuracy=high", Arrays.toString(params[params.length - 1].getName() + "=" + params[params.length - 1].getValue()));

        GeoQuery newGeoQuery = new GeoQuery();
        newGeoQuery.accuracy("high");
        assertEquals(geoQuery, newGeoQuery);
    }

    @Test
    public void testGranularity() {
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.granularity("city");

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals("granularity=city", Arrays.toString(params[params.length - 1].getName() + "=" + params[params.length - 1].getValue()));

        GeoQuery newGeoQuery = new GeoQuery();
        newGeoQuery.granularity("city");
        assertEquals(geoQuery, newGeoQuery);
    }

    @Test
    public void testMaxResults() {
        GeoQuery geoQuery = new GeoQuery();
        geoQuery.maxResults(10);

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals("max_results=10", Arrays.toString(params[params.length - 1].getName() + "=" + params[params.length - 1].getValue()));

        GeoQuery newGeoQuery = new GeoQuery();
        newGeoQuery.maxResults(10);
        assertEquals(geoQuery, newGeoQuery);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        String ip = "192.168.1.1";
        String accuracy = "high";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location1);
        GeoQuery geoQuery3 = new GeoQuery(ip);
        GeoQuery geoQuery4 = new GeoQuery(location2);
        GeoQuery geoQuery5 = new GeoQuery(location1);
        GeoQuery geoQuery6 = new GeoQuery();
        GeoQuery geoQuery7 = new GeoQuery();

        GeoQuery geoQuery8 = new GeoQuery(location1);
        geoQuery8.accuracy(accuracy);
        GeoQuery geoQuery9 = new GeoQuery(location1);
        geoQuery9.granularity(granularity);
        GeoQuery geoQuery10 = new GeoQuery(location1);
        geoQuery10.maxResults(maxResults);

        GeoQuery geoQuery11 = new GeoQuery();
        geoQuery11.location = location1;
        GeoQuery geoQuery12 = new GeoQuery();
        geoQuery12.ip = ip;

        assertEquals(geoQuery1, geoQuery5);
        assertEquals(geoQuery1, geoQuery8);
        assertEquals(geoQuery1, geoQuery9);
        assertEquals(geoQuery1, geoQuery10);
        assertNotEquals(geoQuery1, geoQuery2);
        assertNotEquals(geoQuery1, geoQuery3);
        assertNotEquals(geoQuery1, geoQuery4);
        assertNotEquals(geoQuery1, geoQuery6);
        assertNotEquals(geoQuery1, geoQuery7);
        assertNotEquals(geoQuery1, geoQuery11);
        assertNotEquals(geoQuery1, geoQuery12);
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        String ip = "192.168.1.1";
        String accuracy = "high";
        String granularity = "city";
        int maxResults = 10;

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location1);
        GeoQuery geoQuery3 = new GeoQuery(ip);
        GeoQuery geoQuery4 = new GeoQuery(location2);
        GeoQuery geoQuery5 = new GeoQuery();

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();
        int hashCode3 = geoQuery3.hashCode();
        int hashCode4 = geoQuery4.hashCode();
        int hashCode5 = geoQuery5.hashCode();

        GeoQuery geoQuery6 = new GeoQuery(location1);
        geoQuery6.accuracy(accuracy);
        GeoQuery geoQuery7 = new GeoQuery(location1);
        geoQuery7.granularity(granularity);
        GeoQuery geoQuery8 = new GeoQuery(location1);
        geoQuery8.maxResults(maxResults);

        assertEquals(hashCode1, hashCode6);
        assertEquals(hashCode1, hashCode7);
        assertEquals(hashCode1, hashCode8);
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
    }
}