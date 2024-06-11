package geo.google.datamodel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GeoStatusCodeTest {
    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expected = "G_GEO_SUCCESS";
        String actual = status.getCodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCode_200() {
        int code = 200;
        GeoStatusCode expected = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCode_400() {
        int code = 400;
        GeoStatusCode expected = GeoStatusCode.G_GEO_BAD_REQUEST;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCode_InvalidCode() {
        int code = 100;
        GeoStatusCode expected = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    // Add test cases for all other status codes and methods if needed
}