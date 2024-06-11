package com.example.gequery; //Replace with your package name

import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private String ip = "192.168.0.1";
 private GeoQuery geoQueryWithLocation;
 private GeoQuery geoQueryWithIp;

 @Test
 public void testConstructorWithLocation() {
  geoQueryWithLocation = new GeoQuery(location);
  assertNull(geoQueryWithLocation.getIp());
  assertNotNull(geoQueryWithLocation.getLocation());
  assertEquals(0, geoQueryWithLocation.getMaxResults());
  assertNull(geoQueryWithLocation.getAccuracy());
  assertNull(geoQueryWithLocation.getGranularity());
 }

 @Test
 public void testConstructorWithIp() {
  geoQueryWithIp = new GeoQuery(ip);
  assertNull(geoQueryWithIp.getLocation());
  assertNotNull(geoQueryWithIp.getIp());
  assertEquals(0, geoQueryWithIp.getMaxResults());
  assertNull(geoQueryWithIp.getAccuracy());
  assertNull(geoQueryWithIp.getGranularity());
 }

 @Test
 public void testGetters() {
  geoQueryWithLocation.setLocation(location);
  geoQueryWithLocation.setAccuracy("accurate");
  geoQueryWithLocation.setGranularity("city");
  geoQueryWithLocation.setMaxResults(10);

  assertEquals(location, geoQueryWithLocation.getLocation());
  assertEquals("accurate", geoQueryWithLocation.getAccuracy());
  assertEquals("city", geoQueryWithLocation.getGranularity());
  assertEquals(10, geoQueryWithLocation.getMaxResults());

  geoQueryWithIp.setIp(ip);
  geoQueryWithIp.setIp("127.0.0.1");
  geoQueryWithIp.setAccuracy("exact");
  geoQueryWithIp.setGranularity("neighborhood");
  geoQueryWithIp.setMaxResults(5);

  assertEquals(ip, geoQueryWithIp.getIp());
  assertEquals("127.0.0.1", geoQueryWithIp.getIp());
  assertEquals("exact", geoQueryWithIp.getAccuracy());
  assertEquals("neighborhood", geoQueryWithIp.getGranularity());
  assertEquals(5, geoQueryWithIp.getMaxResults());
 }

 @Test
 public void testSettersReturnSelf() {
  GeoQuery testQuery = new GeoQuery(location);

  GeoQuery testQueryWithLocation = testQuery.location(location);
  GeoQuery testQueryWithIp = testQuery.ip("192.168.0.1");
  GeoQuery testQueryWithAccuracy = testQuery.accuracy("accurate");
  GeoQuery testQueryWithGranularity = testQuery.granularity("city");
  GeoQuery testQueryWithMaxResults = testQuery.maxResults(10);

  assertEquals(testQuery, testQueryWithLocation);
  assertEquals(testQuery, testQueryWithIp);
  assertEquals(testQuery, testQueryWithAccuracy);
  assertEquals(testQuery, testQueryWithGranularity);
  assertEquals(testQuery, testQueryWithMaxResults);
 }

 @Test
 public void testAsHttpParameterArrayWithLocation() {
  geoQueryWithLocation = new GeoQuery(location);
  geoQueryWithLocation.setAccuracy("accurate");
  geoQueryWithLocation.setGranularity("city");
  geoQueryWithLocation.setMaxResults(10);

  List<HttpParameter> httpParameters = Arrays.asList(geoQueryWithLocation.asHttpParameterArray());

  List<String> keys = Arrays.asList("lat", "long", "accuracy", "granularity", "max_results");
  List<String> values = Arrays.asList(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), "accurate", "city", "10");

  for (int i = 0; i < httpParameters.size(); i++) {
      HttpParameter httpParam = httpParameters.get(i);
      String key = httpParam.getName();
      String value = httpParam.getValue();

      assertEquals(keys.get(i), key);
      assertEquals(values.get(i), value);
  }
 }

 @Test(expected = NullPointerException.class)
 public void testAsHttpParameterArrayWithNullLocation() {
  geoQueryWithLocation = new GeoQuery(null);

  try {
      geoQueryWithLocation.asHttpParameterArray();
  } catch (NullPointerException e) {
      // expected
  }
 }

 @Test(expected = NullPointerException.class)
 public void testAsHttpParameterArrayWithNullAccuracy() {
  geoQueryWithLocation = new GeoQuery(location);
  geoQueryWithLocation.setAccuracy(null);

  try {
      geoQueryWithLocation.asHttpParameterArray();
  } catch (NullPointerException e) {
      // expected
  }
 }

 @Test(expected = NumberFormatException.class)
 public void testSetMaxResultsNegativeNumber() {
  geoQueryWithLocation = new GeoQuery(location);

  try {
      geoQueryWithLocation.setMaxResults(-5);
  } catch (NumberFormatException e) {
      // expected
  }
 }

 @Test(expected = NumberFormatException.class)
 public void testSetMaxResultsZero() {
  geoQueryWithLocation = new GeoQuery(location);

  try {
      geoQueryWithLocation.setMaxResults(0);
  } catch (NumberFormatException e) {
      // expected
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGranularityNull() {
  geoQueryWithLocation = new GeoQuery(location);

  try {
      geoQueryWithLocation.setGranularity(null);
  } catch (IllegalArgumentException e) {
      // expected
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetGranularityInvalidValue() {
  geoQueryWithLocation = new GeoQuery(location);

  try {
      geoQueryWithLocation.setGranularity("invalid");
  } catch (IllegalArgumentException e) {
      // expected
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAccuracyNull() {
  geoQueryWithLocation = new GeoQuery(location);

  try {
      geoQueryWithLocation.setAccuracy(null);
  } catch (IllegalArgumentException e) {
      // expected
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAccuracyInvalidValue() {
  geoQueryWithLocation = new GeoQuery(location);

  try {
      geoQueryWithLocation.setAccuracy("invalid");
  } catch (IllegalArgumentException e) {
      // expected
  }
 }
}