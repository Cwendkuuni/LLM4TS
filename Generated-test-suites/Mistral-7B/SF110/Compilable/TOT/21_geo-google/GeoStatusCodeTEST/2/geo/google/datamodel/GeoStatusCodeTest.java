package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCode_G_GEO_SUCCESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals(200, statusCode.getCode());
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode_200() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
 }

 @Test
 public void testGeoStatusCode_G_GEO_BAD_REQUEST() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
 assertEquals(400, statusCode.getCode());
 assertEquals("A directions request could not be successfully parsed. (Since 2.81)", statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode_400() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(400);
 assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, statusCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_InvalidCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(100);
 fail("Should throw IllegalArgumentException for invalid code");
 }

 @Test
 public void testGeoStatusCode_G_GEO_MISSING_QUERY() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertEquals(601, statusCode.getCode());
 assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode_601() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(601);
 assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, statusCode);
 }

 @Test
 public void testAllStatusCode() {
 GeoStatusCode[] allStatusCodes = GeoStatusCode.values();
 assertEquals(13, allStatusCodes.length);
 for (GeoStatusCode statusCode : allStatusCodes) {
 System.out.println("StatusCode - " + statusCode.getCodeName() + " - " + statusCode.getDescription());
 }
 }
}