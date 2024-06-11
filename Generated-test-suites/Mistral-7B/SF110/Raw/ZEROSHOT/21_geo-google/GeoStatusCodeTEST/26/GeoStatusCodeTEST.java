package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCodeValues() {
  assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
  assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
  assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());

  // Test other values similarly...

  assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
  assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
  assertEquals("A directions request could not be successfully parsed. (Since 2.81)", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());

  // ... and so on for all other values in GeoStatusCode enum
 }

 @Test
 public void testGetStatusCode() {
  assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
  assertNull(GeoStatusCode.getStatusCode(199)); // Invalid code
 }
}