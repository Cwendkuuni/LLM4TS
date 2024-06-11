import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private GeoQuery geoQueryWithLocation = new GeoQuery(location);
 private GeoQuery geoQueryWithIp = new GeoQuery("123.45.67.89");

 @Test
 public void testGetLocation() {
 GeoLocation expectedLocation = new GeoLocation(0.0, 0.0);
 GeoLocation actualLocation = geoQueryWithLocation.getLocation();
 assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testGetIp() {
 String expectedIp = "123.45.67.89";
 String actualIp = geoQueryWithIp.getIp();
 assertEquals(expectedIp, actualIp);
 }

 @Test
 public void testGetAccuracy() {
 String expectedAccuracy = null;
 String actualAccuracy = geoQueryWithLocation.getAccuracy();
 assertEquals(expectedAccuracy, actualAccuracy);

 expectedAccuracy = "10m";
 geoQueryWithLocation.setAccuracy(expectedAccuracy);
 actualAccuracy = geoQueryWithLocation.getAccuracy();
 assertEquals(expectedAccuracy, actualAccuracy);
 }

 @Test
 public void testSetAndGetGranularity() {
 String expectedGranularity = "city";
 String actualGranularity = geoQueryWithLocation.getGranularity();
 assertEquals(expectedGranularity, actualGranularity);

 geoQueryWithLocation.setGranularity(expectedGranularity);
 actualGranularity = geoQueryWithLocation.getGranularity();
 assertEquals(expectedGranularity, actualGranularity);
 }

 @Test
 public void testGetMaxResults() {
 int expectedMaxResults = 5;
 int actualMaxResults = geoQueryWithLocation.getMaxResults();
 assertEquals(expectedMaxResults, actualMaxResults);

 geoQueryWithLocation.setMaxResults(expectedMaxResults);
 actualMaxResults = geoQueryWithLocation.getMaxResults();
 assertEquals(expectedMaxResults, actualMaxResults);
 }

 @Test
 public void testAppendParameter() {
 List<HttpParameter> params = new ArrayList<>();
 geoQueryWithLocation.appendParameter("lat", Double.toString(location.getLatitude()), params);
 geoQueryWithLocation.appendParameter("long", Double.toString(location.getLongitude()), params);

 assertEquals(2, params.size());
 HttpParameter latParam = params.get(0);
 assertEquals("lat", latParam.getName());
 assertEquals(Double.toString(location.getLatitude()), latParam.getValue());

 HttpParameter longParam = params.get(1);
 assertEquals("long", longParam.getName());
 assertEquals(Double.toString(location.getLongitude()), longParam.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullName() {
 List<HttpParameter> params = new ArrayList<>();
 geoQueryWithLocation.appendParameter(null, "value", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullValue() {
 List<HttpParameter> params = new ArrayList<>();
 geoQueryWithLocation.appendParameter("name", null, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithEmptyName() {
 List<HttpParameter> params = new ArrayList<>();
 geoQueryWithLocation.appendParameter("", "value", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithEmptyValue() {
 List<HttpParameter> params = new ArrayList<>();
 geoQueryWithLocation.appendParameter("name", "", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNegativeValue() {
 List<HttpParameter> params = new ArrayList<>();
 geoQueryWithLocation.appendParameter("name", "-1", params);
 }

 @Test
 public void testEquals() {
 GeoQuery geoQueryWithSameValues = new GeoQuery(location);
 assertTrue(geoQueryWithLocation.equals(geoQueryWithSameValues));
 }

 @Test
 public void testEqualsWithDifferentIp() {
 GeoQuery geoQueryWithDifferentIp = new GeoQuery("123.45.67.90");
 assertFalse(geoQueryWithLocation.equals(geoQueryWithDifferentIp));
 }

 @Test
 public void testEqualsWithDifferentLocation() {
 GeoLocation differentLocation = new GeoLocation(1.0, 1.0);
 GeoQuery geoQueryWithDifferentLocation = new GeoQuery(differentLocation);
 assertFalse(geoQueryWithLocation.equals(geoQueryWithSameValues));
 }

 @Test
 public void testEqualsWithSameObject() {
 assertTrue(geoQueryWithLocation.equals(geoQueryWithLocation));
 }

 @Test
 public void testHashCode() {
 GeoQuery geoQueryWithSameValues = new GeoQuery(location);
 int expectedHashCode = 5; // calculated based on location, ip, accuracy, granularity, and maxResults
 int actualHashCode = geoQueryWithLocation.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(geoQueryWithSameValues.hashCode(), actualHashCode);
 }

 @Test
 public void testToString() {
 String expectedString = "GeoQuery{location=GeoLocation(latitude=0.0, longitude=0.0), ip='', accuracy='null', granularity='null', maxResults=5}";
 String actualString = geoQueryWithLocation.toString();
 assertEquals(expectedString, actualString);
 }
}