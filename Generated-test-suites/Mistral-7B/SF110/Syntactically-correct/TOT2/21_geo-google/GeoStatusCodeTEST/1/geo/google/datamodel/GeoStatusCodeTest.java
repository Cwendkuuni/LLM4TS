package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.*;
import org.junit.Test;

import geo.google.datamodel.*;

public class GeoStatusCodeTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        // Set up before all tests
    }

    @AfterClass
    public static void tearDownAfterClass() {
        // Tear down after all tests
    }

    @Before
    public void setUp() {
        // Set up before each test
    }

    @After
    public void tearDown() {
        // Tear down after each test
    }

    @Test
    public void testGetCodeSuccess() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        int expectedCode = 200;
        int actualCode = status.getCode();
        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testGetCodeNameSuccess() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expectedName = "G_GEO_SUCCESS";
        String actualName = status.getCodeName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetDescriptionSuccess() {
        GeoStatusCode status = GeoStatusCode.G_GEO_SUCCESS;
        String expectedDescription = "No errors occurred; the address was successfully parsed and its geocode has been returned. (Since 2.55)";
        String actualDescription = status.getDescription();
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testGetStatusCodeSuccess() {
        int code = 200;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_SUCCESS;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeNegativeCode() {
        int code = -1;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
    }

    @Test
    public void testGetStatusCodeNullInput() {
        int code = 0;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertNotNull(status);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeNonExistingCode() {
        int code = 999;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
    }

    @Test
    public void testGetStatusCodeValidExistingCode() {
        int code = 601;
        GeoStatusCode expectedStatusCode = GeoStatusCode.G_GEO_MISSING_QUERY;
        GeoStatusCode actualStatusCode = GeoStatusCode.getStatusCode(code);
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testGetStatusCodeValidCodeName() {
        int code = 500;
        String expectedCodeName = "G_GEO_SERVER_ERROR";
        String actualCodeName = GeoStatusCode.getStatusCode(code).getCodeName();
        assertEquals(expectedCodeName, actualCodeName);
    }

    @Test
    public void testGetStatusCodeValidDescription() {
        int code = 400;
        String expectedDescription = "A directions request could not be successfully parsed. (Since 2.81)";
        String actualDescription = GeoStatusCode.getStatusCode(code).getDescription();
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testGetStatusCodeValidInputNull() {
        int code = null;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertNull(status);
    }

    @Test
    public void testGetStatusCodeValidInputEmpty() {
        int code = 0;
        GeoStatusCode status = GeoStatusCode.getStatusCode(code);
        assertNotNull(status);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        // Tear down after all tests
    }
}