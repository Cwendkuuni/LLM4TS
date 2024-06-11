package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Test for valid GeoStatusCode values
 @Test
 public void testValidStatusCode() {
  assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
  assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", GeoStatusCode.G_GEO_SUCCESS.getDescription());

  assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
  assertEquals("A directions request could not be successfully parsed.", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());

  assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());
  assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.", GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());

  // Other valid status codes can be similarly tested
 }

 // Test for invalid code
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidStatusCode() {
  GeoStatusCode.getStatusCode(-1);
 }

 // Test for null code
 @Test(expected = NullPointerException.class)
 public void testNullCode() {
  GeoStatusCode statusCode = null;
  assertNull(statusCode);
 }

 // Test for null description
 @Test(expected = NullPointerException.class)
 public void testNullDescription() {
  GeoStatusCode statusCode = new GeoStatusCode(-1, null);
  assertNull(statusCode.getDescription());
 }

 // Test for unknown status code
 @Test
 public void testUnknownStatusCode() {
  GeoStatusCode statusCode = GeoStatusCode.getStatusCode(-1);
  assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
 }
}