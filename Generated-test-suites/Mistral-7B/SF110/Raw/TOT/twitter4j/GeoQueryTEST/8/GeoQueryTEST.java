package twitter4j.test;

import org.junit.*;
import static org.junit.Assert.*;
import twitter4j.*;

public class GeoQueryTest {

    @BeforeClass
    public static void setupClass() {
        // Set up any static variables or initialize any static resources
    }

    @AfterClass
    public static void tearDownClass() {
        // Clean up any static resources or perform any other teardown actions
    }

    @Before
    public void setUp() {
        // Set up any instance variables or perform any other setup actions
    }

    @After
    public void tearDown() {
        // Perform any teardown actions or reset any state variables
    }

    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testGetIp() {
        String expectedIp = "192.168.1.1";
        GeoQuery geoQuery = new GeoQuery(null);
        geoQuery.setIp(expectedIp);
        String actualIp = geoQuery.getIp();
        assertEquals(expectedIp, actualIp);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(1.0, 2.0);
        GeoLocation location2 = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);
        GeoQuery geoQuery3 = new GeoQuery(location1);
        GeoQuery geoQuery4 = new GeoQuery(null);

        assertTrue(geoQuery1.equals(geoQuery3));
        assertTrue(geoQuery1.equals(geoQuery1));
        assertFalse(geoQuery1.equals(geoQuery2));
        assertFalse(geoQuery1.equals(geoQuery4));
    }

    @Test
    public void testSettersAndGetters() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(null);

        geoQuery.setLocation(location);
        GeoLocation actualLocation = geoQuery.getLocation();
        assertEquals(location, actualLocation);

        String expectedAccuracy = "10m";
        String expectedGranularity = "country";
        int expectedMaxResults = 5;

        geoQuery.setAccuracy(expectedAccuracy);
        String actualAccuracy = geoQuery.getAccuracy();
        assertEquals(expectedAccuracy, actualAccuracy);

        geoQuery.setGranularity(expectedGranularity);
        String actualGranularity = geoQuery.getGranularity();
        assertEquals(expectedGranularity, actualGranularity);

        geoQuery.setMaxResults(expectedMaxResults);
        int actualMaxResults = geoQuery.getMaxResults();
        assertEquals(expectedMaxResults, actualMaxResults);
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        String expectedString = "GeoQuery{location=" + location + ", ip='' , accuracy='' , granularity='' , maxResults=0}";
        String actualString = geoQuery.toString();
        assertEquals(expectedString, actualString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSettersWithNullValues() {
        GeoQuery geoQuery = new GeoQuery(null);

        geoQuery.setLocation(null);
        geoQuery.setIp(null);
        geoQuery.setAccuracy(null);
        geoQuery.setGranularity(null);
        geoQuery.setMaxResults(-1);
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(1.0, 2.0);
        GeoQuery geoQuery = new GeoQuery(location);
        HttpParameter[] expectedParameters = new HttpParameter[]{
            new HttpParameter("lat", "1.0"),
            new HttpParameter("long", "2.0")
        };
        HttpParameter[] actualParameters = geoQuery.asHttpParameterArray();
        assertArrayEquals(expectedParameters, actualParameters);
    }
}