import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode() {
 // Test valid status codes
 GeoStatusCode statusCode1 = GeoStatusCode.G_GEO_SUCCESS;
 int code1 = statusCode1.getCode();
 GeoStatusCode expectedStatusCode1 = GeoStatusCode.G_GEO_SUCCESS;
 GeoStatusCode actualStatusCode1 = GeoStatusCode.getStatusCode(code1);
 assertEquals(expectedStatusCode1, actualStatusCode1);

 GeoStatusCode statusCode2 = GeoStatusCode.G_GEO_BAD_REQUEST;
 int code2 = statusCode2.getCode();
 GeoStatusCode expectedStatusCode2 = GeoStatusCode.G_GEO_BAD_REQUEST;
 GeoStatusCode actualStatusCode2 = GeoStatusCode.getStatusCode(code2);
 assertEquals(expectedStatusCode2, actualStatusCode2);

 // Test invalid status code
 int invalidCode = 999;
 GeoStatusCode expectedStatusCode3 = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 GeoStatusCode actualStatusCode3 = GeoStatusCode.getStatusCode(invalidCode);
 assertEquals(expectedStatusCode3, actualStatusCode3);
 }

 @Test
 public void testGetStatusCodeName() {
 // Test valid status codes
 GeoStatusCode statusCode1 = GeoStatusCode.G_GEO_SUCCESS;
 String expectedStatusCodeName1 = "G_GEO_SUCCESS";
 String actualStatusCodeName1 = statusCode1.getCodeName();
 assertEquals(expectedStatusCodeName1, actualStatusCodeName1);

 GeoStatusCode statusCode2 = GeoStatusCode.G_GEO_BAD_REQUEST;
 String expectedStatusCodeName2 = "G_GEO_BAD_REQUEST";
 String actualStatusCodeName2 = statusCode2.getCodeName();
 assertEquals(expectedStatusCodeName2, actualStatusCodeName2);

 // Test invalid status code
 GeoStatusCode invalidStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 String expectedStatusCodeName3 = "G_GEO_UNKOWN_STATUS";
 String actualStatusCodeName3 = invalidStatusCode.getCodeName();
 assertEquals(expectedStatusCodeName3, actualStatusCodeName3);
 }

 @Test
 public void testGetStatusCodeDescription() {
 // Test valid status codes
 GeoStatusCode statusCode1 = GeoStatusCode.G_GEO_SUCCESS;
 String expectedStatusCodeDescription1 = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
 String actualStatusCodeDescription1 = statusCode1.getDescription();
 assertEquals(expectedStatusCodeDescription1, actualStatusCodeDescription1);

 GeoStatusCode statusCode2 = GeoStatusCode.G_GEO_BAD_REQUEST;
 String expectedStatusCodeDescription2 = "A directions request could not be successfully parsed. (Since 2.81)";
 String actualStatusCodeDescription2 = statusCode2.getDescription();
 assertEquals(expectedStatusCodeDescription2, actualStatusCodeDescription2);

 // Test invalid status code
 GeoStatusCode invalidStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 String expectedStatusCodeDescription3 = "Uknown Status";
 String actualStatusCodeDescription3 = invalidStatusCode.getDescription();
 assertEquals(expectedStatusCodeDescription3, actualStatusCodeDescription3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithNegativeCode() {
 // Test invalid status code with negative code
 GeoStatusCode statusCode = new GeoStatusCode(-1, "Invalid status code with negative code");
 }
}