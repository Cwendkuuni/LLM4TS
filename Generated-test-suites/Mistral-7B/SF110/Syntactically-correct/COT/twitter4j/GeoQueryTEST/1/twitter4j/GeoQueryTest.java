import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private String ip = "192.168.1.1";
 private GeoQuery standardQuery = new GeoQuery(location);
 private GeoQuery ipQuery = new GeoQuery(ip);

 @Test
 public void testGetLocation() {
 GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));
 GeoLocation actual = query.getLocation();
 GeoLocation expected = new GeoLocation(1.0, 2.0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetIp() {
 GeoQuery queryWithIp = new GeoQuery("192.168.1.1");
 String actual = queryWithIp.getIp();
 String expected = "192.168.1.1";
 assertEquals(expected, actual);
 }

 @Test
 public void testGetAccuracy() {
 GeoQuery queryWithAccuracy = new GeoQuery().accuracy("10m");
 String actual = queryWithAccuracy.getAccuracy();
 String expected = "10m";
 assertEquals(expected, actual);
 }

 @Test
 public void testSetAndGetGranularity() {
 GeoQuery queryWithGranularity = new GeoQuery().granularity("city");
 String actual = queryWithGranularity.getGranularity();
 String expected = "city";
 assertEquals(expected, actual);

 queryWithGranularity.setGranularity("neighborhood");
 actual = queryWithGranularity.getGranularity();
 expected = "neighborhood";
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMaxResults() {
 GeoQuery queryWithMaxResults = new GeoQuery().maxResults(5);
 int actual = queryWithMaxResults.getMaxResults();
 int expected = 5;
 assertEquals(expected, actual);
 }

 @Test
 public void testAppendParameter() {
 List<HttpParameter> params = new ArrayList<>();
 GeoQuery query = new GeoQuery();

 query.appendParameter("lat", "40.7128", params);
 query.appendParameter("long", "-74.0060", params);
 query.appendParameter("accuracy", "10m", params);
 query.appendParameter("granularity", "city", params);
 query.appendParameter("max_results", "10", params);

 HttpParameter[] expectedParams = {
 new HttpParameter("lat", "40.7128"),
 new HttpParameter("long", "-74.0060"),
 new HttpParameter("accuracy", "10m"),
 new HttpParameter("granularity", "city"),
 new HttpParameter("max_results", "10")
 };

 assertArrayEquals(expectedParams, query.asHttpParameterArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithInvalidValue() {
 List<HttpParameter> params = new ArrayList<>();
 GeoQuery query = new GeoQuery();

 query.appendParameter("invalid_param", null, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithEmptyString() {
 List<HttpParameter> params = new ArrayList<>();
 GeoQuery query = new GeoQuery();

 query.appendParameter("empty_string", "", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNegativeInt() {
 List<HttpParameter> params = new ArrayList<>();
 GeoQuery query = new GeoQuery();

 query.appendParameter("negative_int", -1, params);
 }

 @Test
 public void testEquals() {
 GeoQuery query1 = new GeoQuery().location(new GeoLocation(1.0, 2.0)).ip("192.168.1.1").accuracy("10m").granularity("neighborhood").maxResults(5);
 GeoQuery query2 = new GeoQuery().location(new GeoLocation(1.0, 2.0)).ip("192.168.1.1").accuracy("10m").granularity("neighborhood").maxResults(5);

 assertTrue(query1.equals(query2));
 }

 @Test
 public void testHashCode() {
 GeoQuery query1 = new GeoQuery().location(new GeoLocation(1.0, 2.0)).ip("192.168.1.1").accuracy("10m").granularity("neighborhood").maxResults(5);
 GeoQuery query2 = new GeoQuery().location(new GeoLocation(1.0, 2.0)).ip("192.168.1.1").accuracy("10m").granularity("neighborhood").maxResults(5);

 int expectedHashCode = 29 * (31 * (31 * 1 + 0) + (location != null ? location.hashCode() : 0)) + (ip != null ? ip.hashCode() : 0) + (accuracy != null ? accuracy.hashCode() : 0) + granularity.hashCode() + maxResults;
 int actualHashCode1 = query1.hashCode();
 int actualHashCode2 = query2.hashCode();

 assertEquals(expectedHashCode, actualHashCode1);
 assertEquals(expectedHashCode, actualHashCode2);
 }

 @Test
 public void testToString() {
 GeoQuery query = new GeoQuery().location(new GeoLocation(1.0, 2.0)).ip("192.168.1.1").accuracy("10m").granularity("neighborhood").maxResults(5);
 String actual = query.toString();
 String expected = "GeoQuery{location=GeoLocation(1.0, 2.0), ip='192.168.1.1', accuracy='10m', granularity='neighborhood', maxResults=5}";
 assertEquals(expected, actual);
 }

 @Test
 public void testEmptyConstructor() {
 GeoQuery query = new GeoQuery();
 assertNull(query.getLocation());
 assertNull(query.getIp());
 assertNull(query.getAccuracy());
 assertNull(query.getGranularity());
 assertEquals(0, query.getMaxResults());
 }

 @Test
 public void testLocationConstructor() {
 GeoQuery query = new GeoQuery(new GeoLocation(1.0, 2.0));
 GeoLocation actualLocation = query.getLocation();
 GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
 assertEquals(expectedLocation, actualLocation);
 assertNull(query.getIp());
 assertNull(query.getAccuracy());
 assertNull(query.getGranularity());
 assertEquals(0, query.getMaxResults());
 }

 @Test
 public void testIpConstructor() {
 GeoQuery query = new GeoQuery("192.168.1.1");
 assertNull(query.getLocation());
 String actualIp = query.getIp();
 String expectedIp = "192.168.1.1";
 assertEquals(expectedIp, actualIp);
 assertNull(query.getAccuracy());
 assertNull(query.getGranularity());
 assertEquals(0, query.getMaxResults());
 }
}