import org.junit.Test;
import org.junit.Assert;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.internal.http.HttpParameter;

public class GeoQueryTest {

 private GeoLocation geoLocation;
 private String ipAddress;
 private GeoQuery geoQueryWithLocation;
 private GeoQuery geoQueryWithIp;

 @Test
 public void testGeoQueryWithLocation() {
 geoLocation = new GeoLocation(51.5074, 0.1278); // London coordinates
 geoQueryWithLocation = new GeoQuery(geoLocation);
 HttpParameter[] parameters = geoQueryWithLocation.asHttpParameterArray();
 Assert.assertArrayEquals(new HttpParameter[] {
 new HttpParameter("lat", "51.5074"),
 new HttpParameter("long", "0.1278")
 }, parameters);
 }

 @Test
 public void testGeoQueryWithIp() {
 ipAddress = "192.168.0.1";
 geoQueryWithIp = new GeoQuery(ipAddress);
 HttpParameter[] parameters = geoQueryWithIp.asHttpParameterArray();
 Assert.assertArrayEquals(new HttpParameter[] {
 new HttpParameter("ip", "192.168.0.1")
 }, parameters);
 }

 @Test
 public void testGettersAndSetters() {
 GeoLocation testLocation = new GeoLocation(51.5074, 0.1278);
 geoQueryWithLocation = new GeoQuery(testLocation);
 Assert.assertEquals(testLocation, geoQueryWithLocation.getLocation());
 geoQueryWithLocation.setLocation(new GeoLocation(52.5074, 0.1278));
 Assert.assertEquals(new GeoLocation(52.5074, 0.1278), geoQueryWithLocation.getLocation());

 geoQueryWithIp = new GeoQuery(ipAddress);
 Assert.assertEquals(ipAddress, geoQueryWithIp.getIp());
 geoQueryWithIp.setIp("255.255.255.255");
 Assert.assertEquals("255.255.255.255", geoQueryWithIp.getIp());
 }

 @Test
 public void testEqualsAndHashCode() {
 GeoQuery geoQuery1 = new GeoQuery(new GeoLocation(51.5074, 0.1278));
 GeoQuery geoQuery2 = new GeoQuery(new GeoLocation(51.5074, 0.1278));
 GeoQuery geoQuery3 = new GeoQuery(new GeoLocation(51.5073, 0.1278));
 Assert.assertTrue(geoQuery1.equals(geoQuery2));
 Assert.assertFalse(geoQuery1.equals(geoQuery3));
 Assert.assertEquals(geoQuery1.hashCode(), geoQuery2.hashCode());
 }

 @Test
 public void testToString() {
 geoQueryWithLocation = new GeoQuery(new GeoLocation(51.5074, 0.1278));
 Assert.assertEquals("GeoQuery{location=GeoLocation(lat=51.5, lon=0.13, name=London, countryCode=GB, countryName=United Kingdom, fullName=London, timezoneId=Europe/London)}", geoQueryWithLocation.toString());
 }
}