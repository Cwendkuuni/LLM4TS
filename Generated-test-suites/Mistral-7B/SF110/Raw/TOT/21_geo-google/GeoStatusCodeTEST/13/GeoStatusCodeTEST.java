import org.junit.Assert; import org.junit.Test; import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode_ValidCodes() {
 // Test valid status codes
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 int expectedCode = 200;
 String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned.";

 Assert.assertEquals("Invalid status code for G_GEO_SUCCESS", expectedCode, statusCode.getCode());
 Assert.assertEquals("Invalid description for G_GEO_SUCCESS", expectedDescription, statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
 expectedCode = 400;
 expectedDescription = "A directions request could not be successfully parsed.";

 Assert.assertEquals("Invalid status code for G_GEO_BAD_REQUEST", expectedCode, statusCode.getCode());
 Assert.assertEquals("Invalid description for G_GEO_BAD_REQUEST", expectedDescription, statusCode.getDescription());

 // Test other status codes similarly
 // ...
 }

 @Test
 public void testGetStatusCode_InvalidCode() {
 // Test unknown status code
 int unknownCode = 999;
 GeoStatusCode unknownStatus = GeoStatusCode.getStatusCode(unknownCode);
 String expectedUnknownDescription = "Uknown Status";
 int expectedUnknownCode = -1;

 Assert.assertEquals("Invalid status code for unknown status code", expectedUnknownCode, unknownStatus.getCode());
 Assert.assertEquals("Invalid description for unknown status code", expectedUnknownDescription, unknownStatus.getDescription());
 }

 @Test
 public void testGetStatusCode_ValidName() {
 // Test getting status code by name
 String name = GeoStatusCode.G_GEO_MISSING_QUERY.name();
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(Integer.parseInt(name.substring(1)));
 int expectedCode = 601;
 String expectedDescription = "The HTTP q parameter was either missing or had no value.";

 Assert.assertEquals("Invalid status code for name G_GEO_MISSING_QUERY", expectedCode, statusCode.getCode());
 Assert.assertEquals("Invalid description for name G_GEO_MISSING_QUERY", expectedDescription, statusCode.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_InvalidName() {
 // Test getting status code by invalid name
 String invalidName = "X_GEO_INVALID_STATUS";
 GeoStatusCode statusCode = null;

 try {
 statusCode = GeoStatusCode.getStatusCode(Integer.parseInt(invalidName.substring(1)));
 } catch (IllegalArgumentException e) {
 // expected
 }

 Assert.assertNull("Status code should be null for invalid name", statusCode);
 }
}