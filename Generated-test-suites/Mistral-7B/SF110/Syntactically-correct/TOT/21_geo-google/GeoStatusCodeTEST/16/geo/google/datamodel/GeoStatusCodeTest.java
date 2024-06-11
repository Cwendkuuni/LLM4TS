import static org.junit.Assert.*;
import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Test for Enum values with valid codes and descriptions
 @Test
 public void testValidValues() {
  assertNotNull(GeoStatusCode.G_GEO_SUCCESS);
  assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
  assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", GeoStatusCode.G_GEO_SUCCESS.getDescription());

  assertNotNull(GeoStatusCode.G_GEO_BAD_REQUEST);
  assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
  assertEquals("A directions request could not be successfully parsed.", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());

  // Add test cases for other valid Enum values here
 }

 // Test for Enum method getStatusCode()
 @Test
 public void testGetStatusCode() {
  assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
  assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
  // Add test cases for other Enum values and their corresponding codes here
 }

 // Test for Enum method getDescription()
 @Test
 public void testGetDescription() {
  assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", GeoStatusCode.G_GEO_SUCCESS.getDescription());
  assertEquals("A directions request could not be successfully parsed.", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
  // Add test cases for other Enum values and their descriptions here
 }

 // Test for Enum method getCode()
 @Test
 public void testGetCode() {
  assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
  assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
  // Add test cases for other Enum values and their codes here
 }

 // Test for Enum method name()
 @Test
 public void testGetStatusCodeByName() {
  assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCodeByName("G_GEO_SUCCESS"));
  assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCodeByName("G_GEO_BAD_REQUEST"));
  // Add test cases for other Enum values and their names here
 }

 // Test for Enum method getStatusCode() with invalid code
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCode() {
  GeoStatusCode.getStatusCode(-1);
 }
}