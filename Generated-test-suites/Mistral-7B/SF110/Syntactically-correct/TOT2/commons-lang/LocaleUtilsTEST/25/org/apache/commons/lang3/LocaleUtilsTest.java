import org.junit.Test;
import org.junit.Assert.*;
import java.util.Locale;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        // Test valid locales
        assertEquals(new Locale("en_US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr_FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("es_ES"), LocaleUtils.toLocale("es_ES"));

        // Test empty or null string
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid locale format
        try {
            LocaleUtils.toLocale("fr_ES#");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: fr_ES#", e.getMessage());
        }

        try {
            LocaleUtils.toLocale("es");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: es", e.getMessage());
        }
    }
}