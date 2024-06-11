import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location;
 private String ip;
 private String accuracy;
 private String granularity;
 private int maxResults;

 @Test
 public void testGeoQueryWithLocation() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
 assertNull(geoQuery.getIp());
 assertNotNull(geoQuery.getLocation());
 HttpParameter[] params = geoQuery.asHttpParameterArray();
 assertEquals(5, params.length);
 }

 @Test
 public void testGeoQueryWithIp() {
 GeoQuery geoQuery = new GeoQuery("1.2.3.4");
 assertNull(geoQuery.getLocation());
 assertNotNull(geoQuery.getIp());
 HttpParameter[] params = geoQuery.asHttpParameterArray();
 assertEquals(4, params.length);
 }

 @Test (expected = NullPointerException.class)
 public void testSetNullAccuracy() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery.setAccuracy(null);
 }

 @Test
 public void testSetAndGetAccuracy() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery.setAccuracy("10m");
 assertEquals("10m", geoQuery.getAccuracy());
 }

 @Test (expected = NullPointerException.class)
 public void testSetNullGranularity() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery.setGranularity(null);
 }

 @Test
 public void testSetAndGetGranularity() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery.setGranularity("city");
 assertEquals("city", geoQuery.getGranularity());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSetNegativeMaxResults() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery.setMaxResults(-1);
 }

 @Test
 public void testSetAndGetMaxResults() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery.setMaxResults(10);
 assertEquals(10, geoQuery.getMaxResults());
 }

 @Test
 public void testEquals() {
 GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(0.0, 0.0));
 GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery1.setAccuracy("10m");
 geoQuery2.setAccuracy("10m");
 geoQuery1.setGranularity("city");
 geoQuery2.setGranularity("city");
 geoQuery1.setMaxResults(10);
 geoQuery2.setMaxResults(10);
 geoQuery1.setIp("1.2.3.4");
 geoQuery2.setIp("1.2.3.4");
 assertTrue(geoQuery1.equals(geoQuery2));

 GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery1.setIp("1.2.3.4");
 geoQuery3.setIp("1.2.3.5");
 assertFalse(geoQuery1.equals(geoQuery3));
 }

 @Test
 public void testHashCode() {
 GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(0.0, 0.0));
 GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery1.setAccuracy("10m");
 geoQuery2.setAccuracy("10m");
 geoQuery1.setGranularity("city");
 geoQuery2.setGranularity("city");
 geoQuery1.setMaxResults(10);
 geoQuery2.setMaxResults(10);
 geoQuery1.setIp("1.2.3.4");
 geoQuery2.setIp("1.2.3.5");
 assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());

 GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQuery1.setIp("1.2.3.4");
 geoQuery3.setIp("1.2.3.5");
 assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
 }

 @Test
 public void testToString() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(0.0, 0.0));
 assertEquals("GeoQuery{location=null, ip='null', accuracy='null', granularity='null', maxResults=0}", geoQuery.toString());

 GeoQuery geoQueryWithLocation = new GeoQuery(new GeoLocation(0.0, 0.0));
 geoQueryWithLocation.setAccuracy("10m");
 geoQueryWithLocation.setGranularity("city");
 geoQueryWithLocation.setMaxResults(10);
 geoQueryWithLocation.setIp("1.2.3.4");
 GeoLocation location = new GeoLocation(5.5, 6.6);
 geoQueryWithLocation.setLocation(location);
 assertEquals(String.format("GeoQuery{location=%s, ip='1.2.3.4', accuracy='10m', granularity='city', maxResults=10}", location), geoQueryWithLocation.toString());
 }
}