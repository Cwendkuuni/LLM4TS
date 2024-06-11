import org.junit.Test;import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCode_G_GEO_SUCCESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals(200, statusCode.getCode());
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode_ValidCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_InvalidCode() {
 try {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(621);
 } catch (IllegalArgumentException e) {
 assertEquals("Unknown status code: 621", e.getMessage());
 }
 }

 @Test
 public void testGetStatusCode_UnknownStatus() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 assertEquals(-1, statusCode.getCode());
 assertEquals("Unknown Status", statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode_Null() {
 try {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(null);
 } catch (NullPointerException e) {
 // Expected
 }
 }

}