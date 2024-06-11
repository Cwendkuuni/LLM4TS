package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
        assertNull(GeoStatusCode.getStatusCode(-1));
    }

    @Test
    public void testGetCodeName() {
        assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
        assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
        assertEquals("G_GEO_SERVER_ERROR", GeoStatusCode.G_GEO_SERVER_ERROR.getCodeName());
        assertEquals("G_GEO_MISSING_QUERY", GeoStatusCode.G_GEO_MISSING_QUERY.getCodeName());
        assertEquals("G_GEO_UNKNOWN_ADDRESS", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCodeName());
        assertEquals("G_GEO_UNAVAILABLE_ADDRESS", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCodeName());
        assertEquals("G_GEO_UNKNOWN_DIRECTIONS", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCodeName());
        assertEquals("G_GEO_BAD_KEY", GeoStatusCode.G_GEO_BAD_KEY.getCodeName());
        assertEquals("G_GEO_TOO_MANY_QUERIES", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCodeName());
        assertEquals("G_GEO_UNKOWN_STATUS", GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
        // ... Add other test cases for the description of each status code
    }
}