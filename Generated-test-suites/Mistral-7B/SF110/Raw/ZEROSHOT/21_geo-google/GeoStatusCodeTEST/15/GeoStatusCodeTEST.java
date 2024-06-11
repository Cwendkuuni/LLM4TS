package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCodeByCode() {
  for (GeoStatusCode statusCode : GeoStatusCode.values()) {
   int code = statusCode.getCode();
   GeoStatusCode expectedStatusCode = statusCode;
   GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
   assertEquals(expectedStatusCode, actualStatusCode);
  }

  // Test unknown status code
  int unknownCode = -1;
  GeoStatusCode unknownStatusCode = GeoStatusCode.getStatusCode(unknownCode);
  assertNull(unknownStatusCode);
 }

 @Test
 public void testGetDescriptionByCode() {
  for (GeoStatusCode statusCode : GeoStatusCode.values()) {
   int code = statusCode.getCode();
   String expectedDescription = statusCode.getDescription();
   String actualDescription = GeoStatusCode.getStatusCode(code).getDescription();
   assertEquals(expectedDescription, actualDescription);
  }
 }

 @Test(expected = NullPointerException.class)
 public void testGetStatusCodeByNullCode() {
  int nullCode = null;
  GeoStatusCode.getStatusCode(nullCode);
 }
}