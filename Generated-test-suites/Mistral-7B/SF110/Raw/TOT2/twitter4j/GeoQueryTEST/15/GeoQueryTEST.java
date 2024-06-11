
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoQueryTest {

    // Tests for getLocation()
    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLocationWithNullLocation() {
        GeoQuery geoQuery = new GeoQuery(null);
        GeoLocation actualLocation = geoQuery.getLocation();
    }

    // Tests for getIp()
    @Test
    public void testGetIp() {
        String expectedIp = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(expectedIp);
        String actualIp = geoQuery.getIp();
        assertEquals(expectedIp, actualIp);
    }

    @Test(expected = NullPointerException.class)
    public void testGetIpWithNullIp() {
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.getIp();
    }

    // Tests for getAccuracy()
    @Test
    public void testGetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String expectedAccuracy = "city";
        geoQuery.setAccuracy(expectedAccuracy);
        String actualAccuracy = geoQuery.getAccuracy();
        assertEquals(expectedAccuracy, actualAccuracy);
    }

    @Test
    public void testGetAccuracyWithNullAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String actualAccuracy = geoQuery.getAccuracy();
        assertNull(actualAccuracy);
    }

    // Tests for setAccuracy()
    @Test
    public void testSetAccuracy() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String expectedAccuracy = "city";
        geoQuery.setAccuracy(expectedAccuracy);
        String actualAccuracy = geoQuery.getAccuracy();
        assertEquals(expectedAccuracy, actualAccuracy);
    }

    // Tests for getGranularity()
    @Test
    public void testGetGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String expectedGranularity = "neighborhood";
        geoQuery.setGranularity(expectedGranularity);
        String actualGranularity = geoQuery.getGranularity();
        assertEquals(expectedGranularity, actualGranularity);
    }

    @Test
    public void testGetGranularityWithNullGranularity() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        String actualGranularity = geoQuery.getGranularity();
        assertNull(actualGranularity);
    }

    // Tests for getMaxResults()
    @Test
    public void testGetMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        int expectedMaxResults = 10;
        geoQuery.setMaxResults(expectedMaxResults);
        int actualMaxResults = geoQuery.getMaxResults();
        assertEquals(expectedMaxResults, actualMaxResults);
    }

    @Test
    public void testGetMaxResultsWithNegativeMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        int expectedMaxResults = -5;
        geoQuery.setMaxResults(expectedMaxResults);
        int actualMaxResults = geoQuery.getMaxResults();
        assertEquals(-5, actualMaxResults); // should be negative
    }

    // Tests for setMaxResults() and maxResults()
    @Test
    public void testSetMaxResultsAndMaxResults() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(0, 0));
        int expectedMaxResults = 10;
        geoQuery.setMaxResults(expectedMaxResults);
        int actualMaxResults = geoQuery.getMaxResults();
        assertEquals(expectedMaxResults, actualMaxResults);

        GeoQuery geoQueryWithMaxResultsMethod = new GeoQuery(new GeoLocation(0, 0));
        geoQueryWithMaxResultsMethod.maxResults(expectedMaxResults);
        int actualMaxResultsWithMaxResultsMethod = geoQueryWithMaxResultsMethod.getMaxResults();
        assertEquals(expectedMaxResults, actualMaxResultsWithMaxResultsMethod);
    }

    // Tests for asHttpParameterArray()
    @Test
    public void testAsHttpParameterArrayWithLocation() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] expectedParams = new HttpParameter[] {
            new HttpParameter("lat", "1.0"),
            new HttpParameter("long", "2.0")
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testAsHttpParameterArrayWithIp() {
        String ip = "192.168.0.1";
        GeoQuery geoQuery = new GeoQuery(ip);
        HttpParameter[] expectedParams = new HttpParameter[] {
            new HttpParameter("ip", ip)
        };
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParams, actualParams);
    }

    @Test
    public void testAsHttpParameterArrayWithEmptyGeoQuery() {
        GeoQuery geoQuery = new GeoQuery(null);
        HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
        assertArrayEquals(new HttpParameter[0], actualParams);
    }

    // Tests for equals()
    @Test
    public void testEqualsWithSameObject() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = geoQuery1;
        boolean actualResult = geoQuery1.equals(geoQuery2);
        assertTrue(actualResult);
    }

    @Test
    public void testEqualsWithDifferentObject() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(3.0, 4.0));
        boolean actualResult = geoQuery1.equals(geoQuery2);
        assertFalse(actualResult);
    }

    @Test
    public void testEqualsWithNullObject() {
        GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0, 2.0));
        boolean actualResult = geoQuery.equals(null);
        assertFalse(actualResult);
    }

    // Tests for hashCode()
    @Test
    public void testHashCodeWithSameObject() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = geoQuery1;
        int actualHashCode = geoQuery1.hashCode();
        int expectedHashCode = geoQuery2.hashCode();
        assertEquals(actualHashCode, expectedHashCode);
    }

    @Test
    public void testHashCodeWithDifferentObjects() {
        GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
        GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(3.0, 4.0));
        int actualHashCode1 = geoQuery1.hashCode();
        int actualHashCode2 = geoQuery2.hashCode();
        assertNotSame(actualHashCode1, actualHashCode2);
    }

    // Tests for toString()
    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        String expectedString = "GeoQuery{location=" + location + ", ip='', accuracy='', granularity='', maxResults=0}";
        String actualString = geoQuery.toString();
        assertEquals(expectedString, actualString);
    }
}