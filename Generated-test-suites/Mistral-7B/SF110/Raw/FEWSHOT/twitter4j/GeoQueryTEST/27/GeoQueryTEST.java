package twitter4j; // or the package name of the test class if it's different

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import java.util.*;
import org.junit.Test;

public class GeoQueryTest {

 GeoLocation location = new GeoLocation(1.0, 2.0);
 GeoLocation locationNull = null;
 String ip = "192.168.1.1";
 String accuracy = "accurate";
 String granularity = "city";
 int maxResults = 10;

 @Test
 public void testGeoQueryConstructionWithLocation() {
 GeoQuery geoQueryLocation = new GeoQuery(location);
 assertEquals(location, geoQueryLocation.getLocation());
 assertNull(geoQueryLocation.getIp());
 }

 @Test
 public void testGeoQueryConstructionWithIP() {
 GeoQuery geoQueryIp = new GeoQuery(ip);
 assertNull(geoQueryIp.getLocation());
 assertEquals(ip, geoQueryIp.getIp());
 }

 @Test
 public void testGettersAndSetters() {
 GeoQuery geoQuery = new GeoQuery(location);
 geoQuery.setAccuracy(accuracy);
 geoQuery.setGranularity(granularity);
 geoQuery.setMaxResults(maxResults);

 assertEquals(location, geoQuery.getLocation());
 assertNull(geoQuery.getIp());
 assertEquals(accuracy, geoQuery.getAccuracy());
 assertEquals(granularity, geoQuery.getGranularity());
 assertEquals(maxResults, geoQuery.getMaxResults());
 }

 @Test
 public void testAccuracyAndGranularityMethods() {
 GeoQuery query = new GeoQuery(location);
 GeoQuery queryAccuracy = query.accuracy(accuracy);
 GeoQuery queryGranularity = queryAccuracy.granularity(granularity);
 assertEquals(queryGranularity.getAccuracy(), accuracy);
 assertEquals(queryGranularity.getGranularity(), granularity);
 assertEquals(query, queryGranularity);
 }

 @Test
 public void testMaxResultsMethods() {
 GeoQuery query = new GeoQuery(location);
 GeoQuery queryMaxResults = query.maxResults(maxResults);
 assertEquals(queryMaxResults.getMaxResults(), maxResults);
 assertEquals(query, queryMaxResults);
 }

 @Test
 public void testAsHttpParameterArray() {
 GeoQuery query = new GeoQuery(location);
 HttpParameter[] params = query.asHttpParameterArray();
 List<HttpParameter> expectedParams = new ArrayList<>();
 this.appendParameter(expectedParams, "location", location);
 assertArrayEquals(expectedParams.toArray(new HttpParameter[expectedParams.size()]), params);
 }

 @Test
 public void testEqualsAndHashCode() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(locationNull);
 GeoQuery query3 = new GeoQuery(location);
 GeoQuery query4 = query1;

 assertFalse(query1.equals(query2));
 assertTrue(query1.equals(query1));
 assertTrue(query1.equals(query3));
 assertTrue(query1.equals(query4));

 int hashCode1 = query1.hashCode();
 assertSame(hashCode1, query1.hashCode());
 assertNotSame(hashCode1, query2.hashCode());
 assertNotSame(hashCode1, query3.hashCode());
 assertSame(hashCode1, query4.hashCode());
 }

 @Test
 public void testToString() {
 GeoQuery query = new GeoQuery(location);
 assertEquals("GeoQuery{location=GeoLocation(lat=1.0, lon=2.0), ip='null', accuracy='null', granularity='null', maxResults=10}", query.toString());
 }

 private void appendParameter(List<HttpParameter> params, String name, Object value) {
 if (value != null) {
 params.add(new HttpParameter(name, String.valueOf(value)));
 }
 }
}