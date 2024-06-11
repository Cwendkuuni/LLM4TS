package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

 // Test all enum methods

 @Test
 public void testGetCode() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 int expected = 200;
 int actual = status.getCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetCodeName() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 String expected = "G_GEO_SUCCESS";
 String actual = status.getCodeName();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDescription() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 String expected = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
 String actual = status.getDescription();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode() {
 int givenCode = 200;
 GeoStatusCode expected = GeoStatusCode.G_GEO_SUCCESS;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(givenCode);
 assertEquals(expected, actual);
 }

 // Test edge cases and exceptions

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithInvalidCode() {
 int givenCode = -1;
 GeoStatusCode.getStatusCode(givenCode);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetStatusCodeWithUnknownStatusCode() {
 int givenCode = 999;
 GeoStatusCode.getStatusCode(givenCode);
 }

 @Test
 public void testGetStatusCodeWithUnknownStatusName() {
 String givenStatusName = "UnknownStatusX";
 GeoStatusCode expected = null;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(Integer.parseInt(givenStatusName.substring(7)));
 assertEquals(expected, actual);
 }

}