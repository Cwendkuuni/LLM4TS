package geo.google.datamodel;

import org.junit.Test;
import static org.junit.Assert.*;

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
        int code = 625;
        GeoStatusCode expected = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCode_Null() {
        int code = Integer.MIN_VALUE;
        GeoStatusCode expected = null;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertNull(actual);
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
        String expected = "Uknown Status";
        String actual = statusCode.getDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCodeName_ValidStatusCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expected = "G_GEO_SUCCESS";
        String actual = statusCode.getCodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCodeName_InvalidStatusCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        String expected = "G_GEO_UNKOWN_STATUS";
        String actual = statusCode.getCodeName();
        assertEquals(expected, actual);
    }
}