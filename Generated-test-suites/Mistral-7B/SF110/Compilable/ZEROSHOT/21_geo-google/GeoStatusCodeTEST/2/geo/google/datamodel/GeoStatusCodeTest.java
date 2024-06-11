package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

	@Test
	public void testGetStatusCodeByCode() {
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			int code = statusCode.getCode();
			GeoStatusCode expected = statusCode;
			GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
			assertEquals("Expected and actual status codes do not match for code " + code, expected, actual);
		}
	}

	@Test
	public void testGetStatusCodeByCodeName() {
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			String codeName = statusCode.getCodeName();
			GeoStatusCode expected = statusCode;
			GeoStatusCode actual = GeoStatusCode.valueOf(codeName);
			assertEquals("Expected and actual status codes do not match for code name " + codeName, expected, actual);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetUnknownStatusCode() {
		GeoStatusCode.getStatusCode(-1);
	}

	@Test
	public void testGetGeoSuccessStatus() {
		GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
		assertEquals("Expected status code for success is 200", 200, status.getCode());
		assertEquals("Expected description for success is No errors occurred...", "No errors occurred; the address was successfully parsed and its geocode has been returned.", status.getDescription());
	}

	@Test
	public void testGetGeoBadRequestStatus() {
		GeoStatusCode status = GeoStatusCode.G_GEO_BAD_REQUEST;
		assertEquals("Expected status code for bad request is 400", 400, status.getCode());
		assertEquals("Expected description for bad request is A directions request could not be successfully parsed...", "A directions request could not be successfully parsed. (Since 2.81)", status.getDescription());
	}

	// Add more test cases for other status codes here...

	@Test
	public void testToString() {
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			String expected = statusCode.toString();
			String actual = statusCode.name();
			assertEquals("Expected and actual string representations do not match for status code " + statusCode, expected, actual);
		}
	}
}