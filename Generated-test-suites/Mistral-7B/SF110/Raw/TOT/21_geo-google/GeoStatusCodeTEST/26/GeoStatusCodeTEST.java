import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode_ValidCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(200);
 assertEquals(GeoStatusCode.G_GEO_SUCCESS, statusCode);
 }

 @Test
 public void testGetStatusCode_InvalidCode_Default() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(-1);
 assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
 }

 @Test
 public void testGetStatusCode_ValidCode_Name() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
 assertEquals("G_GEO_SUCCESS", statusCode.getCodeName());
 }

 @Test
 public void testGetStatusCode_InvalidCode_Name() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 assertNull(statusCode.getCodeName());
 }

 @Test
 public void testGetStatusCode_ValidCode_Description() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
 assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect.", statusCode.getDescription());
 }

 @Test
 public void testGetStatusCode_InvalidCode_Description() {
 GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
 assertNull(statusCode.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetStatusCode_NegativeCode() {
 GeoStatusCode.getStatusCode(-5);
 }
}