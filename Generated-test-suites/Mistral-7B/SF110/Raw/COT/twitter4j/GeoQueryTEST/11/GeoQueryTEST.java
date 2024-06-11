import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private String ip = "192.168.1.1";
 private GeoQuery expectedQuery;

 @Test
 public void testGeoQueryWithLocation() {
 GeoQuery actualQuery = new GeoQuery(location);
 expectedQuery = new GeoQuery(location);
 assertArrayEquals(expectedQuery.asHttpParameterArray(), actualQuery.asHttpParameterArray());
 }

 @Test
 public void testGeoQueryWithIp() {
 GeoQuery actualQuery = new GeoQuery(ip);
 expectedQuery = new GeoQuery();
 expectedQuery.setIp(ip);
 assertArrayEquals(expectedQuery.asHttpParameterArray(), actualQuery.asHttpParameterArray());
 }

 @Test
 public void testGetters() {
 GeoQuery query = new GeoQuery(location);
 GeoLocation locationFromQuery = query.getLocation();
 assertEquals(location, locationFromQuery);
 String ipFromQuery = query.getIp();
 assertNull(ipFromQuery);
 String accuracyFromQuery = query.getAccuracy();
 assertNull(accuracyFromQuery);
 String granularityFromQuery = query.getGranularity();
 assertNull(granularityFromQuery);
 int maxResultsFromQuery = query.getMaxResults();
 assertEquals(0, maxResultsFromQuery);
 }

 @Test
 public void testSetters() {
 GeoQuery query = new GeoQuery();
 query.setLocation(location);
 GeoLocation locationFromQuery = query.getLocation();
 assertEquals(location, locationFromQuery);
 query.setIp(ip);
 String ipFromQuery = query.getIp();
 assertEquals(ip, ipFromQuery);
 query.setAccuracy("accurate");
 String accuracyFromQuery = query.getAccuracy();
 assertEquals("accurate", accuracyFromQuery);
 query.setGranularity("city");
 String granularityFromQuery = query.getGranularity();
 assertEquals("city", granularityFromQuery);
 query.setMaxResults(5);
 int maxResultsFromQuery = query.getMaxResults();
 assertEquals(5, maxResultsFromQuery);
 }

 @Test
 public void testAppendParameter() {
 List<HttpParameter> params = new ArrayList<>();
 GeoQuery query = new GeoQuery();
 query.appendParameter("lat", "40.7128", params);
 HttpParameter latParam = params.get(0);
 assertEquals("lat", latParam.getName());
 assertEquals("40.7128", latParam.getValue());
 query.appendParameter("long", "74.0060", params);
 HttpParameter longParam = params.get(1);
 assertEquals("long", longParam.getName());
 assertEquals("74.0060", longParam.getValue());
 }

 @Test
 public void testEquals() {
 GeoQuery query1 = new GeoQuery();
 GeoQuery query2 = new GeoQuery();
 assertFalse(query1.equals(query2));
 query1.setLocation(location);
 query2.setLocation(location);
 assertTrue(query1.equals(query2));
 query1.setIp(ip);
 query2.setIp(null);
 assertFalse(query1.equals(query2));
 query2.setIp(ip);
 assertTrue(query1.equals(query2));
 query1.setAccuracy("accurate");
 query2.setAccuracy("precise");
 assertFalse(query1.equals(query2));
 query2.setAccuracy("accurate");
 assertTrue(query1.equals(query2));
 query1.setGranularity("city");
 query2.setGranularity("neighborhood");
 assertFalse(query1.equals(query2));
 query2.setGranularity("city");
 assertTrue(query1.equals(query2));
 query1.setMaxResults(5);
 query2.setMaxResults(10);
 assertFalse(query1.equals(query2));
 query2.setMaxResults(5);
 assertTrue(query1.equals(query2));
 }

 @Test
 public void testHashCode() {
 GeoQuery query1 = new GeoQuery();
 GeoQuery query2 = new GeoQuery();
 assertEquals(query1.hashCode(), query2.hashCode());
 query1.setLocation(location);
 query2.setLocation(location);
 assertEquals(query1.hashCode(), query2.hashCode());
 query1.setIp(ip);
 query2.setIp(ip);
 assertEquals(query1.hashCode(), query2.hashCode());
 query1.setAccuracy("accurate");
 query2.setAccuracy("accurate");
 assertEquals(query1.hashCode(), query2.hashCode());
 query1.setGranularity("city");
 query2.setGranularity("city");
 assertEquals(query1.hashCode(), query2.hashCode());
 query1.setMaxResults(5);
 query2.setMaxResults(5);
 assertEquals(query1.hashCode(), query2.hashCode());
 query1.setMaxResults(10);
 assertNotEquals(query1.hashCode(), query2.hashCode());
 }

 @Test
 public void testToString() {
 GeoQuery query = new GeoQuery();
 assertEquals("GeoQuery{location=null, ip='', accuracy='', granularity='', maxResults=0}", query.toString());
 query.setLocation(location);
 assertEquals("GeoQuery{location=" + location + ", ip='', accuracy='', granularity='', maxResults=0}", query.toString());
 query.setIp(ip);
 assertEquals("GeoQuery{location=" + location + ", ip='" + ip + "', accuracy='', granularity='', maxResults=0}", query.toString());
 query.setAccuracy("accurate");
 assertEquals("GeoQuery{location=" + location + ", ip='" + ip + "', accuracy='accurate', granularity='', maxResults=0}", query.toString());
 query.setGranularity("city");
 assertEquals("GeoQuery{location=" + location + ", ip='" + ip + "', accuracy='accurate', granularity='city', maxResults=0}", query.toString());
 query.setMaxResults(5);
 assertEquals("GeoQuery{location=" + location + ", ip='" + ip + "', accuracy='accurate', granularity='city', maxResults=5}", query.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testSettersWithNull() {
 GeoQuery query = new GeoQuery();
 query.setLocation(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxResultsNegative() {
 GeoQuery query = new GeoQuery();
 query.setMaxResults(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxResultsZero() {
 GeoQuery query = new GeoQuery();
 query.setMaxResults(0);
 }

}