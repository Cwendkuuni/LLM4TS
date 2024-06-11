import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 @Test
 public void testGeoQueryWithLocation() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 GeoQuery query = new GeoQuery(location);

 HttpParameter[] parameters = query.asHttpParameterArray();
 assertEquals(5, parameters.length);

 String expectedLat = String.valueOf(1.0);
 String expectedLng = String.valueOf(2.0);

 for (HttpParameter param : parameters) {
 if (param.getName().equals("lat")) {
 assertEquals(expectedLat, param.getValue());
 }
 if (param.getName().equals("long")) {
 assertEquals(expectedLng, param.getValue());
 }
 }
 }

 @Test
 public void testGeoQueryWithIP() {
 String ip = "192.168.1.1";
 GeoQuery query = new GeoQuery(ip);

 HttpParameter[] parameters = query.asHttpParameterArray();
 assertEquals(5, parameters.length);

 for (HttpParameter param : parameters) {
 if (param.getName().equals("ip")) {
 assertEquals(ip, param.getValue());
 }
 }
 }

 @Test
 public void testGettersAndSetters() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 GeoQuery query = new GeoQuery(location);

 assertEquals(location, query.getLocation());
 query.setLocation(null);
 assertEquals(null, query.getLocation());

 String accuracy = "5km";
 query.setAccuracy(accuracy);
 assertEquals(accuracy, query.getAccuracy());

 String granularity = "city";
 query.setGranularity(granularity);
 assertEquals(granularity, query.getGranularity());

 int maxResults = 10;
 query.setMaxResults(maxResults);
 assertEquals(maxResults, query.getMaxResults());
 }

 @Test
 public void testEquals() {
 GeoLocation location1 = new GeoLocation(1.0, 2.0);
 GeoLocation location2 = new GeoLocation(1.0, 2.0);
 GeoQuery query1 = new GeoQuery(location1);
 GeoQuery query2 = new GeoQuery(location2);
 GeoQuery query3 = new GeoQuery(null);
 GeoQuery query4 = new GeoQuery(null);

 assertTrue(query1.equals(query1));
 assertTrue(query1.equals(query2));
 assertTrue(query1.equals(query2));
 assertFalse(query1.equals(query3));
 assertFalse(query1.equals(query4));
 }

 @Test
 public void testHashCode() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 GeoQuery query3 = new GeoQuery(null);

 int hashCode1 = query1.hashCode();
 int hashCode2 = query2.hashCode();
 int hashCode3 = query3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 }
}