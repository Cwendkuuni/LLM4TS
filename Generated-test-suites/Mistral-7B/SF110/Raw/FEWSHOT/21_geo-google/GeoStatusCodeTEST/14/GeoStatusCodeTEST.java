package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGeoStatusCode_G_GEO_SUCCESS() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        assertEquals(200, status.getCode());
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", status.getDescription());
    }

    @Test
    public void testGeoStatusCode_G_GEO_BAD_REQUEST() {
        GeoStatusCode status = GeoStatusCode.G_GEO_BAD_REQUEST;
        assertEquals(400, status.getCode());
        assertEquals("A directions request could not be successfully parsed. (Since 2.81)", status.getDescription());
    }

    @Test
    public void testGeoStatusCode_G_GEO_SERVER_ERROR() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SERVER_ERROR;
        assertEquals(500, status.getCode());
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known. (Since 2.55)", status.getDescription());
    }

    @Test
    public void testGeoStatusCode_G_GEO_MISSING_QUERY() {
        GeoStatusCode status = GeoStatusCode.G_GEO_MISSING_QUERY;
        assertEquals(601, status.getCode());
        assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input. (Since 2.81)", status.getDescription());
    }

    @Test
    public void testGeoStatusCode_G_GEO_UNKNOWN_ADDRESS() {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;
        assertEquals(602, status.getCode());
        assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect. (Since 2.55)", status.getDescription());
    }

    @Test
    public void testGeoStatusCode_G_GEO_UNAVAILABLE_ADDRESS() {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;
        assertEquals(603, status.getCode());
        assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons. (Since 2.55)", status.getDescription());
    }

    @Test
    public void testGeoStatusCode_G_GEO_UNKNOWN_DIRECTIONS() {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;
        assertEquals(604, status.getCode());
        assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region. (Since 2.81)", status.getDescription());
    }

    @Test
    public void testGeoStatusCode_G_GEO_BAD_KEY() {
        GeoStatusCode status = GeoStatusCode.G_GEO_BAD_KEY;
        assertEquals(610, status.getCode());
        assertNotNull(status.getDescription());
    }

    @Test
    public void testGeoStatusCode_G_GEO_TOO_MANY_QUERIES() {
        GeoStatusCode status = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;
        assertEquals(620, status.getCode());
        assertEquals("The given key has gone over the requests limit in the 24 hour period.", status.getDescription());
    }

    @Test
    public void testGeoStatusCode_G_GEO_UNKOWN_STATUS() {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        assertEquals(-1, status.getCode());
        assertEquals("Uknown Status", status.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        int code = 500;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, status);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCode_IllegalArgumentException() {
        int code = 123;
        GeoStatusCode.getStatusCode(code);
    }
}