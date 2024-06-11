import org.junit.Assert; import static org.junit.Assert.*; import geo.google.datamodel.GeoStatusCode.*;

public class GeoStatusCodeTest {

 @Test
 public void testGeoStatusCode_G_GEO_SUCCESS() {
 int code = 200;
 GeoStatusCode expected = G_GEO_SUCCESS;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(code);

 assertEquals(expected.getCode(), actual.getCode());
 assertEquals(expected.name(), actual.getCodeName());
 assertEquals(expected.getDescription(), actual.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_BAD_REQUEST() {
 int code = 400;
 GeoStatusCode expected = G_GEO_BAD_REQUEST;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(code);

 assertEquals(expected.getCode(), actual.getCode());
 assertEquals(expected.name(), actual.getCodeName());
 assertEquals(expected.getDescription(), actual.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_SERVER_ERROR() {
 int code = 500;
 GeoStatusCode expected = G_GEO_SERVER_ERROR;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(code);

 assertEquals(expected.getCode(), actual.getCode());
 assertEquals(expected.name(), actual.getCodeName());
 assertEquals(expected.getDescription(), actual.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_MISSING_QUERY() {
 int code = 601;
 GeoStatusCode expected = G_GEO_MISSING_QUERY;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(code);

 assertEquals(expected.getCode(), actual.getCode());
 assertEquals(expected.name(), actual.getCodeName());
 assertEquals(expected.getDescription(), actual.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_UNKNOWN_ADDRESS() {
 int code = 602;
 GeoStatusCode expected = G_GEO_UNKNOWN_ADDRESS;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(code);

 assertEquals(expected.getCode(), actual.getCode());
 assertEquals(expected.name(), actual.getCodeName());
 assertEquals(expected.getDescription(), actual.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_UNAVAILABLE_ADDRESS() {
 int code = 603;
 GeoStatusCode expected = G_GEO_UNAVAILABLE_ADDRESS;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(code);

 assertEquals(expected.getCode(), actual.getCode());
 assertEquals(expected.name(), actual.getCodeName());
 assertEquals(expected.getDescription(), actual.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_UNKNOWN_DIRECTIONS() {
 int code = 604;
 GeoStatusCode expected = G_GEO_UNKNOWN_DIRECTIONS;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(code);

 assertEquals(expected.getCode(), actual.getCode());
 assertEquals(expected.name(), actual.getCodeName());
 assertEquals(expected.getDescription(), actual.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_BAD_KEY() {
 int code = 610;
 GeoStatusCode expected = G_GEO_BAD_KEY;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(code);

 assertEquals(expected.getCode(), actual.getCode());
 assertEquals(expected.name(), actual.getCodeName());
 assertEquals(expected.getDescription(), actual.getDescription());
 }

 @Test
 public void testGeoStatusCode_G_GEO_TOO_MANY_QUERIES() {
 int code = 620;
 GeoStatusCode expected = G_GEO_TOO_MANY_QUERIES;
 GeoStatusCode actual = GeoStatusCode.getStatusCode(code);

 assertEquals(expected.getCode(), actual.getCode());
 assertEquals(expected.name(), actual.getCodeName());
 assertEquals(expected.getDescription(), actual.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGeoStatusCode_InvalidCode() {
 int code = -1;
 GeoStatusCode actual = null;
 try {
 actual = GeoStatusCode.getStatusCode(code);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }

 assertNull(actual);
 }
}