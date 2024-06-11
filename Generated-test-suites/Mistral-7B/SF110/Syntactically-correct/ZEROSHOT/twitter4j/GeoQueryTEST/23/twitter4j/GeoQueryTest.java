package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location;
 private String ip;
 private String accuracy;
 private String granularity;
 private int maxResults;

 @Test
 public void testGeoQueryWithLocation() {
     location = new GeoLocation(1.0, 2.0);
     GeoQuery geoQuery = new GeoQuery(location);
     HttpParameter[] params = geoQuery.asHttpParameterArray();
     List<String> expectedParams = Arrays.asList(new String[]{"lat", "1.0", "long", "2.0"});
     List<String> actualParams = new ArrayList<>();
     for (HttpParameter param : params) {
         actualParams.add(param.getName());
     }
     assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testGeoQueryWithIp() {
     ip = "192.168.1.1";
     GeoQuery geoQuery = new GeoQuery(ip);
     HttpParameter[] params = geoQuery.asHttpParameterArray();
     List<String> expectedParams = Arrays.asList(new String[]{"ip", "192.168.1.1"});
     List<String> actualParams = new ArrayList<>();
     for (HttpParameter param : params) {
         actualParams.add(param.getName());
     }
     assertEquals(expectedParams, actualParams);
 }

 @Test
 public void testSettingAndGettingAccuracy() {
     accuracy = "10km";
     GeoQuery geoQuery = new GeoQuery();
     geoQuery.setAccuracy(accuracy);
     assertEquals(accuracy, geoQuery.getAccuracy());
 }

 @Test
 public void testSettingAndGettingGranularity() {
     granularity = GeoQuery.NEIGHBORHOOD;
     GeoQuery geoQuery = new GeoQuery();
     geoQuery.setGranularity(granularity);
     assertEquals(granularity, geoQuery.getGranularity());
 }

 @Test
 public void testSettingAndGettingMaxResults() {
     maxResults = 10;
     GeoQuery geoQuery = new GeoQuery();
     geoQuery.setMaxResults(maxResults);
     assertEquals(maxResults, geoQuery.getMaxResults());
 }

 @Test
 public void testEqualObjects() {
     location = new GeoLocation(1.0, 2.0);
     ip = "192.168.1.1";
     accuracy = "10km";
     granularity = GeoQuery.NEIGHBORHOOD;
     maxResults = 10;

     GeoQuery geoQuery1 = new GeoQuery(location);
     GeoQuery geoQuery2 = new GeoQuery(location);
     GeoQuery geoQuery3 = new GeoQuery(ip);
     GeoQuery geoQuery4 = new GeoQuery();
     GeoQuery geoQuery5 = new GeoQuery();
     geoQuery5.setAccuracy(accuracy);
     geoQuery5.setGranularity(granularity);
     geoQuery5.setMaxResults(maxResults);

     assertTrue(geoQuery1.equals(geoQuery2));
     assertFalse(geoQuery1.equals(geoQuery3));
     assertFalse(geoQuery1.equals(geoQuery4));
     assertFalse(geoQuery1.equals(geoQuery5));
 }

 @Test
 public void testHashCode() {
     location = new GeoLocation(1.0, 2.0);
     ip = "192.168.1.1";
     accuracy = "10km";
     granularity = GeoQuery.NEIGHBORHOOD;
     maxResults = 10;

     GeoQuery geoQuery1 = new GeoQuery(location);
     GeoQuery geoQuery2 = new GeoQuery(location);
     GeoQuery geoQuery3 = new GeoQuery(ip);
     GeoQuery geoQuery4 = new GeoQuery();
     GeoQuery geoQuery5 = new GeoQuery();
     geoQuery5.setAccuracy(accuracy);
     geoQuery5.setGranularity(granularity);
     geoQuery5.setMaxResults(maxResults);

     int hashCode1 = geoQuery1.hashCode();
     int hashCode2 = geoQuery2.hashCode();
     int hashCode3 = geoQuery3.hashCode();
     int hashCode4 = geoQuery4.hashCode();
     int hashCode5 = geoQuery5.hashCode();

     assertEquals(hashCode1, hashCode2);
     assertNotEquals(hashCode1, hashCode3);
     assertNotEquals(hashCode1, hashCode4);
     assertNotEquals(hashCode1, hashCode5);
 }

 @Test
 public void testToString() {
     location = new GeoLocation(1.0, 2.0);
     ip = "192.168.1.1";
     accuracy = "10km";
     granularity = GeoQuery.NEIGHBORHOOD;
     maxResults = 10;

     GeoQuery geoQuery = new GeoQuery(location);
     String expectedString = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='', accuracy='10km', granularity='neighborhood', maxResults=10}";
     String actualString = geoQuery.toString();
     assertEquals(expectedString, actualString);
 }
}