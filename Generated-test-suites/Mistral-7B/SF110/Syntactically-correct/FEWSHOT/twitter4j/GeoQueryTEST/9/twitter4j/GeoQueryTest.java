package twitter4j;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.*;
import org.junit.Test;

public class GeoQueryTest {

 GeoLocation dummyLocation = new GeoLocation(1.0, 2.0);
 String ipAddress = "192.168.1.1";
 String accuracy = "10m";
 String granularity = "city";
 int maxResults = 25;

 @Test
 public void testGeoQueryWithLocation() {
 GeoQuery geoQuery = new GeoQuery(dummyLocation);
 GeoQuery expectedGeoQuery = new GeoQuery(dummyLocation);
 expectedGeoQuery.setAccuracy(accuracy);
 expectedGeoQuery.setGranularity(granularity);
 expectedGeoQuery.setMaxResults(maxResults);

 HttpParameter[] httpParams = geoQuery.asHttpParameterArray();
 HttpParameter[] expectedHttpParams = expectedGeoQuery.asHttpParameterArray();

 assertArrayEquals("Checking if both queries have the same http parameters", expectedHttpParams, httpParams);
 }

 @Test
 public void testGeoQueryWithIp() {
 GeoQuery geoQuery = new GeoQuery(ipAddress);
 assertNull("Checking if GeoLocation is null when IP is set", geoQuery.getLocation());
 String[] httpParams = geoQuery.asHttpParameterArray();
 assertEquals("Checking if IP is the only http parameter", 1, httpParams.length);
 assertEquals("Checking if IP is the correct http parameter", "ip", httpParams[0].getName());
 assertEquals("Checking if IP is the correct value", ipAddress, httpParams[0].getValue());
 }

 @Test
 public void testEquality() {
 GeoQuery geoQuery1 = new GeoQuery(dummyLocation);
 GeoQuery geoQuery2 = new GeoQuery(dummyLocation);
 geoQuery2.setAccuracy("50m");
 GeoQuery geoQuery3 = new GeoQuery(dummyLocation);
 geoQuery3.setMaxResults(20);

 assertEquals("Checking if two identical GeoQueries are equal", geoQuery1, geoQuery2);
 assertNotEquals("Checking if two different GeoQueries are not equal", geoQuery1, geoQuery3);
 }

 @Test
 public void testHashCode() {
 GeoQuery geoQuery1 = new GeoQuery(dummyLocation);
 GeoQuery geoQuery2 = new GeoQuery(dummyLocation);
 geoQuery2.setAccuracy("50m");
 GeoQuery geoQuery3 = new GeoQuery(dummyLocation);
 geoQuery3.setMaxResults(20);

 int hashCode1 = geoQuery1.hashCode();
 int hashCode2 = geoQuery2.hashCode();
 int hashCode3 = geoQuery3.hashCode();

 assertEquals("Checking if two identical GeoQueries have the same hashcode", hashCode1, hashCode2);
 assertNotEquals("Checking if two different GeoQueries have different hashcodes", hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
 GeoQuery geoQuery = new GeoQuery(dummyLocation);
 geoQuery.setAccuracy(accuracy);
 geoQuery.setGranularity(granularity);
 geoQuery.setMaxResults(maxResults);

 assertEquals("Checking if GeoQuery toString method returns correct string representation", "GeoQuery{location=" + dummyLocation + ", ip='" + null + "''" + ", accuracy='" + accuracy + "''" + ", granularity='" + granularity + "''" + ", maxResults=" + maxResults + "}", geoQuery.toString());
 }
}