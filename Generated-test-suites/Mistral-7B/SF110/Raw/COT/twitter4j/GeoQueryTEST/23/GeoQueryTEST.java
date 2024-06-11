package com.example.gequery; // replace with the actual package name if needed

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import twitter4j.GeoLocation;
import twitter4j.internal.http.HttpParameter;

import org.junit.Test;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private GeoQuery geoQuery;

 @Test
 public void testGeoQueryWithLocation() {
  geoQuery = new GeoQuery(location);
  GeoLocation expectedLocation = location;
  HttpParameter[] expectedParams = geoQuery.asHttpParameterArray();
  assertEquals(1, expectedParams.length);
  assertSame(expectedLocation, geoQuery.getLocation());
  assertNull(geoQuery.getIp());
  assertEquals("", geoQuery.getAccuracy());
  assertEquals("", geoQuery.getGranularity());
  assertEquals(0, geoQuery.getMaxResults());
 }

 @Test
 public void testGeoQueryWithIp() {
  geoQuery = new GeoQuery("123.45.67.89");
  assertNull(geoQuery.getLocation());
  assertEquals("123.45.67.89", geoQuery.getIp());
  assertEquals("", geoQuery.getAccuracy());
  assertEquals("", geoQuery.getGranularity());
  assertEquals(0, geoQuery.getMaxResults());
 }

 @Test
 public void testGettersAndSetters() {
  geoQuery = new GeoQuery(null);
  geoQuery.setLocation(location);
  assertEquals(location, geoQuery.getLocation());
  geoQuery.setIp("123.45.67.89");
  assertEquals("123.45.67.89", geoQuery.getIp());
  geoQuery.setAccuracy("testAccuracy");
  assertEquals("testAccuracy", geoQuery.getAccuracy());
  geoQuery.setGranularity("testGranularity");
  assertEquals("testGranularity", geoQuery.getGranularity());
  geoQuery.setMaxResults(5);
  assertEquals(5, geoQuery.getMaxResults());
 }

 @Test
 public void testAsHttpParameterArray() {
  geoQuery = new GeoQuery(location);
  HttpParameter[] expectedParams = geoQuery.asHttpParameterArray();
  assertArrayEquals(new HttpParameter[] { new HttpParameter("lat", String.valueOf(location.getLatitude())), new HttpParameter("long", String.valueOf(location.getLongitude())) }, expectedParams);
  geoQuery.setIp("123.45.67.89");
  HttpParameter ipParam = new HttpParameter("ip", "123.45.67.89");
  HttpParameter[] newExpectedParams = new HttpParameter[] { ipParam, new HttpParameter("lat", String.valueOf(location.getLatitude())), new HttpParameter("long", String.valueOf(location.getLongitude())) };
  assertArrayEquals(newExpectedParams, geoQuery.asHttpParameterArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullName() {
  List<HttpParameter> params = new ArrayList<>();
  geoQuery = new GeoQuery(null);
  geoQuery.appendParameter(null, "testValue", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullValue() {
  List<HttpParameter> params = new ArrayList<>();
  geoQuery = new GeoQuery(null);
  geoQuery.appendParameter("testName", null, params);
 }

 @Test
 public void testEqualsMethod() {
  GeoQuery geoQuery1 = new GeoQuery(null);
  GeoQuery geoQuery2 = new GeoQuery(null);
  GeoQuery geoQuery3 = new GeoQuery(location);
  GeoQuery geoQuery4 = new GeoQuery(location);
  GeoLocation newLocation = new GeoLocation(1.0, 1.0);
  GeoQuery geoQuery5 = new GeoQuery(newLocation);
  GeoQuery geoQuery6 = new GeoQuery("123.45.67.89");

  assertFalse(geoQuery1.equals(geoQuery2));
  assertTrue(geoQuery1.equals(geoQuery1));
  assertFalse(geoQuery1.equals(geoQuery3));
  assertTrue(geoQuery3.equals(geoQuery4));
  assertFalse(geoQuery3.equals(geoQuery5));
  assertFalse(geoQuery3.equals(geoQuery6));
 }

 @Test
 public void testHashCodeMethod() {
  GeoQuery geoQuery1 = new GeoQuery(null);
  GeoQuery geoQuery2 = new GeoQuery(null);
  GeoQuery geoQuery3 = new GeoQuery(location);
  GeoQuery geoQuery4 = new GeoQuery(location);
  GeoLocation newLocation = new GeoLocation(1.0, 1.0);
  GeoQuery geoQuery5 = new GeoQuery(newLocation);
  GeoQuery geoQuery6 = new GeoQuery("123.45.67.89");

  assertEquals(geoQuery1.hashCode(), geoQuery1.hashCode());
  assertNotEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
  assertEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
  assertEquals(geoQuery3.hashCode(), geoQuery4.hashCode());
  assertNotEquals(geoQuery3.hashCode(), geoQuery5.hashCode());
  assertNotEquals(geoQuery3.hashCode(), geoQuery6.hashCode());
 }

 @Test
 public void testToStringMethod() {
  GeoQuery geoQuery = new GeoQuery(location);
  assertEquals("GeoQuery{location=" + location + ", ip='' , accuracy='' , granularity='' , maxResults=0}", geoQuery.toString());
 }
}