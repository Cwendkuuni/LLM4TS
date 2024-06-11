import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testG_GEO_SUCCESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals(200, statusCode.getCode());
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
 }

 @Test
 public void testUnknownStatusCode() {
 int unknownCode = -1;
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(unknownCode);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidStatusCode() {
 int invalidCode = 999;
 GeoStatusCode.getStatusCode(invalidCode); // This line should throw an IllegalArgumentException
 }

 @Test
 public void testGetCodeName() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
 assertEquals("G_GEO_BAD_REQUEST", statusCode.getCodeName());
 }

 @Test
 public void testGetStatusCodeByName() {
 String statusCodeName = "G_GEO_SERVER_ERROR";
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(StatusCodes.valueOf(statusCodeName.toUpperCase()));
 assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, statusCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidStatusCodeName() {
 String invalidStatusCodeName = "G_INVALID_STATUS_CODE";
 GeoStatusCode.getStatusCode(StatusCodes.valueOf(invalidStatusCodeName.toUpperCase())); // This line should throw an IllegalArgumentException
 }
}