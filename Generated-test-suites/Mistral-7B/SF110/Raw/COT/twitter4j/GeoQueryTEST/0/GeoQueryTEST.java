import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private GeoQuery geoQuery;

 @Test
 public void testGeoQueryWithLocationConstructor() {
 geoQuery = new GeoQuery(location);
 assertNull(geoQuery.getIp());
 assertNotNull(geoQuery.getLocation());
 assertNull(geoQuery.getAccuracy());
 assertNull(geoQuery.getGranularity());
 assertNotNull(geoQuery.asHttpParameterArray());
 }

 @Test
 public void testGeoQueryWithIpConstructor() {
 geoQuery = new GeoQuery("123.123.123.123");
 assertNull(geoQuery.getLocation());
 assertNotNull(geoQuery.getIp());
 assertNull(geoQuery.getAccuracy());
 assertNull(geoQuery.getGranularity());
 assertNotNull(geoQuery.asHttpParameterArray());
 }

 @Test
 public void testGettersForLocation() {
 geoQuery = new GeoQuery(location);
 GeoLocation expectedLocation = new GeoLocation(0.0, 0.0);
 assertEquals(expectedLocation, geoQuery.getLocation());
 }

 @Test
 public void testGettersForIp() {
 geoQuery = new GeoQuery("123.123.123.123");
 String expectedIp = "123.123.123.123";
 assertEquals(expectedIp, geoQuery.getIp());
 }

 @Test
 public void testGettersForAccuracy() {
 geoQuery = new GeoQuery();
 String expectedAccuracy = "city";
 geoQuery.accuracy("city");
 assertEquals(expectedAccuracy, geoQuery.getAccuracy());
 }

 @Test
 public void testGettersForGranularity() {
 geoQuery = new GeoQuery();
 String expectedGranularity = "neighborhood";
 geoQuery.granularity("neighborhood");
 assertEquals(expectedGranularity, geoQuery.getGranularity());
 }

 @Test
 public void testGettersForMaxResults() {
 geoQuery = new GeoQuery();
 int expectedMaxResults = 5;
 geoQuery.maxResults(5);
 assertEquals(expectedMaxResults, geoQuery.getMaxResults());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersForNegativeMaxResults() {
 geoQuery = new GeoQuery();
 geoQuery.setMaxResults(-1);
 }

 @Test
 public void testEqualsMethod() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 GeoQuery geoQuery3 = new GeoQuery("123.123.123.123");
 GeoQuery geoQuery4 = new GeoQuery("123.123.123.123");

 assertTrue(geoQuery1.equals(geoQuery1));
 assertTrue(geoQuery1.equals(geoQuery2));
 assertFalse(geoQuery1.equals(geoQuery3));
 assertFalse(geoQuery1.equals(geoQuery4));
 }

 @Test
 public void testHashCodeMethod() {
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 GeoQuery geoQuery3 = new GeoQuery("123.123.123.123");

 assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
 assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
 }

 @Test
 public void testToStringMethod() {
 GeoQuery geoQuery = new GeoQuery(location);
 String expectedToString = "GeoQuery{location=GeoLocation[latitude=0.0, longitude=0.0], ip='null', accuracy='null', granularity='null', maxResults=0}";
 assertEquals(expectedToString, geoQuery.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsMethodWithNullObject() {
 GeoQuery geoQuery = new GeoQuery(location);
 assertFalse(geoQuery.equals(null));
 }

 @Test
 public void testEqualsMethodWithNonGeoQueryObject() {
 GeoQuery geoQuery = new GeoQuery(location);
 String nonGeoQueryObject = "Test Object";
 assertFalse(geoQuery.equals(nonGeoQueryObject));
 }

 @Test
 public void testAppendParameterMethodWithNullName() {
 List<HttpParameter> params = new ArrayList<>();
 assertThrows(NullPointerException.class, () -> geoQuery.appendParameter(null, "Test Value", params));
 }

 @Test
 public void testAppendParameterMethodWithNullValue() {
 List<HttpParameter> params = new ArrayList<>();
 geoQuery.appendParameter("test", null, params);
 assertTrue(params.isEmpty());
 }

 @Test
 public void testAppendParameterMethodWithValidArguments() {
 List<HttpParameter> params = new ArrayList<>();
 geoQuery.appendParameter("test", "Test Value", params);
 HttpParameter[] expectedParams = new HttpParameter[] { new HttpParameter("test", "Test Value") };
 assertArrayEquals(expectedParams, params.toArray(new HttpParameter[params.size()]));
 }

 @Test
 public void testAsHttpParameterArrayMethodWithEmptyParamsList() {
 HttpParameter[] expectedParams = new HttpParameter[0];
 assertArrayEquals(expectedParams, geoQuery.asHttpParameterArray());
 }

 @Test
 public void testAsHttpParameterArrayMethodWithParamsList() {
 HttpParameter testParam1 = new HttpParameter("test1", "Test Value 1");
 HttpParameter testParam2 = new HttpParameter("test2", "Test Value 2");
 List<HttpParameter> params = new ArrayList<>();
 params.add(testParam1);
 params.add(testParam2);

 HttpParameter[] expectedParams = new HttpParameter[] { testParam1, testParam2 };
 HttpParameter[] actualParams = geoQuery.asHttpParameterArray();
 assertArrayEquals(expectedParams, actualParams);
 }
}