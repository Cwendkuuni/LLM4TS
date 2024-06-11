package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetStatusCode_ValidCode() {
        int code = 200;
        GeoStatusCode expected = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCode_InvalidCode() {
        int code = 700;
        GeoStatusCode expected = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCode_ValidName() {
        String name = "G_GEO_MISSING_QUERY";
        GeoStatusCode expected = GeoStatusCode.G_GEO_MISSING_QUERY;
        GeoStatusCode actual = GeoStatusCode.valueOf(name);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCode_InvalidName() {
        String name = "InvalidName";
        GeoStatusCode actual = GeoStatusCode.valueOf(name);
    }

    @Test
    public void testGetDescription_ValidStatusCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expected = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
        String actual = statusCode.getDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDescription_InvalidStatusCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        String expected = "Unknown Status";
        String actual = statusCode.getDescription();
        assertEquals(expected, actual);
    }
}