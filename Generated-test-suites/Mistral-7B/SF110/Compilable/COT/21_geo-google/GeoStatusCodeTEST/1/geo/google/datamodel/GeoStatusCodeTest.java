package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Test all getter methods of GeoStatusCode enum
 @Test
 public void testGetterMethods() {
  GeoStatusCode success = GeoStatusCode.G_GEO_SUCCESS;
  GeoStatusCode badRequest = GeoStatusCode.G_GEO_BAD_REQUEST;
  GeoStatusCode serverError = GeoStatusCode.G_GEO_SERVER_ERROR;
  GeoStatusCode missingQuery = GeoStatusCode.G_GEO_MISSING_QUERY;
  GeoStatusCode unknownAddress = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
  GeoStatusCode unavailableAddress = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
  GeoStatusCode unknownDirections = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
  GeoStatusCode badKey = GeoStatusCode.G_GEO_BAD_KEY;
  GeoStatusCode tooManyQueries = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
  GeoStatusCode unknownStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;

  assertEquals(200, success.getCode());
  assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", success.getDescription());

  assertEquals(400, badRequest.getCode());
  assertEquals("A directions request could not be successfully parsed.", badRequest.getDescription());

  assertEquals(500, serverError.getCode());
  assertNull(serverError.getDescription()); // null description for this status code

  assertEquals(601, missingQuery.getCode());
  assertEquals("The HTTP q parameter was either missing or had no value.", missingQuery.getDescription());

  assertEquals(602, unknownAddress.getCode());
  assertEquals("No corresponding geographic location could be found for the specified address.", unknownAddress.getDescription());

  assertEquals(603, unavailableAddress.getCode());
  assertNull(unavailableAddress.getDescription()); // null description for this status code

  assertEquals(604, unknownDirections.getCode());
  assertEquals("The GDirections object could not compute directions between the points mentioned in the query.", unknownDirections.getDescription());

  assertEquals(610, badKey.getCode());
  assertEquals("The given key is either invalid or does not match the domain for which it was given.", badKey.getDescription());

  assertEquals(620, tooManyQueries.getCode());
  assertEquals("The given key has gone over the requests limit in the 24 hour period.", tooManyQueries.getDescription());

  assertEquals(-1, unknownStatus.getCode());
  assertNull(unknownStatus.getDescription()); // null description for this status code
}

 // Test the getStatusCode method
 @Test
 public void testGetStatusCode() {
  int codeTest1 = 200;
  GeoStatusCode expectedStatusCode1 = GeoStatusCode.G_GEO_SUCCESS;
  GeoStatusCode actualStatusCode1 = GeoStatusCode.getStatusCode(codeTest1);
  assertEquals(expectedStatusCode1, actualStatusCode1);

  int codeTest2 = 500;
  GeoStatusCode expectedStatusCode2 = GeoStatusCode.G_GEO_SERVER_ERROR;
  GeoStatusCode actualStatusCode2 = GeoStatusCode.getStatusCode(codeTest2);
  assertEquals(expectedStatusCode2, actualStatusCode2);

  int codeTest3 = 605; // Invalid code
  GeoStatusCode expectedStatusCode3 = GeoStatusCode.G_GEO_UNKOWN_STATUS;
  GeoStatusCode actualStatusCode3 = GeoStatusCode.getStatusCode(codeTest3);
  assertEquals(expectedStatusCode3, actualStatusCode3);
}

}