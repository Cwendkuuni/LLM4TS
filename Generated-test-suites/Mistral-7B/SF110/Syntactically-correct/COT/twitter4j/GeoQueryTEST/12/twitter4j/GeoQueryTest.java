import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

    @Test
    public void testGeoQueryLocationConstructor() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        assertNull(geoQuery.getIp());
        assertNotNull(geoQuery.getLocation());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertNotNull(geoQuery.asHttpParameterArray());
        assertFalse(geoQuery.equals(null));
    }

    @Test
    public void testGeoQueryIpConstructor() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertNull(geoQuery.getLocation());
        assertNotNull(geoQuery.getIp());
        assertNull(geoQuery.getAccuracy());
        assertNull(geoQuery.getGranularity());
        assertNotNull(geoQuery.asHttpParameterArray());
        assertFalse(geoQuery.equals(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeoQueryNullLocation() {
        GeoQuery geoQuery = new GeoQuery(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGeoQueryNullIp() {
        GeoQuery geoQuery = new GeoQuery(null);
    }

    @Test
    public void testGetLocationMethod() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        GeoLocation retrievedLocation = geoQuery.getLocation();
        assertEquals(location, retrievedLocation);
    }

    @Test
    public void testGetIpMethod() {
        String ip = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        assertEquals(ip, geoQuery.getIp());
    }

    @Test
    public void testGetAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery();
        assertNull(geoQuery.getAccuracy());

        GeoQuery geoQueryWithAccuracy = new GeoQuery();
        geoQueryWithAccuracy.setAccuracy("10m");
        assertEquals("10m", geoQueryWithAccuracy.getAccuracy());
    }

    @Test
    public void testSetAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery();

        geoQuery.setAccuracy("10m");
        assertEquals("10m", geoQuery.getAccuracy());

        geoQuery.setAccuracy(null);
        assertNull(geoQuery.getAccuracy());
    }

    @Test
    public void testAccuracyMethod() {
        GeoQuery geoQuery = new GeoQuery();

        GeoQuery geoQueryWithAccuracy = geoQuery.accuracy("10m");
        assertEquals("10m", geoQueryWithAccuracy.getAccuracy());
        assertEquals(geoQuery, geoQueryWithAccuracy);

        GeoQuery geoQueryWithoutAccuracy = geoQuery.accuracy(null);
        assertNull(geoQueryWithoutAccuracy.getAccuracy());
        assertNotSame(geoQuery, geoQueryWithoutAccuracy);
    }

    @Test
    public void testGetGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery();
        assertNull(geoQuery.getGranularity());

        GeoQuery geoQueryWithGranularity = new GeoQuery();
        geoQueryWithGranularity.setGranularity("country");
        assertEquals("country", geoQueryWithGranularity.getGranularity());
    }

    @Test
    public void testSetGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery();

        geoQuery.setGranularity("country");
        assertEquals("country", geoQuery.getGranularity());

        geoQuery.setGranularity(null);
        assertNull(geoQuery.getGranularity());
    }

    @Test
    public void testGranularityMethod() {
        GeoQuery geoQuery = new GeoQuery();

        GeoQuery geoQueryWithGranularity = geoQuery.granularity("country");
        assertEquals("country", geoQueryWithGranularity.getGranularity());
        assertEquals(geoQuery, geoQueryWithGranularity);

        GeoQuery geoQueryWithoutGranularity = geoQuery.granularity(null);
        assertNull(geoQueryWithoutGranularity.getGranularity());
        assertNotSame(geoQuery, geoQueryWithoutGranularity);
    }

    @Test
    public void testGetMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery();
        assertEquals(0, geoQuery.getMaxResults());

        GeoQuery geoQueryWithMaxResults = new GeoQuery();
        geoQueryWithMaxResults.setMaxResults(5);
        assertEquals(5, geoQueryWithMaxResults.getMaxResults());
    }

    @Test
    public void testSetMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery();

        geoQuery.setMaxResults(5);
        assertEquals(5, geoQuery.getMaxResults());

        geoQuery.setMaxResults(0);
        assertEquals(0, geoQuery.getMaxResults());
    }

    @Test
    public void testMaxResultsMethod() {
        GeoQuery geoQuery = new GeoQuery();

        GeoQuery geoQueryWithMaxResults = geoQuery.maxResults(5);
        assertEquals(5, geoQueryWithMaxResults.getMaxResults());
        assertEquals(geoQuery, geoQueryWithMaxResults);

        GeoQuery geoQueryWithoutMaxResults = geoQuery.maxResults(0);
        assertEquals(0, geoQueryWithoutMaxResults.getMaxResults());
        assertNotSame(geoQuery, geoQueryWithoutMaxResults);
    }

    @Test
    public void testAsHttpParameterArrayMethod() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        HttpParameter[] httpParameters = geoQuery.asHttpParameterArray();
        assertEquals(6, httpParameters.length);

        for (HttpParameter httpParameter : httpParameters) {
            if ("lat".equals(httpParameter.getName())) {
                assertEquals(String.valueOf(location.getLatitude()), httpParameter.getValue());
            } else if ("long".equals(httpParameter.getName())) {
                assertEquals(String.valueOf(location.getLongitude()), httpParameter.getValue());
            }
        }
    }

    @Test
    public void testEqualsMethod() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoLocation location3 = new GeoLocation(1.1, 2.1);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location3);

        GeoQuery sameGeoQuery = new GeoQuery(location1);
        assertEquals(geoQuery1, sameGeoQuery);

        GeoQuery differentAccuracy = new GeoQuery(location1);
        differentAccuracy.setAccuracy("5m");
        assertNotEquals(geoQuery1, differentAccuracy);

        GeoQuery differentGranularity = new GeoQuery(location1);
        differentGranularity.setGranularity("city");
        assertNotEquals(geoQuery1, differentGranularity);

        GeoQuery differentIp = new GeoQuery(location1);
        differentIp.setIp("192.168.1.2");
        assertNotEquals(geoQuery1, differentIp);

        GeoQuery differentLocation = new GeoQuery(location2);
        assertNotEquals(geoQuery1, differentLocation);

        GeoQuery differentMaxResults = new GeoQuery(location1);
        differentMaxResults.setMaxResults(10);
        assertNotEquals(geoQuery1, differentMaxResults);
    }

    @Test
    public void testHashCodeMethod() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoLocation location3 = new GeoLocation(1.1, 2.1);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location3);

        int hashCode1 = geoQuery1.hashCode();
        int hashCode2 = geoQuery2.hashCode();
        int hashCode3 = geoQuery3.hashCode();

        GeoQuery sameGeoQuery = new GeoQuery(location1);
        assertEquals(hashCode1, sameGeoQuery.hashCode());

        GeoQuery differentAccuracy = new GeoQuery(location1);
        differentAccuracy.setAccuracy("5m");
        assertNotEquals(hashCode1, differentAccuracy.hashCode());

        GeoQuery differentGranularity = new GeoQuery(location1);
        differentGranularity.setGranularity("city");
        assertNotEquals(hashCode1, differentGranularity.hashCode());

        GeoQuery differentIp = new GeoQuery(location1);
        differentIp.setIp("192.168.1.2");
        assertNotEquals(hashCode1, differentIp.hashCode());

        GeoQuery differentLocation = new GeoQuery(location2);
        assertNotEquals(hashCode1, differentLocation.hashCode());

        GeoQuery differentMaxResults = new GeoQuery(location1);
        differentMaxResults.setMaxResults(10);
        assertNotEquals(hashCode1, differentMaxResults.hashCode());
    }

    @Test
    public void testToStringMethod() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);

        String expected = "GeoQuery{location=" + location.toString() + ", ip='' , accuracy='' , granularity='' , maxResults=0}";
        assertEquals(expected, geoQuery.toString());

        GeoQuery geoQueryWithValues = new GeoQuery(location)
                .accuracy("10m")
                .granularity("city")
                .maxResults(5);

        String expectedWithValues = "GeoQuery{location=" + location.toString() + ", ip='' , accuracy='10m' , granularity='city' , maxResults=5}";
        assertEquals(expectedWithValues, geoQueryWithValues.toString());
    }
}