package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCodeSuccess() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", statusCode.getDescription());
 assertEquals(200, statusCode.getCode());
 }

 @Test
 public void testGetStatusCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", statusCode.getDescription());
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeInvalidCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(100);
 fail("Unexpected result. expected an IllegalArgumentException");
 }

 @Test
 public void testGeoStatusCodeMisssingQuery() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertEquals("The HTTP q parameter was either missing or had no value.", statusCode.getDescription());
 assertEquals(601, statusCode.getCode());
 }

 @Test
 public void testGetStatusCodeUnknownStatus() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 assertEquals("Uknown Status", statusCode.getDescription());
 assertEquals(-1, statusCode.getCode());
 }

 @Test
 public void testGetStatusCodeUnknownStatusCode() {
 int code = 999;
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
 }
}