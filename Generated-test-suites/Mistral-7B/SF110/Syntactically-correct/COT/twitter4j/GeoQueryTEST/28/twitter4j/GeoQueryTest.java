import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private String ipAddress = "192.168.1.1";

 @Test
 public void testGeoQueryWithLocation() {
 GeoQuery geoQuery = new GeoQuery(location);
 HttpParameter[] params = geoQuery.asHttpParameterArray();
 assertNotNull(params);
 assertEquals(5, params.length);
 }

 @Test
 public void testGeoQueryWithIpAddress() {
 GeoQuery geoQuery = new GeoQuery(ipAddress);
 HttpParameter[] params = geoQuery.asHttpParameterArray();
 assertNotNull(params);
 assertEquals(2, params.length);
 }

 @Test(expected = NullPointerException.class)
 public void testGeoQueryWithNullLocation() {
 GeoQuery geoQuery = new GeoQuery(null);
 HttpParameter[] params = geoQuery.asHttpParameterArray();
 fail("Expected NullPointerException but got: " + params);
 }

 @Test(expected = NullPointerException.class)
 public void testGeoQueryWithNullIpAddress() {
 GeoQuery geoQuery = new GeoQuery(null);
 HttpParameter[] params = geoQuery.asHttpParameterArray();
 fail("Expected NullPointerException but got: " + params);
 }

 @Test
 public void testSetAndGetAccuracy() {
 GeoQuery geoQuery = new GeoQuery(location);
 geoQuery.setAccuracy("city");
 assertEquals("city", geoQuery.getAccuracy());
 geoQuery.setAccuracy("neighborhood");
 assertEquals("neighborhood", geoQuery.getAccuracy());
 }

 @Test
 public void testSetAndGetGranularity() {
 GeoQuery geoQuery = new GeoQuery(location);
 geoQuery.setGranularity("city");
 assertEquals("city", geoQuery.getGranularity());
 geoQuery.setGranularity("neighborhood");
 assertEquals("neighborhood", geoQuery.getGranularity());
 }

 @Test
 public void testSetAndGetMaxResults() {
 GeoQuery geoQuery = new GeoQuery(location);
 geoQuery.setMaxResults(10);
 assertEquals(10, geoQuery.getMaxResults());
 geoQuery.setMaxResults(5);
 assertEquals(5, geoQuery.getMaxResults());
 }

 @Test
 public void testEquals() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 assertTrue(geoQuery1.equals(geoQuery2));
 geoQuery1.setMaxResults(10);
 assertFalse(geoQuery1.equals(geoQuery2));
 geoQuery2.setMaxResults(10);
 assertTrue(geoQuery1.equals(geoQuery2));
 }

 @Test
 public void testHashCode() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 int hashCode1 = geoQuery1.hashCode();
 int hashCode2 = geoQuery2.hashCode();
 assertEquals(hashCode1, hashCode2);
 geoQuery1.setMaxResults(10);
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testToString() {
 GeoQuery geoQuery = new GeoQuery(location);
 assertEquals("GeoQuery{location=" + location.toString() + ", ip='null' , accuracy='null' , granularity='null' , maxResults=0}", geoQuery.toString());
 }
}