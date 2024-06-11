package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest {

	@Test
	public void testGetStatusCodeByCode() {
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			int code = statusCode.getCode();
			GeoStatusCode expectedStatusCode = statusCode;
			GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
			assertEquals("Unexpected status code for code: " + code, expectedStatusCode, actualStatusCode);
		}

		// Test unknown code
		assertNull("Unexpected status code for unknown code", GeoStatusCode.getStatusCode(Integer.MAX_VALUE));
	}

	@Test
	public void testGetDescription() {
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			String expectedDescription = statusCode.getDescription();
			String actualDescription = statusCode.name();
			assertEquals("Unexpected description for status code: " + statusCode, expectedDescription, actualDescription);
		}
	}
}