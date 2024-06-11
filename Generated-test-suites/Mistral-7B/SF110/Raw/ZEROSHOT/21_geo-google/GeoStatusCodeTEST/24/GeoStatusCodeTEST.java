package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

	@Test
	public void testGetStatusCodeByCode() {
		// Test valid codes
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			assertEquals(statusCode, GeoStatusCode.getStatusCode(statusCode.getCode()));
		}

		// Test invalid code
		assertNull(GeoStatusCode.getStatusCode(-1));
	}

	@Test
	public void testGetCodeByName() {
		// Test valid names
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			assertEquals(statusCode.getCode(), GeoStatusCode.getStatusCode(statusCode.name()).getCode());
		}

		// Test invalid name
		assertNull(GeoStatusCode.getStatusCode("INVALID_NAME"));
	}

	@Test
	public void testGetDescription() {
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			assertEquals(statusCode.getDescription(), GeoStatusCode.getStatusCode(statusCode.getCode()).getDescription());
		}
	}
}