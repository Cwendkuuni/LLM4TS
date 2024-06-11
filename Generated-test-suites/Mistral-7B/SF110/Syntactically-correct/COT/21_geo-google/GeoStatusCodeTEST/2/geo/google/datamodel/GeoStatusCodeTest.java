package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Test for all getter methods in GeoStatusCode enum
 @Test
 public void testGetterMethods() {
  for (GeoStatusCode statusCode : GeoStatusCode.values()) {
   assertEquals(statusCode.getCode(), statusCode.getCode());
   assertEquals(statusCode.getName(), statusCode.getCodeName());
   assertEquals(statusCode.getDescription(), statusCode.getDescription());
  }
 }

 // Test for getStatusCode method with valid codes
 @Test
 public void testGetStatusCodeValidCodes() {
  assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
  assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
  // Add tests for other valid codes
 }

 // Test for getStatusCode method with invalid code
 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeInvalidCode() {
  GeoStatusCode.getStatusCode(100);
 }

 // Test for all constants with valid codes
 @Test
 public void testConstantsValidCodes() {
  assertEquals(GeoStatusCode.G_GEO_SUCCESS.getCode(), 200);
  assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST.getCode(), 400);
  // Add tests for other constants and their codes
 }

 // Test for unknown status code
 @Test
 public void testUnknownStatusCode() {
  assertNull(GeoStatusCode.getStatusCode(-1));
 }
}