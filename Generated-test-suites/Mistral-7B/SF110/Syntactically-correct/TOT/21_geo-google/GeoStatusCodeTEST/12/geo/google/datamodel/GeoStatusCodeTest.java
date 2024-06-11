import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 @Test
 public void testG_GEO_SUCCESS() {
 GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals(200, status.getCode());
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", status.getDescription());
 }

 @Test
 public void testG_GEO_BAD_REQUEST_invalidCode() {
 int code = 999;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testG_GEO_BAD_REQUEST_validCode() {
 int code = 400;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, status);
 }

 @Test
 public void testG_GEO_SERVER_ERROR_invalidCode() {
 int code = 999;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testG_GEO_SERVER_ERROR_validCode() {
 int code = 500;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, status);
 }

 @Test
 public void testG_GEO_MISSING_QUERY_invalidCode() {
 int code = 999;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testG_GEO_MISSING_QUERY_validCode() {
 int code = 601;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, status);
 }

 @Test
 public void testG_GEO_UNKNOWN_ADDRESS_invalidCode() {
 int code = 999;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testG_GEO_UNKNOWN_ADDRESS_validCode() {
 int code = 602;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, status);
 }

 @Test
 public void testG_GEO_UNAVAILABLE_ADDRESS_invalidCode() {
 int code = 999;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testG_GEO_UNAVAILABLE_ADDRESS_validCode() {
 int code = 603;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, status);
 }

 @Test
 public void testG_GEO_UNKNOWN_DIRECTIONS_invalidCode() {
 int code = 999;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testG_GEO_UNKNOWN_DIRECTIONS_validCode() {
 int code = 604;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, status);
 }

 @Test
 public void testG_GEO_BAD_KEY_invalidCode() {
 int code = 999;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testG_GEO_BAD_KEY_validCode() {
 int code = 610;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_BAD_KEY, status);
 }

 @Test
 public void testG_GEO_TOO_MANY_QUERIES_invalidCode() {
 int code = 999;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
 }

 @Test
 public void testG_GEO_TOO_MANY_QUERIES_validCode() {
 int code = 620;
 GeoStatusCode status = GeoStatusCode.getStatusCode(code);
 assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, status);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testG_GEO_UNKOWN_STATUS_invalidEnumValue() {
 GeoStatusCode status = GeoStatusCode.values()[-1];
 }

}