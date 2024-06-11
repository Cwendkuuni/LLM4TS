package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGeoStatusCodeValues() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertNotNull(statusCode);
            assertEquals(statusCode.name(), statusCode.getCodeName());
        }
    }

    @Test
    public void testGetStatusCode() {
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        int code = statusCode.getCode();
        GeoStatusCode returnedStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(statusCode, returnedStatusCode);

        for (GeoStatusCode statusCode2 : GeoStatusCode.values()) {
            if (statusCode2 != statusCode) {
                int code2 = statusCode2.getCode();
                GeoStatusCode returnedStatusCode2 = GeoStatusCode.getStatusCode(code2);
                assertNotEquals(statusCode, returnedStatusCode2);
            }
        }
    }
}