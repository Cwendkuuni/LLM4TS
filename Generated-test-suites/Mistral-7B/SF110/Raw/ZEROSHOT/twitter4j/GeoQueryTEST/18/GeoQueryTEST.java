package com.example.geotest;

import static org.junit.Assert.*;

import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(1.0, 2.0);
 private String ipAddress = "192.168.0.1";
 private GeoQuery queryWithLocation = new GeoQuery(location);
 private GeoQuery queryWithIp = new GeoQuery(ipAddress);

 @Test
 public void testGeoQueryWithLocation() {
 GeoQuery query = queryWithLocation;
 GeoLocation expectedLocation = query.getLocation();
 String expectedAccuracy = null;
 String expectedGranularity = null;
 int expectedMaxResults = 0;

 assertEquals(location, expectedLocation);
 assertNull(query.getIp());
 assertNull(query.getAccuracy());
 assertNull(query.getGranularity());
 assertEquals(0, query.getMaxResults());

 HttpParameter[] httpParams = query.asHttpParameterArray();
 assertEquals(5, httpParams.length);
 assertEquals("lat", httpParams[0].getName());
 assertEquals("1.0", httpParams[0].getValue());
 assertEquals("long", httpParams[1].getName());
 assertEquals("2.0", httpParams[1].getValue());
 }

 @Test
 public void testGeoQueryWithIp() {
 GeoQuery query = queryWithIp;
 String expectedIp = query.getIp();
 GeoLocation expectedLocation = null;
 String expectedAccuracy = null;
 String expectedGranularity = null;
 int expectedMaxResults = 0;

 assertEquals(expectedIp, query.getIp());
 assertNull(query.getLocation());
 assertNull(query.getAccuracy());
 assertNull(query.getGranularity());
 assertEquals(0, query.getMaxResults());

 HttpParameter[] httpParams = query.asHttpParameterArray();
 assertEquals(1, httpParams.length);
 assertEquals("ip", httpParams[0].getName());
 assertEquals(ipAddress, httpParams[0].getValue());
 }

 @Test
 public void testSettersAndGetters() {
 GeoQuery query = queryWithLocation;
 String accuracy = "20m";
 String granularity = "city";
 int maxResults = 5;

 query.setAccuracy(accuracy);
 assertEquals(accuracy, query.getAccuracy());

 query.setGranularity(granularity);
 assertEquals(granularity, query.getGranularity());

 query.setMaxResults(maxResults);
 assertEquals(maxResults, query.getMaxResults());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettersNullValues() {
 GeoQuery query = queryWithLocation;

 query.setLocation(null);
 query.setIp(null);
 query.setAccuracy(null);
 query.setGranularity(null);
 query.setMaxResults(-1);
 }

 @Test
 public void testEquals() {
 GeoQuery query1 = queryWithLocation;
 GeoQuery query2 = queryWithLocation;
 GeoQuery query3 = queryWithIp;
 GeoQuery query4 = new GeoQuery(location);
 GeoQuery query5 = new GeoQuery(location);
 GeoQuery query6 = new GeoQuery(new GeoLocation(-1.0, -1.0));

 assertEquals(query1, query1);
 assertEquals(query1, query2);
 assertNotEquals(query1, query3);
 assertNotEquals(query1, query4);
 assertNotEquals(query1, query5);
 assertNotEquals(query1, query6);
 }

 @Test
 public void testHashCode() {
 GeoQuery query1 = queryWithLocation;
 GeoQuery query2 = queryWithLocation;
 GeoQuery query3 = queryWithIp;
 GeoQuery query4 = new GeoQuery(location);
 GeoQuery query5 = new GeoQuery(location);
 GeoQuery query6 = new GeoQuery(new GeoLocation(-1.0, -1.0));

 int hashCode1 = query1.hashCode();
 int hashCode2 = query2.hashCode();
 int hashCode3 = query3.hashCode();
 int hashCode4 = query4.hashCode();
 int hashCode5 = query5.hashCode();
 int hashCode6 = query6.hashCode();

 assertSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 assertNotSame(hashCode1, hashCode4);
 assertNotSame(hashCode1, hashCode5);
 assertNotSame(hashCode1, hashCode6);
 }

 @Test
 public void testToString() {
 GeoQuery query = queryWithLocation;
 String expectedString = "GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='null', accuracy='null', granularity='null', maxResults=0}";

 assertEquals(expectedString, query.toString());
 }
}