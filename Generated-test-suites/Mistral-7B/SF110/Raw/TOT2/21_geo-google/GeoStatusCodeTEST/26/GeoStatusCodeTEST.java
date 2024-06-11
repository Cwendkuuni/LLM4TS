package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {
    @Test
    public void testValidStatusCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int expectedCode = statusCode.getCode();
            String expectedDescription = statusCode.getDescription();

            int actualCode = statusCode.getCode();
            String actualDescription = statusCode.getDescription();

            assertEquals(expectedCode, actualCode);
            assertEquals(expectedDescription, actualDescription);
        }
    }

    @Test
    public void testGetStatusCodeByCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int code = statusCode.getCode();
            GeoStatusCode expectedStatus = statusCode;

            GeoStatusCode actualStatus = GeoStatusCode.getStatusCode(code);

            assertEquals(expectedStatus, actualStatus);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByInvalidCode() {
        int invalidCode = 999;

        GeoStatusCode actualStatus = GeoStatusCode.getStatusCode(invalidCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByNegativeCode() {
        int negativeCode = -10;

        GeoStatusCode actualStatus = GeoStatusCode.getStatusCode(negativeCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByMaxIntValue() {
        int maxIntValue = Integer.MAX_VALUE;

        GeoStatusCode actualStatus = GeoStatusCode.getStatusCode(maxIntValue);
    }

    @Test
    public void testGetStatusCodeByName() {
        GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualStatus = GeoStatusCode.getStatusCode("G_GEO_SUCCESS");

        assertEquals(expectedStatus, actualStatus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByInvalidName() {
        String invalidName = "invalidName";

        GeoStatusCode actualStatus = GeoStatusCode.getStatusCode(invalidName);
    }

    @Test
    public void testGetStatusCodeIsNull() {
        GeoStatusCode actualStatus = GeoStatusCode.getStatusCode(null);

        assertNull(actualStatus);
    }
}