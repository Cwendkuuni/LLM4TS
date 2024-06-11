package geo.google.datamodel.test;

import org.junit.Test;
import static org.junit.Assert.*;
import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCode_ValidCodes() {
 for (GeoStatusCode statusCode : GeoStatusCode.values()) {
 if (statusCode.getCode() > 0) {
 assertEquals("Unexpected description for status code " + statusCode.getCode(), statusCode.getDescription(), statusCode.getName());
 }
 }
 }

 @Test
 public void testGetStatusCode_ValidCode_ReturnsCorrectStatusCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
 assertEquals("Unexpected status code for HTTP status code 200", GeoStatusCode.G_GEO_SUCCESS, statusCode);
 }

 @Test
 public void testGetStatusCode_InvalidCode_ReturnsUnknownStatus() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(-1);
 assertEquals("Unexpected status code for invalid code", GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_NullCode_ThrowsIllegalArgumentException() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(null);
 fail("Unexpectedly did not receive an IllegalArgumentException when code was null");
 }
}