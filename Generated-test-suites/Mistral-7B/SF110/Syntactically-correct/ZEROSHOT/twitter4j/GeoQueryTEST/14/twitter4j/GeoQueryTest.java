package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(1.0, 2.0);
 private GeoQuery queryWithLocation = new GeoQuery(location);
 private GeoQuery queryWithIp = new GeoQuery("123.123.123.123");

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
     assertEquals("123.123.123.123", queryWithIp.getIp());
     assertNull(queryWithIp.getAccuracy());
     assertNull(queryWithIp.getGranularity());
     assertEquals(0, queryWithIp.getMaxResults());
 }

 @Test
 public void testGettersAndSetters() {
     GeoQuery query = new GeoQuery();
     assertNull(query.getLocation());
     assertNull(query.getIp());
     assertNull(query.getAccuracy());
     assertNull(query.getGranularity());
     assertEquals(0, query.getMaxResults());

     query.setLocation(location);
     assertEquals(location, query.getLocation());

     query.setIp("192.168.1.1");
     assertEquals("192.168.1.1", query.getIp());

     query.setAccuracy("10m");
     assertEquals("10m", query.getAccuracy());

     query.setGranularity("city");
     assertEquals("city", query.getGranularity());

     query.setMaxResults(10);
     assertEquals(10, query.getMaxResults());
 }

 @Test
 public void testAccuracyAndGranularityMethods() {
     GeoQuery query = new GeoQuery();

     query = query.accuracy("10m");
     assertEquals("10m", query.getAccuracy());

     query = query.granularity("city");
     assertEquals("city", query.getGranularity());
 }

 @Test
 public void testMaxResultsMethod() {
     GeoQuery query = new GeoQuery();

     query = query.maxResults(5);
     assertEquals(5, query.getMaxResults());
 }

 @Test
 public void testAsHttpParameterArray() {
     List<HttpParameter> params = new ArrayList<>();

     GeoQuery queryWithLocation = new GeoQuery(location);
     queryWithLocation.setAccuracy("10m");
     queryWithLocation.setGranularity("city");
     queryWithLocation.setMaxResults(5);
     queryWithLocation.asHttpParameterArray(params);

     assertEquals(5, params.size());
     assertEquals("lat", params.get(0).getName());
     assertEquals("1.0", params.get(0).getValue());
     assertEquals("long", params.get(1).getName());
     assertEquals("2.0", params.get(1).getValue());
     assertEquals("accuracy", params.get(2).getName());
     assertEquals("10m", params.get(2).getValue());
     assertEquals("granularity", params.get(3).getName());
     assertEquals("city", params.get(3).getValue());
     assertEquals("max_results", params.get(4).getName());
     assertEquals("5", params.get(4).getValue());

     GeoQuery queryWithIp = new GeoQuery("192.168.1.1");
     queryWithIp.setMaxResults(10);
     queryWithIp.asHttpParameterArray(params);

     assertEquals(5, params.size());
     assertEquals("ip", params.get(0).getName());
     assertEquals("192.168.1.1", params.get(0).getValue());
     assertEquals("max_results", params.get(1).getName());
     assertEquals("10", params.get(1).getValue());
 }

 @Test
 public void testEquals() {
     GeoQuery query1 = new GeoQuery(location);
     GeoQuery query2 = new GeoQuery(location);
     GeoQuery query3 = new GeoQuery(new GeoLocation(1.1, 2.1));
     GeoQuery query4 = new GeoQuery("123.123.123.123");

     assertTrue(query1.equals(query1));
     assertTrue(query1.equals(query2));
     assertFalse(query1.equals(query3));
     assertFalse(query1.equals(query4));
 }

 @Test
 public void testHashCode() {
     GeoQuery query1 = new GeoQuery(location);
     GeoQuery query2 = new GeoQuery(location);
     GeoQuery query3 = new GeoQuery(new GeoLocation(1.1, 2.1));
     GeoQuery query4 = new GeoQuery("123.123.123.123");

     int hashCode1 = query1.hashCode();
     int hashCode2 = query2.hashCode();
     int hashCode3 = query3.hashCode();
     int hashCode4 = query4.hashCode();

     assertEquals(hashCode1, hashCode2);
     assertNotEquals(hashCode1, hashCode3);
     assertNotEquals(hashCode1, hashCode4);
 }

 @Test
 public void testToString() {
     GeoQuery query = new GeoQuery();
     assertEquals("GeoQuery{}", query.toString());

     query.setLocation(location);
     query.setIp("192.168.1.1");
     query.setAccuracy("10m");
     query.setGranularity("city");
     query.setMaxResults(5);

     assertEquals("GeoQuery{location=GeoLocation{latitude=1.0, longitude=2.0}, ip='192.168.1.1', accuracy='10m', granularity='city', maxResults=5}", query.toString());
 }
}