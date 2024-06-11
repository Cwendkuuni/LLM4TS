import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private String ipAddress = "127.0.0.1";
 private GeoQuery geoQueryLocation, geoQueryIp, geoQueryNull;

 @Test
 public void testConstructorWithLocation() {
     geoQueryLocation = new GeoQuery(location);
     assertNull(geoQueryLocation.getIp());
     assertNotNull(geoQueryLocation.getLocation());
 }

 @Test
 public void testConstructorWithIp() {
     geoQueryIp = new GeoQuery(ipAddress);
     assertNull(geoQueryIp.getLocation());
     assertNotNull(geoQueryIp.getIp());
 }

 @Test
 public void testGetLocation() {
     geoQueryLocation = new GeoQuery(location);
     assertEquals(location, geoQueryLocation.getLocation());
 }

 @Test
 public void testGetIp() {
     geoQueryIp = new GeoQuery(ipAddress);
     assertEquals(ipAddress, geoQueryIp.getIp());
 }

 @Test
 public void testGetAccuracy() {
     String accuracy = "10m";
     geoQueryLocation = new GeoQuery(location).accuracy(accuracy);
     assertEquals(accuracy, geoQueryLocation.getAccuracy());
 }

 @Test
 public void testSetAndGetGranularity() {
     String granularity = "city";
     geoQueryLocation = new GeoQuery(location).granularity(granularity);
     assertEquals(granularity, geoQueryLocation.getGranularity());
 }

 @Test
 public void testGetMaxResults() {
     geoQueryLocation = new GeoQuery(location).maxResults(5);
     assertEquals(5, geoQueryLocation.getMaxResults());
 }

 @Test
 public void testAsHttpParameterArray() {
     geoQueryLocation = new GeoQuery(location).accuracy("10m").granularity("city").maxResults(5);
     HttpParameter[] expectedParams = {new HttpParameter("lat", String.valueOf(location.getLatitude())),
             new HttpParameter("long", String.valueOf(location.getLongitude())),
             new HttpParameter("accuracy", "10m"),
             new HttpParameter("granularity", "city"),
             new HttpParameter("max_results", "5")};
     HttpParameter[] actualParams = geoQueryLocation.asHttpParameterArray();
     assertArrayEquals(expectedParams, actualParams);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeMaxResults() {
     geoQueryLocation = new GeoQuery(location);
     geoQueryLocation.maxResults(-1);
 }

 @Test
 public void testEqualsObjectNull() {
     assertFalse(geoQueryLocation.equals(null));
 }

 @Test
 public void testEqualsObjectDifferentType() {
     assertFalse(geoQueryLocation.equals(new Object()));
 }

 @Test
 public void testEqualsObjectSameInstance() {
     GeoQuery geoQueryLocationCopy = new GeoQuery(location).accuracy("10m").granularity("city").maxResults(5);
     assertTrue(geoQueryLocation.equals(geoQueryLocationCopy));
 }

 @Test
 public void testEqualsObjectSameValuesButDifferentInstance() {
     GeoQuery geoQueryLocationCopy = new GeoQuery(new GeoLocation(0.0, 0.0)).accuracy("10m").granularity("city").maxResults(5);
     assertTrue(geoQueryLocation.equals(geoQueryLocationCopy));
 }

 @Test
 public void testEqualsObjectDifferentValues() {
     GeoQuery geoQueryLocationDifferent = new GeoQuery(location).accuracy("50m").granularity("neighborhood").maxResults(10);
     assertFalse(geoQueryLocation.equals(geoQueryLocationDifferent));
 }

 @Test
 public void testHashCode() {
     GeoQuery geoQueryLocation1 = new GeoQuery(location).accuracy("10m").granularity("city").maxResults(5);
     GeoQuery geoQueryLocation2 = new GeoQuery(new GeoLocation(0.0, 0.0)).accuracy("10m").granularity("city").maxResults(5);
     GeoQuery geoQueryLocation3 = new GeoQuery(location).accuracy("10m").granularity("city").maxResults(10);
     assertEquals(geoQueryLocation1.hashCode(), geoQueryLocation2.hashCode());
     assertNotEquals(geoQueryLocation1.hashCode(), geoQueryLocation3.hashCode());
 }

 @Test
 public void testToString() {
     String expectedString = "GeoQuery{location=GeoLocation{latitude=0.0, longitude=0.0}, ip='', accuracy='10m', granularity='city', maxResults=5}";
     String actualString = geoQueryLocation.toString();
     assertEquals(expectedString, actualString);
 }

 // Edge cases and exception handling
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullLocation() {
     geoQueryNull = new GeoQuery(null);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullIp() {
     String nullIp = null;
     geoQueryIp = new GeoQuery(nullIp);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeAccuracy() {
     geoQueryLocation = new GeoQuery(location);
     geoQueryLocation.accuracy("-10m");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNullAccuracy() {
     geoQueryLocation = new GeoQuery(location);
     geoQueryLocation.accuracy(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetEmptyGranularity() {
     geoQueryLocation = new GeoQuery(location);
     geoQueryLocation.granularity("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNullGranularity() {
     geoQueryLocation = new GeoQuery(location);
     geoQueryLocation.granularity(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetNegativeMaxResults() {
     geoQueryLocation = new GeoQuery(location);
     geoQueryLocation.maxResults(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetZeroMaxResults() {
     geoQueryLocation = new GeoQuery(location);
     geoQueryLocation.maxResults(0);
 }

}