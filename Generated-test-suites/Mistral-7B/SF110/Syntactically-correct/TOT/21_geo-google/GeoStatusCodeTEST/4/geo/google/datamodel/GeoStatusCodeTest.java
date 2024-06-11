import static org.junit.Assert.*;
import static geo.google.datamodel.GeoStatusCode.*;

import org.junit.Test;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode() {
 // Test valid codes
 assertEquals(G_GEO_SUCCESS, getStatusCode(200));
 assertEquals(G_GEO_BAD_REQUEST, getStatusCode(400));
 assertEquals(G_GEO_SERVER_ERROR, getStatusCode(500));
 assertEquals(G_GEO_MISSING_QUERY, getStatusCode(601));
 assertEquals(G_GEO_UNKNOWN_ADDRESS, getStatusCode(602));
 assertEquals(G_GEO_UNAVAILABLE_ADDRESS, getStatusCode(603));
 assertEquals(G_GEO_UNKNOWN_DIRECTIONS, getStatusCode(604));
 assertEquals(G_GEO_BAD_KEY, getStatusCode(610));
 assertEquals(G_GEO_TOO_MANY_QUERIES, getStatusCode(620));

 // Test invalid code
 assertEquals(G_GEO_UNKOWN_STATUS, getStatusCode(-1));
 }

 @Test
 public void testGetStatusCodeDescription() {
 // Test valid codes
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", G_GEO_SUCCESS.getDescription());
 assertEquals("A directions request could not be successfully parsed. (Since 2.81)", G_GEO_BAD_REQUEST.getDescription());
 assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", G_GEO_SERVER_ERROR.getDescription());
 assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", G_GEO_MISSING_QUERY.getDescription());
 assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", G_GEO_UNKNOWN_ADDRESS.getDescription());
 assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", G_GEO_UNAVAILABLE_ADDRESS.getDescription());
 assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", G_GEO_UNKNOWN_DIRECTIONS.getDescription());
 assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", G_GEO_BAD_KEY.getDescription());
 assertEquals("The given key has gone over the requests limit in the 24 hour period. ", G_GEO_TOO_MANY_QUERIES.getDescription());

 // Test invalid code
 assertEquals("Unknown Status", G_GEO_UNKOWN_STATUS.getDescription());
 }
}