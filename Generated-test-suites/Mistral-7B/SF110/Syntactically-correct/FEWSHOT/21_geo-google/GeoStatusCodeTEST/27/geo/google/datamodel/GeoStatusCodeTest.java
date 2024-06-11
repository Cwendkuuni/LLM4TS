package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGetStatusCodeByCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        int code = status.getCode();
        GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(expectedStatus.getDescription(), GeoStatusCode.getStatusCode(code).getDescription());
        assertEquals(expectedStatus.name(), GeoStatusCode.getStatusCode(code).name());

        for (GeoStatusCode value : GeoStatusCode.values()) {
            code = value.getCode();
            status = GeoStatusCode.getStatusCode(code);
            expectedStatus = value;
            assertEquals(expectedStatus.getDescription(), status.getDescription());
            assertEquals(expectedStatus.name(), status.name());
        }
    }

    @Test
    public void testNullStatusCode() {
        assertNull(GeoStatusCode.getStatusCode(null));
    }

    @Test
    public void testUnknownStatusCode() {
        int unknownCode = -1;
        GeoStatusCode status = GeoStatusCode.getStatusCode(unknownCode);
        GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals(expectedStatus.getDescription(), status.getDescription());
        assertEquals(expectedStatus.name(), status.name());
    }
}