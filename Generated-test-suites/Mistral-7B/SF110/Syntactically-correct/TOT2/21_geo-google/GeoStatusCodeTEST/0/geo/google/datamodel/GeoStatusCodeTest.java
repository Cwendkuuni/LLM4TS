package geo.google.datamodel;

import static org.junit.Assert.*;

import org.junit.Test;

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
        String expectedName = "G_GEO_SUCCESS";
        String actualName = status.getCodeName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
        String actualDescription = status.getDescription();
        assertEquals(expectedDescription, actualDescription);
    }

    // Edge cases and exception handling scenarios:
    // Test invalid code
    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithInvalidCode() {
        int invalidCode = -5;
        GeoStatusCode status = GeoStatusCode.getStatusCode(invalidCode);
    }

    // Test unknown status
    @Test
    public void testGetStatusCodeWithUnknownStatus() {
        int unknownCode = -1;
        GeoStatusCode status = GeoStatusCode.getStatusCode(unknownCode);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, status);
    }

    // Test null description
    @Test
    public void testNullDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_MISSING_QUERY;
        String expectedDescription = null;
        String actualDescription = status.getDescription();
        assertEquals(expectedDescription, actualDescription);
    }

    // Test empty description
    @Test
    public void testEmptyDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        String expectedDescription = "";
        String actualDescription = status.getDescription();
        assertEquals(expectedDescription, actualDescription);
    }

    // Test getStatusCode with invalid code
    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithInvalidCode() {
        int invalidCode = 100;
        GeoStatusCode status = GeoStatusCode.getStatusCode(invalidCode);
    }

    // Test getStatusCode with null argument
    @Test(expected = NullPointerException.class)
    public void testGetStatusCodeWithNullArgument() {
        int[] codes = null;
        GeoStatusCode status = GeoStatusCode.getStatusCode(codes[0]);
    }
}