package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

	@Test
	public void testGetStatusCode_ValidCodes() {
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			int expectedCode = statusCode.getCode();
			GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(expectedCode);
			assertEquals(expectedCode, actualStatusCode.getCode());
		}
	}

	@Test
	public void testGetStatusCode_InvalidCode() {
		int invalidCode = -1;
		GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(invalidCode);
		assertEquals(GeoStatusCode.G_GEO_UNKOWN_STATUS, actualStatusCode);
	}

	@Test
	public void testGetStatusCode_ValidNames() {
		for (GeoStatusCode statusCode : GeoStatusCode.values()) {
			String expectedName = statusCode.getCodeName();
			GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(Integer.parseInt(expectedName));
			assertEquals(expectedName, actualStatusCode.getCodeName());
		}
	}

	@Test(expected = NumberFormatException.class)
	public void testGetStatusCode_InvalidName() {
		String invalidName = "X_GEO_STATUS";
		GeoStatusCode.getStatusCode(Integer.parseInt(invalidName));
	}
}