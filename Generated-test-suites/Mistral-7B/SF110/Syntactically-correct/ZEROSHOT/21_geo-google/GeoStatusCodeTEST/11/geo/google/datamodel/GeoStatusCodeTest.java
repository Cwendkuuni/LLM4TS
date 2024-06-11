package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

    @Test
    public void testGetStatusCode() {
        // Test valid codes
        assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
        assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());

        assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
        assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());

        assertEquals("G_GEO_SERVER_ERROR", GeoStatusCode.G_GEO_SERVER_ERROR.getCodeName());
        assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());

        assertEquals("G_GEO_MISSING_QUERY", GeoStatusCode.G_GEO_MISSING_QUERY.getCodeName());
        assertEquals(601, GeoStatusCode.G_GEO_MISSING_QUERY.getCode());

        assertEquals("G_GEO_UNKNOWN_ADDRESS", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCodeName());
        assertEquals(602, GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCode());

        assertEquals("G_GEO_UNAVAILABLE_ADDRESS", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCodeName());
        assertEquals(603, GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCode());

        assertEquals("G_GEO_UNKNOWN_DIRECTIONS", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCodeName());
        assertEquals(604, GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCode());

        assertEquals("G_GEO_BAD_KEY", GeoStatusCode.G_GEO_BAD_KEY.getCodeName());
        assertEquals(610, GeoStatusCode.G_GEO_BAD_KEY.getCode());

        assertEquals("G_GEO_TOO_MANY_QUERIES", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCodeName());
        assertEquals(620, GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCode());

        // Test invalid code
        assertNull(GeoStatusCode.getStatusCode(100));
    }

    @Test
    public void testGetStatusCodeWithInvalidCode() {
        assertNull(GeoStatusCode.getStatusCode(-1));
    }

    @Test
    public void testGetStatusCodeWithValidCode() {
        GeoStatusCode status = GeoStatusCode.getStatusCode(200);
        assertEquals("G_GEO_SUCCESS", status.getCodeName());
        assertEquals(200, status.getCode());
    }
}