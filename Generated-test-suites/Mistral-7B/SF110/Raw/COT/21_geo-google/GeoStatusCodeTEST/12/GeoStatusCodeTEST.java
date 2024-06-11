package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Test all getter methods
 @Test
 public void testGetters() {
 GeoStatusCode status1 = GeoStatusCode.G_GEO_SUCCESS;
 GeoStatusCode status2 = GeoStatusCode.G_GEO_BAD_REQUEST;
 GeoStatusCode status3 = GeoStatusCode.G_GEO_SERVER_ERROR;
 GeoStatusCode status4 = GeoStatusCode.G_GEO_MISSING_QUERY;
 GeoStatusCode status5 = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 GeoStatusCode status6 = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 GeoStatusCode status7 = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 GeoStatusCode status8 = GeoStatusCode.G_GEO_BAD_KEY;
 GeoStatusCode status9 = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 GeoStatusCode status10 = GeoStatusCode.G_GEO_UNKOWN_STATUS;

 assertEquals(200, status1.getCode());
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", status1.getDescription());
 assertNotNull(status1.getCodeName());

 assertEquals(400, status2.getCode());
 assertEquals("A directions request could not be successfully parsed. (Since 2.81)", status2.getDescription());
 assertNotNull(status2.getCodeName());

 assertEquals(500, status3.getCode());
 assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", status3.getDescription());
 assertNotNull(status3.getCodeName());

 assertEquals(601, status4.getCode());
 assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", status4.getDescription());
 assertNotNull(status4.getCodeName());

 assertEquals(602, status5.getCode());
 assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", status5.getDescription());
 assertNotNull(status5.getCodeName());

 assertEquals(603, status6.getCode());
 assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", status6.getDescription());
 assertNotNull(status6.getCodeName());

 assertEquals(604, status7.getCode());
 assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", status7.getDescription());
 assertNotNull(status7.getCodeName());

 assertEquals(610, status8.getCode());
 assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", status8.getDescription());
 assertNotNull(status8.getCodeName());

 assertEquals(620, status9.getCode());
 assertEquals("The given key has gone over the requests limit in the 24 hour period.", status9.getDescription());
 assertNotNull(status9.getCodeName());

 assertEquals(-1, status10.getCode());
 assertEquals("Uknown Status", status10.getCodeName());
 assertNull(status10.getDescription());
 }

 // Test getStatusCode method with valid codes
 @Test
 public void testGetStatusCodeWithValidCodes() {
 for (GeoStatusCode status : GeoStatusCode.values()) {
 GeoStatusCode expectedStatus = status;
 int code = status.getCode();
 GeoStatusCode actualStatus = GeoStatusCode.getStatusCode(code);
 assertEquals(expectedStatus, actualStatus);
 }
 }

 // Test getStatusCode method with invalid code
 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithInvalidCode() {
 GeoStatusCode.getStatusCode(12345);
 }

}