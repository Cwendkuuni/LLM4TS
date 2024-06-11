package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

 // Test all getter methods of GeoStatusCode enum

 @Test
 public void testGetCode() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals(200, status.getCode());
 }

 @Test
 public void testGetCodeName() {
 GeoStatusCode status = GeoStatusCode.G_GEO_BAD_REQUEST;
 assertEquals("G_GEO_BAD_REQUEST", status.getCodeName());
 }

 @Test
 public void testGetDescription() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SERVER_ERROR;
 assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", status.getDescription());
 }

 // Test the getStatusCode method

 @Test
 public void testGetStatusCode_ValidCode() {
 int code = 200;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);
 }

 @Test
 public void testGetStatusCode_InvalidCode() {
 int code = 999;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 // Test edge cases and exception handling

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_NullCode() {
 int code = 0;
 GeoStatusCode.getStatusCode(code);
 }

 @Test
 public void testGetStatusCode_NegativeCode() {
 int code = -1;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testGetStatusCode_OutOfRangeCode() {
 int code = 621;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

}