package geo.google.datamodel;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGeoStatusCodeValues() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertNotNull(statusCode);
            assertEquals(statusCode.name(), statusCode.getCodeName());
        }
    }

    @Test
    public void testGetStatusCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        int code = statusCode.getCode();
        assertEquals("G_GEO_SUCCESS", statusCode.getCodeName());
        assertEquals(200, code);

        statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        code = statusCode.getCode();
        assertEquals("G_GEO_BAD_REQUEST", statusCode.getCodeName());
        assertEquals(400, code);

        statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        code = statusCode.getCode();
        assertEquals("G_GEO_SERVER_ERROR", statusCode.getCodeName());
        assertEquals(500, code);

        statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        code = statusCode.getCode();
        assertEquals("G_GEO_MISSING_QUERY", statusCode.getCodeName());
        assertEquals(601, code);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        code = statusCode.getCode();
        assertEquals("G_GEO_UNKNOWN_ADDRESS", statusCode.getCodeName());
        assertEquals(602, code);

        statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        code = statusCode.getCode();
        assertEquals("G_GEO_UNAVAILABLE_ADDRESS", statusCode.getCodeName());
        assertEquals(603, code);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        code = statusCode.getCode();
        assertEquals("G_GEO_UNKNOWN_DIRECTIONS", statusCode.getCodeName());
        assertEquals(604, code);

        statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        code = statusCode.getCode();
        assertEquals("G_GEO_BAD_KEY", statusCode.getCodeName());
        assertEquals(610, code);

        statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        code = statusCode.getCode();
        assertEquals("G_GEO_TOO_MANY_QUERIES", statusCode.getCodeName());
        assertEquals(620, code);

        statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        code = statusCode.getCode();
        assertEquals("G_GEO_UNKOWN_STATUS", statusCode.getCodeName());
        assertEquals(-1, code);
    }

    @Test
    public void testGetStatusCodeWithInvalidCode() {
        int invalidCode = 1234;
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(invalidCode);
        assertNotNull(statusCode);
        assertEquals("G_GEO_UNKOWN_STATUS", statusCode.getCodeName());
        assertEquals(-1, statusCode.getCode());
    }
}