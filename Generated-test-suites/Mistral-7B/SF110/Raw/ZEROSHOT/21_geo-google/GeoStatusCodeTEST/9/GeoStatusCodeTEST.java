package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

/**
 * Test class for GeoStatusCode enum
 *
 * @author [Your Name]
 */
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
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals(601, status.getCode());
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals("G_GEO_SERVER_ERROR", status.getCodeName());
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", status.getDescription());
    }
}