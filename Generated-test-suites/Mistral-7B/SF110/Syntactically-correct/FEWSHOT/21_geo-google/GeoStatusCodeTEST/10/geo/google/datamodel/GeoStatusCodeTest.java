package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGeoStatusCode_Values() {
        assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
        assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());

        assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
        assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());

        // ... Add tests for other values
    }

    @Test
    public void testGetStatusCode() {
        // Test valid codes
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
        // ... Add tests for other values

        // Test invalid code
        assertNull(GeoStatusCode.getStatusCode(100));
    }

    @Test
    public void testGetStatusCode_NegativeCode() {
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(-1));
    }
}