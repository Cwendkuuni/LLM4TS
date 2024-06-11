import org.junit.Before;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GeoQueryTest {

 private GeoLocation location;
 private GeoQuery queryWithLocation;
 private GeoQuery queryWithIP;
 private List<HttpParameter> parameters;

 @Before
 public void setUp() {
 location = new GeoLocation(1.0, 2.0);
 queryWithLocation = new GeoQuery(location);
 queryWithIP = new GeoQuery("127.0.0.1");
 parameters = new ArrayList<>();
 }

 @Test
 public void testConstructorWithLocation() {
 assertEquals(location, queryWithLocation.getLocation());
 assertNull(queryWithLocation.getIp());
 }

 @Test
 public void testConstructorWithIP() {
 assertNull(queryWithIP.getLocation());
 assertEquals("127.0.0.1", queryWithIP.getIp());
 }

 @Test
 public void testGettersAndSetters() {
 // Set location and get it back
 GeoLocation newLocation = new GeoLocation(3.0, 4.0);
 queryWithLocation.setLocation(newLocation);
 assertEquals(newLocation, queryWithLocation.getLocation());

 // Set accuracy and get it back
 String accuracy = "10m";
 queryWithLocation.setAccuracy(accuracy);
 assertEquals(accuracy, queryWithLocation.getAccuracy());

 // Set granularity and get it back
 String granularity = "city";
 queryWithLocation.setGranularity(granularity);
 assertEquals(granularity, queryWithLocation.getGranularity());

 // Set maxResults and get it back
 int maxResults = 10;
 queryWithLocation.setMaxResults(maxResults);
 assertEquals(maxResults, queryWithLocation.getMaxResults());
 }

 @Test
 public void testAppendParameter() {
 // Append parameter for location
 queryWithLocation.appendParameter("lat", location.getLatitude(), parameters);
 assertEquals(1, parameters.size());
 assertEquals("lat", parameters.get(0).getName());
 assertEquals(String.valueOf(location.getLatitude()), parameters.get(0).getValue());

 // Append parameter for accuracy
 queryWithLocation.appendParameter("accuracy", "10m", parameters);
 assertEquals(2, parameters.size());
 assertEquals("accuracy", parameters.get(1).getName());
 assertEquals("10m", parameters.get(1).getValue());
 }

 @Test
 public void testAsHttpParameterArray() {
 HttpParameter[] expected = new HttpParameter[]{
 new HttpParameter("lat", String.valueOf(location.getLatitude())),
 new HttpParameter("long", String.valueOf(location.getLongitude())),
 new HttpParameter("accuracy", accuracy),
 new HttpParameter("granularity", granularity),
 new HttpParameter("max_results", String.valueOf(queryWithLocation.getMaxResults()))
 };
 assertArrayEquals(expected, queryWithLocation.asHttpParameterArray());
 }

 @Test
 public void testEquals() {
 GeoQuery queryWithSameValues = new GeoQuery(location);
 queryWithSameValues.setAccuracy(accuracy);
 queryWithSameValues.setGranularity(granularity);
 queryWithSameValues.setMaxResults(queryWithLocation.getMaxResults());

 assertTrue(queryWithLocation.equals(queryWithSameValues));

 GeoQuery queryWithDifferentMaxResults = new GeoQuery(location);
 queryWithDifferentMaxResults.setAccuracy(accuracy);
 queryWithDifferentMaxResults.setGranularity(granularity);
 queryWithDifferentMaxResults.setMaxResults(queryWithLocation.getMaxResults() + 1);

 assertFalse(queryWithLocation.equals(queryWithDifferentMaxResults));

 GeoQuery queryWithDifferentAccuracy = new GeoQuery(location);
 queryWithDifferentAccuracy.setGranularity(granularity);
 queryWithDifferentAccuracy.setMaxResults(queryWithLocation.getMaxResults());
 queryWithDifferentAccuracy.setAccuracy("20m");

 assertFalse(queryWithLocation.equals(queryWithDifferentAccuracy));

 GeoQuery queryWithDifferentGranularity = new GeoQuery(location);
 queryWithDifferentGranularity.setAccuracy(accuracy);
 queryWithDifferentGranularity.setMaxResults(queryWithLocation.getMaxResults());
 queryWithDifferentGranularity.setGranularity("country");

 assertFalse(queryWithLocation.equals(queryWithDifferentGranularity));

 GeoQuery queryWithDifferentIp = new GeoQuery("127.0.0.2");
 queryWithDifferentIp.setAccuracy(accuracy);
 queryWithDifferentIp.setGranularity(granularity);
 queryWithDifferentIp.setMaxResults(queryWithLocation.getMaxResults());

 assertFalse(queryWithLocation.equals(queryWithDifferentIp));

 GeoQuery queryWithDifferentLocation = new GeoQuery(new GeoLocation(-1.0, -2.0));
 queryWithDifferentLocation.setAccuracy(accuracy);
 queryWithDifferentLocation.setGranularity(granularity);
 queryWithDifferentLocation.setMaxResults(queryWithLocation.getMaxResults());

 assertFalse(queryWithLocation.equals(queryWithDifferentLocation));
 }

 @Test
 public void testHashCode() {
 GeoQuery queryWithSameValuesHashCode = new GeoQuery(location);
 queryWithSameValuesHashCode.setAccuracy(accuracy);
 queryWithSameValuesHashCode.setGranularity(granularity);
 queryWithSameValuesHashCode.setMaxResults(queryWithLocation.getMaxResults());

 assertEquals(queryWithLocation.hashCode(), queryWithSameValuesHashCode.hashCode());

 GeoQuery queryWithDifferentMaxResultsHashCode = new GeoQuery(location);
 queryWithDifferentMaxResultsHashCode.setAccuracy(accuracy);
 queryWithDifferentMaxResultsHashCode.setGranularity(granularity);
 queryWithDifferentMaxResultsHashCode.setMaxResults(queryWithLocation.getMaxResults() + 1);

 assertNotEquals(queryWithLocation.hashCode(), queryWithDifferentMaxResultsHashCode.hashCode());

 GeoQuery queryWithDifferentAccuracyHashCode = new GeoQuery(location);
 queryWithDifferentAccuracyHashCode.setGranularity(granularity);
 queryWithDifferentAccuracyHashCode.setMaxResults(queryWithLocation.getMaxResults());
 queryWithDifferentAccuracyHashCode.setAccuracy("20m");

 assertNotEquals(queryWithLocation.hashCode(), queryWithDifferentAccuracyHashCode.hashCode());

 GeoQuery queryWithDifferentGranularityHashCode = new GeoQuery(location);
 queryWithDifferentGranularityHashCode.setAccuracy(accuracy);
 queryWithDifferentGranularityHashCode.setMaxResults(queryWithLocation.getMaxResults());
 queryWithDifferentGranularityHashCode.setGranularity("country");

 assertNotEquals(queryWithLocation.hashCode(), queryWithDifferentGranularityHashCode.hashCode());

 GeoQuery queryWithDifferentIpHashCode = new GeoQuery("127.0.0.2");
 queryWithDifferentIpHashCode.setAccuracy(accuracy);
 queryWithDifferentIpHashCode.setGranularity(granularity);
 queryWithDifferentIpHashCode.setMaxResults(queryWithLocation.getMaxResults());

 assertNotEquals(queryWithLocation.hashCode(), queryWithDifferentIpHashCode.hashCode());

 GeoQuery queryWithDifferentLocationHashCode = new GeoQuery(new GeoLocation(-1.0, -2.0));
 queryWithDifferentLocationHashCode.setAccuracy(accuracy);
 queryWithDifferentLocationHashCode.setGranularity(granularity);
 queryWithDifferentLocationHashCode.setMaxResults(queryWithLocation.getMaxResults());

 assertNotEquals(queryWithLocation.hashCode(), queryWithDifferentLocationHashCode.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("GeoQuery{location=" + location + ", ip='' , accuracy='" + accuracy + "', granularity='" + granularity + "', maxResults=" + queryWithLocation.getMaxResults() + "}", queryWithLocation.toString());
 }
}