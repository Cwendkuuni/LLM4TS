import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode() {
 // Test valid codes
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
 assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
 assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
 assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
 assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
 assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
 assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
 assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));

 // Test invalid code
 assertNull(GeoStatusCode.getStatusCode(-1));
 }

 @Test
 public void testGetDescription() {
 // Test valid codes
 assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", GeoStatusCode.G_GEO_SUCCESS.getDescription());
 assertEquals("A directions request could not be successfully parsed.", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
 // ... and so on for other status codes

 // Test unknown status code
 assertNull(GeoStatusCode.G_UNKOWN_STATUS.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeNegativeCode() {
 GeoStatusCode.getStatusCode(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCodeNullCode() {
 GeoStatusCode.getStatusCode(null);
 }
}