package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

/**
 * Test class for GeoStatusCode enum.
 */
public class GeoStatusCodeTest {

    /**
     * Test method for {@link GeoStatusCode#GeoStatusCode(int, String)}.
     */
    @Test
    public void testGeoStatusCodeConstructor() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertNotNull(statusCode);
            assertEquals(statusCode.getCode(), statusCode.hashCode());
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getCode()}.
     */
    @Test
    public void testGetCodeMethod() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertEquals(statusCode.getCode(), statusCode.getCode());
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getCodeName()}.
     */
    @Test
    public void testGetCodeNameMethod() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertEquals(statusCode.name(), statusCode.getCodeName());
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getDescription()}.
     */
    @Test
    public void testGetDescriptionMethod() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertNotNull(statusCode.getDescription());
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getStatusCode(int)}.
     */
    @Test
    public void testGetStatusCodeMethod() {
        for (int code : new int[] { 200, 400, 500, 601, 602, 603, 604, 610, 620, Integer.MIN_VALUE }) {
            GeoStatusCode statusCode = GeoStatusCode.getStatusCode(code);
            assertNotNull(statusCode);
            assertEquals(code, statusCode.getCode());
        }

        assertNull(GeoStatusCode.getStatusCode(-1));
    }
}