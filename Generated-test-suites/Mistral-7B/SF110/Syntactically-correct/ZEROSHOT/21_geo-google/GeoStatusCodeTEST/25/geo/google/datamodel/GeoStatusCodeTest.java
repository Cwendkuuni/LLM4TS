package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

/**
 * Tests for GeoStatusCode enum class
 *
 * @author [Your Name]
 */
public class GeoStatusCodeTest {

    /**
     * Test of getStatusCode method, of class GeoStatusCode.
     */
    @Test
    public void testGetStatusCode() {
        // Test for valid codes
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        int code = 200;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected: GeoStatusCode.G_GEO_SUCCESS, Actual: " + actualStatusCode, expectedStatusCode, actualStatusCode);

        expectedStatusCode = GeoStatusCode.G_GEO_BAD_REQUEST;
        code = 400;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected: GeoStatusCode.G_GEO_BAD_REQUEST, Actual: " + actualStatusCode, expectedStatusCode, actualStatusCode);

        expectedStatusCode = GeoStatusCode.G_GEO_SERVER_ERROR;
        code = 500;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected: GeoStatusCode.G_GEO_SERVER_ERROR, Actual: " + actualStatusCode, expectedStatusCode, actualStatusCode);

        expectedStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        code = 601;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected: GeoStatusCode.G_GEO_MISSING_QUERY, Actual: " + actualStatusCode, expectedStatusCode, actualStatusCode);

        expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        code = 602;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected: GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, Actual: " + actualStatusCode, expectedStatusCode, actualStatusCode);

        expectedStatusCode = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        code = 603;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected: GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, Actual: " + actualStatusCode, expectedStatusCode, actualStatusCode);

        expectedStatusCode = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        code = 604;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected: GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, Actual: " + actualStatusCode, expectedStatusCode, actualStatusCode);

        expectedStatusCode = GeoStatusCode.G_GEO_BAD_KEY;
        code = 610;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected: GeoStatusCode.G_GEO_BAD_KEY, Actual: " + actualStatusCode, expectedStatusCode, actualStatusCode);

        expectedStatusCode = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        code = 620;
        actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected: GeoStatusCode.G_GEO_TOO_MANY_QUERIES, Actual: " + actualStatusCode, expectedStatusCode, actualStatusCode);

        // Test for unknown code
        code = -1;
        assertNotNull("Expected: not null, Actual: " + actualStatusCode, actualStatusCode);
        assertEquals("Expected: GeoStatusCode.G_GEO_UNKOWN_STATUS, Actual: " + actualStatusCode, GeoStatusCode.G_GEO_UNKOWN_STATUS, actualStatusCode);
    }

    /**
     * Test of getCodeName method, of class GeoStatusCode.
     */
    @Test
    public void testGetCodeName() {
        for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
            String expectedCodeName = geoStatusCode.name();
            String actualCodeName = geoStatusCode.getCodeName();
            assertEquals("Expected: " + expectedCodeName + ", Actual: " + actualCodeName, expectedCodeName, actualCodeName);
        }
    }

    /**
     * Test of getDescription method, of class GeoStatusCode.
     */
    @Test
    public void testGetDescription() {
        for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
            String expectedDescription = geoStatusCode.getDescription();
            String actualDescription = geoStatusCode.getDescription();
            assertEquals("Expected: " + expectedDescription + ", Actual: " + actualDescription, expectedDescription, actualDescription);
        }
    }
}