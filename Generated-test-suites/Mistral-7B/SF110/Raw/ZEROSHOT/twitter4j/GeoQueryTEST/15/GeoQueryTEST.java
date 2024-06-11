package com.example.twitter4j.test;

import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import static org.junit.Assert.*;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(1.0d, 2.0d);
 private GeoQuery geoQueryWithLocation = new GeoQuery(location);
 private GeoQuery geoQueryWithIp = new GeoQuery("192.168.1.1");

 @Test
 public void testConstructorWithLocation() {
     assertNull(geoQueryWithLocation.getIp());
     assertEquals(location, geoQueryWithLocation.getLocation());
     assertNull(geoQueryWithLocation.getAccuracy());
     assertNull(geoQueryWithLocation.getGranularity());
     assertEquals(0, geoQueryWithLocation.getMaxResults());
 }

 @Test
 public void testConstructorWithIp() {
     assertNull(geoQueryWithIp.getLocation());
     assertEquals("192.168.1.1", geoQueryWithIp.getIp());
     assertNull(geoQueryWithIp.getAccuracy());
     assertNull(geoQueryWithIp.getGranularity());
     assertEquals(0, geoQueryWithIp.getMaxResults());
 }

 @Test
 public void testGettersAndSetters() {
     GeoQuery geoQuery = new GeoQuery();

     geoQuery.setLocation(location);
     assertEquals(location, geoQuery.getLocation());

     geoQuery.setIp("192.168.2.2");
     assertEquals("192.168.2.2", geoQuery.getIp());

     geoQuery.setAccuracy("10m");
     assertEquals("10m", geoQuery.getAccuracy());

     geoQuery.setGranularity("neighborhood");
     assertEquals("neighborhood", geoQuery.getGranularity());

     geoQuery.setMaxResults(5);
     assertEquals(5, geoQuery.getMaxResults());
 }

 @Test
 public void testSettersReturnInstance() {
     GeoQuery geoQuery = new GeoQuery();

     geoQuery = geoQuery.accuracy("10m");
     assertEquals("10m", geoQuery.getAccuracy());

     geoQuery = geoQuery.granularity("city");
     assertEquals("city", geoQuery.getGranularity());

     geoQuery = geoQuery.maxResults(10);
     assertEquals(10, geoQuery.getMaxResults());
 }

 @Test
 public void testAsHttpParameterArray() {
     HttpParameter[] httpParametersWithLocation = geoQueryWithLocation.asHttpParameterArray();
     HttpParameter[] httpParametersWithIp = geoQueryWithIp.asHttpParameterArray();

     assertEquals(5, httpParametersWithLocation.length);
     assertEquals("lat", httpParametersWithLocation[0].getName());
     assertEquals(String.valueOf(location.getLatitude()), httpParametersWithLocation[0].getValue());

     assertEquals("long", httpParametersWithLocation[1].getName());
     assertEquals(String.valueOf(location.getLongitude()), httpParametersWithLocation[1].getValue());

     assertEquals("accuracy", httpParametersWithLocation[2].getName());
     assertNull(httpParametersWithLocation[2].getValue());

     assertEquals("granularity", httpParametersWithLocation[3].getName());
     assertNull(httpParametersWithLocation[3].getValue());

     assertEquals("max_results", httpParametersWithLocation[4].getName());
     assertEquals(String.valueOf(geoQueryWithLocation.getMaxResults()), httpParametersWithLocation[4].getValue());

     assertEquals(5, httpParametersWithIp.length);
     assertEquals("ip", httpParametersWithIp[0].getName());
     assertEquals("192.168.1.1", httpParametersWithIp[0].getValue());

     assertEquals("accuracy", httpParametersWithIp[1].getName());
     assertNull(httpParametersWithIp[1].getValue());

     assertEquals("granularity", httpParametersWithIp[2].getName());
     assertNull(httpParametersWithIp[2].getValue());

     assertEquals("max_results", httpParametersWithIp[3].getName());
     assertEquals(String.valueOf(geoQueryWithIp.getMaxResults()), httpParametersWithIp[3].getValue());
 }

 @Test
 public void testEquals() {
     GeoQuery geoQuery1 = new GeoQuery();
     GeoQuery geoQuery2 = new GeoQuery();
     GeoQuery geoQuery3 = new GeoQuery();
     GeoLocation location1 = new GeoLocation(1.0d, 2.0d);
     GeoLocation location2 = new GeoLocation(1.0d, 2.0d);
     GeoLocation location3 = new GeoLocation(1.0d, 3.0d);

     assertEquals(geoQuery1, geoQuery1);
     assertNotEquals(geoQuery1, geoQuery2);

     geoQuery1.setLocation(location1);
     geoQuery2.setLocation(location1);
     assertEquals(geoQuery1, geoQuery2);

     geoQuery2.setAccuracy("10m");
     assertNotEquals(geoQuery1, geoQuery2);

     geoQuery3.setLocation(location2);
     geoQuery3.setIp("192.168.1.1");
     assertNotEquals(geoQuery1, geoQuery3);

     geoQuery1.setIp("192.168.1.1");
     assertEquals(geoQuery1, geoQuery3);
 }

 @Test
 public void testHashCode() {
     GeoQuery geoQuery1 = new GeoQuery();
     GeoQuery geoQuery2 = new GeoQuery();
     GeoLocation location1 = new GeoLocation(1.0d, 2.0d);
     GeoLocation location2 = new GeoLocation(1.0d, 2.0d);
     GeoLocation location3 = new GeoLocation(1.0d, 3.0d);

     int hashCode1 = geoQuery1.hashCode();
     int hashCode2 = geoQuery2.hashCode();

     geoQuery1.setLocation(location1);
     geoQuery2.setLocation(location1);
     assertEquals(hashCode1, geoQuery2.hashCode());

     geoQuery2.setAccuracy("10m");
     assertNotEquals(hashCode1, geoQuery2.hashCode());

     GeoQuery geoQuery3 = new GeoQuery();
     geoQuery3.setLocation(location2);
     geoQuery3.setIp("192.168.1.1");
     assertNotEquals(hashCode1, geoQuery3.hashCode());

     geoQuery1.setIp("192.168.1.1");
     assertEquals(hashCode1, geoQuery3.hashCode());

     GeoQuery geoQuery4 = new GeoQuery();
     geoQuery4.setLocation(location3);
     assertNotEquals(hashCode1, geoQuery4.hashCode());
 }

 @Test
 public void testToString() {
     assertEquals("GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='null', accuracy='null', granularity='null', maxResults=0}", geoQueryWithLocation.toString());
     assertEquals("GeoQuery{location=null, ip='192.168.1.1', accuracy='null', granularity='null', maxResults=0}", geoQueryWithIp.toString());
 }
}