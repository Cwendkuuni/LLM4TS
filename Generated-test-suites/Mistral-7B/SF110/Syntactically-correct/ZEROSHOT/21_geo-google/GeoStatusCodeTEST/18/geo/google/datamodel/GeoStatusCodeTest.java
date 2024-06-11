package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCodeConstruction() {
 GeoStatusCode statusCode;

 // Test valid status codes
 statusCode = GeoStatusCode.G_GEO_SUCCESS;
 assertNotNull(statusCode);
 assertEquals(200, statusCode.getCode());
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
 assertNotNull(statusCode);
 assertEquals(400, statusCode.getCode());
 assertEquals("A directions request could not be successfully parsed.", statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
 assertNotNull(statusCode);
 assertEquals(500, statusCode.getCode());
 assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.", statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertNotNull(statusCode);
 assertEquals(601, statusCode.getCode());
 assertEquals("The HTTP q parameter was either missing or had no value.", statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 assertNotNull(statusCode);
 assertEquals(602, statusCode.getCode());
 assertEquals("No corresponding geographic location could be found for the specified address.", statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 assertNotNull(statusCode);
 assertEquals(603, statusCode.getCode());
 assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons.", statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 assertNotNull(statusCode);
 assertEquals(604, statusCode.getCode());
 assertEquals("The GDirections object could not compute directions between the points mentioned in the query.", statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_BAD_KEY;
 assertNotNull(statusCode);
 assertEquals(610, statusCode.getCode());
 assertEquals("The given key is either invalid or does not match the domain for which it was given.", statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 assertNotNull(statusCode);
 assertEquals(620, statusCode.getCode());
 assertEquals("The given key has gone over the requests limit in the 24 hour period.", statusCode.getDescription());

 statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 assertNotNull(statusCode);
 assertEquals(-1, statusCode.getCode());
 assertEquals("Uknown Status", statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode() {
 GeoStatusCode statusCode;
 int code;

 // Test valid status codes
 code = 200;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);

 code = 400;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, statusCode);

 code = 500;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, statusCode);

 code = 601;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, statusCode);

 code = 602;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, statusCode);

 code = 603;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, statusCode);

 code = 604;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, statusCode);

 code = 610;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_BAD_KEY, statusCode);

 code = 620;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, statusCode);

 code = -1;
 statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeInvalidCode() {
 GeoStatusCode statusCode;
 int code = 100; // Invalid code
 statusCode = GeoStatusCode.getStatusCode(code);
 }
}