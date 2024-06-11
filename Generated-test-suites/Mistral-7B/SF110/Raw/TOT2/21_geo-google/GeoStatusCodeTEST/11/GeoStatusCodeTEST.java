import static org.junit.Assert.assertEquals;
import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {
    // Test for getCode() method
    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = status.getCode();
        assertEquals(expectedCode, actualCode);
    }
}