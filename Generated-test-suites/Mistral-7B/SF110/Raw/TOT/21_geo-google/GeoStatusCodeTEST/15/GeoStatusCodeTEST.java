import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCode_G_GEO_SUCCESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals(200, statusCode.getCode());
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", statusCode.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_BAD_REQUEST() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
 assertEquals(400, statusCode.getCode());
 assertEquals("A directions request could not be successfully parsed.", statusCode.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_SERVER_ERROR() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
 assertEquals(500, statusCode.getCode());
 assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.", statusCode.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_MISSING_QUERY() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertEquals(601, statusCode.getCode());
 assertEquals("The HTTP q parameter was either missing or had no value.", statusCode.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_UNKNOWN_ADDRESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 assertEquals(602, statusCode.getCode());
 assertEquals("No corresponding geographic location could be found for the specified address.", statusCode.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_UNAVAILABLE_ADDRESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 assertEquals(603, statusCode.getCode());
 assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons.", statusCode.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_UNKNOWN_DIRECTIONS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 assertEquals(604, statusCode.getCode());
 assertEquals("The GDirections object could not compute directions between the points mentioned in the query.", statusCode.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_BAD_KEY() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_KEY;
 assertEquals(610, statusCode.getCode());
 assertEquals("The given key is either invalid or does not match the domain for which it was given.", statusCode.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_TOO_MANY_QUERIES() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 assertEquals(620, statusCode.getCode());
 assertEquals("The given key has gone over the requests limit in the 24 hour period.", statusCode.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_UNKOWN_STATUS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 assertEquals(-1, statusCode.getCode());
 assertEquals("Uknown Status", statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 int testCode = 601;
 GeoStatusCode returnedStatusCode = GeoStatusCode.getStatusCode(testCode);
 assertEquals(statusCode, returnedStatusCode);
 }
}