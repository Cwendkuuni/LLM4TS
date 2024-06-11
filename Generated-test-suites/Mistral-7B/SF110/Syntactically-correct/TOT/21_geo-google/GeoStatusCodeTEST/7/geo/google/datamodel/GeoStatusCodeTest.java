import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testG_GEO_SUCCESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", statusCode.getDescription());
 assertEquals(200, statusCode.getCode());
 }

 @Test
 public void testG_GEO_BAD_REQUEST() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
 assertEquals("A directions request could not be successfully parsed.", statusCode.getDescription());
 assertEquals(400, statusCode.getCode());
 }

 @Test
 public void testG_GEO_SERVER_ERROR() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
 assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.", statusCode.getDescription());
 assertEquals(500, statusCode.getCode());
 }

 @Test
 public void testG_GEO_MISSING_QUERY() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertEquals("The HTTP q parameter was either missing or had no value.", statusCode.getDescription());
 assertEquals(601, statusCode.getCode());
 }

 @Test
 public void testG_GEO_UNKNOWN_ADDRESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 assertEquals("No corresponding geographic location could be found for the specified address.", statusCode.getDescription());
 assertEquals(602, statusCode.getCode());
 }

 @Test
 public void testG_GEO_UNAVAILABLE_ADDRESS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons.", statusCode.getDescription());
 assertEquals(603, statusCode.getCode());
 }

 @Test
 public void testG_GEO_UNKNOWN_DIRECTIONS() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 assertEquals("The GDirections object could not compute directions between the points mentioned in the query.", statusCode.getDescription());
 assertEquals(604, statusCode.getCode());
 }

 @Test
 public void testG_GEO_BAD_KEY() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_BAD_KEY;
 assertEquals("The given key is either invalid or does not match the domain for which it was given.", statusCode.getDescription());
 assertEquals(610, statusCode.getCode());
 }

 @Test
 public void testG_GEO_TOO_MANY_QUERIES() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 assertEquals("The given key has gone over the requests limit in the 24 hour period.", statusCode.getDescription());
 assertEquals(620, statusCode.getCode());
 }

 @Test
 public void testGetStatusCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);

 statusCode = GeoStatusCode.getStatusCode(400);
 assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, statusCode);

 statusCode = GeoStatusCode.getStatusCode(500);
 assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, statusCode);

 statusCode = GeoStatusCode.getStatusCode(601);
 assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, statusCode);

 statusCode = GeoStatusCode.getStatusCode(602);
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, statusCode);

 statusCode = GeoStatusCode.getStatusCode(603);
 assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, statusCode);

 statusCode = GeoStatusCode.getStatusCode(604);
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, statusCode);

 statusCode = GeoStatusCode.getStatusCode(610);
 assertEquals(GeoStatusCode.G_GEO_BAD_KEY, statusCode);

 statusCode = GeoStatusCode.getStatusCode(620);
 assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, statusCode);
 }

 @Test
 public void testUnknownStatus() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(-1);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
 }
}