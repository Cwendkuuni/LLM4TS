import org.junit.Assert;import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 private GeoStatusCode statusCode;

 @Test
 public void testGetStatusCode() {
  // Test for known status codes
  statusCode = GeoStatusCode.G_GEO_SUCCESS;
  Assert.assertEquals("Expected G_GEO_SUCCESS with code 200", 200, statusCode.getCode());

  statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
  Assert.assertEquals("Expected G_GEO_BAD_REQUEST with code 400", 400, statusCode.getCode());

  statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
  Assert.assertEquals("Expected G_GEO_SERVER_ERROR with code 500", 500, statusCode.getCode());

  statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
  Assert.assertEquals("Expected G_GEO_MISSING_QUERY with code 601", 601, statusCode.getCode());

  statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
  Assert.assertEquals("Expected G_GEO_UNKNOWN_ADDRESS with code 602", 602, statusCode.getCode());

  statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
  Assert.assertEquals("Expected G_GEO_UNAVAILABLE_ADDRESS with code 603", 603, statusCode.getCode());

  statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
  Assert.assertEquals("Expected G_GEO_UNKNOWN_DIRECTIONS with code 604", 604, statusCode.getCode());

  statusCode = GeoStatusCode.G_GEO_BAD_KEY;
  Assert.assertEquals("Expected G_GEO_BAD_KEY with code 610", 610, statusCode.getCode());

  statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
  Assert.assertEquals("Expected G_GEO_TOO_MANY_QUERIES with code 620", 620, statusCode.getCode());

  // Test for unknown status code
  statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
  Assert.assertEquals("Expected G_GEO_UNKOWN_STATUS with code -1", -1, statusCode.getCode());
 }

 @Test
 public void testGetStatusCode_InvalidCode() {
  try {
   statusCode = GeoStatusCode.getStatusCode(Integer.MIN_VALUE);
   fail("Expecting an exception with the given code");
  } catch (IllegalArgumentException e) {
   String expectedMessage = String.format("No status code with code %d", Integer.MIN_VALUE);
   String actualMessage = e.getMessage();
   assertEquals(expectedMessage, actualMessage);
  }
 }

 @Test
 public void testGetStatusCode_NullCode() {
  try {
   statusCode = GeoStatusCode.getStatusCode(null);
   fail("Expecting an exception with the given code");
  } catch (NullPointerException e) {
   String expectedMessage = "java.lang.NullPointerException";
   String actualMessage = e.getMessage();
   assertEquals(expectedMessage, actualMessage);
  }
 }

 @Test
 public void testGetStatusCodeName() {
  statusCode = GeoStatusCode.G_GEO_SUCCESS;
  String expectedName = "G_GEO_SUCCESS";
  String actualName = statusCode.getCodeName();
  assertEquals("Expected G_GEO_SUCCESS with name G_GEO_SUCCESS", expectedName, actualName);

  statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
  expectedName = "G_GEO_BAD_REQUEST";
  actualName = statusCode.getCodeName();
  assertEquals("Expected G_GEO_BAD_REQUEST with name G_GEO_BAD_REQUEST", expectedName, actualName);

  statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
  expectedName = "G_GEO_SERVER_ERROR";
  actualName = statusCode.getCodeName();
  assertEquals("Expected G_GEO_SERVER_ERROR with name G_GEO_SERVER_ERROR", expectedName, actualName);
 }

 @Test
 public void testGetStatusCodeName_UnknownStatusCode() {
  statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
  String expectedName = "G_GEO_UNKOWN_STATUS";
  String actualName = statusCode.getCodeName();
  assertEquals("Expected G_GEO_UNKOWN_STATUS with name G_GEO_UNKOWN_STATUS", expectedName, actualName);
 }

 @Test
 public void testGetStatusCodeDescription() {
  statusCode = GeoStatusCode.G_GEO_SUCCESS;
  String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
  String actualDescription = statusCode.getDescription();
  assertEquals("Expected G_GEO_SUCCESS with description No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", expectedDescription, actualDescription);

  statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
  expectedDescription = "A directions request could not be successfully parsed. (Since 2.81)";
  actualDescription = statusCode.getDescription();
  assertEquals("Expected G_GEO_BAD_REQUEST with description A directions request could not be successfully parsed. (Since 2.81)", expectedDescription, actualDescription);

  statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
  expectedDescription = "A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)";
  actualDescription = statusCode.getDescription();
  assertEquals("Expected G_GEO_SERVER_ERROR with description A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", expectedDescription, actualDescription);
 }

 @Test
 public void testGetStatusCodeDescription_UnknownStatusCode() {
  statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
  String expectedDescription = "Uknown Status";
  String actualDescription = statusCode.getDescription();
  assertEquals("Expected G_GEO_UNKOWN_STATUS with description Uknown Status", expectedDescription, actualDescription);
 }
}