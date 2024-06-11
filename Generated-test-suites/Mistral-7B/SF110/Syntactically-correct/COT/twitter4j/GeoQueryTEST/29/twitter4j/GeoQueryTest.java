import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(1.0, 2.0);
 private GeoQuery queryWithLocation;
 private GeoQuery queryWithIP;

 @Test
 public void testGeoQueryWithLocation() {
 queryWithLocation = new GeoQuery(location);
 HttpParameter[] params = queryWithLocation.asHttpParameterArray();
 Assert.assertNotNull(params);
 Assert.assertEquals(5, params.length);
 }

 @Test(expected = NullPointerException.class)
 public void testGeoQueryWithNullLocation() {
 queryWithLocation = new GeoQuery(null);
 }

 @Test
 public void testGeoQueryWithIP() {
 queryWithIP = new GeoQuery("192.168.1.1");
 HttpParameter[] params = queryWithIP.asHttpParameterArray();
 Assert.assertNotNull(params);
 Assert.assertEquals(4, params.length);
 }

 @Test(expected = NullPointerException.class)
 public void testGeoQueryWithNullIP() {
 queryWithIP = new GeoQuery(null);
 }

 @Test
 public void testGetLocation() {
 GeoLocation expectedLocation = location;
 GeoLocation actualLocation = queryWithLocation.getLocation();
 Assert.assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testGetIp() {
 String expectedIp = "192.168.1.1";
 String actualIp = queryWithIP.getIp();
 Assert.assertEquals(expectedIp, actualIp);
 }

 @Test
 public void testGetAccuracy() {
 String expectedAccuracy = null;
 String actualAccuracy = queryWithLocation.getAccuracy();
 Assert.assertEquals(expectedAccuracy, actualAccuracy);
 }

 @Test
 public void testSetAndGetAccuracy() {
 String newAccuracy = "precise";
 queryWithLocation.setAccuracy(newAccuracy);
 String actualAccuracy = queryWithLocation.getAccuracy();
 Assert.assertEquals(newAccuracy, actualAccuracy);
 }

 @Test
 public void testGetGranularity() {
 String expectedGranularity = null;
 String actualGranularity = queryWithLocation.getGranularity();
 Assert.assertEquals(expectedGranularity, actualGranularity);
 }

 @Test
 public void testSetAndGetGranularity() {
 String newGranularity = "country";
 queryWithLocation.setGranularity(newGranularity);
 String actualGranularity = queryWithLocation.getGranularity();
 Assert.assertEquals(newGranularity, actualGranularity);
 }

 @Test
 public void testGetMaxResults() {
 int expectedMaxResults = 10;
 int actualMaxResults = queryWithLocation.getMaxResults();
 Assert.assertEquals(expectedMaxResults, actualMaxResults);
 }

 @Test
 public void testSetAndGetMaxResults() {
 int newMaxResults = 20;
 queryWithLocation.setMaxResults(newMaxResults);
 int actualMaxResults = queryWithLocation.getMaxResults();
 Assert.assertEquals(newMaxResults, actualMaxResults);
 }

 @Test
 public void testAppendParameterWithStringValue() {
 List<HttpParameter> params = new ArrayList<>();
 queryWithLocation.appendParameter("lat", "1.0", params);
 HttpParameter param = params.get(0);
 Assert.assertEquals("lat", param.getName());
 Assert.assertEquals("1.0", param.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testAppendParameterWithNullName() {
 List<HttpParameter> params = new ArrayList<>();
 queryWithLocation.appendParameter(null, "1.0", params);
 }

 @Test(expected = NullPointerException.class)
 public void testAppendParameterWithNullValue() {
 List<HttpParameter> params = new ArrayList<>();
 queryWithLocation.appendParameter("lat", null, params);
 }

 @Test
 public void testAppendParameterWithIntValue() {
 List<HttpParameter> params = new ArrayList<>();
 queryWithLocation.appendParameter("max_results", 10, params);
 HttpParameter param = params.get(params.size() - 1);
 Assert.assertEquals("max_results", param.getName());
 Assert.assertEquals("10", param.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNegativeIntValue() {
 List<HttpParameter> params = new ArrayList<>();
 queryWithLocation.appendParameter("max_results", -1, params);
 }

 @Test
 public void testEquals() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 assertTrue(geoQuery1.equals(geoQuery2));
 }

 @Test
 public void testEqualsWithDifferentMaxResults() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 geoQuery2.setMaxResults(20);
 assertFalse(geoQuery1.equals(geoQuery2));
 }

 @Test
 public void testEqualsWithDifferentAccuracy() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 geoQuery2.setAccuracy("coarse");
 assertFalse(geoQuery1.equals(geoQuery2));
 }

 @Test
 public void testEqualsWithDifferentGranularity() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 geoQuery2.setGranularity("city");
 assertFalse(geoQuery1.equals(geoQuery2));
 }

 @Test
 public void testEqualsWithDifferentIP() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery("192.168.1.2");
 assertFalse(geoQuery1.equals(geoQuery2));
 }

 @Test
 public void testEqualsWithDifferentLocation() {
 GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(1.0, 2.0));
 GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(3.0, 4.0));
 assertFalse(geoQuery1.equals(geoQuery2));
 }

 @Test
 public void testHashCode() {
 int expectedHashCode = 51;
 int actualHashCode = queryWithLocation.hashCode();
 Assert.assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testToString() {
 String expectedToString = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='null', accuracy='null', granularity='null', maxResults=10}";
 String actualToString = queryWithLocation.toString();
 Assert.assertEquals(expectedToString, actualToString);
 }

}