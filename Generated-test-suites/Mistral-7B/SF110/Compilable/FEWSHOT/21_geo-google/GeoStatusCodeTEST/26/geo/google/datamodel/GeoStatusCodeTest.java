package geo.google.datamodel;

import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetStatusCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int code = statusCode.getCode();
            GeoStatusCode expectedStatusCode = GeoStatusCode.getStatusCode(code);
            GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
            assertEquals(expectedStatusCode, actualStatusCode);
        }

        int invalidCode = 100;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(invalidCode);
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, actualStatusCode);
    }

    @Test
    public void testGetCodeName() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            String expectedCodeName = statusCode.name();
            String actualCodeName = statusCode.getCodeName();
            assertEquals(expectedCodeName, actualCodeName);
        }
    }

    @Test
    public void testGetDescription() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            String expectedDescription = statusCode.getDescription();
            String actualDescription = statusCode.getDescription();
            assertEquals(expectedDescription, actualDescription);
        }
    }
}