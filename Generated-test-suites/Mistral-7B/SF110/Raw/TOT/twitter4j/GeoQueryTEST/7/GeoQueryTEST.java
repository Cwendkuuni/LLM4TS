package twitter4j.test;

import org.junit.*;
import static org.junit.Assert.*;
import twitter4j.*;
import twitter4j.internal.http.*;
import java.util.*;

public class GeoQueryTest {

    @BeforeClass
    public static void setUpClass() {
        // Perform any setup here
    }

    @AfterClass
    public static void tearDownClass() {
        // Perform any cleanup here
    }

    @Before
    public void setUp() {
        // Perform any setup here for each test
    }

    @After
    public void tearDown() {
        // Perform any cleanup here for each test
    }

    @Test
    public void testGetLocation() {
        GeoLocation expectedLocation = new GeoLocation(12.9715998, 77.594562);
        GeoQuery geoQuery = new GeoQuery(expectedLocation);
        GeoLocation actualLocation = geoQuery.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testAsHttpParameterArray() {
        GeoLocation location = new GeoLocation(12.9715998, 77.594562);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("10m");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(20);

        HttpParameter[] expectedParameters = new HttpParameter[]{
            new HttpParameter("lat", "12.9715998"),
            new HttpParameter("long", "77.594562"),
            new HttpParameter("accuracy", "10m"),
            new HttpParameter("granularity", "city"),
            new HttpParameter("max_results", "20")
        };

        HttpParameter[] actualParameters = geoQuery.asHttpParameterArray();

        assertArrayEquals(expectedParameters, actualParameters);
    }

    @Test
    public void testEquals() {
        GeoLocation location1 = new GeoLocation(12.9715998, 77.594562);
        GeoLocation location2 = new GeoLocation(12.9715998, 77.594562);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        // Two identical objects should be equal
        assertTrue(geoQuery1.equals(geoQuery2));

        // Changing the accuracy should make the objects unequal
        geoQuery1.setAccuracy("10m");
        geoQuery2.setAccuracy("50m");
        assertFalse(geoQuery1.equals(geoQuery2));

        // Changing the granularity should also make the objects unequal
        geoQuery1.setGranularity("city");
        geoQuery2.setGranularity("neighborhood");
        assertFalse(geoQuery1.equals(geoQuery2));

        // Changing the maxResults should also make the objects unequal
        geoQuery1.setMaxResults(20);
        geoQuery2.setMaxResults(10);
        assertFalse(geoQuery1.equals(geoQuery2));

        // Creating a GeoQuery with null location should be unequal to another GeoQuery with a non-null location
        geoQuery1 = new GeoQuery(null);
        assertFalse(geoQuery1.equals(geoQuery2));

        // Creating a GeoQuery with null accuracy should be equal to another GeoQuery with a null accuracy
        geoQuery1.setLocation(null);
        geoQuery1.setAccuracy(null);
        geoQuery2.setLocation(null);
        geoQuery2.setAccuracy(null);
        assertTrue(geoQuery1.equals(geoQuery2));
    }

    @Test
    public void testHashCode() {
        GeoLocation location1 = new GeoLocation(12.9715998, 77.594562);
        GeoLocation location2 = new GeoLocation(12.9715998, 77.594562);
        GeoQuery geoQuery1 = new GeoQuery(location1);
        GeoQuery geoQuery2 = new GeoQuery(location2);

        // Two identical objects should have the same hashcode
        assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());

        // Changing the accuracy should change the hashcode
        geoQuery1.setAccuracy("10m");
        geoQuery2.setAccuracy("50m");
        assertNotSame(geoQuery1.hashCode(), geoQuery2.hashCode());

        // Changing the granularity should also change the hashcode
        geoQuery1.setGranularity("city");
        geoQuery2.setGranularity("neighborhood");
        assertNotSame(geoQuery1.hashCode(), geoQuery2.hashCode());

        // Changing the maxResults should also change the hashcode
        geoQuery1.setMaxResults(20);
        geoQuery2.setMaxResults(10);
        assertNotSame(geoQuery1.hashCode(), geoQuery2.hashCode());
    }

    @Test
    public void testToString() {
        GeoLocation location = new GeoLocation(12.9715998, 77.594562);
        GeoQuery geoQuery = new GeoQuery(location);
        geoQuery.setAccuracy("10m");
        geoQuery.setGranularity("city");
        geoQuery.setMaxResults(20);

        String expectedString = "GeoQuery{location=GeoLocation{lat=12.9715998, lon=77.594562}, ip='', accuracy='10m', granularity='city', maxResults=20}";
        String actualString = geoQuery.toString();

        assertEquals(expectedString, actualString);
    }
}