package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCode_G_GEO_SUCCESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 int expectedCode = 200;
 String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned.";

 assertEquals(expectedCode, statusCode.getCode());
 assertEquals(expectedDescription, statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode_ValidCode() {
 int code = 200;
 GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;

 GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);

 assertEquals(expectedStatusCode, actualStatusCode);
 }

 @Test
 public void testGetStatusCode_InvalidCode() {
 int code = 700;
 GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;

 GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);

 assertEquals(expectedStatusCode, actualStatusCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_NegativeCode() {
 int code = -1;

 GeoStatusCode.getStatusCode(code);
 }

 @Test
 public void testGetStatusCode_UnknownStatusCode() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 int expectedCode = -1;
 String expectedDescription = "Uknown Status";

 assertEquals(expectedCode, statusCode.getCode());
 assertEquals(expectedDescription, statusCode.getDescription());
 }
}