package geo.google.datamodel.test;

import org.junit.Test;
import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Testing getStatusCode method with valid codes
 @Test
 public void testGetStatusCodeWithValidCodes() {
  int[] validCodes = {200, 400, 500, 601, 602, 603, 604, 610, 620};
  GeoStatusCode expectedStatusCode;
  for (int code : validCodes) {
   expectedStatusCode = GeoStatusCode.getStatusCode(code);
   assertEquals(expectedStatusCode.name(), GeoStatusCode.getStatusCode(code).name());
   assertEquals(expectedStatusCode.getCode(), GeoStatusCode.getStatusCode(code).getCode());
   assertEquals(expectedStatusCode.getDescription(), GeoStatusCode.getStatusCode(code).getDescription());
  }
 }

 // Testing getStatusCode method with invalid code
 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithInvalidCode() {
  GeoStatusCode.getStatusCode(-1);
 }

 // Testing getStatusCode method with null code
 @Test(expected = NullPointerException.class)
 public void testGetStatusCodeWithNullCode() {
  GeoStatusCode.getStatusCode(null);
 }

 // Testing getStatusCode method when code is not a number
 @Test(expected = NumberFormatException.class)
 public void testGetStatusCodeWithNonNumberCode() {
  GeoStatusCode.getStatusCode("notANumber");
 }

 // Testing getStatusCode method with code outside of valid range
 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithCodeOutsideOfValidRange() {
  GeoStatusCode.getStatusCode(1000);
 }

 // Testing getStatusCode method when enum values change
 @Test
 public void testGetStatusCodeWithEnumValuesChange() {
  GeoStatusCode[] oldValues = GeoStatusCode.values();
  GeoStatusCode newStatusCode = new GeoStatusCode(700, "New Status Code");
  GeoStatusCode[] newValues = GeoStatusCode.values();
  assertNotSame(oldValues, newValues);
  assertFalse(oldValues.contains(newStatusCode));
  int code = newStatusCode.getCode();
  GeoStatusCode expectedStatusCode = GeoStatusCode.getStatusCode(code);
  assertEquals(newStatusCode.getName(), expectedStatusCode.getName());
  assertEquals(newStatusCode.getCode(), expectedStatusCode.getCode());
 }

 // Testing enum constants
 @Test
 public void testEnumConstants() {
  GeoStatusCode[] geoStatusCodes = GeoStatusCode.values();
  for (GeoStatusCode statusCode : geoStatusCodes) {
   assertEquals(statusCode.getCode(), statusCode.ordinal() + 200);
   assertEquals(statusCode.name(), statusCode.toString());
  }
 }

 // Testing getDescription method with valid codes
 @Test
 public void testGetDescriptionWithValidCodes() {
  GeoStatusCode[] validCodes = GeoStatusCode.values();
  String expectedDescription;
  for (GeoStatusCode code : validCodes) {
   expectedDescription = code.getDescription();
   assertEquals(expectedDescription, GeoStatusCode.getStatusCode(code.getCode()).getDescription());
  }
 }

 // Testing getDescription method with invalid code
 @Test
 public void testGetDescriptionWithInvalidCode() {
  int invalidCode = 800;
  String expectedDescription = GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription();
  String actualDescription = GeoStatusCode.getStatusCode(invalidCode).getDescription();
  assertEquals(expectedDescription, actualDescription);
 }

}