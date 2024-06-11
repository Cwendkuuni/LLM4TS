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
			assertEquals("Expected status code to match the actual status code for given status code", expectedStatusCode, actualStatusCode);
		}

		// Test unknown status code
		int unknownCode = -1;
		assertNull("Expected null for unknown status code", GeoStatusCode.getStatusCode(unknownCode));
	}

	@Test
	public void testGetStatusCodeByName() {
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			String name = statusCode.name();
			int code = statusCode.getCode();
			GeoStatusCode expectedStatusCode = statusCode;
			GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
			assertEquals("Expected status code to match the actual status code for given status code name", expectedStatusCode, actualStatusCode);
		}

		// Test unknown status code name
		String unknownName = "UNKNOWN_STATUS";
		assertNull("Expected null for unknown status code name", GeoStatusCode.getStatusCode(unknownName));
	}
}