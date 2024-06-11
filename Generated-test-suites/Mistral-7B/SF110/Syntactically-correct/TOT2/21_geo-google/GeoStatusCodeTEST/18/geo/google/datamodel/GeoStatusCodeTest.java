package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import geo.google.datamodel.GeoStatusCode;
import geo.google.datamodel.GeoStatusCode.GeoStatusCodeCategory;

@Category(GeoStatusCodeCategory.class)
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
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithInvalidCode() {
        int code = -1;
        GeoStatusCode.getStatusCode(code);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStatusCodeWithNullInput() {
        int code = 200;
        GeoStatusCode[] statusCodes = null;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code, statusCodes);
    }

    @Test
    public void testGetStatusCodeWithNullStatusCodeArray() {
        int code = 200;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code, null);
        assertNull(actualStatusCode);
    }

    @Test
    public void testGetStatusCodeWithValidStatusCodeArray() {
        int code = 200;
        GeoStatusCode[] statusCodes = {GeoStatusCode.G_GEO_SUCCESS};
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code, statusCodes);
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, actualStatusCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithOutOfRangeCode() {
        int code = 201;
        GeoStatusCode.getStatusCode(code);
    }

    @Test(expected = NullPointerException.class)
    public void testGetStatusCodeWithNullStatusCode() {
        int code = 200;
        GeoStatusCode status = null;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code, status);
        assertNull(actualStatusCode);
    }
}