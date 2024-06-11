package com.yourpackage; // replace with your package name

import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(1.0, 2.0);
 private GeoQuery queryWithLocation = new GeoQuery(location);
 private GeoQuery queryWithIp = new GeoQuery("1.2.3.4");

 @Test
 public void testConstructorWithLocation() {
     assertNull(queryWithLocation.getIp());
     assertEquals(location, queryWithLocation.getLocation());
     assertNull(queryWithLocation.getAccuracy());
     assertNull(queryWithLocation.getGranularity());
     assertEquals(0, queryWithLocation.getMaxResults());
 }

 @Test
 public void testConstructorWithIp() {
     assertNull(queryWithIp.getLocation());
     assertEquals("1.2.3.4", queryWithIp.getIp());
     assertNull(queryWithIp.getAccuracy());
     assertNull(queryWithIp.getGranularity());
     assertEquals(0, queryWithIp.getMaxResults());
 }

 @Test
 public void testGettersAndSetters() {
     GeoLocation newLocation = new GeoLocation(3.0, 4.0);
     String newAccuracy = "exact";
     String newGranularity = "city";
     int newMaxResults = 20;

     queryWithLocation.setLocation(newLocation);
     queryWithLocation.setAccuracy(newAccuracy);
     queryWithLocation.setGranularity(newGranularity);
     queryWithLocation.setMaxResults(newMaxResults);

     assertEquals(newLocation, queryWithLocation.getLocation());
     assertEquals(newAccuracy, queryWithLocation.getAccuracy());
     assertEquals(newGranularity, queryWithLocation.getGranularity());
     assertEquals(newMaxResults, queryWithLocation.getMaxResults());

     queryWithIp.setLocation(newLocation);
     queryWithIp.setAccuracy(newAccuracy);
     queryWithIp.setGranularity(newGranularity);
     queryWithIp.setMaxResults(newMaxResults);

     assertNull(queryWithIp.getLocation());
     assertEquals(newLocation, queryWithIp.getLocation()); // should not change the original location
     assertEquals(newAccuracy, queryWithIp.getAccuracy());
     assertEquals(newGranularity, queryWithIp.getGranularity());
     assertEquals(newMaxResults, queryWithIp.getMaxResults());
 }

 @Test
 public void testEquals() {
     GeoQuery query1 = new GeoQuery(location);
     GeoQuery query2 = new GeoQuery(location);
     GeoQuery query3 = new GeoQuery("1.2.3.4");
     GeoQuery query4 = new GeoQuery("1.2.3.5");
     GeoQuery query5 = query1;

     assertEquals(query1, query1);
     assertEquals(query1, query2);
     assertNotEquals(query1, query3);
     assertNotEquals(query1, query4);
     assertNotEquals(query1, query5); // should be equal since they are the same object
 }

 @Test
 public void testHashCode() {
     GeoQuery query1 = new GeoQuery(location);
     GeoQuery query2 = new GeoQuery(location);
     GeoQuery query3 = new GeoQuery("1.2.3.4");
     GeoQuery query4 = new GeoQuery("1.2.3.5");
     GeoQuery query5 = query1;

     int hashCode1 = query1.hashCode();
     int hashCode2 = query2.hashCode();
     int hashCode3 = query3.hashCode();
     int hashCode4 = query4.hashCode();
     int hashCode5 = query5.hashCode();

     assertEquals(hashCode1, hashCode2);
     assertNotEquals(hashCode1, hashCode3);
     assertNotEquals(hashCode1, hashCode4);
     assertEquals(hashCode1, hashCode5);
 }

 @Test
 public void testToString() {
     assertEquals("GeoQuery{location=" + location + ", ip='null\'" + ", accuracy='null'" + ", granularity='null'" + ", maxResults=0}", queryWithLocation.toString());
     assertEquals("GeoQuery{location=null, ip='1.2.3.4'" + ", accuracy='null'" + ", granularity='null'" + ", maxResults=0}", queryWithIp.toString());
 }

}