package test;

import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.Arrays;
import java.util.List;

public class GeoQueryTest {

 @org.junit.Before
 public void setUp() {
 }

 @org.junit.After
 public void tearDown() {
 }

 @org.junit.Test
 public void testConstructorWithLocation() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 GeoQuery query = new GeoQuery(location);
 assertEquals(location, query.getLocation());
 assertNull(query.getIp());
 }

 @org.junit.Test
 public void testConstructorWithIp() {
 String ip = "192.168.1.1";
 GeoQuery query = new GeoQuery(ip);
 assertNull(query.getLocation());
 assertEquals(ip, query.getIp());
 }

 @org.junit.Test
 public void testGetters() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 GeoQuery query = new GeoQuery(location);

 assertEquals(location, query.getLocation());
 assertNull(query.getIp());

 String accuracy = "10m";
 query.setAccuracy(accuracy);
 assertEquals(accuracy, query.getAccuracy());

 String granularity = "city";
 query.setGranularity(granularity);
 assertEquals(granularity, query.getGranularity());

 int maxResults = 10;
 query.setMaxResults(maxResults);
 assertEquals(maxResults, query.getMaxResults());
 }

 @org.junit.Test
 public void testSettersAndChainMethods() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 GeoQuery query = new GeoQuery(location);

 query.accuracy("50m")
 .granularity("neighborhood")
 .maxResults(5);

 assertEquals("50m", query.getAccuracy());
 assertEquals("neighborhood", query.getGranularity());
 assertEquals(5, query.getMaxResults());
 }

 @org.junit.Test
 public void testAsHttpParameterArray() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 GeoQuery query = new GeoQuery(location);
 query.setAccuracy("10m");
 query.setGranularity("city");
 query.setMaxResults(10);

 List<HttpParameter> params = Arrays.asList(query.asHttpParameterArray());
 assertEquals(5, params.size());

 String latParamName = "lat";
 double latParamValue = 1.0;
 String longParamName = "long";
 double longParamValue = 2.0;
 String accuracyParamName = "accuracy";
 String accuracyParamValue = "10m";
 String granularityParamName = "granularity";
 String granularityParamValue = "city";
 String maxResultsParamName = "max_results";
 int maxResultsParamValue = 10;

 for (HttpParameter param : params) {
 String paramName = param.getName();
 String expectedParamValue = "";
 if (latParamName.equals(paramName)) {
 expectedParamValue = String.valueOf(latParamValue);
 } else if (longParamName.equals(paramName)) {
 expectedParamValue = String.valueOf(longParamValue);
 } else if (accuracyParamName.equals(paramName)) {
 expectedParamValue = accuracyParamValue;
 } else if (granularityParamName.equals(paramName)) {
 expectedParamValue = granularityParamValue;
 } else if (maxResultsParamName.equals(paramName)) {
 expectedParamValue = String.valueOf(maxResultsParamValue);
 }
 assertEquals(expectedParamValue, param.getValue());
 }
 }

 @org.junit.Test
 public void testEquals() {
 GeoLocation location1 = new GeoLocation(1.0, 2.0);
 GeoLocation location2 = new GeoLocation(1.0, 2.0);
 GeoQuery query1 = new GeoQuery(location1);
 GeoQuery query2 = new GeoQuery(location2);
 GeoQuery query3 = new GeoQuery(location1);

 assertTrue(query1.equals(query1));
 assertTrue(query1.equals(query3));
 assertTrue(query1.equals(query3));
 assertTrue(query1.equals(query2));
 assertTrue(query2.equals(query1));
 assertTrue(query2.equals(query3));
 assertTrue(query3.equals(query1));
 assertTrue(query3.equals(query2));

 GeoQuery query4 = new GeoQuery(location1);
 query4.setAccuracy("10m");
 assertFalse(query1.equals(query4));
 assertFalse(query4.equals(query1));

 query1.setAccuracy("10m");
 assertTrue(query1.equals(query4));
 assertTrue(query4.equals(query1));
 }

 @org.junit.Test
 public void testHashCode() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 GeoQuery query1 = new GeoQuery(location);
 GeoQuery query2 = new GeoQuery(location);
 GeoQuery query3 = new GeoQuery(new GeoLocation(1.1, 2.1));

 assertEquals(query1.hashCode(), query1.hashCode());
 assertEquals(query1.hashCode(), query2.hashCode());
 assertNotEquals(query1.hashCode(), query3.hashCode());
 }
}