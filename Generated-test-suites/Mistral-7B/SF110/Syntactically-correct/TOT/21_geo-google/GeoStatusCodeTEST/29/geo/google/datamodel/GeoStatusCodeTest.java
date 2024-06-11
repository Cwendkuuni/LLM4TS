import static org.junit.Assert.*;
import static geo.google.datamodel.GeoStatusCode.*;

import org.junit.Test;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode_ValidCodes() {
  for (GeoStatusCode statusCode : GeoStatusCode.values()) {
   int expectedCode = statusCode.getCode();
   String expectedDescription = statusCode.getDescription();

   int actualCode = GeoStatusCode.getStatusCode(expectedCode).getCode();
   String actualDescription = GeoStatusCode.getStatusCode(expectedCode).getDescription();

   assertEquals("Expected code " + expectedCode + " to map to " + statusCode + " but got " + GeoStatusCode.getStatusCode(expectedCode), statusCode, GeoStatusCode.getStatusCode(expectedCode));
   assertEquals("Expected code " + expectedCode + " to have description " + expectedDescription + " but got " + GeoStatusCode.getStatusCode(expectedCode).getDescription(), expectedDescription, GeoStatusCode.getStatusCode(expectedCode).getDescription());
   assertEquals("Expected code " + expectedCode + " to have expected code " + expectedCode + " but got " + actualCode, expectedCode, actualCode);
  }
 }

 @Test
 public void testGetStatusCode_InvalidCode() {
  int invalidCode = -1;
  GeoStatusCode statusCode = GeoStatusCode.getStatusCode(invalidCode);
  assertEquals("Expected getStatusCode(-1) to return UNKNOWN_STATUS but got " + statusCode, UNKNOWN_STATUS, statusCode);
 }
}