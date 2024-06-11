package twitter4j.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeoQueryTest {

 private GeoQuery geoQuery;

 @Before
 public void setUp() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 this.geoQuery = new GeoQuery(location);
 }

 @Test
 public void testConstructorWithLocation() {
 Assert.assertNotNull(this.geoQuery.getLocation());
 Assert.assertNull(this.geoQuery.getIp());
 }

 @Test
 public void testConstructorWithIp() {
 Assert.assertNull(this.geoQuery.getLocation());
 Assert.assertEquals("1.0.0.1", this.geoQuery.getIp());
 }

 @Test
 public void testGetLocation() {
 GeoLocation expectedLocation = new GeoLocation(1.0, 2.0);
 GeoLocation actualLocation = this.geoQuery.getLocation();
 Assert.assertEquals(expectedLocation, actualLocation);
 }

 @Test
 public void testGetIp() {
 String expectedIp = "1.0.0.1";
 String actualIp = this.geoQuery.getIp();
 Assert.assertEquals(expectedIp, actualIp);
 }

 @Test
 public void testGetAccuracy() {
 Assert.assertNull(this.geoQuery.getAccuracy());
 }

 @Test
 public void testSetAndGetAccuracy() {
 String accuracy = "10m";
 this.geoQuery.setAccuracy(accuracy);
 Assert.assertEquals(accuracy, this.geoQuery.getAccuracy());
 }

 @Test
 public void testGetGranularity() {
 Assert.assertNull(this.geoQuery.getGranularity());
 }

 @Test
 public void testSetAndGetGranularity() {
 String granularity = "city";
 this.geoQuery.setGranularity(granularity);
 Assert.assertEquals(granularity, this.geoQuery.getGranularity());
 }

 @Test
 public void testGetMaxResults() {
 Assert.assertEquals(0, this.geoQuery.getMaxResults());
 }

 @Test
 public void testSetAndGetMaxResults() {
 int maxResults = 5;
 this.geoQuery.setMaxResults(maxResults);
 Assert.assertEquals(maxResults, this.geoQuery.getMaxResults());
 }

 @Test
 public void testAsHttpParameterArray() {
 GeoLocation location = new GeoLocation(1.0, 2.0);
 this.geoQuery = new GeoQuery(location);
 List<HttpParameter> expectedParameters = new ArrayList<>();
 this.geoQuery.appendParameter("lat", "1.0", expectedParameters);
 this.geoQuery.appendParameter("long", "2.0", expectedParameters);
 HttpParameter[] actualParameters = this.geoQuery.asHttpParameterArray();
 Assert.assertArrayEquals(expectedParameters.toArray(new HttpParameter[0]), actualParameters);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullName() {
 this.geoQuery = new GeoQuery(null);
 List<HttpParameter> params = new ArrayList<>();
 this.geoQuery.appendParameter(null, "value", params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullValue() {
 this.geoQuery = new GeoQuery(null);
 List<HttpParameter> params = new ArrayList<>();
 this.geoQuery.appendParameter("name", null, params);
 }

 @Test(expected = IllegalArgumentException.class) public void testAppendParameterWithEmptyList() {
 List<HttpParameter> params = new ArrayList<>();
 this.geoQuery.asHttpParameterArray();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNegativeMaxResults() {
 this.geoQuery = new GeoQuery(null);
 this.geoQuery.setMaxResults(-1);
 List<HttpParameter> params = new ArrayList<>();
 this.geoQuery.asHttpParameterArray();
 }
}