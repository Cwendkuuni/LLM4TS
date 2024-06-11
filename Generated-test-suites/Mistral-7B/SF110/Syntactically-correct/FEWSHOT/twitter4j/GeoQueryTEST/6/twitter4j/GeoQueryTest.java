package twitter4j;

import static org.junit.Assert.*;
import org.junit.Test;

import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testGeoQueryWithLocation() {
        GeoLocation location = new GeoLocation(1.2, 3.4);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] params = geoQuery.asHttpParameterArray();
        assertEquals(5, params.length);

        for (HttpParameter param : params) {
            System.out.println("Name: " + param.getName() + ", Value: " + param.getValue());
        }

        GeoQuery expectedGeoQuery = new GeoQuery(location);
        expectedGeoQuery.setAccuracy("accurate");
        expectedGeoQuery.setGranularity("city");
        expectedGeoQuery.setMaxResults(10);

        assertEquals(expectedGeoQuery.toString(), geoQuery.toString());
    }

    @Test
    public void testGeoQueryWithIp() {
        GeoQuery geoQueryWithIp = new GeoQuery("192.168.1.1");
        HttpParameter[] params = geoQueryWithIp.asHttpParameterArray();
        assertEquals(4, params.length);

        for (HttpParameter param : params) {
            System.out.println("Name: " + param.getName() + ", Value: " + param.getValue());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullAccuracy() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setAccuracy(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSettingNullGranularity() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setGranularity(null);
    }

    @Test(expected = NumberFormatException.class)
    public void testSettingNegativeMaxResults() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setMaxResults(-1);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.2, 3.4);
        GeoLocation location2 = new GeoLocation(1.2, 3.4);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        GeoQuery geoQuery3 = new GeoQuery(location1);
        geoQuery3.setAccuracy("accurate");

        GeoQuery geoQuery4 = new GeoQuery(location2);
        geoQuery4.setAccuracy("accurate");

        GeoQuery geoQuery5 = new GeoQuery(location1);
        geoQuery5.setMaxResults(5);

        assertEquals(geoQuery1, geoQuery3);
        assertEquals(geoQuery1, geoQuery4);
        assertNotEquals(geoQuery1, geoQuery5);

        GeoQuery geoQuery6 = new GeoQuery(null);
        assertNotEquals(geoQuery1, geoQuery6);
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(1.2, 3.4);
        GeoLocation location2 = new GeoLocation(1.2, 3.4);

        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location1);
        GeoQuery geoQuery4 = new GeoQuery(location2);
        GeoQuery geoQuery5 = new GeoQuery(location1);
        GeoQuery geoQuery6 = new GeoQuery(null);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();
        int hashCode3 = geoQuery3.hashCode();
        int hashCode4 = geoQuery4.hashCode();
        int hashCode5 = geoQuery5.hashCode();
        int hashCode6 = geoQuery6.hashCode();

        assertEquals(hashCode1, hashCode3);
        assertEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode1, hashCode5);
        assertNotEquals(hashCode1, hashCode6);
    }
}