package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCode_ValidCases() {
  // Test valid cases by getting the GeoStatusCode enum object using getStatusCode method with valid codes
  GeoStatusCode statusCodeG_GEO_SUCCESS = GeoStatusCode.getStatusCode(200);
  GeoStatusCode statusCodeG_GEO_BAD_REQUEST = GeoStatusCode.getStatusCode(400);
  GeoStatusCode statusCodeG_GEO_SERVER_ERROR = GeoStatusCode.getStatusCode(500);
  GeoStatusCode statusCodeG_GEO_MISSING_QUERY = GeoStatusCode.getStatusCode(601);
  GeoStatusCode statusCodeG_GEO_UNKNOWN_ADDRESS = GeoStatusCode.getStatusCode(602);
  GeoStatusCode statusCodeG_GEO_UNAVAILABLE_ADDRESS = GeoStatusCode.getStatusCode(603);
  GeoStatusCode statusCodeG_GEO_UNKNOWN_DIRECTIONS = GeoStatusCode.getStatusCode(604);
  GeoStatusCode statusCodeG_GEO_BAD_KEY = GeoStatusCode.getStatusCode(610);
  GeoStatusCode statusCodeG_GEO_TOO_MANY_QUERIES = GeoStatusCode.getStatusCode(620);

  // Assert that the GeoStatusCode objects are not null and have the expected values
  assertNotNull(statusCodeG_GEO_SUCCESS);
  assertEquals(200, statusCodeG_GEO_SUCCESS.getCode());
  assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", statusCodeG_GEO_SUCCESS.getDescription());

  assertNotNull(statusCodeG_GEO_BAD_REQUEST);
  assertEquals(400, statusCodeG_GEO_BAD_REQUEST.getCode());
  assertEquals("A directions request could not be successfully parsed. (Since 2.81)", statusCodeG_GEO_BAD_REQUEST.getDescription());

  assertNotNull(statusCodeG_GEO_SERVER_ERROR);
  assertEquals(500, statusCodeG_GEO_SERVER_ERROR.getCode());
  assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", statusCodeG_GEO_SERVER_ERROR.getDescription());

  assertNotNull(statusCodeG_GEO_MISSING_QUERY);
  assertEquals(601, statusCodeG_GEO_MISSING_QUERY.getCode());
  assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", statusCodeG_GEO_MISSING_QUERY.getDescription());

  assertNotNull(statusCodeG_GEO_UNKNOWN_ADDRESS);
  assertEquals(602, statusCodeG_GEO_UNKNOWN_ADDRESS.getCode());
  assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", statusCodeG_GEO_UNKNOWN_ADDRESS.getDescription());

  assertNotNull(statusCodeG_GEO_UNAVAILABLE_ADDRESS);
  assertEquals(603, statusCodeG_GEO_UNAVAILABLE_ADDRESS.getCode());
  assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", statusCodeG_GEO_UNAVAILABLE_ADDRESS.getDescription());

  assertNotNull(statusCodeG_GEO_UNKNOWN_DIRECTIONS);
  assertEquals(604, statusCodeG_GEO_UNKNOWN_DIRECTIONS.getCode());
  assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", statusCodeG_GEO_UNKNOWN_DIRECTIONS.getDescription());

  assertNotNull(statusCodeG_GEO_BAD_KEY);
  assertEquals(610, statusCodeG_GEO_BAD_KEY.getCode());
  assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", statusCodeG_GEO_BAD_KEY.getDescription());

  assertNotNull(statusCodeG_GEO_TOO_MANY_QUERIES);
  assertEquals(620, statusCodeG_GEO_TOO_MANY_QUERIES.getCode());
  assertEquals("The given key has gone over the requests limit in the 24 hour period. ", statusCodeG_GEO_TOO_MANY_QUERIES.getDescription());
 }

 @Test
 public void testGeoStatusCode_InvalidCode() {
  // Test invalid code cases by getting the GeoStatusCode.G_GEO_UNKOWN_STATUS
  GeoStatusCode statusCodeUnknown = GeoStatusCode.getStatusCode(-1);

  // Assert that the GeoStatusCode object is not null and has the expected values
  assertNotNull(statusCodeUnknown);
  assertEquals(-1, statusCodeUnknown.getCode());
  assertEquals("Uknown Status", statusCodeUnknown.getDescription());
 }
}