
package geo.google.datamodel;

import org.junit.Assert;
import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = geoStatusCode.getCode();
        assertEquals("Expected code is not equal to the actual code.", expectedCode, actualCode);
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expectedCodeName = "G_GEO_SUCCESS";
        String actualCodeName = geoStatusCode.getCodeName();
        assertEquals("Expected codeName is not equal to the actual codeName.", expectedCodeName, actualCodeName);
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned.";
        String actualDescription = geoStatusCode.getDescription();
        assertEquals("Expected description is not equal to the actual description.", expectedDescription, actualDescription);
    }

    @Test
    public void testGetStatusCode() {
        int code = 200;
        GeoStatusCode expectedGeoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualGeoStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected GeoStatusCode is not equal to the actual GeoStatusCode.", expectedGeoStatusCode, actualGeoStatusCode);
    }

    @Test
    public void testGetStatusCodeWithInvalidCode() {
        int code = 100;
        GeoStatusCode expectedGeoStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actualGeoStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals("Expected GeoStatusCode is not equal to the actual GeoStatusCode.", expectedGeoStatusCode, actualGeoStatusCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithNullInput() {
        int code = null;
        GeoStatusCode.getStatusCode(code);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithUnknownCode() {
        int code = 1000;
        try {
            GeoStatusCode.getStatusCode(code);
            fail("An IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
