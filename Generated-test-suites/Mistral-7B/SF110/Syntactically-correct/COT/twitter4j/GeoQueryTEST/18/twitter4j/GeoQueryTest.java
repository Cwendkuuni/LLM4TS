package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private GeoQuery queryWithLocation = new GeoQuery(location);
 private GeoQuery queryWithIp = new GeoQuery("1.2.3.4");

 @Test
 public void testGetLocation() {
     GeoQuery query = queryWithLocation;
     GeoLocation expectedLocation = location;
     GeoLocation actualLocation = query.getLocation();
     assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testGetIp() {
     GeoQuery query = queryWithIp;
     String expectedIp = "1.2.3.4";
     String actualIp = query.getIp();
     assertEquals(expectedIp, actualIp);
 }

 @Test
 public void testGetAccuracy_Null() {
     GeoQuery query = new GeoQuery();
     assertNull(query.getAccuracy());
 }

 @Test
 public void testGetAccuracy_NotEmpty() {
     GeoQuery query = new GeoQuery();
     query.setAccuracy("accurate");
     String expectedAccuracy = "accurate";
     String actualAccuracy = query.getAccuracy();
     assertEquals(expectedAccuracy, actualAccuracy);
 }

 @Test (expected = NullPointerException.class)
 public void testSetAccuracy_Null() {
     GeoQuery query = new GeoQuery();
     query.setAccuracy(null);
 }

 @Test
 public void testAccuracy_Setter_And_Getters() {
     GeoQuery query = new GeoQuery();
     query.setAccuracy("accurate");
     String expectedAccuracy = "accurate";
     query.accuracy("precise");
     String actualAccuracy = query.getAccuracy();
     assertEquals(expectedAccuracy, actualAccuracy);
 }

 @Test
 public void testGetGranularity_Null() {
     GeoQuery query = new GeoQuery();
     assertNull(query.getGranularity());
 }

 @Test
 public void testGetGranularity_NotEmpty() {
     GeoQuery query = new GeoQuery();
     query.setGranularity("city");
     String expectedGranularity = "city";
     String actualGranularity = query.getGranularity();
     assertEquals(expectedGranularity, actualGranularity);
 }

 @Test (expected = NullPointerException.class)
 public void testSetGranularity_Null() {
     GeoQuery query = new GeoQuery();
     query.setGranularity(null);
 }

 @Test
 public void testGranularity_Setter_And_Getters() {
     GeoQuery query = new GeoQuery();
     query.setGranularity("city");
     String expectedGranularity = "city";
     query.granularity("neighborhood");
     String actualGranularity = query.getGranularity();
     assertEquals(expectedGranularity, actualGranularity);
 }

 @Test
 public void testGetMaxResults_Zero() {
     GeoQuery query = new GeoQuery();
     int expectedMaxResults = 0;
     int actualMaxResults = query.getMaxResults();
     assertEquals(expectedMaxResults, actualMaxResults);
 }

 @Test
 public void testGetMaxResults_PositiveNumber() {
     GeoQuery query = new GeoQuery();
     query.setMaxResults(5);
     int expectedMaxResults = 5;
     int actualMaxResults = query.getMaxResults();
     assertEquals(expectedMaxResults, actualMaxResults);
 }

 @Test
 public void testSetMaxResults_NegativeNumber() {
     GeoQuery query = new GeoQuery();
     query.setMaxResults(-5);
     int expectedMaxResults = 5; // or the default value
     int actualMaxResults = query.getMaxResults();
     assertEquals(expectedMaxResults, actualMaxResults);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSetMaxResults_LessThanZero() {
     GeoQuery query = new GeoQuery();
     query.setMaxResults(-5);
 }

 @Test
 public void testMaxResults_Setter_And_Getters() {
     GeoQuery query = new GeoQuery();
     query.setMaxResults(5);
     int expectedMaxResults = 5;
     query.maxResults(10);
     int actualMaxResults = query.getMaxResults();
     assertEquals(expectedMaxResults, actualMaxResults);
 }

 @Test
 public void testAsHttpParameterArray_NoParameters() {
     GeoQuery query = new GeoQuery();
     HttpParameter[] expectedParams = new HttpParameter[0];
     HttpParameter[] actualParams = query.asHttpParameterArray();
     assertArrayEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsHttpParameterArray_WithLocation() {
     double latitude = 1.0;
     double longitude = 2.0;
     GeoLocation location = new GeoLocation(latitude, longitude);
     GeoQuery query = new GeoQuery(location);
     HttpParameter[] expectedParams = new HttpParameter[] { new HttpParameter("lat", String.valueOf(latitude)), new HttpParameter("long", String.valueOf(longitude)) };
     HttpParameter[] actualParams = query.asHttpParameterArray();
     assertArrayEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsHttpParameterArray_WithIp() {
     GeoQuery query = new GeoQuery("1.2.3.4");
     HttpParameter[] expectedParams = new HttpParameter[] { new HttpParameter("ip", "1.2.3.4") };
     HttpParameter[] actualParams = query.asHttpParameterArray();
     assertArrayEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsHttpParameterArray_WithAllParameters() {
     double latitude = 1.0;
     double longitude = 2.0;
     GeoLocation location = new GeoLocation(latitude, longitude);
     GeoQuery query = new GeoQuery(location).accuracy("accurate").granularity("city").maxResults(10);
     HttpParameter[] expectedParams = new HttpParameter[] { new HttpParameter("lat", String.valueOf(latitude)), new HttpParameter("long", String.valueOf(longitude)), new HttpParameter("accuracy", "accurate"), new HttpParameter("granularity", "city"), new HttpParameter("max_results", "10") };
     HttpParameter[] actualParams = query.asHttpParameterArray();
     assertArrayEquals(expectedParams, actualParams);
 }

}