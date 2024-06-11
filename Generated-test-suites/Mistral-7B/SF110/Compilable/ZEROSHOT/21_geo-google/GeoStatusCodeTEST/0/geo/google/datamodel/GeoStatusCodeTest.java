package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

	@Test
	public void testGeoStatusCode_ValidCases() {
		// Test valid cases using getStatusCode() method
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
		// Test exception for invalid code
		GeoStatusCode.getStatusCode(-1);
	}

	@Test
	public void testGetCodeName() {
		// Test getCodeName() method for each GeoStatusCode
		assertEquals("G_GEO_SUCCESS", GeoStatusCode.G_GEO_SUCCESS.getCodeName());
		assertEquals("G_GEO_BAD_REQUEST", GeoStatusCode.G_GEO_BAD_REQUEST.getCodeName());
		// ... and so on for all other GeoStatusCode
	}

	@Test
	public void testEnumValues() {
		// Test all values of GeoStatusCode enum
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			assertNotNull(statusCode);
			assertEquals(statusCode.getClass(), GeoStatusCode.class);
			assertEquals(statusCode.getCode(), statusCode.ordinal() + 200);
			assertEquals(statusCode.name(), statusCode.getCodeName());
			System.out.println(statusCode + ":\t" + statusCode.getDescription());
		}
	}

	@Test
	public void testGetDescription() {
		// Test getDescription() method for each GeoStatusCode
		assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)", GeoStatusCode.G_GEO_SUCCESS.getDescription());
		// ... and so on for all other GeoStatusCode
	}
}