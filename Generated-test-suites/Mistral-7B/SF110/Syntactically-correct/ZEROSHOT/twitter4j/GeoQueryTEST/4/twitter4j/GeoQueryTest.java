import org.junit.Test;
import org.junit.Assert.*;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import java.util.ArrayList;
import java.util.List;

public class GeoQueryTest {

 @Test
 public void testGeoQueryWithLocation() {
 GeoLocation location = new GeoLocation(51.5074, -0.1278);
 GeoQuery geoQuery = new GeoQuery(location);

 HttpParameter[] parameters = geoQuery.asHttpParameterArray();
 List<String> paramsNames = new ArrayList<>();
 paramsNames.add("lat");
 paramsNames.add("long");

 for (int i = 0; i < parameters.length; i++) {
 String paramName = paramsNames.get(i);
 String paramValue = parameters[i].getName();
 assertEquals(paramName, paramValue);
 }
 }

 @Test
 public void testGeoQueryWithIP() {
 GeoQuery geoQuery = new GeoQuery("192.168.1.1");
 HttpParameter[] parameters = geoQuery.asHttpParameterArray();
 String paramName = "ip";
 String paramValue = "192.168.1.1";

 for (int i = 0; i < parameters.length; i++) {
 String name = parameters[i].getName();
 assertEquals(paramName, name);
 String value = parameters[i].getValue();
 assertEquals(paramValue, value);
 }
 }

 @Test
 public void testSettingAndGettingAccuracy() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(51.5074, -0.1278));
 geoQuery.setAccuracy("10m");
 assertEquals("10m", geoQuery.getAccuracy());
 }

 @Test
 public void testSettingAndGettingGranularity() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(51.5074, -0.1278));
 geoQuery.setGranularity("city");
 assertEquals("city", geoQuery.getGranularity());
 }

 @Test
 public void testSettingAndGettingMaxResults() {
 GeoQuery geoQuery = new GeoQuery(new GeoLocation(51.5074, -0.1278));
 geoQuery.setMaxResults(10);
 assertEquals(10, geoQuery.getMaxResults());
 }

 @Test
 public void testEquals() {
 GeoLocation location1 = new GeoLocation(51.5074, -0.1278);
 GeoLocation location2 = new GeoLocation(51.5074, -0.1278);
 GeoQuery geoQuery1 = new GeoQuery(location1);
 GeoQuery geoQuery2 = new GeoQuery(location2);
 GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(51.5075, -0.1278));

 assertTrue(geoQuery1.equals(geoQuery1));
 assertTrue(geoQuery1.equals(geoQuery2));
 assertFalse(geoQuery1.equals(geoQuery3));
 }

 @Test
 public void testHashCode() {
 GeoLocation location1 = new GeoLocation(51.5074, -0.1278);
 GeoLocation location2 = new GeoLocation(51.5074, -0.1278);
 GeoQuery geoQuery1 = new GeoQuery(location1);
 GeoQuery geoQuery2 = new GeoQuery(location2);
 GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(51.5075, -0.1278));
 int hashCode1 = geoQuery1.hashCode();
 int hashCode2 = geoQuery2.hashCode();
 int hashCode3 = geoQuery3.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }
}