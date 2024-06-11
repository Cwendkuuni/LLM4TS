package com.example.geiquerytest;

import static org.junit.Assert.*;

import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private GeoQuery queryWithLocation = new GeoQuery(location);
 private GeoQuery queryWithIP = new GeoQuery("1.2.3.4");

 @Test
 public void testGetLocation() {
     GeoLocation expectedLocation = location;
     GeoLocation actualLocation = queryWithLocation.getLocation();
     assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testGetIp() {
     String expectedIp = "1.2.3.4";
     String actualIp = queryWithIP.getIp();
     assertEquals(expectedIp, actualIp);
 }

 @Test
 public void testGetAccuracy_Null() {
     GeoQuery query = new GeoQuery();
     assertNull(query.getAccuracy());
 }

 @Test
 public void testGetAccuracy_NotNull() {
     String accuracy = "10m";
     GeoQuery query = new GeoQuery().accuracy(accuracy);
     assertEquals(accuracy, query.getAccuracy());
 }

 @Test
 public void testSetAndGetGranularity_Null() {
     GeoQuery query = new GeoQuery();
     assertNull(query.getGranularity());
     query.setGranularity(null);
     assertNull(query.getGranularity());
 }

 @Test
 public void testSetAndGetGranularity_String() {
     String granularity = "country";
     GeoQuery query = new GeoQuery().granularity(granularity);
     assertEquals(granularity, query.getGranularity());
 }

 @Test
 public void testGetMaxResults_Zero() {
     GeoQuery query = new GeoQuery();
     assertEquals(0, query.getMaxResults());
 }

 @Test
 public void testGetMaxResults_Positive() {
     int maxResults = 10;
     GeoQuery query = new GeoQuery().maxResults(maxResults);
     assertEquals(maxResults, query.getMaxResults());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetMaxResults_Negative() {
     GeoQuery query = new GeoQuery();
     query.setMaxResults(-1);
 }

 @Test
 public void testAsHttpParameterArray_WithLocation() {
     String expectedNameLat = "lat";
     double expectedValueLat = 0.0;
     String expectedNameLong = "long";
     double expectedValueLong = 0.0;
     HttpParameter[] expectedParams = {new HttpParameter(expectedNameLat, String.valueOf(expectedValueLat)),
             new HttpParameter(expectedNameLong, String.valueOf(expectedValueLong))};
     HttpParameter[] actualParams = queryWithLocation.asHttpParameterArray();
     assertArrayEquals(expectedParams, actualParams);
 }

 @Test
 public void testAsHttpParameterArray_WithIP() {
     String expectedNameIp = "ip";
     String expectedValueIp = "1.2.3.4";
     HttpParameter[] expectedParams = {new HttpParameter(expectedNameIp, expectedValueIp)};
     HttpParameter[] actualParams = queryWithIP.asHttpParameterArray();
     assertArrayEquals(expectedParams, actualParams);
 }

 @Test
 public void testEquals_SameObject() {
     GeoQuery query1 = new GeoQuery();
     GeoQuery query2 = query1;
     assertTrue(query1.equals(query2));
 }

 @Test
 public void testEquals_DifferentObjectWithSameValues() {
     GeoQuery query1 = new GeoQuery();
     query1.setAccuracy("10m");
     query1.setGranularity("country");
     query1.setIp("1.2.3.4");
     query1.setLocation(location);
     query1.setMaxResults(5);
     GeoQuery query2 = new GeoQuery();
     query2.setAccuracy("10m");
     query2.setGranularity("country");
     query2.setIp("1.2.3.4");
     query2.setLocation(location);
     query2.setMaxResults(5);
     assertTrue(query1.equals(query2));
 }

 @Test
 public void testEquals_DifferentObjectWithDifferentValues() {
     GeoQuery query1 = new GeoQuery();
     query1.setAccuracy("10m");
     query1.setGranularity("country");
     query1.setIp("1.2.3.4");
     query1.setLocation(location);
     query1.setMaxResults(5);
     GeoQuery query2 = new GeoQuery();
     query2.setAccuracy("50m");
     query2.setGranularity("city");
     query2.setIp("2.2.2.2");
     query2.setLocation(new GeoLocation(1.0, 1.0));
     query2.setMaxResults(10);
     assertFalse(query1.equals(query2));
 }

 @Test
 public void testHashCode_SameObject() {
     GeoQuery query1 = new GeoQuery();
     GeoQuery query2 = query1;
     int expectedHashCode = query1.hashCode();
     int actualHashCode = query2.hashCode();
     assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testHashCode_DifferentObjectsWithSameValues() {
     GeoQuery query1 = new GeoQuery();
     query1.setAccuracy("10m");
     query1.setGranularity("country");
     query1.setIp("1.2.3.4");
     query1.setLocation(location);
     query1.setMaxResults(5);
     GeoQuery query2 = new GeoQuery();
     query2.setAccuracy("10m");
     query2.setGranularity("country");
     query2.setIp("1.2.3.4");
     query2.setLocation(location);
     query2.setMaxResults(5);
     int expectedHashCode = query1.hashCode();
     int actualHashCode = query2.hashCode();
     assertEquals(expectedHashCode, actualHashCode);
 }

 @Test
 public void testHashCode_DifferentObjectsWithDifferentValues() {
     GeoQuery query1 = new GeoQuery();
     query1.setAccuracy("10m");
     query1.setGranularity("country");
     query1.setIp("1.2.3.4");
     query1.setLocation(location);
     query1.setMaxResults(5);
     GeoQuery query2 = new GeoQuery();
     query2.setAccuracy("50m");
     query2.setGranularity("city");
     query2.setIp("2.2.2.2");
     query2.setLocation(new GeoLocation(1.0, 1.0));
     query2.setMaxResults(10);
     int expectedHashCode1 = query1.hashCode();
     int actualHashCode1 = query1.hashCode();
     assertEquals(expectedHashCode1, actualHashCode1);
     int expectedHashCode2 = query2.hashCode();
     int actualHashCode2 = query2.hashCode();
     assertNotEquals(expectedHashCode1, expectedHashCode2);
 }

 @Test
 public void testToString() {
     GeoQuery query = new GeoQuery();
     String expectedString = "GeoQuery{location=null, ip='', accuracy='', granularity='', maxResults=0}";
     String actualString = query.toString();
     assertEquals(expectedString, actualString);
 }

}