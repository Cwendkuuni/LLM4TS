import static org.junit.Assert.assertEquals;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = status.getCode();
        assertEquals(expectedCode, actualCode);
    }
}