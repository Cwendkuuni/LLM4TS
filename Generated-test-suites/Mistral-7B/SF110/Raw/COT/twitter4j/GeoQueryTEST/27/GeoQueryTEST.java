import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

public class GeoQueryTest {

 private GeoLocation location = new GeoLocation(1.0, 2.0);
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
 public void testGetters() {
     geoQueryWithLocation.setAccuracy("high");
     geoQueryWithLocation.setGranularity("city");
     geoQueryWithLocation.setMaxResults(10);

     assertEquals("high", geoQueryWithLocation.getAccuracy());
     assertEquals("city", geoQueryWithLocation.getGranularity());
     assertEquals(10, geoQueryWithLocation.getMaxResults());
     assertNull(geoQueryWithIp.getLocation());
     assertNull(geoQueryWithIp.getAccuracy());
     assertNull(geoQueryWithIp.getGranularity());
     assertEquals(0, geoQueryWithIp.getMaxResults());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullName() {
     List<HttpParameter> params = new ArrayList<>();
     GeoQuery geoQuery = new GeoQuery(null);
     geoQuery.appendParameter(null, 1, params);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAppendParameterWithNullValue() {
     List<HttpParameter> params = new ArrayList<>();
     GeoQuery geoQuery = new GeoQuery(null);
     geoQuery.appendParameter("name", null, params);
 }

 @Test
 public void testEquals() {
     GeoQuery geoQuery1 = new GeoQuery(location);
     GeoQuery geoQuery2 = new GeoQuery(location);
     GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0, 2.1));

     assertEquals(geoQuery1, geoQuery1);
     assertEquals(geoQuery1, geoQuery2);
     assertNotEquals(geoQuery1, geoQuery3);
 }

 @Test
 public void testHashCode() {
     GeoQuery geoQuery1 = new GeoQuery(location);
     GeoQuery geoQuery2 = new GeoQuery(location);
     GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(1.0, 2.1));
     GeoQuery geoQuery4 = new GeoQuery(new GeoLocation(1.1, 2.1));

     assertEquals(geoQuery1.hashCode(), geoQuery1.hashCode());
     assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
     assertNotEquals(geoQuery1.hashCode(), geoQuery3.hashCode());
     assertNotEquals(geoQuery1.hashCode(), geoQuery4.hashCode());
 }

 @Test
 public void testToString() {
     assertEquals("GeoQuery{location=GeoLocation(latitude=1.0, longitude=2.0), ip='null', accuracy='null', granularity='null', maxResults=0}",
             geoQueryWithLocation.toString());
     assertEquals("GeoQuery{location=null, ip='192.168.1.1', accuracy='null', granularity='null', maxResults=0}",
             geoQueryWithIp.toString());
 }

 @Test
 public void testAsHttpParameterArray() {
     geoQueryWithLocation.setAccuracy("high");
     geoQueryWithLocation.setGranularity("city");
     geoQueryWithLocation.setMaxResults(10);

     HttpParameter[] params = geoQueryWithLocation.asHttpParameterArray();
     List<String> expectedNames = Arrays.asList("lat", "long", "accuracy", "granularity", "max_results");
     List<String> expectedValues = Arrays.asList(String.valueOf(location.getLatitude()),
             String.valueOf(location.getLongitude()), "high", "city", String.valueOf(10));

     for (int i = 0; i < params.length; i++) {
         assertEquals(expectedNames.get(i), params[i].getName());
         assertEquals(expectedValues.get(i), params[i].getValue());
     }
 }
}