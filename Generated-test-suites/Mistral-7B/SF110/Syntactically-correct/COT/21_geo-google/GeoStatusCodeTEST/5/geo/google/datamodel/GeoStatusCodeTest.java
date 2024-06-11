package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

	// Test all getter methods of GeoStatusCode enum
	@Test
	public void testGetters(){
		int code = GeoStatusCode.G_GEO_SUCCESS.getCode();
		String codeName = GeoStatusCode.G_GEO_SUCCESS.getCodeName();
		String description = GeoStatusCode.G_GEO_SUCCESS.getDescription();

		assertEquals(200, code);
		assertEquals("G_GEO_SUCCESS", codeName);
		assertEquals("No errors occurred; the address was successfully parsed and its geocode has been returned.", description);

		// Repeat the test for other enum values
	}

	// Test getStatusCode method with valid codes
	@Test
	public void testGetStatusCodeValidCode(){
		GeoStatusCode status = GeoStatusCode.getStatusCode(200);
		assertEquals(GeoStatusCode.G_GEO_SUCCESS, status);
	}

	// Test getStatusCode method with invalid code
	@Test(expected = IllegalArgumentException.class)
	public void testGetStatusCodeInvalidCode(){
		GeoStatusCode status = GeoStatusCode.getStatusCode(700);
	}

	// Test edge cases and exception handling scenarios
	@Test(expected = IllegalArgumentException.class)
	public void testGetStatusCodeWithNegativeCode(){
		GeoStatusCode status = GeoStatusCode.getStatusCode(-1);
	}

	@Test
	public void testNullCodePassedToGetStatusCode(){
		assertNull(GeoStatusCode.getStatusCode(null));
	}
}