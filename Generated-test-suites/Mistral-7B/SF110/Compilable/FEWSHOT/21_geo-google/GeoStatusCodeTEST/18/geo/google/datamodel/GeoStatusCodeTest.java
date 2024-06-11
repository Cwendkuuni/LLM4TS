package geo.google.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GeoStatusCodeTest {

    @Test
    public void testGeoStatusCodeValues() {
        assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", GeoStatusCode.G_GEO_SUCCESS.getDescription());

        assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
        assertEquals("A directions request could not be successfully parsed.", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());

        assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());
        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.", GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());

        assertEquals(601, GeoStatusCode.G_GEO_MISSING_QUERY.getCode());
        assertEquals("The HTTP q parameter was either missing or had no value. For geocoding requests, this means that an empty address was specified as input. For directions requests, this means that no query was specified in the input.", GeoStatusCode.G_GEO_MISSING_QUERY.getDescription());

        assertEquals(602, GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCode());
        assertEquals("No corresponding geographic location could be found for the specified address. This may be due to the fact that the address is relatively new, or it may be incorrect.", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getDescription());

        assertEquals(603, GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCode());
        assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons.", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getDescription());

        assertEquals(604, GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCode());
        assertEquals("The GDirections object could not compute directions between the points mentioned in the query. This is usually because there is no route available between the two points, or because we do not have data for routing in that region.", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getDescription());

        assertEquals(610, GeoStatusCode.G_GEO_BAD_KEY.getCode());
        assertEquals("The given key is either invalid or does not match the domain for which it was given.", GeoStatusCode.G_GEO_BAD_KEY.getDescription());

        assertEquals(620, GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCode());
        assertEquals("The given key has gone over the requests limit in the 24 hour period.", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getDescription());

        assertEquals(-1, GeoStatusCode.G_GEO_UNKOWN_STATUS.getCode());
        assertNull(GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
    }

    @Test
    public void testGetStatusCode() {
        assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
        assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
        assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
        assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
        assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
        assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
        assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
        assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
        assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, GeoStatusCode.getStatusCode(666));
        assertNull(GeoStatusCode.getStatusCode(100));
    }
}