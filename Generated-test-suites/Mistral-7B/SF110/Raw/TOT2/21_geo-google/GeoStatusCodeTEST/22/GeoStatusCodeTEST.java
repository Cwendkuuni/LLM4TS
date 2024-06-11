package geo.google.datamodel;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;

        int actualCode = status.getCode();

        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expectedCodeName = "G_GEO_SUCCESS";

        String actualCodeName = status.getCodeName();

        assertEquals(expectedCodeName, actualCodeName);
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";

        String actualDescription = status.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testGetStatusCode() {
        int code = 200;
        GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_SUCCESS;

        GeoStatusCode actualStatus = GeoStatusCode.getStatusCode(code);

        assertEquals(expectedStatus, actualStatus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithNegativeCode() {
        int code = -1;

        GeoStatusCode status = GeoStatusCode.getStatusCode(code);

        assertNull(status);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithInvalidCode() {
        int code = 1000;

        GeoStatusCode status = GeoStatusCode.getStatusCode(code);

        assertNull(status);
    }

    @Test
    public void testGetStatusCodeWithNullCode() {
        int code = null;

        GeoStatusCode status = GeoStatusCode.getStatusCode(code);

        assertNull(status);
    }
}