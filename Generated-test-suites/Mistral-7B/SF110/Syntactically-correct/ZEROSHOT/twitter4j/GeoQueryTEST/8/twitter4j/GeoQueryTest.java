import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

 @Test
 public void testGeoQueryWithLocation() {
 GeoLocation location = new GeoLocation(1.0d, 2.0d);
 GeoQuery query = new GeoQuery(location);

 assertNull(query.getIp());
 assertNotNull(query.getLocation());
 assertEquals(1.0d, query.getLocation().getLatitude(), 0.0);
 assertEquals(2.0d, query.getLocation().getLongitude(), 0.0);
 assertEquals(0, query.getMaxResults());
 assertNull(query.getAccuracy());
 assertNull(query.getGranularity());

 HttpParameter[] params = query.asHttpParameterArray();
 assertEquals(5, params.length);
 assertEquals("lat", params[0].getName());
 assertEquals("1.0", params[0].getValue());
 assertEquals("long", params[1].getName());
 assertEquals("2.0", params[1].getValue());
 assertEquals("accuracy", params[2].getName());
 assertNull(params[2].getValue());
 assertEquals("granularity", params[3].getName());
 assertNull(params[3].getValue());
 assertEquals("max_results", params[4].getName());
 assertEquals("0", params[4].getValue());
 }

 @Test
 public void testGeoQueryWithIp() {
 GeoQuery query = new GeoQuery("127.0.0.1");

 assertNull(query.getLocation());
 assertNotNull(query.getIp());
 assertEquals("127.0.0.1", query.getIp());
 assertEquals(0, query.getMaxResults());
 assertNull(query.getAccuracy());
 assertNull(query.getGranularity());

 HttpParameter[] params = query.asHttpParameterArray();
 assertEquals(1, params.length);
 assertEquals("ip", params[0].getName());
 assertEquals("127.0.0.1", params[0].getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGeoQueryWithNullLocation() {
 GeoLocation location = null;
 GeoQuery query = new GeoQuery(location);
 fail("Expected IllegalArgumentException, but got: " + query);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGeoQueryWithNullIp() {
 GeoQuery query = new GeoQuery(null);
 fail("Expected IllegalArgumentException, but got: " + query);
 }

 @Test
 public void testEquals() {
 GeoLocation location1 = new GeoLocation(1.0d, 2.0d);
 GeoLocation location2 = new GeoLocation(1.0d, 2.0d);
 GeoQuery query1 = new GeoQuery(location1);
 GeoQuery query2 = new GeoQuery(location2);

 GeoQuery query3 = new GeoQuery(new GeoLocation(2.0d, 2.0d));
 assertFalse(query1.equals(query3));

 GeoQuery query4 = new GeoQuery(location1);
 assertTrue(query1.equals(query4));

 GeoQuery query5 = new GeoQuery();
 query5.setIp("127.0.0.1");
 query5.setGranularity("city");
 query5.setMaxResults(10);
 GeoQuery query6 = new GeoQuery();
 query6.setIp("127.0.0.1");
 query6.setGranularity("city");
 query6.setMaxResults(10);
 assertTrue(query5.equals(query6));

 query5.setMaxResults(11);
 assertFalse(query5.equals(query6));
 }

 @Test
 public void testHashCode() {
 GeoLocation location1 = new GeoLocation(1.0d, 2.0d);
 GeoLocation location2 = new GeoLocation(1.0d, 2.0d);
 GeoQuery query1 = new GeoQuery(location1);
 GeoQuery query2 = new GeoQuery(location2);

 GeoQuery query3 = new GeoQuery(new GeoLocation(2.0d, 2.0d));
 int hashCode1 = query1.hashCode();
 int hashCode2 = query2.hashCode();
 int hashCode3 = query3.hashCode();

 GeoQuery query4 = new GeoQuery(location1);
 assertEquals(hashCode1, query4.hashCode());

 GeoQuery query5 = new GeoQuery();
 query5.setIp("127.0.0.1");
 query5.setGranularity("city");
 query5.setMaxResults(10);
 GeoQuery query6 = new GeoQuery();
 query6.setIp("127.0.0.1");
 query6.setGranularity("city");
 query6.setMaxResults(10);
 assertEquals(hashCode5, query6.hashCode());

 query5.setMaxResults(11);
 assertNotEquals(hashCode5, query6.hashCode());
 }

 @Test
 public void testToString() {
 GeoLocation location = new GeoLocation(1.0d, 2.0d);
 GeoQuery query = new GeoQuery(location);
 String str = query.toString();
 assertTrue(str.startsWith("GeoQuery{location="));
 assertTrue(str.contains("latitude=1.0"));
 assertTrue(str.contains("longitude=2.0"));
 assertTrue(str.contains("ip=''"));
 assertTrue(str.contains("accuracy=''"));
 assertTrue(str.contains("granularity=''"));
 assertTrue(str.contains("maxResults=0}"));
 }
}