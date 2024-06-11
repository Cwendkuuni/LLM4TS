package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

	@Test
	public void testValidStatusCodes() {
		int[] codes = { 200, 400, 500, 601, 602, 603, 604, 610, 620 };
		GeoStatusCode[] expectedStatus = { GeoStatusCode.G_GEO_SUCCESS, GeoStatusCode.G_GEO_BAD_REQUEST,
				GeoStatusCode.G_GEO_SERVER_ERROR, GeoStatusCode.G_GEO_MISSING_QUERY, GeoStatusCode.G_GEO_UNKNOWN_ADDRESS,
				GeoStatusCode.G_GEO_UNAVAILABLE_ADDRESS, GeoStatusCode.G_GEO_UNKNOWN_DIRECTIONS, GeoStatusCode.G_GEO_BAD_KEY,
				GeoStatusCode.G_GEO_TOO_MANY_QUERIES };

		for (int i = 0; i < codes.length; i++) {
			GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(codes[i]);
			assertEquals(expectedStatus[i], actualStatusCode);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidStatusCode() {
		GeoStatusCode.getStatusCode(-1);
	}

	@Test
	public void testGetCodeName() {
		assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
		assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
		// Additional checks for all the status codes
	}

	@Test
	public void testGetDescription() {
		assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)",
				GeoStatusCode.G_GEO_SUCCESS.getDescription());
		assertEquals("A directions request could not be successfully parsed. (Since 2.81)",
				GeoStatusCode.G_GEO_BAD_REQUEST.getDescription());
		// Additional checks for all the status codes
	}
}