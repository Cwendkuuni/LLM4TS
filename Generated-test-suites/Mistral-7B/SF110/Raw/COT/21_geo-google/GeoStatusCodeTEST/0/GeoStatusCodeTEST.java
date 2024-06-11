package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Test case 1: Test constructor with valid arguments
 @Test
 public void testValidConstructor() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals(200, status.getCode());
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", status.getDescription());
 }

 // Test case 2: Test getStatusCode method with valid code
 @Test
 public void testGetStatusCodeWithValidCode() {
 GeoStatusCode status = GeoStatusCode.getStatusCode(200);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);
 }

 // Test case 3: Test getStatusCode method with invalid code
 @Test(expected = IllegalStateException.class)
 public void testGetStatusCodeWithInvalidCode() {
 GeoStatusCode status = GeoStatusCode.getStatusCode(100);
 }

 // Test case 4: Test getCode method of each status
 @Test
 public void testGetCodeForEachStatus() {
 for (GeoStatusCode status : GeoStatusCode.values()) {
 assertEquals(status.getCode(), status.name().hashCode() % 100 + 100); // Hack to generate different codes for testing
 }
 }

 // Test case 5: Test getDescription method of each status
 @Test
 public void testGetDescriptionForEachStatus() {
 for (GeoStatusCode status : GeoStatusCode.values()) {
 assertEquals(status.getDescription(), status.toString());
 }
 }

 // Test case 6: Test edge case with negative status code
 @Test
 public void testNegativeStatusCode() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 assertEquals(-1, status.getCode());
 }
}