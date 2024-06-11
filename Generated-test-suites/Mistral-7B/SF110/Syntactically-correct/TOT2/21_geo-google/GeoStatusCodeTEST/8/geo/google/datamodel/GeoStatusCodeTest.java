import static org.junit.Assert.assertEquals;
import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {
    @Test
    public void testGetCode() {
        GeoStatusCode geoStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = geoStatusCode.getCode();
        assertEquals(expectedCode, actualCode);
    }
}