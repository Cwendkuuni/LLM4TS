package twitter4j.test;

import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(0.0, 0.0);
 private String ip = "192.168.1.1";

 @Test
 public void testGeoQueryWithLocation() {
 GeoQuery query = new GeoQuery(location);
 assertNull(query.getIp());
 assertNotNull(query.getLocation());
 HttpParameter[] params = query.asHttpParameterArray();
 assertArrayEquals(new HttpParameter[]{
 new HttpParameter("lat", Double.toString(location.getLatitude())),
 new HttpParameter("long", Double.toString(location.getLongitude()))
 }, params);
 }

 @Test
 public void testGeoQueryWithIp() {
 GeoQuery query = new GeoQuery(ip);
 assertNull(query.getLocation());
 assertNotNull(query.getIp());
 HttpParameter[] params = query.asHttpParameterArray();
 assertArrayEquals(new HttpParameter[]{
 new HttpParameter("ip", ip)
 }, params);
 }

 @Test(expected = NullPointerException.class)
 public void testNullLocation() {
 GeoQuery query = new GeoQuery(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullIp() {
 GeoQuery query = new GeoQuery(null);
 query.setIp(null);
 }

 @Test
 public void testSetGetAccuracy() {
 GeoQuery query = new GeoQuery();
 query.setAccuracy("10m");
 assertEquals("10m", query.getAccuracy());
 query.setAccuracy(null);
 assertNull(query.getAccuracy());
 }

 @Test
 public void testSetGetGranularity() {
 GeoQuery query = new GeoQuery();
 query.setGranularity("city");
 assertEquals("city", query.getGranularity());
 query.setGranularity(null);
 assertNull(query.getGranularity());
 }

 @Test
 public void testSetGetMaxResults() {
 GeoQuery query = new GeoQuery();
 query.setMaxResults(5);
 assertEquals(5, query.getMaxResults());
 query.setMaxResults(0);
 assertEquals(0, query.getMaxResults());
 }

 @Test
 public void testEquals() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 GeoQuery query3 = new GeoQuery(new GeoLocation(1.0, 1.0));
 assertTrue(query1.equals(query2));
 assertFalse(query1.equals(query3));
 }

 @Test
 public void testHashCode() {
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 GeoQuery query3 = new GeoQuery(new GeoLocation(1.0, 1.0));
 assertEquals(query1.hashCode(), query2.hashCode());
 assertNotSame(query1.hashCode(), query3.hashCode());
 }

 @Test
 public void testToString() {
 GeoQuery query = new GeoQuery(location);
 assertEquals("GeoQuery{location=" + location + ", ip='null' , accuracy='null' , granularity='null' , maxResults=0}", query.toString());
 }
}