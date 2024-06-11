package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGeoStatusCodeValues() {
        assertEquals(Integer.valueOf(200), GeoStatusCode.G_GEO_SUCCESS.getCode());
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());

        assertEquals(Integer.valueOf(400), GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());

        assertEquals(Integer.valueOf(500), GeoStatusCode.G_GEO_SERVER_ERROR.getCode());
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());

        // Add other test cases for remaining GeoStatusCode values
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
        // Add other test cases for remaining GeoStatusCode values

        assertNull(GeoStatusCode.getStatusCode(-1));
    }

    @Test
    public void testGetCodeName() {
        assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
        assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
        // Add other test cases for remaining GeoStatusCode values
    }
}