package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Test all methods in the GeoStatusCode enum

 @Test
 public void testGetStatusCode() {
 // Test valid cases
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
 assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
 // Test edge cases and exceptions
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-1));
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(255));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithInvalidCode() {
 GeoStatusCode.getStatusCode(Integer.MAX_VALUE);
 }

 @Test
 public void testGetCode() {
 // Test valid cases
 assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
 // Test edge cases and exceptions
 assertNull(GeoStatusCode.G_GEO_UNKOWN_STATUS.getCode());
 }

 @Test
 public void testGetCodeName() {
 // Test valid cases
 assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
 // Test edge cases and exceptions
 assertEquals("G_GEO_UNKOWN_STATUS", GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
 }

 @Test
 public void testGetDescription() {
 // Test valid cases
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());
 // Test edge cases and exceptions
 assertNull(GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
 }

 @Test
 public void testGetStatusCodeByName() {
 // You may need to add test cases here for all enum constants based on your requirements
 GeoStatusCode status = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertEquals(601, status.getCode());
 }

}