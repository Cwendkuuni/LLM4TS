package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCodeEnumValues() {
 // Testing the enum values
 GeoStatusCode[] geoStatusCodes = GeoStatusCode.values();
 for (GeoStatusCode statusCode : geoStatusCodes) {
 System.out.println("Status code: " + statusCode.getCode() + ", Name: " + statusCode.getCodeName() + ", Description: " + statusCode.getDescription());
 assertNotNull(statusCode.getCodeName());
 assertNotNull(statusCode.getDescription());
 }
 }

 @Test
 public void testGetStatusCodeByCode() {
 // Testing the getStatusCode method
 for (GeoStatusCode statusCode : GeoStatusCode.values()) {
 int code = statusCode.getCode();
 GeoStatusCode expectedStatusCode = statusCode;
 GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertEquals("Expected and actual status codes should be the same", expectedStatusCode, actualStatusCode);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithInvalidCode() {
 // Testing the getStatusCode method with an invalid code
 int invalidCode = -1;
 GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(invalidCode);
 assertNull("No status code should be returned for an invalid code", actualStatusCode);
 }
}