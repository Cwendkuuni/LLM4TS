import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation validLocation = new GeoLocation(51.5072, -0.1278);
 private GeoLocation invalidLocation = new GeoLocation(90.0, 180.0);
 private String validIp = "192.168.1.1";
 private String invalidIp = "256.0.0.1";
 private String validAccuracy = "10m";
 private String invalidAccuracy = null;
 private String validGranularity = "city";
 private String invalidGranularity = null;
 private int validMaxResults = 10;
 private int invalidMaxResults = -1;

 @Test
 public void testConstructorWithValidLocation() {
 GeoQuery geoQuery = new GeoQuery(validLocation);
 assertNull(geoQuery.getIp());
 assertNotNull(geoQuery.getLocation());
 assertNull(geoQuery.getAccuracy());
 assertNull(geoQuery.getGranularity());
 assertNotNull(geoQuery.asHttpParameterArray());
 assertFalse(geoQuery.equals(null));
 }

 @Test
 public void testConstructorWithValidIp() {
 GeoQuery geoQuery = new GeoQuery(validIp);
 assertNull(geoQuery.getLocation());
 assertNull(geoQuery.getIp());
 assertNull(geoQuery.getAccuracy());
 assertNull(geoQuery.getGranularity());
 assertNotNull(geoQuery.asHttpParameterArray());
 assertFalse(geoQuery.equals(null));
 }

 @Test
 public void testGetLocation() {
 GeoQuery geoQuery = new GeoQuery(validLocation);
 GeoLocation actualLocation = geoQuery.getLocation();
 assertEquals(validLocation, actualLocation);
 }

 @Test
 public void testGetIp() {
 GeoQuery geoQuery1 = new GeoQuery(validLocation);
 geoQuery1.setIp(validIp);
 GeoQuery geoQuery2 = new GeoQuery(validIp);
 String actualIp1 = geoQuery1.getIp();
 String actualIp2 = geoQuery2.getIp();
 assertEquals(validIp, actualIp1);
 assertEquals(validIp, actualIp2);
 }

 @Test
 public void testSetAndGetAccuracy() {
 GeoQuery geoQuery = new GeoQuery(validLocation);
 String validAccuracy = "50m";
 String actualAccuracy = geoQuery.setAccuracy(validAccuracy).getAccuracy();
 assertEquals(validAccuracy, actualAccuracy);
 }

 @Test
 public void testSetAndGetGranularity() {
 GeoQuery geoQuery = new GeoQuery(validLocation);
 String validGranularity = "neighborhood";
 String actualGranularity = geoQuery.setGranularity(validGranularity).getGranularity();
 assertEquals(validGranularity, actualGranularity);
 }

 @Test
 public void testGetMaxResults() {
 GeoQuery geoQuery = new GeoQuery(validLocation);
 int validMaxResults = 5;
 int actualMaxResults = geoQuery.setMaxResults(validMaxResults).getMaxResults();
 assertEquals(validMaxResults, actualMaxResults);
 }

 @Test
 public void testAppendParameter() {
 List<HttpParameter> params = new ArrayList<>();
 GeoQuery geoQuery = new GeoQuery(validLocation);
 geoQuery.appendParameter("name", "test", params);
 HttpParameter[] expectedParamArray = new HttpParameter[] { new HttpParameter("name", "test") };
 HttpParameter[] actualParamArray = geoQuery.asHttpParameterArray();
 assertArrayEquals(expectedParamArray, actualParamArray);
 }

 @Test
 public void testEquals() {
 GeoQuery geoQuery1 = new GeoQuery(validLocation);
 GeoQuery geoQuery2 = new GeoQuery(validLocation);
 GeoQuery geoQuery3 = new GeoQuery(invalidLocation);
 assertFalse(geoQuery1.equals(null));
 assertTrue(geoQuery1.equals(geoQuery2));
 assertFalse(geoQuery1.equals(geoQuery3));
 }

 @Test
 public void testHashCode() {
 GeoQuery geoQuery1 = new GeoQuery(validLocation);
 GeoQuery geoQuery2 = new GeoQuery(validLocation);
 GeoQuery geoQuery3 = new GeoQuery(invalidLocation);
 int expectedHashCode = 31 * (31 * validLocation.hashCode() + validLocation.hashCode()) + validLocation.hashCode();
 assertEquals(expectedHashCode, geoQuery1.hashCode());
 assertEquals(expectedHashCode, geoQuery2.hashCode());
 int differentHashCode = 31 * (31 * invalidLocation.hashCode() + invalidLocation.hashCode()) + invalidLocation.hashCode();
 assertNotEquals(expectedHashCode, geoQuery3.hashCode());
 }

 @Test
 public void testToString() {
 GeoQuery geoQuery = new GeoQuery(validLocation);
 String expectedString = "GeoQuery{location=" + validLocation + ", ip='" + null + "'\"" + ", accuracy='" + null + "'\"" + ", granularity='" + null + "'\"" + ", maxResults=10}";
 String actualString = geoQuery.toString();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testConstructorWithInvalidLocation() {
 assertThrows(IllegalArgumentException.class, () -> new GeoQuery(invalidLocation));
 }

 @Test
 public void testConstructorWithInvalidIp() {
 assertThrows(IllegalArgumentException.class, () -> new GeoQuery(invalidIp));
 }

 @Test
 public void testSetAndGetAccuracyWithNullValue() {
 GeoQuery geoQuery = new GeoQuery(validLocation);
 assertNull(geoQuery.getAccuracy());
 geoQuery.setAccuracy(invalidAccuracy);
 assertNull(geoQuery.getAccuracy());
 }

 @Test
 public void testSetAndGetGranularityWithNullValue() {
 GeoQuery geoQuery = new GeoQuery(validLocation);
 assertNull(geoQuery.getGranularity());
 geoQuery.setGranularity(invalidGranularity);
 assertNull(geoQuery.getGranularity());
 }

 @Test
 public void testSetMaxResultsNegativeValue() {
 assertThrows(IllegalArgumentException.class, () -> new GeoQuery(validLocation).maxResults(-1));
 }

 @Test
 public void testSetAndGetMaxResultsZeroValue() {
 GeoQuery geoQuery = new GeoQuery(validLocation);
 int zeroValue = 0;
 int actualMaxResults = geoQuery.setMaxResults(zeroValue).getMaxResults();
 assertEquals(zeroValue, actualMaxResults);
 }
}