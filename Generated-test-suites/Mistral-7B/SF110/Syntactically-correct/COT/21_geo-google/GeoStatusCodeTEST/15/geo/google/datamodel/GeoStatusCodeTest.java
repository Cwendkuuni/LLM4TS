package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

/**
 * Test class for GeoStatusCode enum class.
 */
public class GeoStatusCodeTest {

    /**
     * Test method for {@link GeoStatusCode#GeoStatusCode(int, String)} constructor.
     */
    @Test
    public void testConstructor() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertEquals(statusCode.getCode(), statusCode.newInstance(statusCode.getCode(), statusCode.getDescription()).getCode());
            assertEquals(statusCode.getDescription(), statusCode.newInstance(statusCode.getCode(), statusCode.getDescription()).getDescription());
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
     * Test method for {@link GeoStatusCode#getStatusCode(int)} static method.
     */
    @Test
    public void testGetStatusCode() {
        for (GeoStatusCode statusCode : GeoStatusCode.values()) {
            assertNull(GeoStatusCode.getStatusCode(statusCode.getCode() - 1));
            assertEquals(statusCode, GeoStatusCode.getStatusCode(statusCode.getCode()));
        }

        assertNull(GeoStatusCode.getStatusCode(-1));
        assertNull(GeoStatusCode.getStatusCode(Integer.MAX_VALUE));
    }

    /**
     * Test edge cases for {@link GeoStatusCode#getStatusCode(int)} method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetStatusCodeEdgeCases() {
        for (int code : new int[] { Integer.MIN_VALUE, 0 }) {
            GeoStatusCode.getStatusCode(code);
        }
    }
}