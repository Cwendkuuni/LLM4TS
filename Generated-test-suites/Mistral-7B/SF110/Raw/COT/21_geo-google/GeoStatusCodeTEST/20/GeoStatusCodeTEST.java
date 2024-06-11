package geo.google.datamodel.test;

import org.junit.Test;
import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Testing getStatusCode(int code) method
 @Test
 public void testGetStatusCode() {
  for (GeoStatusCode statusCode : GeoStatusCode.values()) {
   int code = statusCode.getCode();
   GeoStatusCode expectedStatusCode = statusCode;
   GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
   assertEquals(expectedStatusCode, actualStatusCode);
  }
 }

 // Testing getCode() method for all enum values
 @Test
 public void testGetCode() {
  for (GeoStatusCode statusCode : GeoStatusCode.values()) {
   int expectedCode = statusCode.getCode();
   int actualCode = statusCode.getCode();
   assertEquals(expectedCode, actualCode);
  }
 }

 // Testing getCodeName() method for all enum values
 @Test
 public void testGetCodeName() {
  for (GeoStatusCode statusCode : GeoStatusCode.values()) {
   String expectedCodeName = statusCode.name();
   String actualCodeName = statusCode.getCodeName();
   assertEquals(expectedCodeName, actualCodeName);
  }
 }

 // Testing getCodeName() method for unknown status code
 @Test(expected = IllegalArgumentException.class)
 public void testGetCodeNameForUnknownStatusCode() {
  int unknownCode = -1;
  GeoStatusCode.getStatusCode(unknownCode);
 }

 // Testing getDescription() method for all enum values
 @Test
 public void testGetDescription() {
  for (GeoStatusCode statusCode : GeoStatusCode.values()) {
   String expectedDescription = statusCode.getDescription();
   String actualDescription = statusCode.getDescription();
   assertEquals(expectedDescription, actualDescription);
  }
 }

 // Testing getDescription() method for unknown status code
 @Test(expected = IllegalArgumentException.class)
 public void testGetDescriptionForUnknownStatusCode() {
  int unknownCode = -1;
  GeoStatusCode statusCode = GeoStatusCode.getStatusCode(unknownCode);
  String unknownDescription = statusCode.getDescription();
 }

 // Testing edge cases for getStatusCode(int code) method
 @Test
 public void testGetStatusCodeEdgeCases() {
  int code = 255; // Below minimum valid code
  GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
  GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
  assertEquals(expectedStatusCode, actualStatusCode);

  code = 201; // Valid code but not defined in the enum
  expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
  actualStatusCode = GeoStatusCode.getStatusCode(code);
  assertEquals(expectedStatusCode, actualStatusCode);

  code = 699; // Above maximum valid code
  expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
  actualStatusCode = GeoStatusCode.getStatusCode(code);
  assertEquals(expectedStatusCode, actualStatusCode);
 }

 // Testing exception handling for getStatusCode(int code) method with invalid code
 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeInvalidCode() {
  int invalidCode = -1;
  GeoStatusCode.getStatusCode(invalidCode);
 }

}