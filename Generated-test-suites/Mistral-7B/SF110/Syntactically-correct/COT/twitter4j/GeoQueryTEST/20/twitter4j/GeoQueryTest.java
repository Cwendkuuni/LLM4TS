import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation location;
 private String ipAddress;
 private GeoQuery queryWithLocation;
 private GeoQuery queryWithIPAddress;
 private List<HttpParameter> params;

 @Test
 public void testConstructorWithLocation() {
     location = new GeoLocation(51.5074, -0.1278);
     queryWithLocation = new GeoQuery(location);
     assertNull(queryWithLocation.getIp());
     assertEquals(location, queryWithLocation.getLocation());
     assertNull(queryWithLocation.getAccuracy());
     assertNull(queryWithLocation.getGranularity());
     assertEquals(0, queryWithLocation.getMaxResults());
     HttpParameter[] paramsArray = queryWithLocation.asHttpParameterArray();
     List<HttpParameter> params = new ArrayList<>();
     params.addAll(Arrays.asList(paramsArray));
     assertEquals(5, params.size());
     assertEquals("lat", params.get(0).getName());
     assertEquals("51.5074", params.get(0).getValue());
     assertEquals("long", params.get(1).getName());
     assertEquals("-0.1278", params.get(1).getValue());
     assertNull(params.get(2));
     assertNull(params.get(3));
     assertNull(params.get(4));
 }

 @Test
 public void testConstructorWithIPAddress() {
     ipAddress = "192.168.1.1";
     queryWithIPAddress = new GeoQuery(ipAddress);
     assertNull(queryWithIPAddress.getLocation());
     assertEquals(ipAddress, queryWithIPAddress.getIp());
     assertNull(queryWithIPAddress.getAccuracy());
     assertNull(queryWithIPAddress.getGranularity());
     assertEquals(0, queryWithIPAddress.getMaxResults());
     params = new ArrayList<>();
     queryWithIPAddress.asHttpParameterArray(params);
     assertEquals(4, params.size());
     assertEquals("ip", params.get(0).getName());
     assertEquals(ipAddress, params.get(0).getValue());
     assertNull(params.get(1));
     assertNull(params.get(2));
     assertNull(params.get(3));
 }

 @Test(expected = NullPointerException.class)
 public void testSettersWithNullValues() {
     queryWithLocation = new GeoQuery(location);
     queryWithLocation.setAccuracy(null);
     queryWithLocation.setGranularity(null);
     queryWithLocation.setMaxResults(-1);
 }

 @Test
 public void testSettersWithValidValues() {
     queryWithLocation = new GeoQuery(location);
     String accuracy = "5km";
     String granularity = GeoQuery.CITY;
     int maxResults = 10;
     queryWithLocation.setAccuracy(accuracy);
     assertEquals(accuracy, queryWithLocation.getAccuracy());
     queryWithLocation.setGranularity(granularity);
     assertEquals(granularity, queryWithLocation.getGranularity());
     queryWithLocation.setMaxResults(maxResults);
     assertEquals(maxResults, queryWithLocation.getMaxResults());
 }

 @Test
 public void testEquals() {
     location = new GeoLocation(51.5074, -0.1278);
     GeoQuery query1 = new GeoQuery(location);
     GeoQuery query2 = new GeoQuery(location);
     GeoQuery query3 = new GeoQuery(new GeoLocation(51.5075, -0.1278));
     assertFalse(query1.equals(null));
     assertTrue(query1.equals(query1));
     assertTrue(query1.equals(query2));
     assertFalse(query1.equals(query3));
 }

 @Test
 public void testHashCode() {
     location = new GeoLocation(51.5074, -0.1278);
     GeoQuery query1 = new GeoQuery(location);
     GeoQuery query2 = new GeoQuery(location);
     GeoQuery query3 = new GeoQuery(new GeoLocation(51.5075, -0.1278));
     int hashCode1 = query1.hashCode();
     int hashCode2 = query2.hashCode();
     int hashCode3 = query3.hashCode();
     assertEquals(hashCode1, hashCode2);
     assertNotEquals(hashCode1, hashCode3);
 }

 @Test
 public void testToString() {
     location = new GeoLocation(51.5074, -0.1278);
     queryWithLocation = new GeoQuery(location);
     assertEquals("GeoQuery{location=GeoLocation(latitude=51.5, longitude=-0.1278), ip='null', accuracy='null', granularity='null', maxResults=0}", queryWithLocation.toString());
 }

}