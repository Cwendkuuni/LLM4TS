import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class GeoStatusCodeTest
{
    @Test
    public void testGetStatusCode_ValidCode_ReturnsCorrectEnum()
    {
        final GeoStatusCode expectedEnum = GeoStatusCode.values()[0]; // G_GEO_SUCCESS
        final int inputCode = expectedEnum.getCode();
        final GeoStatusCode actualEnum = GeoStatusCode.getStatusCode(inputCode);

        assertEquals(expectedEnum, actualEnum);
    }

    @Test
    public void testGetStatusCode_InvalidCode_ReturnsUnknownStatus()
    {
        final GeoStatusCode expectedEnum = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        final int inputCode = 100;
        final GeoStatusCode actualEnum = GeoStatusCode.getStatusCode(inputCode);

        assertEquals(expectedEnum, actualEnum);
    }

    @Test
    public void testGetStatusCode_NullInput_ThrowsNullPointerException()
    {
        assertNull(GeoStatusCode.getStatusCode(null));
    }

    @Test(expected = NullPointerException.class)
    public void testGetStatusCode_NegativeCode_ThrowsNullPointerException()
    {
        GeoStatusCode.getStatusCode(-1);
    }

    @Test
    public void testGetCode_ValidEnum_ReturnsCorrectCode()
    {
        final GeoStatusCode enumWithValidCode = GeoStatusCode.G_GEO_SUCCESS;
        final int expectedCode = enumWithValidCode.getCode();
        final int actualCode = enumWithValidCode.getCode();

        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCodeName_ValidEnum_ReturnsCorrectName()
    {
        final GeoStatusCode enumWithValidName = GeoStatusCode.G_GEO_SUCCESS;
        final String expectedName = "G_GEO_SUCCESS";
        final String actualName = enumWithValidName.getCodeName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetDescription_ValidEnum_ReturnsCorrectDescription()
    {
        final GeoStatusCode enumWithValidDescription = GeoStatusCode.G_GEO_SUCCESS;
        final String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
        final String actualDescription = enumWithValidDescription.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testGetDescription_UnknownEnum_ReturnsEmptyString()
    {
        final GeoStatusCode unknownEnum = GeoStatusCode.G_GEO_UNKOWN_STATUS;
        final String expectedDescription = "";
        final String actualDescription = unknownEnum.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }
}