package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Testing constructor, getters, and enum values
 @Test
 public void testGeoStatusCodeConstructorAndGetters() {
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
     assertEquals("G_GEO_SUCCESS", success.getCodeName());

     assertEquals(400, badRequest.getCode());
     assertEquals("A directions request could not be successfully parsed.", badRequest.getDescription());
     assertEquals("G_GEO_BAD_REQUEST", badRequest.getCodeName());

     assertEquals(500, serverError.getCode());
     assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.", serverError.getDescription());
     assertEquals("G_GEO_SERVER_ERROR", serverError.getCodeName());

     assertNull(missingQuery.getDescription());
     assertNull(unknownAddress.getDescription());
     assertNull(unavailableAddress.getDescription());
     assertNull(unknownDirections.getDescription());

     assertEquals(601, missingQuery.getCode());
     assertEquals("G_GEO_MISSING_QUERY", missingQuery.getCodeName());

     assertEquals(602, unknownAddress.getCode());
     assertEquals("G_GEO_UNKNOWN_ADDRESS", unknownAddress.getCodeName());

     assertEquals(603, unavailableAddress.getCode());
     assertEquals("G_GEO_UNAVAILABLE_ADDRESS", unavailableAddress.getCodeName());

     assertEquals(604, unknownDirections.getCode());
     assertEquals("G_GEO_UNKNOWN_DIRECTIONS", unknownDirections.getCodeName());

     assertEquals(610, badKey.getCode());
     assertEquals("G_GEO_BAD_KEY", badKey.getCodeName());

     assertEquals(620, tooManyQueries.getCode());
     assertEquals("G_GEO_TOO_MANY_QUERIES", tooManyQueries.getCodeName());

     assertEquals(-1, unknownStatus.getCode());
     assertEquals("Uknown Status", unknownStatus.getCodeName());
 }

 // Testing getStatusCode method
 @Test
 public void testGetStatusCode() {
     int successCode = 200;
     int badRequestCode = 400;
     int serverErrorCode = 500;
     int missingQueryCode = 601;
     int unknownAddressCode = 602;
     int unavailableAddressCode = 603;
     int unknownDirectionsCode = 604;
     int badKeyCode = 610;
     int tooManyQueriesCode = 620;

     GeoStatusCode status = GeoStatusCode.getStatusCode(successCode);
     assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);

     status = GeoStatusCode.getStatusCode(badRequestCode);
     assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, status);

     status = GeoStatusCode.getStatusCode(serverErrorCode);
     assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, status);

     status = GeoStatusCode.getStatusCode(missingQueryCode);
     assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, status);

     status = GeoStatusCode.getStatusCode(unknownAddressCode);
     assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, status);

     status = GeoStatusCode.getStatusCode(unavailableAddressCode);
     assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, status);

     status = GeoStatusCode.getStatusCode(unknownDirectionsCode);
     assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, status);

     status = GeoStatusCode.getStatusCode(badKeyCode);
     assertEquals(GeoStatusCode.G_GEO_BAD_KEY, status);

     status = GeoStatusCode.getStatusCode(tooManyQueriesCode);
     assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, status);

     status = GeoStatusCode.getStatusCode(-1); // Negative value testing
     assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 // Testing edge cases and exceptions
 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeNegativeCode() {
     GeoStatusCode.getStatusCode(-10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeNullCode() {
     GeoStatusCode.getStatusCode(null);
 }

 @Test
 public void testGetStatusCodeUnknownCode() {
     int unknownCode = 999;
     GeoStatusCode status = GeoStatusCode.getStatusCode(unknownCode);
     assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

}