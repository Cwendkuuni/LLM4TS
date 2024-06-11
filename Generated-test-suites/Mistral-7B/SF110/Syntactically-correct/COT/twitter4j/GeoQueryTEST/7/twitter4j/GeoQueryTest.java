import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private GeoQuery queryWithLocation = new GeoQuery(location);
 private GeoQuery queryWithIp = new GeoQuery("192.168.1.1");

 @Test
 public void testGetLocation() {
     assertNotNull(queryWithLocation.getLocation());
     assertEquals(location, queryWithLocation.getLocation());
 }

 @Test
 public void testGetIp() {
     assertNull(queryWithLocation.getIp());
     assertEquals("192.168.1.1", queryWithIp.getIp());
 }

 @Test
 public void testGetAccuracy() {
     assertNull(queryWithLocation.getAccuracy());
     assertNull(queryWithIp.getAccuracy());
 }

 @Test
 public void testSetAndGetAccuracy() {
     String accuracy = "10m";
     GeoQuery query = new GeoQuery().accuracy(accuracy);
     assertEquals(accuracy, query.getAccuracy());
 }

 @Test
 public void testGetGranularity() {
     assertNull(queryWithLocation.getGranularity());
     assertNull(queryWithIp.getGranularity());
 }

 @Test
 public void testSetAndGetGranularity() {
     String granularity = "city";
     GeoQuery query = new GeoQuery().granularity(granularity);
     assertEquals(granularity, query.getGranularity());
 }

 @Test
 public void testGetMaxResults() {
     assertDefaultMaxResults(queryWithLocation);
     assertDefaultMaxResults(queryWithIp);
 }

 @Test
 public void testSetAndGetMaxResults() {
     int maxResults = 50;
     GeoQuery query = new GeoQuery().maxResults(maxResults);
     assertEquals(maxResults, query.getMaxResults());
 }

 @Test
 public void testAsHttpParameterArray() {
     HttpParameter[] paramsWithLocation = queryWithLocation.asHttpParameterArray();
     HttpParameter[] paramsWithIp = queryWithIp.asHttpParameterArray();

     assertEquals(5, paramsWithLocation.length);
     assertEquals(5, paramsWithIp.length);

     for (int i = 0; i < paramsWithLocation.length; i++) {
         String name = paramsWithLocation[i].getName();
         String value = paramsWithLocation[i].getValue();

         if (i < 3) {
             assertEquals("lat", name);
             assertEquals("long", name, () -> { throw new AssertionError("Name should be 'lat' for index " + i); });
             assertEquals(0.0 + i, Double.parseDouble(value), 0.001);
         } else {
             switch (name) {
                 case "accuracy":
                     assertNull(value);
                     break;
                 case "granularity":
                     assertNull(value);
                     break;
                 case "max_results":
                     assertEquals(Integer.toString(42 + i), value);
                     break;
                 default:
                     fail("Unexpected name: " + name);
             }
         }
     }

     for (int i = 0; i < paramsWithIp.length; i++) {
         String name = paramsWithIp[i].getName();
         String value = paramsWithIp[i].getValue();

         assertEquals("ip", name);
         assertEquals("192.168.1." + (i + 1), value);
     }
 }

 private void assertDefaultMaxResults(GeoQuery query) {
     int maxResults = query.getMaxResults();
     assertEquals(25, maxResults);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeMaxResults() {
     GeoQuery query = new GeoQuery();
     query.setMaxResults(-1);
 }

 @Test
 public void testEquals() {
     GeoQuery query1 = new GeoQuery();
     GeoQuery query2 = new GeoQuery();
     GeoQuery query3 = new GeoQuery().maxResults(50);

     assertEquals(query1, query1);
     assertEquals(query1, query2);
     assertNotEquals(query1, query3);
 }

 @Test
 public void testHashCode() {
     GeoQuery query1 = new GeoQuery();
     GeoQuery query2 = new GeoQuery().maxResults(50);

     int hashCode1 = query1.hashCode();
     int hashCode2 = query2.hashCode();

     GeoLocation location = new GeoLocation(1.0, 2.0);
     GeoQuery query3 = new GeoQuery().location(location);
     int hashCode3 = query3.hashCode();

     assertEquals(hashCode1, hashCode1);
     assertNotEquals(hashCode1, hashCode2);
     assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
     GeoQuery query = new GeoQuery();
     assertNotNull(query.toString());
 }
}