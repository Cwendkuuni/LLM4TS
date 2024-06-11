import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCodeSuccess() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 int expectedCode = 200;
 String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeBadRequest() {
 GeoStatusCode status = GeoStatusCode.G_GEO_BAD_REQUEST;
 int expectedCode = 400;
 String expectedDescription = "A directions request could not be successfully parsed.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeServerError() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SERVER_ERROR;
 int expectedCode = 500;
 String expectedDescription = "A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeMissingQuery() {
 GeoStatusCode status = GeoStatusCode.G_GEO_MISSING_QUERY;
 int expectedCode = 601;
 String expectedDescription = "The HTTP q parameter was either missing or had no value.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeUnknownAddress() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 int expectedCode = 602;
 String expectedDescription = "No corresponding geographic location could be found for the specified address.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeUnavailableAddress() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 int expectedCode = 603;
 String expectedDescription = "The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeUnknownDirections() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 int expectedCode = 604;
 String expectedDescription = "The GDirections object could not compute directions between the points mentioned in the query.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeBadKey() {
 GeoStatusCode status = GeoStatusCode.G_GEO_BAD_KEY;
 int expectedCode = 610;
 String expectedDescription = "The given key is either invalid or does not match the domain for which it was given.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeTooManyQueries() {
 GeoStatusCode status = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 int expectedCode = 620;
 String expectedDescription = "The given key has gone over the requests limit in the 24 hour period.";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeUnknownStatus() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 int expectedCode = -1;
 String expectedDescription = "Uknown Status";

 assertEquals(expectedCode, status.getCode());
 assertEquals(expectedDescription, status.getDescription());
 }

 @Test
 public void testGetStatusCodeByCode() {
 int code = 200;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_SUCCESS;

 assertEquals(expectedStatus, status);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testGetStatusCodeByInvalidCode() {
 int code = 621;
 GeoStatusCode status = null;
 try {
 status = GeoStatusCode.getStatusCode(code);
 } catch (IllegalArgumentException e) {
 // expected
 }

 assertNull(status);
 }
}