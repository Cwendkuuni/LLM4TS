import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 @Test
 public void testGeoQueryWithLocation() {
 GeoLocation location = new GeoLocation(51.5074, -0.1278); // London coordinates
 GeoQuery query = new GeoQuery(location);

 HttpParameter[] parameters = query.asHttpParameterArray();
 assertEquals(5, parameters.length);

 for (HttpParameter parameter : parameters) {
 System.out.println("Name: " + parameter.getName() + ", Value: " + parameter.getValue());
 }

 // Check equals method with same object
 GeoQuery query2 = new GeoQuery(location);
 assertTrue(query.equals(query2));

 // Check equals method with different object but same values
 GeoLocation location2 = new GeoLocation(51.5074, -0.1278);
 GeoQuery query3 = new GeoQuery(location2);
 assertTrue(query.equals(query3));
 }

 @Test
 public void testGeoQueryWithIP() {
 String ip = "192.168.1.1";
 GeoQuery query = new GeoQuery(ip);

 HttpParameter[] parameters = query.asHttpParameterArray();
 assertEquals(5, parameters.length);

 for (HttpParameter parameter : parameters) {
 System.out.println("Name: " + parameter.getName() + ", Value: " + parameter.getValue());
 }

 // Check equals method with same object
 GeoQuery query2 = new GeoQuery(ip);
 assertTrue(query.equals(query2));

 // Check equals method with different object but same value
 GeoQuery query3 = new GeoQuery("192.168.1.1");
 assertTrue(query.equals(query3));
 }

 @Test
 public void testGeoQueryWithAccuracy() {
 GeoQuery query = new GeoQuery(new GeoLocation(51.5074, -0.1278));
 query.setAccuracy("city");

 HttpParameter[] parameters = query.asHttpParameterArray();
 assertEquals(6, parameters.length);
 assertEquals("city", parameters[4].getValue());
 }

 @Test
 public void testGeoQueryWithGranularity() {
 GeoQuery query = new GeoQuery(new GeoLocation(51.5074, -0.1278));
 query.setGranularity("neighborhood");

 HttpParameter[] parameters = query.asHttpParameterArray();
 assertEquals(6, parameters.length);
 assertEquals("neighborhood", parameters[5].getValue());
 }

 @Test
 public void testGeoQueryWithMaxResults() {
 GeoQuery query = new GeoQuery(new GeoLocation(51.5074, -0.1278));
 query.setMaxResults(10);

 HttpParameter[] parameters = query.asHttpParameterArray();
 assertEquals(6, parameters.length);
 assertEquals("10", parameters[4].getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGeoQueryWithNegativeMaxResults() {
 GeoQuery query = new GeoQuery(new GeoLocation(51.5074, -0.1278));
 query.setMaxResults(-5);
 }

 @Test
 public void testHashCode() {
 GeoLocation location = new GeoLocation(51.5074, -0.1278);
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 GeoQuery query3 = new GeoQuery(new GeoLocation(51.5075, -0.1278));

 int hashCode1 = query1.hashCode();
 int hashCode2 = query2.hashCode();
 int hashCode3 = query3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}