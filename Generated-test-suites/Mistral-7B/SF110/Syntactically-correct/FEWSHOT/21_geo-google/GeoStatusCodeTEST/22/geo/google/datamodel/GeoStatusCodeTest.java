package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GeoStatusCodeTest {

 @Test
 public void testAllGeoStatusCodes() {
 GeoStatusCode[] geoStatusCodes = GeoStatusCode.values();
 for (GeoStatusCode geoStatusCode : geoStatusCodes) {
 System.out.println("Code: " + geoStatusCode.getCode() + ", Name: " + geoStatusCode.getCodeName() + ", Description: " + geoStatusCode.getDescription());
 assertEquals(geoStatusCode.getCode(), geoStatusCode.getCode()); // Checking for equality of code and codeName for all status codes
 }
 }

 @Test
 public void testGetStatusCode() {
 int code = 200;
 GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
 GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);

 code = 400;
 expectedStatusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
 actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);

 code = 500;
 expectedStatusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
 actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);

 code = 601;
 expectedStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);

 code = 602;
 expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);

 code = 603;
 expectedStatusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);

 code = 604;
 expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);

 code = 610;
 expectedStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
 actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);

 code = 620;
 expectedStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);

 code = -1;
 expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertNotNull(actualStatusCode);
 assertEquals(expectedStatusCode, actualStatusCode);
 }
}