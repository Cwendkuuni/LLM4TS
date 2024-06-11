import org.junit.Assert;import org.junit.Test;import twitter4j.GeoLocation;import twitter4j.GeoQuery;import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 @Test
 public void testGeoQueryWithValidLocation() {
 GeoLocation location = new GeoLocation(37.7749, -122.4194);
 GeoQuery query = new GeoQuery(location);

 // Test setting and getting location
 Assert.assertEquals(location, query.getLocation());

 // Test appending parameters to the query
 HttpParameter[] params = query.asHttpParameterArray();
 Assert.assertEquals(5, params.length);
 Assert.assertEquals("lat", params[0].getName());
 Assert.assertEquals(String.valueOf(37.7749), params[0].getValue());
 Assert.assertEquals("long", params[1].getName());
 Assert.assertEquals(String.valueOf(-122.4194), params[1].getValue());
 }

 @Test
 public void testGeoQueryWithValidIPAddress() {
 String ipAddress = "192.168.0.1";
 GeoQuery query = new GeoQuery(ipAddress);

 // Test setting and getting IP address
 Assert.assertEquals(ipAddress, query.getIp());

 // Test appending parameters to the query
 HttpParameter[] params = query.asHttpParameterArray();
 Assert.assertEquals(2, params.length);
 Assert.assertEquals("ip", params[0].getName());
 Assert.assertEquals(ipAddress, params[0].getValue());
 }

 @Test
 public void testGeoQueryWithValidAccuracy() {
 GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194)).accuracy("10m");

 // Test setting and getting accuracy
 Assert.assertEquals("10m", query.getAccuracy());

 // Test appending parameters to the query
 HttpParameter[] params = query.asHttpParameterArray();
 Assert.assertEquals(6, params.length);
 Assert.assertEquals("accuracy", params[4].getName());
 Assert.assertEquals("10m", params[4].getValue());
 }

 @Test
 public void testGeoQueryWithValidGranularity() {
 GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194)).granularity("country");

 // Test setting and getting granularity
 Assert.assertEquals("country", query.getGranularity());

 // Test appending parameters to the query
 HttpParameter[] params = query.asHttpParameterArray();
 Assert.assertEquals(6, params.length);
 Assert.assertEquals("granularity", params[5].getName());
 Assert.assertEquals("country", params[5].getValue());
 }

 @Test
 public void testGeoQueryWithValidMaxResults() {
 GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194)).maxResults(10);

 // Test setting and getting max results
 Assert.assertEquals(10, query.getMaxResults());

 // Test appending parameters to the query
 HttpParameter[] params = query.asHttpParameterArray();
 Assert.assertEquals(6, params.length);
 Assert.assertEquals("max_results", params[4].getName());
 Assert.assertEquals("10", params[4].getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGeoQueryWithNullLocation() {
 GeoQuery query = new GeoQuery(null);
 // This constructor should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGeoQueryWithNullAccuracy() {
 GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194)).accuracy(null);
 // This method call should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGeoQueryWithNullGranularity() {
 GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194)).granularity(null);
 // This method call should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGeoQueryWithNegativeMaxResults() {
 GeoQuery query = new GeoQuery(new GeoLocation(37.7749, -122.4194)).maxResults(-5);
 // This method call should throw an IllegalArgumentException
 }
}