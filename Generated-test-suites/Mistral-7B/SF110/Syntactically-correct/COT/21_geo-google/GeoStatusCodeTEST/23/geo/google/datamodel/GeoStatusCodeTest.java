package geo.google.datamodel.test;

import org.junit.Assert;
import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 // Testing constructor with valid arguments
 @Test
 public void testValidConstructor() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 Assert.assertEquals(200, status.getCode());
 Assert.assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", status.getDescription());
 }

 // Testing getStatusCode method with valid codes
 @Test
 public void testGetStatusCodeWithValidCodes() {
 GeoStatusCode status200 = GeoStatusCode.G_GEO_SUCCESS;
 GeoStatusCode status400 = GeoStatusCode.G_GEO_BAD_REQUEST;
 GeoStatusCode status500 = GeoStatusCode.G_GEO_SERVER_ERROR;
 GeoStatusCode status601 = GeoStatusCode.G_GEO_MISSING_QUERY;
 GeoStatusCode status602 = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 GeoStatusCode status603 = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 GeoStatusCode status604 = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 GeoStatusCode status610 = GeoStatusCode.G_GEO_BAD_KEY;
 GeoStatusCode status620 = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 GeoStatusCode statusUnknown = GeoStatusCode.G_GEO_UNKOWN_STATUS;

 Assert.assertEquals(status200, GeoStatusCode.getStatusCode(200));
 Assert.assertEquals(status400, GeoStatusCode.getStatusCode(400));
 Assert.assertEquals(status500, GeoStatusCode.getStatusCode(500));
 Assert.assertEquals(status601, GeoStatusCode.getStatusCode(601));
 Assert.assertEquals(status602, GeoStatusCode.getStatusCode(602));
 Assert.assertEquals(status603, GeoStatusCode.getStatusCode(603));
 Assert.assertEquals(status604, GeoStatusCode.getStatusCode(604));
 Assert.assertEquals(status610, GeoStatusCode.getStatusCode(610));
 Assert.assertEquals(status620, GeoStatusCode.getStatusCode(620));
 Assert.assertEquals(statusUnknown, GeoStatusCode.getStatusCode(-1));
 }

 // Testing getStatusCode method with invalid codes
 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithInvalidCode() {
 GeoStatusCode.getStatusCode(100);
 }

 // Testing getStatusCode method with unknown status
 @Test
 public void testGetStatusCodeWithUnknownStatus() {
 GeoStatusCode status = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 Assert.assertEquals(-1, status.getCode());
 Assert.assertEquals("Unknown Status", status.getDescription());
 }
}