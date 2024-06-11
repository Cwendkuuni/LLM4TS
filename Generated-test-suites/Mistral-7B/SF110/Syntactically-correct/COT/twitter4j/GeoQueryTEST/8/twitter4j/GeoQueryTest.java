import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location;
 private String ipAddress;
 private String accuracy;
 private String granularity;
 private int maxResults;

 @Test
 public void testGeoQueryWithLocation() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 HttpParameter[] params = geoQuery.asHttpParameterArray();
 assertEquals(5, params.length);
 Double lat = (Double) getValueFromParams(params, "lat");
 Double lon = (Double) getValueFromParams(params, "long");
 assertNotNull(lat);
 assertNotNull(lon);
 assertEquals(1.0d, lat.doubleValue(), 0.01);
 assertEquals(2.0d, lon.doubleValue(), 0.01);
 }

 @Test
 public void testGeoQueryWithIPAddress() {
 GeoQuery geoQuery = new GeoQuery("192.168.1.1");
 HttpParameter[] params = geoQuery.asHttpParameterArray();
 assertEquals(5, params.length);
 String ip = (String) getValueFromParams(params, "ip");
 assertNotNull(ip);
 assertEquals("192.168.1.1", ip);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetLocationNull() {
 GeoQuery geoQuery = new GeoQuery(null);
 assertNull(geoQuery.getLocation());
 try {
 geoQuery.setLocation(null);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAccuracyNull() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 String nullAccuracy = null;
 try {
 geoQuery.setAccuracy(nullAccuracy);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSetMaxResultsNegative() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 try {
 geoQuery.setMaxResults(-1);
 fail("Should not allow negative maxResults");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testSetMaxResultsZero() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 geoQuery.setMaxResults(0);
 assertEquals(0, geoQuery.getMaxResults());
 }

 @Test
 public void testSetMaxResultsPositive() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 geoQuery.setMaxResults(10);
 assertEquals(10, geoQuery.getMaxResults());
 }

 @Test
 public void testEquals() {
 GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 GeoQuery geoQuery3 = new GeoQuery();
 GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 geoQuery1.setMaxResults(5);
 geoQuery2.setMaxResults(10);
 geoQuery3.setAccuracy("city");
 geoQuery4.setAccuracy("neighborhood");

 assertTrue(geoQuery1.equals(geoQuery1));
 assertTrue(geoQuery1.equals(geoQuery2));
 assertTrue(geoQuery2.equals(geoQuery1));
 assertFalse(geoQuery1.equals(geoQuery3));
 assertFalse(geoQuery1.equals(geoQuery4));
 assertFalse(geoQuery3.equals(geoQuery4));
 }

 @Test
 public void testHashCode() {
 GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 GeoQuery geoQuery3 = new GeoQuery();
 GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 geoQuery1.setMaxResults(5);
 geoQuery2.setMaxResults(10);
 geoQuery3.setAccuracy("city");
 geoQuery4.setAccuracy("neighborhood");

 int hashCode1 = geoQuery1.hashCode();
 int hashCode2 = geoQuery2.hashCode();
 int hashCode3 = geoQuery3.hashCode();
 int hashCode4 = geoQuery4.hashCode();

 assertTrue(geoQuery1.equals(geoQuery1));
 assertTrue(geoQuery1.hashCode() == hashCode1);
 assertTrue(geoQuery1.equals(geoQuery2));
 assertTrue(geoQuery1.hashCode() == hashCode2);
 assertFalse(geoQuery1.equals(geoQuery3));
 assertFalse(geoQuery1.hashCode() == hashCode3);
 assertFalse(geoQuery1.equals(geoQuery4));
 assertFalse(geoQuery1.hashCode() == hashCode4);
 }

 @Test
 public void testToString() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(1.0d, 2.0d));
 geoQuery.setAccuracy("city");
 geoQuery.setGranularity("neighborhood");
 geoQuery.setMaxResults(10);
 String expected = "GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='', accuracy='city', granularity='neighborhood', maxResults=10}";
 assertEquals(expected, geoQuery.toString());
 }

 private Object getValueFromParams(HttpParameter[] params, String name) {
 for (HttpParameter param : params) {
 if (param.getName().equals(name)) {
 return param.getValue();
 }
 }
 return null;
 }
}