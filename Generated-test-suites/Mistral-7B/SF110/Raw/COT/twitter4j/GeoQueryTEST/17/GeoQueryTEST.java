package com.example.gequery; // replace with your package name

import static org.junit.Assert.*;

import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(1.0, 2.0);
 private String ipAddress = "192.168.1.1";
 private String accuracy = "5m";
 private String granularity = GeoQuery.NEIGHBORHOOD;
 private int maxResults = 10;

 @Test
 public void testGeoQueryWithLocation() {
     GeoQuery query = new GeoQuery(location);
     assertNull(query.getIp());
     assertNotNull(query.getLocation());
     assertEquals(location, query.getLocation());
     assertEquals(GeoQuery.NEIGHBORHOOD, query.getGranularity());
     assertEquals(maxResults, query.getMaxResults());
     assertArrayEquals(new HttpParameter[5], query.asHttpParameterArray());
 }

 @Test
 public void testGeoQueryWithIpAddress() {
     GeoQuery query = new GeoQuery(ipAddress);
     assertNull(query.getLocation());
     assertNotNull(query.getIp());
     assertEquals(ipAddress, query.getIp());
     assertEquals(GeoQuery.NEIGHBORHOOD, query.getGranularity());
     assertEquals(maxResults, query.getMaxResults());
     assertArrayEquals(new HttpParameter[3], query.asHttpParameterArray());
 }

 @Test(expected = NullPointerException.class)
 public void testNullLocation() {
     try {
         new GeoQuery(null);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // expected
     }
 }

 @Test(expected = NullPointerException.class)
 public void testNullIpAddress() {
     try {
         new GeoQuery(null);
         fail("Expected NullPointerException");
     } catch (NullPointerException e) {
         // expected
     }
 }

 @Test
 public void testGettersAndSetters() {
     GeoQuery query = new GeoQuery(location);
     assertEquals(location, query.getLocation());
     query.setLocation(new GeoLocation(3.0, 4.0));
     assertEquals(new GeoLocation(3.0, 4.0), query.getLocation());
     query.setIp(ipAddress);
     assertEquals(ipAddress, query.getIp());
     query.setAccuracy(accuracy);
     assertEquals(accuracy, query.getAccuracy());
     query.setGranularity(granularity);
     assertEquals(granularity, query.getGranularity());
     query.setMaxResults(maxResults * 2);
     assertEquals(maxResults * 2, query.getMaxResults());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidAccuracy() {
     try {
         new GeoQuery("").accuracy("");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidGranularity() {
     try {
         new GeoQuery("").granularity("InvalidGranularity");
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMaxResults() {
     try {
         new GeoQuery("").maxResults(-1);
         fail("Expected IllegalArgumentException");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testEquals() {
     GeoQuery query1 = new GeoQuery(location);
     GeoQuery query2 = new GeoQuery(location);
     GeoQuery query3 = new GeoQuery(new GeoLocation(1.1, 2.1));
     assertTrue(query1.equals(query1));
     assertTrue(query1.equals(query2));
     assertFalse(query1.equals(query3));
     assertFalse(query1.equals(null));
 }

 @Test
 public void testHashCode() {
     GeoQuery query1 = new GeoQuery(location);
     GeoQuery query2 = new GeoQuery(location);
     GeoQuery query3 = new GeoQuery(new GeoLocation(1.1, 2.1));
     int hashCode1 = query1.hashCode();
     int hashCode2 = query2.hashCode();
     int hashCode3 = query3.hashCode();
     assertTrue(hashCode1 == hashCode2);
     assertFalse(hashCode1 == hashCode3);
 }

 @Test
 public void testToString() {
     GeoQuery query = new GeoQuery(location);
     String expected = String.format("GeoQuery{location=GeoLocation{lat=1.0, lng=2.0}, ip='%s', accuracy='%s', granularity='%s', maxResults=10}", "", accuracy, granularity);
     assertEquals(expected, query.toString());
 }
}