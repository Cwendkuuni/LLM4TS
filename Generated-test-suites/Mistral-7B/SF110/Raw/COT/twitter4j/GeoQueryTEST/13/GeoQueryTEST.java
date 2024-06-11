import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(1.0d, 2.0d);
 private GeoQuery queryWithLocation = new GeoQuery(location);
 private GeoQuery queryWithIp = new GeoQuery("1.2.3.4");

 @Test
 public void testGetLocation() {
 GeoLocation actualLocation = queryWithLocation.getLocation();
 assertEquals(location, actualLocation);
 }

 @Test
 public void testGetIp() {
 String actualIp = queryWithIp.getIp();
 assertEquals("1.2.3.4", actualIp);
 }

 @Test
 public void testGetAccuracy() {
 String actualAccuracy = queryWithLocation.getAccuracy();
 assertNull(actualAccuracy);
 }

 @Test
 public void testGetGranularity() {
 String actualGranularity = queryWithLocation.getGranularity();
 assertNull(actualGranularity);
 }

 @Test
 public void testGetMaxResults() {
 int actualMaxResults = queryWithLocation.getMaxResults();
 assertNotNull(actualMaxResults);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAccuracyWithNullArgument() {
 GeoQuery query = new GeoQuery();
 query.accuracy(null);
 }

 @Test
 public void testAccuracy() {
 GeoQuery query = new GeoQuery();
 String accuracy = "high";
 query.accuracy(accuracy);
 assertEquals(accuracy, query.getAccuracy());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testGranularityWithNullArgument() {
 GeoQuery query = new GeoQuery();
 query.granularity(null);
 }

 @Test
 public void testGranularity() {
 GeoQuery query = new GeoQuery();
 String granularity = "country";
 query.granularity(granularity);
 assertEquals(granularity, query.getGranularity());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testMaxResultsWithNegativeArgument() {
 GeoQuery query = new GeoQuery();
 query.maxResults(-1);
 }

 @Test
 public void testMaxResults() {
 GeoQuery query = new GeoQuery();
 int maxResults = 5;
 query.maxResults(maxResults);
 assertEquals(maxResults, query.getMaxResults());
 }

 @Test
 public void testEquals() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 assertTrue(query1.equals(query2));
 }

 @Test
 public void testEqualsDifferentMaxResults() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 query2.setMaxResults(1);
 assertFalse(query1.equals(query2));
 }

 @Test
 public void testEqualsDifferentAccuracy() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 query2.setAccuracy("high");
 assertFalse(query1.equals(query2));
 }

 @Test
 public void testEqualsDifferentGranularity() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 query2.setGranularity("city");
 assertFalse(query1.equals(query2));
 }

 @Test
 public void testEqualsDifferentIp() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(new GeoLocation(2.0d, 3.0d));
 assertFalse(query1.equals(query2));
 }

 @Test
 public void testHashcode() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 assertEquals(query1.hashCode(), query2.hashCode());
 }

 @Test
 public void testHashcodeDifferentLocation() {
 GeoQuery query1 = new GeoQuery(location);
 GeoLocation differentLocation = new GeoLocation(3.0d, 4.0d);
 GeoQuery query2 = new GeoQuery(differentLocation);
 assertNotEquals(query1.hashCode(), query2.hashCode());
 }

 @Test
 public void testHashcodeDifferentAccuracy() {
 GeoQuery query1 = new GeoQuery(location);
 query1.setAccuracy("high");
 GeoQuery query2 = new GeoQuery(location);
 assertNotEquals(query1.hashCode(), query2.hashCode());
 }

 @Test
 public void testHashcodeDifferentGranularity() {
 GeoQuery query1 = new GeoQuery(location);
 query1.setGranularity("city");
 GeoQuery query2 = new GeoQuery(location);
 assertNotEquals(query1.hashCode(), query2.hashCode());
 }

 @Test
 public void testHashcodeDifferentIp() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 assertNotEquals(query1.hashCode(), query2.hashCode());
 }

 @Test
 public void testHashcodeDifferentMaxResults() {
 GeoQuery query1 = new GeoQuery(location);
 query1.setMaxResults(5);
 GeoQuery query2 = new GeoQuery(location);
 query2.setMaxResults(10);
 assertNotEquals(query1.hashCode(), query2.hashCode());
 }
}