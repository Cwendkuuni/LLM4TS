import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = status.getCode();
        assertEquals(expectedCode, actualCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullGeoStatusCode() {
        GeoStatusCode status = null;
        status.getCode();
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidGeoStatusCode() {
        GeoStatusCode status = new GeoStatusCode(-1, "");
        status.getCode();
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expectedCodeName = "G_GEO_SUCCESS";
        String actualCodeName = status.getCodeName();
        assertEquals(expectedCodeName, actualCodeName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStatusCode() {
        GeoStatusCode status = null;
        status.getDescription();
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned.";
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
    public void testNegativeCode() {
        int code = -1;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCode() {
        int code = null;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnknownStatus() {
        int code = -1;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
    }
}