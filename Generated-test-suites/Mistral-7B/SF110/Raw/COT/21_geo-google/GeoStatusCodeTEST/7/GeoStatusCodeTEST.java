package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

/**
 * GeoStatusCodeTest.java - JUnit test cases for GeoStatusCode enum class
 */
public class GeoStatusCodeTest {

    /**
     * Test method for {@link GeoStatusCode#GeoStatusCode(int, String)} constructor.
     */
    @Test
    public void testConstructor() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertEquals(statusCode.getCode(), statusCode.newInstance(statusCode.getCode(), statusCode.getDescription()).getCode());
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getCode()} method.
     */
    @Test
    public void testGetCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertEquals(statusCode.getCode(), statusCode.getCode());
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getCodeName()} method.
     */
    @Test
    public void testGetCodeName() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertEquals(statusCode.name(), statusCode.getCodeName());
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getDescription()} method.
     */
    @Test
    public void testGetDescription() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertEquals(statusCode.getDescription(), statusCode.getDescription());
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getStatusCode(int)} method.
     */
    @Test
    public void testGetStatusCode() {
        for (int code : new int[] { 200, 400, 500, 601, 602, 603, 604, 610, 620, -1 }) {
            GeoStatusCode expected = GeoStatusCode.getStatusCode(code);
            GeoStatusCode actual = GeoStatusCode.getStatusCode(code);
            assertEquals(expected.getCode(), actual.getCode());
        }

        assertNull(GeoStatusCode.getStatusCode(-100));
    }

    /**
     * Test for {@link GeoStatusCode#G_GEO_UNKOWN_STATUS} when invalid code is passed.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeWithInvalidCode() {
        GeoStatusCode.getStatusCode(-100);
    }
}