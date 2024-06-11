import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode() {
 // Test valid status codes
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
 assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
 assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
 assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
 assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
 assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
 assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));

 // Test invalid status code
 assertNull(GeoStatusCode.getStatusCode(-1));
 }

 @Test
 public void testGetCodeName() {
 // Test valid status codes
 assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
 assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
 assertEquals("G_GEO_SERVER_ERROR", GeoStatusCode.G_GEO_SERVER_ERROR.getCodeName());
 assertEquals("G_GEO_MISSING_QUERY", GeoStatusCode.G_GEO_MISSING_QUERY.getCodeName());
 assertEquals("G_GEO_UNKNOWN_ADDRESS", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCodeName());
 assertEquals("G_GEO_UNAVAILABLE_ADDRESS", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCodeName());
 assertEquals("G_GEO_UNKNOWN_DIRECTIONS", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCodeName());
 assertEquals("G_GEO_BAD_KEY", GeoStatusCode.G_GEO_BAD_KEY.getCodeName());
 assertEquals("G_GEO_TOO_MANY_QUERIES", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCodeName());

 // Test unknown status code
 assertNull(GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
 }

 @Test
 public void testGetDescription() {
 // Test valid status codes
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());
 assertEquals("A directions request could not be successfully parsed. (Since 2.81)", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
 assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());
 // ... (include descriptions for all status codes)

 // Test unknown status code
 assertNull(GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
 }
}