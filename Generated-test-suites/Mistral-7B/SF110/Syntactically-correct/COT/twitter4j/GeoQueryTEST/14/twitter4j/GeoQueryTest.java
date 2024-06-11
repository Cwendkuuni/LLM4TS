import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private String ipAddress = "192.168.1.1";

 @Test
 public void testGeoQueryWithLocation() {
 GeoQuery geoQuery = new GeoQuery(location);
 assertNull(geoQuery.getIp());
 GeoLocation expectedLocation = new GeoLocation(0.0, 0.0);
 GeoLocation actualLocation = geoQuery.getLocation();
 assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testGeoQueryWithIpAddress() {
 GeoQuery geoQuery = new GeoQuery(ipAddress);
 assertNull(geoQuery.getLocation());
 }

 @Test
 public void testGetLocation() {
 GeoQuery geoQuery = new GeoQuery(location);
 GeoLocation expectedLocation = new GeoLocation(0.0, 0.0);
 GeoLocation actualLocation = geoQuery.getLocation();
 assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testGetIp() {
 GeoQuery geoQueryWithLocation = new GeoQuery(location);
 assertNull(geoQueryWithLocation.getIp());

 GeoQuery geoQueryWithIp = new GeoQuery(ipAddress);
 String expectedIp = ipAddress;
 String actualIp = geoQueryWithIp.getIp();
 assertEquals(expectedIp, actualIp);
 }

 @Test
 public void testGetAccuracy() {
 GeoQuery geoQuery = new GeoQuery(location);
 assertNull(geoQuery.getAccuracy());

 GeoQuery geoQueryWithAccuracy = new GeoQuery(location).accuracy("10m");
 String expectedAccuracy = "10m";
 String actualAccuracy = geoQueryWithAccuracy.getAccuracy();
 assertEquals(expectedAccuracy, actualAccuracy);
 }

 @Test
 public void testGetGranularity() {
 GeoQuery geoQuery = new GeoQuery(location);
 assertNull(geoQuery.getGranularity());

 GeoQuery geoQueryWithGranularity = new GeoQuery(location).granularity("city");
 String expectedGranularity = "city";
 String actualGranularity = geoQueryWithGranularity.getGranularity();
 assertEquals(expectedGranularity, actualGranularity);
 }

 @Test
 public void testGetMaxResults() {
 GeoQuery geoQuery = new GeoQuery(location);
 int expectedMaxResults = 15;
 geoQuery.maxResults(expectedMaxResults);
 int actualMaxResults = geoQuery.getMaxResults();
 assertEquals(expectedMaxResults, actualMaxResults);
 }

 @Test
 public void testAsHttpParameterArray() {
 GeoQuery geoQuery = new GeoQuery(location).accuracy("10m").granularity("city").maxResults(15);
 HttpParameter[] expectedParams = new HttpParameter[] {
 new HttpParameter("lat", String.valueOf(location.getLatitude())),
 new HttpParameter("long", String.valueOf(location.getLongitude())),
 new HttpParameter("accuracy", "10m"),
 new HttpParameter("granularity", "city"),
 new HttpParameter("max_results", String.valueOf(15))
 };
 HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
 assertArrayEquals(expectedParams, actualParams);
 }

 @Test(expected = NullPointerException.class)
 public void testNullLocation() {
 new GeoQuery(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxResults() {
 GeoQuery geoQuery = new GeoQuery(location);
 geoQuery.maxResults(-1);
 }
}