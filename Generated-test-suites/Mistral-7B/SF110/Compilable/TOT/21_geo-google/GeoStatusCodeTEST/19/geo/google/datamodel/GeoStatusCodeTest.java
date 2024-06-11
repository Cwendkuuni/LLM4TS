package geo.google.datamodel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoStatusCodeTest {

 @Test
 public void testGetStatusCode() {
 // Test valid codes
 assertEquals("No errors occurred", GeoStatusCode.G_GEO_SUCCESS.getDescription());
 assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());

 // Test unknown code
 assertNull(GeoStatusCode.getStatusCode(-1));

 // Test known codes
 assertEquals("A geocoding or directions request could not be successfully processed", GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());
 assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());

 // Test edge cases
 assertEquals("The HTTP q parameter was either missing or had no value", GeoStatusCode.G_GEO_MISSING_QUERY.getDescription());
 assertEquals(601, GeoStatusCode.G_GEO_MISSING_QUERY.getCode());
 }

 @Test
 public void testGetStatusCodeByName() {
 // Test valid enum names
 assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
 assertEquals(Integer.toString(200), GeoStatusCode.G_GEO_SUCCESS.name());

 // Test unknown enum name
 assertNull(GeoStatusCode.valueOf("unknownStatus"));
 }
}