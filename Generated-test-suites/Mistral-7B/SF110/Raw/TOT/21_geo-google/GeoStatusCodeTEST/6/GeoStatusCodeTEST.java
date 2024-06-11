import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCodeSuccess() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 int expectedCode = 200;
 String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned.";

 assertEquals(expectedCode, statusCode.getCode());
 assertEquals(expectedDescription, statusCode.getDescription());
 }

 @Test
 public void testGetStatusCodeForValidCode() {
 int code = 200;
 GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;

 GeoStatusCode returnedStatusCode = GeoStatusCode.getStatusCode(code);

 assertEquals(expectedStatusCode, returnedStatusCode);
 }

 @Test
 public void testGetStatusCodeForInvalidCode() {
 int code = 100;
 GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;

 GeoStatusCode returnedStatusCode = GeoStatusCode.getStatusCode(code);

 assertEquals(expectedStatusCode, returnedStatusCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeForNegativeCode() {
 int code = -1;

 GeoStatusCode.getStatusCode(code);
 }

}