import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        // ... (same as above)
    }

    @Test
    public void testGetCodeName() {
        // ... (same as above)
    }

    @Test
    public void testGetDescription() {
        // ... (same as above)
    }

    @Test
    public void testGetStatusCode() {
        // ... (same as above)
    }

    @Test
    public void testGetStatusCodeWithInvalidCode() {
        // ... (same as above)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithNegativeCode() {
        int code = -1;
        GeoStatusCode.getStatusCode(code);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithNonIntegerCode() {
        String code = "test";
        GeoStatusCode.getStatusCode(Integer.parseInt(code));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithInvalidCodeOutsideRange() {
        int code = 1000;
        GeoStatusCode.getStatusCode(code);
    }

    @Test
    public void testGetStatusCodeWithUnknownCode() {
        int code = -1;
        GeoStatusCode expectedGeoStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actualGeoStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(expectedGeoStatusCode, actualGeoStatusCode);
    }

    @Test
    public void testGetStatusCodeWithValidCode() {
        int code = 200;
        GeoStatusCode expectedGeoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualGeoStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(expectedGeoStatusCode, actualGeoStatusCode);
    }

    @Test
    public void testGetStatusCodeWithValidCodeName() {
        String codeName = "G_GEO_SUCCESS";
        GeoStatusCode expectedGeoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualGeoStatusCode = GeoStatusCode.getStatusCode(expectedGeoStatusCode.getCode());
        assertEquals(expectedGeoStatusCode, actualGeoStatusCode);
    }

    @Test
    public void testGetStatusCodeWithInvalidCodeName() {
        String codeName = "G_INVALID_STATUS";
        GeoStatusCode expectedGeoStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actualGeoStatusCode = GeoStatusCode.getStatusCode(Integer.parseInt(codeName));
        assertEquals(expectedGeoStatusCode, actualGeoStatusCode);
    }
}