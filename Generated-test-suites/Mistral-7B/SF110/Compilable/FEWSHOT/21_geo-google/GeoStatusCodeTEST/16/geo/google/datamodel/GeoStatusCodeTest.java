package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetStatusCodeByCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int code = statusCode.getCode();
            GeoStatusCode expectedStatusCode = statusCode;
            GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
            assertEquals(expectedStatusCode, actualStatusCode);
        }

        int unknownCode = -1;
        GeoStatusCode unknownStatusCode = GeoStatusCode.getStatusCode(unknownCode);
        assertNull(unknownStatusCode);
    }

    @Test
    public void testGetCodeByStatusCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int expectedCode = statusCode.getCode();
            int actualCode = statusCode.getCode();
            assertEquals(expectedCode, actualCode);
        }

        GeoStatusCode unknownStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        int unknownCode = unknownStatusCode.getCode();
        assertEquals(-1, unknownCode);
    }

    @Test
    public void testGetDescriptionByStatusCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            String expectedDescription = statusCode.getDescription();
            String actualDescription = statusCode.getDescription();
            assertEquals(expectedDescription, actualDescription);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByInvalidCode() {
        GeoStatusCode.getStatusCode(Integer.MAX_VALUE);
    }
}