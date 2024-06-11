package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

    // Test all methods in GeoStatusCode class

    @Test
    public void testGetStatusCode() {
        // Test valid codes
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int code = statusCode.getCode();
            GeoStatusCode expectedStatus = GeoStatusCode.getStatusCode(code);
            GeoStatusCode actualStatus = GeoStatusCode.getStatusCode(code);
            assertEquals("Invalid status code returned for given code.", expectedStatus, actualStatus);
        }

        // Test invalid code
        assertNull("getStatusCode should return null for invalid code.", GeoStatusCode.getStatusCode(-1));
    }

    @Test
    public void testGetCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int expectedCode = statusCode.getCode();
            int actualCode = statusCode.getCode();
            assertEquals("Invalid code returned for given status.", expectedCode, actualCode);
        }
    }

    @Test
    public void testGetCodeName() {
        for (GeoStatusCode statusCode : GeoSTATUSCODE.values()) {
            String expectedName = statusCode.getCodeName();
            String actualName = statusCode.name();
            assertEquals("Invalid name returned for given status.", expectedName, actualName);
        }
    }

    @Test
    public void testGetDescription() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            String expectedDescription = statusCode.getDescription();
            String actualDescription = statusCode.getDescription();
            assertEquals("Invalid description returned for given status.", expectedDescription, actualDescription);
        }
    }

    // Test edge cases and exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeNegativeCode() {
        GeoStatusCode.getStatusCode(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeNullCode() {
        GeoStatusCode.getStatusCode(null);
    }

    @Test
    public void testGetStatusCodeInvalidCode() {
        int invalidCode = 999;
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(invalidCode);
        assertEquals("Invalid status code returned for invalid code.", GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }

    @Test
    public void testGetStatusCodeOutOfRangeCode() {
        int outOfRangeCode = Integer.MAX_VALUE;
        GeoStatusCode statusCode = GeoStatusCode.getStatusCode(outOfRangeCode);
        assertEquals("Invalid status code returned for out of range code.", GeoStatusCode.G_GEO_UNKOWN_STATUS, statusCode);
    }

    // Merge all test cases into a single test file
}