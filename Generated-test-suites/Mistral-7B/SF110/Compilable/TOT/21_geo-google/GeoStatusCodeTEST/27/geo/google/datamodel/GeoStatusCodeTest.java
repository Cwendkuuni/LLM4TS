package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode() {
 // Test valid status codes
 assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
 assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());

 assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
 assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());

 assertEquals("G_GEO_SERVER_ERROR", GeoStatusCode.G_GEO_SERVER_ERROR.getCodeName());
 assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());

 // Test invalid status codes
 assertNull(GeoStatusCode.getStatusCode(-1));
 assertNull(GeoStatusCode.getStatusCode(630)); // Invalid status code
 }

 @Test
 public void testGetStatusCodeWithCode() {
 // Test valid status codes
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
 assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
 assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));

 // Test invalid status codes
 assertNull(GeoStatusCode.getStatusCode(630)); // Invalid status code
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithNegativeCode() {
 GeoStatusCode.getStatusCode(-1); // negative code should throw an exception
 }
}