import static org.junit.Assert.assertEquals;
import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {
    @Test
    public void testGetCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            int expectedCode = statusCode.getCode();
            int actualCode = statusCode.getCode();
            assertEquals("Incorrect code for status code: " + statusCode.name(), expectedCode, actualCode);
        }
    }
}