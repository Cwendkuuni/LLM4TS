package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

    @Test
    public void testGeoStatusCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertNotNull(statusCode);
            assertEquals(statusCode.getCode(), statusCode.getCode());
            assertEquals(statusCode.getDescription(), statusCode._description);
        }
    }

    @Test
    public void testGetStatusCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        int code = statusCode.getCode();
        GeoStatusCode returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        statusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        code = statusCode.getCode();
        returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        statusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        code = statusCode.getCode();
        returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        code = statusCode.getCode();
        returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        code = statusCode.getCode();
        returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        statusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        code = statusCode.getCode();
        returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        statusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        code = statusCode.getCode();
        returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        statusCode = GeoStatusCode.G_GEO_BAD_KEY;
        code = statusCode.getCode();
        returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        statusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        code = statusCode.getCode();
        returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        code = statusCode.getCode();
        returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        int invalidCode = 999;
        returnedStatusCode = GeoStatusCode.getStatusCode(invalidCode);
        assertNull(returnedStatusCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStatusCode() {
        GeoStatusCode.getStatusCode(-1);
    }
}