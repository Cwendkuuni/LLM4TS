package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Test all methods in GeoStatusCode class

 // Test constructor
 @Test
 public void testGeoStatusCodeConstructor() {
 GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
 assertNotNull(geoStatusCode);
 }

 // Test getCode method
 @Test
 public void testGetCode() {
 GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
 int expectedCode = 200;
 int actualCode = geoStatusCode.getCode();
 assertEquals(expectedCode, actualCode);
 }

 // Test getCodeName method
 @Test
 public void testGetCodeName() {
 GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
 String expectedCodeName = "G_GEO_SUCCESS";
 String actualCodeName = geoStatusCode.getCodeName();
 assertEquals(expectedCodeName, actualCodeName);
 }

 // Test getDescription method
 @Test
 public void testGetDescription() {
 GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
 String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
 String actualDescription = geoStatusCode.getDescription();
 assertEquals(expectedDescription, actualDescription);
 }

 // Test getStatusCode method
 @Test
 public void testGetStatusCode() {
 int code = 200;
 GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
 GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(expectedStatusCode, actualStatusCode);
 }

 // Test getStatusCode with unknown code
 @Test (expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithUnknownCode() {
 int code = 100;
 GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
 }

 // Test edge cases and exceptions
 @Test
 public void testEdgeCasesAndExceptions() {
 // Test negative code value
 int code = -10;
 GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(expectedStatusCode, actualStatusCode);

 // Test code value larger than the maximum value
 int maxCode = 629;
 GeoStatusCode expectedStatusCodeMax = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 GeoStatusCode actualStatusCodeMax = GeoStatusCode.getStatusCode(maxCode);
 assertEquals(expectedStatusCodeMax, actualStatusCodeMax);
 }

}