package geo.google.datamodel.test;

import org.junit.Test;
import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCodeConstruction() {
 GeoStatusCode[] statusCodes = GeoStatusCode.values();
 for (GeoStatusCode statusCode : statusCodes) {
 assertNotNull(statusCode);
 assertEquals(statusCode.getCode(), statusCode.ordinal() + 200);
 }
 }

 @Test
 public void testGetStatusCode() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 int code = 601;
 assertSame(statusCode, GeoStatusCode.getStatusCode(code));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeWithInvalidCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(-1);
 fail("Unexpected null return");
 }

 @Test
 public void testGetStatusCodeWithUnknownCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(700);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
 }

 @Test
 public void testGetCodeName() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertEquals("G_GEO_MISSING_QUERY", statusCode.getCodeName());
 }

 @Test
 public void testGetDescription() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input.", statusCode.getDescription());
 }
}