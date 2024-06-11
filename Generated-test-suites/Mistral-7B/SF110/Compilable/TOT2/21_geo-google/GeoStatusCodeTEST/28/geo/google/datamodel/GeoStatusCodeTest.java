package geo.google.datamodel;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeoStatusCodeTest {

    @Test
    public void testGetCode() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        int expected = 200;
        int actual = status.getCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCodeName() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expected = "G_GEO_SUCCESS";
        String actual = status.getCodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDescription() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expected = "No errors occurred; the address was successfully parsed and its geocode has been returned.";
        String actual = status.getDescription();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStatusCode() {
        int code = 200;
        GeoStatusCode expected = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
        assertEquals(expected, actual);
    }
}