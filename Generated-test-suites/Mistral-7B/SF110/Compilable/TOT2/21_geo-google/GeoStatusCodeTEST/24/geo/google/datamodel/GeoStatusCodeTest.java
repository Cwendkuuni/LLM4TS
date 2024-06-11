package geo.google.datamodel.test;

import org.junit.Test;
import static org.junit.Assert.*;
import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = geoStatusCode.getCode();
        assertEquals(expectedCode, actualCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithNullArg() {
        int code = 0;
        GeoStatusCode geoStatusCode = GeoStatusCode.getStatusCode(code);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithEmptyStringArg() {
        String codeStr = "";
        GeoStatusCode geoStatusCode = GeoStatusCode.getStatusCode(Integer.parseInt(codeStr));
    }

    @Test
    public void testGetStatusCodeWithInvalidCode() {
        int code = 1000;
        GeoStatusCode geoStatusCode = GeoStatusCode.getStatusCode(code);
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals(expectedStatusCode, geoStatusCode);
    }

    @Test
    public void testGetStatusCodeWithValidCodes() {
        int code1 = 200;
        GeoStatusCode geoStatusCode1 = GeoStatusCode.G_GEO_SUCCESS;
        int code2 = 620;
        GeoStatusCode geoStatusCode2 = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;

        assertEquals(geoStatusCode1, GeoStatusCode.getStatusCode(code1));
        assertEquals(geoStatusCode2, GeoStatusCode.getStatusCode(code2));
    }

}