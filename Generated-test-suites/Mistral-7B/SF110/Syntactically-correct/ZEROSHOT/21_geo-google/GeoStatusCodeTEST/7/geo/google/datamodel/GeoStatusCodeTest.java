package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

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

        // Test unknown status code
        int unknownCode = -1;
        GeoStatusCode unknownStatusCode = GeoStatusCode.getStatusCode(unknownCode);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, unknownStatusCode);
    }

    @Test
    public void testGetDescriptionByCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int code = statusCode.getCode();
            String expectedDescription = statusCode.getDescription();
            String actualDescription = GeoStatusCode.getStatusCode(code).getDescription();
            assertEquals(expectedDescription, actualDescription);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByInvalidCode() {
        GeoStatusCode.getStatusCode(-1);
    }
}