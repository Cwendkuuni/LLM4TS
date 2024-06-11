package twitter4j.test;

import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private static final String VALID_LATITUDE = "51.5074";
 private static final String VALID_LONGITUDE = "-0.1278";
 private static final String VALID_IP = "192.168.1.1";
 private static final String VALID_ACCURACY = "city";
 private static final String VALID_GRANULARITY = "neighborhood";
 private static final int VALID_MAX_RESULTS = 10;

 @Test
 public void testConstructors() {
 GeoLocation location = new GeoLocation(VALID_LATITUDE, VALID_LONGITUDE);
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(VALID_IP);

 assertNull(geoQuery1.getIp());
 assertNotNull(geoQuery1.getLocation());
 assertNull(geoQuery2.getLocation());
 assertNotNull(geoQuery2.getIp());
 }

 @Test
 public void testGettersAndSetters() {
 GeoLocation location = new GeoLocation(VALID_LATITUDE, VALID_LONGITUDE);
 GeoQuery geoQuery = new GeoQuery(location);

 assertEquals(location, geoQuery.getLocation());
 assertEquals(VALID_ACCURACY, geoQuery.getAccuracy());
 assertEquals(VALID_GRANULARITY, geoQuery.getGranularity());
 assertEquals(VALID_MAX_RESULTS, geoQuery.getMaxResults());

 geoQuery.setAccuracy(VALID_ACCURACY);
 geoQuery.setGranularity(VALID_GRANULARITY);
 geoQuery.setMaxResults(VALID_MAX_RESULTS);

 assertEquals(VALID_ACCURACY, geoQuery.getAccuracy());
 assertEquals(VALID_GRANULARITY, geoQuery.getGranularity());
 assertEquals(VALID_MAX_RESULTS, geoQuery.getMaxResults());
 }

 @Test
 public void testAsHttpParameterArray() {
 GeoLocation location = new GeoLocation(VALID_LATITUDE, VALID_LONGITUDE);
 GeoQuery geoQuery = new GeoQuery(location);
 HttpParameter[] params = geoQuery.asHttpParameterArray();

 assertEquals(5, params.length);
 assertEquals("lat", params[0].getName());
 assertEquals(VALID_LATITUDE, params[0].getValue());
 assertEquals("long", params[1].getName());
 assertEquals(VALID_LONGITUDE, params[1].getValue());
 assertNull(params[2].getName());
 assertNull(params[2].getValue()); // accuracy is not included as a parameter if set
 assertEquals("max_results", params[3].getName());
 assertEquals(String.valueOf(VALID_MAX_RESULTS), params[3].getValue());
 assertNull(params[4].getName());
 assertNull(params[4].getValue()); // location is not included as a parameter
 }

 @Test(expected = NullPointerException.class)
 public void testAppendParameterNullName() {
 List<HttpParameter> params = new ArrayList<>();
 GeoQuery geoQuery = new GeoQuery();
 geoQuery.appendParameter(null, VALID_MAX_RESULTS, params);
 }

 @Test
 public void testEquals() {
 GeoLocation location1 = new GeoLocation(VALID_LATITUDE, VALID_LONGITUDE);
 GeoLocation location2 = new GeoLocation(VALID_LATITUDE, VALID_LONGITUDE);
 GeoQuery geoQuery1 = new GeoQuery(location1);
 GeoQuery geoQuery2 = new GeoQuery(location2);
 GeoQuery geoQuery3 = new GeoQuery();

 assertTrue(geoQuery1.equals(geoQuery1));
 assertTrue(geoQuery1.equals(geoQuery2));
 assertFalse(geoQuery1.equals(geoQuery3));
 }

 @Test
 public void testHashCode() {
 GeoLocation location = new GeoLocation(VALID_LATITUDE, VALID_LONGITUDE);
 GeoQuery geoQuery1 = new GeoQuery(location);
 GeoQuery geoQuery2 = new GeoQuery(location);
 GeoQuery geoQuery3 = new GeoQuery();

 int hashCode1 = geoQuery1.hashCode();
 int hashCode2 = geoQuery2.hashCode();
 int hashCode3 = geoQuery3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 GeoLocation location = new GeoLocation(VALID_LATITUDE, VALID_LONGITUDE);
 GeoQuery geoQuery = new GeoQuery(location);

 assertEquals("GeoQuery{location=" + location + ", ip=''"+ ", accuracy='" + VALID_ACCURACY + '\'' + ", granularity='" + VALID_GRANULARITY + '\'' + ", maxResults=" + VALID_MAX_RESULTS + '}', geoQuery.toString());
 }
}