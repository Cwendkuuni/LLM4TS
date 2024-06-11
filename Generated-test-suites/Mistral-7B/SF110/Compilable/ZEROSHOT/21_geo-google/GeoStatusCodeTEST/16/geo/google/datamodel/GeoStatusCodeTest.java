package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

/**
 * Test class for GeoStatusCode enum
 *
 * @author [Your Name]
 */
public class GeoStatusCodeTest
{

    /**
     * Test method for GeoStatusCode.G_GEO_SUCCESS
     */
    @Test
    public void testGeoSuccess()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;

        assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", status.getDescription());
        assertEquals(200, status.getCode());
    }

    /**
     * Test method for GeoStatusCode.G_GEO_BAD_REQUEST
     */
    @Test
    public void testGeoBadRequest()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_BAD_REQUEST;

        assertEquals("A directions request could not be successfully parsed.", status.getDescription());
        assertEquals(400, status.getCode());
    }

    /**
     * Test method for GeoStatusCode.G_GEO_SERVER_ERROR
     */
    @Test
    public void testGeoServerError()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_SERVER_ERROR;

        assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.", status.getDescription());
        assertEquals(500, status.getCode());
    }

    /**
     * Test method for GeoStatusCode.G_GEO_MISSING_QUERY
     */
    @Test
    public void testGeoMissingQuery()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_MISSING_QUERY;

        assertEquals("The HTTP q parameter was either missing or had no value.", status.getDescription());
        assertEquals(601, status.getCode());
    }

    /**
     * Test method for GeoStatusCode.G_GEO_UNKNOWN_ADDRESS
     */
    @Test
    public void testGeoUnknownAddress()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNKNOWN_ADDRESS;

        assertEquals("No corresponding geographic location could be found for the specified address.", status.getDescription());
        assertEquals(602, status.getCode());
    }

    /**
     * Test method for GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS
     */
    @Test
    public void testGeoUnavailableAddress()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;

        assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons.", status.getDescription());
        assertEquals(603, status.getCode());
    }

    /**
     * Test method for GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS
     */
    @Test
    public void testGeoUnknownDirections()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS;

        assertEquals("The GDirections object could not compute directions between the points mentioned in the query.", status.getDescription());
        assertEquals(604, status.getCode());
    }

    /**
     * Test method for GeoStatusCode.G_GEO_BAD_KEY
     */
    @Test
    public void testGeoBadKey()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_BAD_KEY;

        assertEquals("The given key is either invalid or does not match the domain for which it was given.", status.getDescription());
        assertEquals(610, status.getCode());
    }

    /**
     * Test method for GeoStatusCode.G_GEO_TOO_MANY_QUERIES
     */
    @Test
    public void testGeoTooManyQueries()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_TOO_MANY_QUERIES;

        assertEquals("The given key has gone over the requests limit in the 24 hour period.", status.getDescription());
        assertEquals(620, status.getCode());
    }

    /**
     * Test method for GeoStatusCode.getStatusCode()
     */
    @Test
    public void testGetStatusCode()
    {
        int code = 603;
        GeoStatusCode expectedStatus = GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS;

        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertNotNull(status);
        assertEquals(expectedStatus.getDescription(), status.getDescription());
        assertEquals(expectedStatus.getCode(), status.getCode());
    }

    /**
     * Test method for getUnknownStatus()
     */
    @Test
    public void testGetUnknownStatus()
    {
        GeoStatusCode status = GeoStatusCode.G_GEO_UNKOWN_STATUS;

        assertEquals("-1", Integer.toString(status.getCode()));
        assertEquals("Uknown Status", status.getDescription());
    }
}