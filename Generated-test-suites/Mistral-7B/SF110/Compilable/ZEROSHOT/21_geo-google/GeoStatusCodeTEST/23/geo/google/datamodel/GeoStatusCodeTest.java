package geo.google.datamodel.test;

import static org.junit.Assert.*;

import geo.google.datamodel.GeoStatusCode;

import org.junit.Test;

public class GeoStatusCodeTest {

// Testing getStatusCode method with valid codes
@Test
public void testGetStatusCodeWithValidCodes() {
 for (GeoStatusCode statusCode : GeoStatusCode.values()) {
 int code = statusCode.getCode();
 GeoStatusCode expectedStatusCode = statusCode;
 GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
 assertEquals("Expected and actual status codes should match", expectedStatusCode, actualStatusCode);
 }
}

// Testing getStatusCode method with an invalid code
@Test(expected = IllegalStateException.class)
public void testGetStatusCodeWithInvalidCode() {
 GeoStatusCode statusCode = GeoStatusCode.getStatusCode(1234);
 fail("Unexpected valid status code returned for invalid code");
}

// Testing getCodeName method for all status codes
@Test
public void testGetCodeNameForAllStatusCodes() {
 for (GeoStatusCode statusCode : GeoStatusCode.values()) {
 String expectedCodeName = statusCode.name();
 String actualCodeName = statusCode.getCodeName();
 assertEquals("Expected and actual code names should match", expectedCodeName, actualCodeName);
 }
}

// Testing getDescription method for all status codes
@Test
public void testGetDescriptionForAllStatusCodes() {
 for (GeoStatusCode statusCode : GeoStatusCode.values()) {
 String expectedDescription = statusCode.getDescription();
 String actualDescription = statusCode.getDescription();
 assertEquals("Expected and actual descriptions should match", expectedDescription, actualDescription);
 }
}

}