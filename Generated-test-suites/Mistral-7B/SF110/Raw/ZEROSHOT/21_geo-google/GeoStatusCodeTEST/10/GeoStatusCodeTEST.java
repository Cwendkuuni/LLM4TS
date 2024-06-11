package geo.google.datamodel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import geo.google.datamodel.GeoStatusCode;

/**
 * GeoStatusCodeTest class for testing the GeoStatusCode enum.
 *
 * @author [Your Name]
 */
public class GeoStatusCodeTest {

    /**
     * Test method for {@link GeoStatusCode#GeoStatusCode(int, String)}.
     */
    @Test
    public void testGeoStatusCodeConstructor() {
        for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
            int code = geoStatusCode.getCode();
            String description = geoStatusCode.getDescription();

            GeoStatusCode newGeoStatusCode = new GeoStatusCode(code, description);

            assertEquals(geoStatusCode, newGeoStatusCode);
            assertEquals(code, newGeoStatusCode.getCode());
            assertEquals(description, newGeoStatusCode.getDescription());
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getDescription()}.
     */
    @Test
    public void testGetDescription() {
        for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
            String description = geoStatusCode.getDescription();
            assertNotNull(description);
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getCodeName()}.
     */
    @Test
    public void testGetCodeName() {
        for (GeoStatusCode geoStatusCode : GeoStatusCode.values()) {
            String codeName = geoStatusCode.getCodeName();
            assertEquals(geoStatusCode.name(), codeName);
        }
    }

    /**
     * Test method for {@link GeoStatusCode#getStatusCode(int)}.
     */
    @Test
    public void testGetStatusCode() {
        for (int code : new int[] { 200, 400, 500, 601, 602, 603, 604, 610, 620 }) {
            GeoStatusCode geoStatusCode = GeoStatusCode.getStatusCode(code);
            assertNotNull(geoStatusCode);
        }

        try {
            GeoStatusCode geoStatusCode = GeoStatusCode.getStatusCode(-1);
            fail("Unexpected GeoStatusCode: " + geoStatusCode);
        } catch (NullPointerException e) {
            // Expected
        }
    }
}