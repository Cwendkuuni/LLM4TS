package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit. Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

public class GeoStatusCodeTest
{
    @Test
    public void testGetStatusCode_ValidCode()
    {
        int code = 200;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);

        assertEquals("Expected G_GEO_SUCCESS for code 200", expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCode_InvalidCode1()
    {
        int code = 100;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);

        assertEquals("Expected G_GEO_UNKOWN_STATUS for code 100", expectedStatusCode, actualStatusCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCode_InvalidCode2()
    {
        int code = -5;
        GeoStatusCode.getStatusCode(code);
    }

    @Test
    public void testGetCodeName_ValidStatusCode()
    {
        String expectedStatusCodeName = "G_GEO_SUCCESS";
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String actualStatusCodeName = statusCode.getCodeName();

        assertEquals("Expected G_GEO_SUCCESS for G_GEO_SUCCESS", expectedStatusCodeName, actualStatusCodeName);
    }

    @Test
    public void testGetCodeName_InvalidStatusCode()
    {
        String expectedStatusCodeName = "G_GEO_UNKOWN_STATUS";
        String actualStatusCodeName = GeoStatusCode.G_GEO_UNKOWN_STATUS.getCodeName();

        assertEquals("Expected G_GEO_UNKOWN_STATUS for G_GEO_UNKOWN_STATUS", expectedStatusCodeName, actualStatusCodeName);
    }

    @Test
    public void testGetDescription_ValidStatusCode()
    {
        String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
        GeoStatusCode statusCode = GeoStatusCode.G_GEO_SUCCESS;
        String actualDescription = statusCode.getDescription();

        assertEquals("Expected 'No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)' for G_GEO_SUCCESS", expectedDescription, actualDescription);
    }

    @Test
    public void testGetDescription_InvalidStatusCode()
    {
        String expectedDescription = "Uknown Status";
        String actualDescription = GeoStatusCode.G_GEO_UNKOWN_STATUS.getDescription();

        assertEquals("Expected 'Uknown Status' for G_GEO_UNKOWN_STATUS", expectedDescription, actualDescription);
    }
}