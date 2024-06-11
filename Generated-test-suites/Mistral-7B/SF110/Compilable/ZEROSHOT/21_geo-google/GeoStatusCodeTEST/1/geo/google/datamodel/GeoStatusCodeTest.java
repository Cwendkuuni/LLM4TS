package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode() {
 // Test valid status codes
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 int code = statusCode.getCode();
 GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
 String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned.";
 assertEquals(expectedStatusCode.getCodeName(), statusCode.getCodeName());
 assertEquals(expectedDescription, statusCode.getDescription());
 assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));

 // Test invalid status code
 statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 code = -1;
 assertNull(GeoStatusCode.getStatusCode(code));
 }

 @Test
 public void testGetStatusCodeInvalidCode() {
 // Test invalid status code
 int invalidCode = 100;
 assertNull(GeoStatusCode.getStatusCode(invalidCode));
 }

 @Test
 public void testGetStatusCodeValidCodes() {
 // Test valid status codes
 for (GeoStatusCode statusCode : GeoStatusCode.values()) {
 int code = statusCode.getCode();
 GeoStatusCode expectedStatusCode = statusCode;
 String expectedDescription = statusCode.getDescription();
 assertEquals(expectedStatusCode.getCodeName(), statusCode.getCodeName());
 assertEquals(expectedDescription, statusCode.getDescription());
 assertEquals(expectedStatusCode, GeoStatusCode.getStatusCode(code));
 }
 }

 @Test
 public void testGetStatusCodeDescription() {
 // Test getting status description by name
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 String expectedDescription = "The HTTP q parameter was either missing or had no value.";
 assertEquals(expectedDescription, statusCode.getDescription());
 }

 @Test
 public void testGetStatusCodeName() {
 // Test getting status name by code
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
 String expectedStatusCodeName = "G_GEO_SERVER_ERROR";
 assertEquals(expectedStatusCodeName, statusCode.getCodeName());
 }
}