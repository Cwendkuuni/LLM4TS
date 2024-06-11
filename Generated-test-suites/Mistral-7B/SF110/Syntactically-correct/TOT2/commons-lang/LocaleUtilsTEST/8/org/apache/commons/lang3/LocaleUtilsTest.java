import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        // Test valid locales
        assertEquals(new Locale("en_US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr_FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("de_DE"), LocaleUtils.toLocale("de_DE"));

        // Test invalid locales with null or empty string
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid locales with invalid format
        try {
            LocaleUtils.toLocale("en_US#");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US__");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}