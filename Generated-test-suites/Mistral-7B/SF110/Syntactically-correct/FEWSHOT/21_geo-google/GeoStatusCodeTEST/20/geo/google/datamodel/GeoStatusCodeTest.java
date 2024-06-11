package geo.google.datamodel;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GeoStatusCodeTest {

    @Test
    public void testGetStatusCode() {
        int code = 200;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Unexpected status code for G_GEO_SUCCESS", expectedStatusCode, actualStatusCode);

        code = 400;
        expectedStatusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Unexpected status code for G_GEO_BAD_REQUEST", expectedStatusCode, actualStatusCode);

        code = 500;
        expectedStatusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Unexpected status code for G_GEO_SERVER_ERROR", expectedStatusCode, actualStatusCode);

        code = 601;
        expectedStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Unexpected status code for G_GEO_MISSING_QUERY", expectedStatusCode, actualStatusCode);

        code = 602;
        expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Unexpected status code for G_GEO_UNKNOWN_ADDRESS", expectedStatusCode, actualStatusCode);

        code = 603;
        expectedStatusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Unexpected status code for G_GEO_UNAVAILABLE_ADDRESS", expectedStatusCode, actualStatusCode);

        code = 604;
        expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Unexpected status code for G_GEO_UNKNOWN_DIRECTIONS", expectedStatusCode, actualStatusCode);

        code = 610;
        expectedStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Unexpected status code for G_GEO_BAD_KEY", expectedStatusCode, actualStatusCode);

        code = 620;
        expectedStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Unexpected status code for G_GEO_TOO_MANY_QUERIES", expectedStatusCode, actualStatusCode);

        code = 621; // Invalid status code
        GeoStatusCode actualInvalidStatusCode = GeoStatusCode.getStatusCode(code);
        assertNull("Unexpected valid status code for invalid code", actualInvalidStatusCode);
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expectedCodeName = "G_GEO_SUCCESS";
        String actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_SUCCESS", expectedCodeName, actualCodeName);

        geoStatusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        expectedCodeName = "G_GEO_BAD_REQUEST";
        actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_BAD_REQUEST", expectedCodeName, actualCodeName);

        geoStatusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        expectedCodeName = "G_GEO_SERVER_ERROR";
        actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_SERVER_ERROR", expectedCodeName, actualCodeName);

        geoStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        expectedCodeName = "G_GEO_MISSING_QUERY";
        actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_MISSING_QUERY", expectedCodeName, actualCodeName);

        geoStatusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        expectedCodeName = "G_GEO_UNKNOWN_ADDRESS";
        actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_UNKNOWN_ADDRESS", expectedCodeName, actualCodeName);

        geoStatusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        expectedCodeName = "G_GEO_UNAVAILABLE_ADDRESS";
        actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_UNAVAILABLE_ADDRESS", expectedCodeName, actualCodeName);

        geoStatusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        expectedCodeName = "G_GEO_UNKNOWN_DIRECTIONS";
        actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_UNKNOWN_DIRECTIONS", expectedCodeName, actualCodeName);

        geoStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
        expectedCodeName = "G_GEO_BAD_KEY";
        actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_BAD_KEY", expectedCodeName, actualCodeName);

        geoStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        expectedCodeName = "G_GEO_TOO_MANY_QUERIES";
        actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_TOO_MANY_QUERIES", expectedCodeName, actualCodeName);

        geoStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        expectedCodeName = "G_GEO_UNKOWN_STATUS";
        actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Unexpected code name for G_GEO_UNKOWN_STATUS", expectedCodeName, actualCodeName);
    }

    @Test
    public void testGetCode() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_SUCCESS", expectedCode, actualCode);

        geoStatusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        expectedCode = 400;
        actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_BAD_REQUEST", expectedCode, actualCode);

        geoStatusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        expectedCode = 500;
        actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_SERVER_ERROR", expectedCode, actualCode);

        geoStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        expectedCode = 601;
        actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_MISSING_QUERY", expectedCode, actualCode);

        geoStatusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        expectedCode = 602;
        actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_UNKNOWN_ADDRESS", expectedCode, actualCode);

        geoStatusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        expectedCode = 603;
        actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_UNAVAILABLE_ADDRESS", expectedCode, actualCode);

        geoStatusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        expectedCode = 604;
        actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_UNKNOWN_DIRECTIONS", expectedCode, actualCode);

        geoStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
        expectedCode = 610;
        actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_BAD_KEY", expectedCode, actualCode);

        geoStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        expectedCode = 620;
        actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_TOO_MANY_QUERIES", expectedCode, actualCode);

        geoStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        expectedCode = -1;
        actualCode = geoStatusCode.getCode();
        assertEquals("Unexpected code for G_GEO_UNKOWN_STATUS", expectedCode, actualCode);
    }
}