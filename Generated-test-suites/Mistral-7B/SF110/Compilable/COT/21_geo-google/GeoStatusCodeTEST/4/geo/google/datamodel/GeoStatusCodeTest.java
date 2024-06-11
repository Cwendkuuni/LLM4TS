package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

 private GeoStatusCode getStatusCode(int code) {
 return GeoStatusCode.getStatusCode(code);
 }

 @Test
 public void testGetStatusCode_G_GEO_SUCCESS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_SUCCESS;
 int code = 200;
 GeoStatusCode actual = getStatusCode(code);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode_G_GEO_BAD_REQUEST() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_BAD_REQUEST;
 int code = 400;
 GeoStatusCode actual = getStatusCode(code);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode_G_GEO_SERVER_ERROR() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_SERVER_ERROR;
 int code = 500;
 GeoStatusCode actual = getStatusCode(code);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode_G_GEO_MISSING_QUERY() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_MISSING_QUERY;
 int code = 601;
 GeoStatusCode actual = getStatusCode(code);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode_G_GEO_UNKNOWN_ADDRESS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 int code = 602;
 GeoStatusCode actual = getStatusCode(code);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode_G_GEO_UNAVAILABLE_ADDRESS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 int code = 603;
 GeoStatusCode actual = getStatusCode(code);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode_G_GEO_UNKNOWN_DIRECTIONS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 int code = 604;
 GeoStatusCode actual = getStatusCode(code);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode_G_GEO_BAD_KEY() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_BAD_KEY;
 int code = 610;
 GeoStatusCode actual = getStatusCode(code);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetStatusCode_G_GEO_TOO_MANY_QUERIES() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 int code = 620;
 GeoStatusCode actual = getStatusCode(code);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_InvalidCode() {
 int code = -1;
 GeoStatusCode actual = getStatusCode(code);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testGetCodeName_G_GEO_SUCCESS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_SUCCESS;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_SUCCESS", codeName);
 }

 @Test
 public void testGetCodeName_G_GEO_BAD_REQUEST() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_BAD_REQUEST;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_BAD_REQUEST", codeName);
 }

 @Test
 public void testGetCodeName_G_GEO_SERVER_ERROR() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_SERVER_ERROR;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_SERVER_ERROR", codeName);
 }

 @Test
 public void testGetCodeName_G_GEO_MISSING_QUERY() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_MISSING_QUERY;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_MISSING_QUERY", codeName);
 }

 @Test
 public void testGetCodeName_G_GEO_UNKNOWN_ADDRESS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_UNKNOWN_ADDRESS", codeName);
 }

 @Test
 public void testGetCodeName_G_GEO_UNAVAILABLE_ADDRESS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_UNAVAILABLE_ADDRESS", codeName);
 }

 @Test
 public void testGetCodeName_G_GEO_UNKNOWN_DIRECTIONS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_UNKNOWN_DIRECTIONS", codeName);
 }

 @Test
 public void testGetCodeName_G_GEO_BAD_KEY() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_BAD_KEY;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_BAD_KEY", codeName);
 }

 @Test
 public void testGetCodeName_G_GEO_TOO_MANY_QUERIES() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_TOO_MANY_QUERIES", codeName);
 }

 @Test
 public void testGetCodeName_G_GEO_UNKOWN_STATUS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 String codeName = expected.getCodeName();
 assertEquals("G_GEO_UNKOWN_STATUS", codeName);
 }

 @Test
 public void testGetDescription_G_GEO_SUCCESS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_SUCCESS;
 String description = expected.getDescription();
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", description);
 }

 @Test
 public void testGetDescription_G_GEO_BAD_REQUEST() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_BAD_REQUEST;
 String description = expected.getDescription();
 assertEquals("A directions request could not be successfully parsed. (Since 2.81)", description);
 }

 @Test
 public void testGetDescription_G_GEO_SERVER_ERROR() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_SERVER_ERROR;
 String description = expected.getDescription();
 assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", description);
 }

 @Test
 public void testGetDescription_G_GEO_MISSING_QUERY() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_MISSING_QUERY;
 String description = expected.getDescription();
 assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", description);
 }

 @Test
 public void testGetDescription_G_GEO_UNKNOWN_ADDRESS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
 String description = expected.getDescription();
 assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", description);
 }

 @Test
 public void testGetDescription_G_GEO_UNAVAILABLE_ADDRESS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
 String description = expected.getDescription();
 assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", description);
 }

 @Test
 public void testGetDescription_G_GEO_UNKNOWN_DIRECTIONS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
 String description = expected.getDescription();
 assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", description);
 }

 @Test
 public void testGetDescription_G_GEO_BAD_KEY() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_BAD_KEY;
 String description = expected.getDescription();
 assertEquals("The given key is either invalid or does not match the domain for which it was given. (Since 2.55)", description);
 }

 @Test
 public void testGetDescription_G_GEO_TOO_MANY_QUERIES() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
 String description = expected.getDescription();
 assertEquals("The given key has gone over the requests limit in the 24 hour period. ", description);
 }

 @Test
 public void testGetDescription_G_GEO_UNKOWN_STATUS() {
 GeoStatusCode expected = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 String description = expected.getDescription();
 assertEquals("Uknown Status", description);
 }
}