import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

 @Test
 public void testG_GEO_SUCCESS() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals(200, status.getCode());
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", status.getDescription());
 }

 @Test
 public void testG_GEO_BAD_REQUEST() {
 GeoStatusCode status = GeoStatusCode.G_GEO_BAD_REQUEST;
 assertEquals(400, status.getCode());
 assertEquals("A directions request could not be successfully parsed.", status.getDescription());
 }

 @Test
 public void testG_GEO_SERVER_ERROR() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SERVER_ERROR;
 assertEquals(500, status.getCode());
 assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.", status.getDescription());
 }

 @Test
 public void testG_GEO_MISSING_QUERY() {
 GeoStatusCode status = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertEquals(601, status.getCode());
 assertEquals("The HTTP q parameter was either missing or had no value.", status.getDescription());
 }

 @Test
 public void testG_GEO_UNKNOWN_ADDRESS() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 assertEquals(602, status.getCode());
 assertEquals("No corresponding geographic location could be found for the specified address.", status.getDescription());
 }

 @Test
 public void testG_GEO_UNAVAILABLE_ADDRESS() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 assertEquals(603, status.getCode());
 assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons.", status.getDescription());
 }

 @Test
 public void testG_GEO_UNKNOWN_DIRECTIONS() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 assertEquals(604, status.getCode());
 assertEquals("The GDirections object could not compute directions between the points mentioned in the query.", status.getDescription());
 }

 @Test
 public void testG_GEO_BAD_KEY() {
 GeoStatusCode status = GeoStatusCode.G_GEO_BAD_KEY;
 assertEquals(610, status.getCode());
 assertEquals("The given key is either invalid or does not match the domain for which it was given.", status.getDescription());
 }

 @Test
 public void testG_GEO_TOO_MANY_QUERIES() {
 GeoStatusCode status = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 assertEquals(620, status.getCode());
 assertEquals("The given key has gone over the requests limit in the 24 hour period.", status.getDescription());
 }

 @Test
 public void testGetStatusCode() {
 GeoStatusCode status = GeoStatusCode.getStatusCode(200);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);

 status = GeoStatusCode.getStatusCode(400);
 assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, status);

 status = GeoStatusCode.getStatusCode(500);
 assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, status);

 status = GeoStatusCode.getStatusCode(601);
 assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, status);

 status = GeoStatusCode.getStatusCode(602);
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, status);

 status = GeoStatusCode.getStatusCode(603);
 assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, status);

 status = GeoStatusCode.getStatusCode(604);
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, status);

 status = GeoStatusCode.getStatusCode(610);
 assertEquals(GeoStatusCode.G_GEO_BAD_KEY, status);

 status = GeoStatusCode.getStatusCode(620);
 assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, status);
 }

 @Test
 public void testUnknownStatus() {
 GeoStatusCode status = GeoStatusCode.getStatusCode(-1);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }
}