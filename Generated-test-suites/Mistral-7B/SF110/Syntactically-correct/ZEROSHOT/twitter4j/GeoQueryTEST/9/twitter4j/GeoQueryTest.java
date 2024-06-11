import org.junit.Before;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

public class GeoQueryTest {

 private GeoLocation location;
 private GeoQuery geoQueryWithLocation;
 private GeoQuery geoQueryWithIp;
 private List<HttpParameter> httpParameters;

 @Before
 public void setUp() {
 location = new GeoLocation(51.5074, -0.1278);
 geoQueryWithLocation = new GeoQuery(location);
 geoQueryWithIp = new GeoQuery("192.168.1.1");
 httpParameters = new ArrayList<>();
 }

 @Test
 public void testCreateGeoQueryWithLocation() {
 assertEquals(location, geoQueryWithLocation.getLocation());
 assertNull(geoQueryWithLocation.getIp());
 }

 @Test
 public void testCreateGeoQueryWithIp() {
 assertNull(geoQueryWithIp.getLocation());
 assertEquals("192.168.1.1", geoQueryWithIp.getIp());
 }

 @Test
 public void testGetLocation() {
 GeoLocation testLocation = new GeoLocation(51.5174, -0.1228);
 geoQueryWithLocation.setLocation(testLocation);
 assertEquals(testLocation, geoQueryWithLocation.getLocation());
 }

 @Test
 public void testSetLocation() {
 GeoLocation testLocation = new GeoLocation(51.5174, -0.1228);
 geoQueryWithLocation.setLocation(testLocation);
 assertEquals(testLocation, geoQueryWithLocation.getLocation());
 }

 @Test
 public void testGetIp() {
 assertEquals("192.168.1.1", geoQueryWithIp.getIp());
 }

 @Test(expected = NullPointerException.class)
 public void testGetIpWithoutIp() {
 geoQueryWithLocation.getIp();
 }

 @Test
 public void testSetIp() {
 String testIp = "192.168.2.1";
 geoQueryWithIp.setIp(testIp);
 assertEquals(testIp, geoQueryWithIp.getIp());
 }

 @Test
 public void testGetAccuracy() {
 assertNull(geoQueryWithLocation.getAccuracy());
 }

 @Test
 public void testSetAccuracy() {
 String testAccuracy = "10m";
 geoQueryWithLocation.setAccuracy(testAccuracy);
 assertEquals(testAccuracy, geoQueryWithLocation.getAccuracy());
 }

 @Test
 public void testAccuracyMethod() {
 String testAccuracy = "10m";
 GeoQuery testGeoQuery = geoQueryWithLocation.accuracy(testAccuracy);
 assertEquals(testAccuracy, testGeoQuery.getAccuracy());
 assertEquals(geoQueryWithLocation, testGeoQuery);
 }

 @Test
 public void testGetGranularity() {
 assertNull(geoQueryWithLocation.getGranularity());
 }

 @Test
 public void testSetGranularity() {
 String testGranularity = "city";
 geoQueryWithLocation.setGranularity(testGranularity);
 assertEquals(testGranularity, geoQueryWithLocation.getGranularity());
 }

 @Test
 public void testGranularityMethod() {
 String testGranularity = "city";
 GeoQuery testGeoQuery = geoQueryWithLocation.granularity(testGranularity);
 assertEquals(testGranularity, testGeoQuery.getGranularity());
 assertEquals(geoQueryWithLocation, testGeoQuery);
 }

 @Test
 public void testGetMaxResults() {
 assertEquals(10, geoQueryWithLocation.getMaxResults());
 }

 @Test
 public void testSetMaxResults() {
 geoQueryWithLocation.setMaxResults(20);
 assertEquals(20, geoQueryWithLocation.getMaxResults());
 }

 @Test
 public void testMaxResultsMethod() {
 GeoQuery testGeoQuery = geoQueryWithLocation.maxResults(20);
 assertEquals(20, testGeoQuery.getMaxResults());
 assertEquals(geoQueryWithLocation, testGeoQuery);
 }

 @Test
 public void testAsHttpParameterArray() {
 HttpParameter[] httpParams = geoQueryWithLocation.asHttpParameterArray();
 assertEquals(5, httpParams.length);

 List<String> keys = new ArrayList<>(Arrays.asList("lat", "long", "accuracy", "granularity", "max_results"));
 for (int i = 0; i < httpParams.length; i++) {
 assertEquals(keys.get(i), httpParams[i].getName());
 }

 assertEquals(location.getLatitude(), Double.parseDouble(httpParams[0].getValue()));
 assertEquals(location.getLongitude(), Double.parseDouble(httpParams[1].getValue()));
 assertNull(httpParams[2].getValue()); // accuracy is null
 assertEquals("city", httpParams[3].getValue()); // granularity is set to city by default
 assertEquals(10, Integer.parseInt(httpParams[4].getValue())); // maxResults is set to 10 by default
 }

 @Test
 public void testEquals() {
 GeoQuery testGeoQuery = geoQueryWithLocation;
 GeoQuery anotherGeoQuery = new GeoQuery(location);

 assertEquals(testGeoQuery, testGeoQuery); // reflexive property
 assertEquals(testGeoQuery, anotherGeoQuery); // transitive property
 assertNotEquals(testGeoQuery, new Object()); // not equals null object
 assertNotEquals(anotherGeoQuery, new Object()); // not equals null object
 assertNotEquals(testGeoQuery, null); // not equals null object
 assertNotEquals(testGeoQuery, anotherGeoQuery); // different objects

 testGeoQuery.setLocation(new GeoLocation(51.5174, -0.1228));
 anotherGeoQuery.setLocation(new GeoLocation(51.5174, -0.1228));
 assertEquals(testGeoQuery, anotherGeoQuery);

 testGeoQuery.setAccuracy("10m");
 anotherGeoQuery.setAccuracy("15m");
 assertNotEquals(testGeoQuery, anotherGeoQuery);

 testGeoQuery.setGranularity("city");
 anotherGeoQuery.setGranularity("neighborhood");
 assertNotEquals(testGeoQuery, anotherGeoQuery);

 testGeoQuery.setMaxResults(10);
 anotherGeoQuery.setMaxResults(20);
 assertNotEquals(testGeoQuery, anotherGeoQuery);

 testGeoQuery.setIp("192.168.1.1");
 anotherGeoQuery.setIp("192.168.2.1");
 assertNotEquals(testGeoQuery, anotherGeoQuery);

 testGeoQuery.setLocation(new GeoLocation(51.5174, -0.1228));
 anotherGeoQuery.setLocation(new GeoLocation(51.5174, -0.1227));
 assertNotEquals(testGeoQuery, anotherGeoQuery);
 }

 @Test
 public void testHashCode() {
 int testHashcode1 = geoQueryWithLocation.hashCode();
 int testHashcode2 = new GeoQuery(location).hashCode();
 assertEquals(testHashcode1, testHashcode2);
 }

 @Test
 public void testToString() {
 String testToString = "GeoQuery{location=GeoLocation(latitude=51.5074, longitude=-0.1278), ip='null', accuracy='null', granularity='city', maxResults=10}";
 assertEquals(testToString, geoQueryWithLocation.toString());
 }
}