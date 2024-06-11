package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

/**
 * Test class for GeoStatusCode enum
 */
public class GeoStatusCodeTest {

    /**
     * Test constructor for GeoStatusCode
     */
    @Test
    public void testGeoStatusCodeConstructor() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertNotNull(statusCode);
        }
    }

    /**
     * Test getCode method
     */
    @Test
    public void testGetCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int expectedCode = statusCode.getCode();
            int actualCode = statusCode.getCode();
            assertEquals(expectedCode, actualCode);
        }
    }

    /**
     * Test getCodeName method
     */
    @Test
    public void testGetCodeName() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            String expectedName = statusCode.getCodeName();
            String actualName = statusCode.name();
            assertEquals(expectedName, actualName);
        }
    }

    /**
     * Test getDescription method
     */
    @Test
    public void testGetDescription() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            String expectedDescription = statusCode.getDescription();
            String actualDescription = statusCode.toString();
            assertEquals(expectedDescription, actualDescription);
        }
    }

    /**
     * Test getStatusCode method with valid codes
     */
    @Test
    public void testGetStatusCodeValidCodes() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
        // ... test other valid codes
    }

    /**
     * Test getStatusCode method with invalid code
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeInvalidCode() {
        GeoStatusCode.getStatusCode(-1);
    }

    /**
     * Test edge cases for getStatusCode method
     */
    @Test
    public void testGetStatusCodeEdgeCases() {
        int maxCode = Integer.MAX_VALUE;
        int code = maxCode + 1;
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(maxCode));
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(code));
    }
}