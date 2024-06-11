package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGeoStatusCodeEnumValues() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertNotNull(statusCode.getCodeName());
            assertNotNull(statusCode.getDescription());
        }
    }

    @Test
    public void testGetStatusCodeByCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        int code = statusCode.getCode();
        GeoStatusCode expectedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(expectedStatusCode, statusCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByInvalidCode() {
        GeoStatusCode.getStatusCode(-1);
    }

    @Test
    public void testGetStatusCodeByCodeName() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        String codeName = statusCode.getCodeName();
        GeoStatusCode expectedStatusCode = GeoStatusCode.getStatusCode(Integer.parseInt(codeName.substring(5)));
        assertEquals(expectedStatusCode, statusCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeByInvalidCodeName() {
        GeoStatusCode.getStatusCode("X_GEO_UNKNOWN_STATUS");
    }
}