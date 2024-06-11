package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

    @Test
    public void testValidStatusCode() {
        // Test valid codes
        for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
            int code = geoStatusCode.getCode();
            assertNotNull("Code is null for: " + geoStatusCode.name(), code);
            assertEquals("Description does not match for: " + geoStatusCode.name(), geoStatusCode.getDescription(), GeoStatusCode.getStatusCode(code).getDescription());
        }
    }

    @Test
    public void testGetStatusCodeByCode() {
        // Test getting status code by code
        int[] testCodes = {200, 400, 500, 601, 602, 603, 604, 610, 620};
        GeoStatusCode[] expectedStatusCodes = {GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.G_GEO_TOO_MANY_QUERIES};

        for (int i = 0; i < testCodes.length; i++) {
            GeoStatusCode statusCode = GeoStatusCode.getStatusCode(testCodes[i]);
            assertEquals("Unexpected status code for given code: " + testCodes[i], expectedStatusCodes[i], statusCode);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByNegativeCode() {
        // Test getting status code by negative code
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByInvalidCode() {
        // Test getting status code by invalid code
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(621);
    }
}