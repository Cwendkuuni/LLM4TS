package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

	@Test
	public void testGetStatusCode_ValidCodes() {
		// Test valid codes
		assertEquals(GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.getStatusCode(200));
		assertEquals(GeoStatusCode.G_GEO_BAD_REQUEST, GeoStatusCode.getStatusCode(400));
		assertEquals(GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.getStatusCode(500));
		assertEquals(GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.getStatusCode(601));
		assertEquals(GeoStatusCode.G_GEO_UNKNOWN_ADDRESS, GeoStatusCode.getStatusCode(602));
		assertEquals(GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.getStatusCode(603));
		assertEquals(GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.getStatusCode(604));
		assertEquals(GeoStatusCode.G_GEO_BAD_KEY, GeoStatusCode.getStatusCode(610));
		assertEquals(GeoStatusCode.G_GEO_TOO_MANY_QUERIES, GeoStatusCode.getStatusCode(620));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetStatusCode_InvalidCode() {
		// Test an invalid code
		GeoStatusCode.getStatusCode(-1);
	}

	@Test
	public void testGetDescription_ValidCodes() {
		// Test valid codes
		assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", GeoStatusCode.G_GEO_SUCCESS.getDescription());
		assertEquals("A directions request could not be successfully parsed.", GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
		assertEquals("A geocoding or directions request could not be successfully processed, yet the exact reason for the failure is not known.", GeoStatusCode.G_GEO_SERVER_ERROR.getDescription());
		assertEquals("The HTTP q parameter was either missing or had no value.", GeoStatusCode.G_GEO_MISSING_QUERY.getDescription());
		assertEquals("No corresponding geographic location could be found for the specified address.", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getDescription());
		assertEquals("The geocode for the given address or the route for the given directions query cannot be returned due to legal or contractual reasons.", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getDescription());
		assertEquals("The GDirections object could not compute directions between the points mentioned in the query.", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getDescription());
		assertEquals("The given key is either invalid or does not match the domain for which it was given.", GeoStatusCode.G_GEO_BAD_KEY.getDescription());
		assertEquals("The given key has gone over the requests limit in the 24 hour period.", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getDescription());
		assertEquals("Uknown Status", GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription());
	}

	@Test
	public void testGetName_ValidCodes() {
		// Test valid codes
		assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
		assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
		assertEquals("G_GEO_SERVER_ERROR", GeoStatusCode.G_GEO_SERVER_ERROR.getCodeName());
		assertEquals("G_GEO_MISSING_QUERY", GeoStatusCode.G_GEO_MISSING_QUERY.getCodeName());
		assertEquals("G_GEO_UNKNOWN_ADDRESS", GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCodeName());
		assertEquals("G_GEO_UNAVAILABLE_ADDRESS", GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCodeName());
		assertEquals("G_GEO_UNKNOWN_DIRECTIONS", GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCodeName());
		assertEquals("G_GEO_BAD_KEY", GeoStatusCode.G_GEO_BAD_KEY.getCodeName());
		assertEquals("G_GEO_TOO_MANY_QUERIES", GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCodeName());
		assertEquals("G_GEO_UNKOWN_STATUS", GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName());
	}

	@Test
	public void testGetCode_ValidEnumInstances() {
		// Test valid enum instances
		assertEquals(200, GeoStatusCode.G_GEO_SUCCESS.getCode());
		assertEquals(400, GeoStatusCode.G_GEO_BAD_REQUEST.getCode());
		assertEquals(500, GeoStatusCode.G_GEO_SERVER_ERROR.getCode());
		assertEquals(601, GeoStatusCode.G_GEO_MISSING_QUERY.getCode());
		assertEquals(602, GeoStatusCode.G_GEO_UNKNOWN_ADDRESS.getCode());
		assertEquals(603, GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS.getCode());
		assertEquals(604, GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS.getCode());
		assertEquals(610, GeoStatusCode.G_GEO_BAD_KEY.getCode());
		assertEquals(620, GeoStatusCode.G_GEO_TOO_MANY_QUERIES.getCode());
		assertEquals(-1, GeoStatusCode.G_GEO_UNKOWN_STATUS.getCode());
	}

	@Test(expected = IllegalStateException.class)
	public void testEnumInstance_InvalidCode() {
		// Test an invalid enum instance
		GeoStatusCode invalidStatusCode = GeoStatusCode.values()[-1];
	}
}