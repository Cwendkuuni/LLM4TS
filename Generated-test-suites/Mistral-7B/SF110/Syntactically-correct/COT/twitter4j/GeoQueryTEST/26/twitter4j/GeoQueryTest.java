import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private String ipAddress = "192.168.1.1";

 @Test
 public void testGeoQueryWithLocation() {
 GeoQuery geoQuery = new GeoQuery(location);
 assertNull(geoQuery.getIp());
 assertNotNull(geoQuery.getLocation());
 assertNull(geoQuery.getAccuracy());
 assertNull(geoQuery.getGranularity());
 assertNotNull(geoQuery.asHttpParameterArray());
 }

 @Test
 public void testGeoQueryWithIP() {
 GeoQuery geoQuery = new GeoQuery(ipAddress);
 assertNull(geoQuery.getLocation());
 assertNotNull(geoQuery.getIp());
 assertNull(geoQuery.getAccuracy());
 assertNull(geoQuery.getGranularity());
 assertNotNull(geoQuery.asHttpParameterArray());
 }

 @Test
 public void testSettersAndGetters() {
 GeoQuery geoQuery = new GeoQuery(location);
 geoQuery.setAccuracy("10m");
 geoQuery.setGranularity("city");
 geoQuery.setMaxResults(5);

 assertEquals("10m", geoQuery.getAccuracy());
 assertEquals("city", geoQuery.getGranularity());
 assertEquals(5, geoQuery.getMaxResults());

 HttpParameter[] params = geoQuery.asHttpParameterArray();
 assertEquals(5, params.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingNegativeMaxResults() {
 GeoQuery geoQuery = new GeoQuery(location);
 geoQuery.setMaxResults(-1);
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullLocation() {
 GeoQuery geoQuery = new GeoQuery(ipAddress);
 geoQuery.setLocation(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullAccuracy() {
 GeoQuery geoQuery = new GeoQuery(location);
 geoQuery.setAccuracy(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullGranularity() {
 GeoQuery geoQuery = new GeoQuery(location);
 geoQuery.setGranularity(null);
 }

 @Test
 public void testEquals() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0, 1.0));

 assertTrue(geoQuery1.equals(geoQuery1));
 assertTrue(geoQuery1.equals(geoQuery2));
 assertFalse(geoQuery1.equals(geoQuery3));
 }

 @Test
 public void testHashCode() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0, 1.0));

 int hashCode1 = geoQuery1.hashCode();
 int hashCode2 = geoQuery1.hashCode();
 assertSame(hashCode1, hashCode2);
 int hashCode3 = geoQuery3.hashCode();
 assertNotSame(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 GeoQuery geoQuery = new GeoQuery(location);
 String expected = "GeoQuery{location=GeoLocation[lat=0.0, lng=0.0], ip='null', accuracy='null', granularity='null', maxResults=0}";
 assertEquals(expected, geoQuery.toString());
 }
}