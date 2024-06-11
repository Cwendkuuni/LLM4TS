import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));
        
        // Test valid locale formats
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr_CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es_ES"));
        
        // Test invalid locale formats
        try {
            LocaleUtils.toLocale("abc");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: abc", e.getMessage());
        }
        
        try {
            LocaleUtils.toLocale("en_US#");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_US#", e.getMessage());
        }

        try {
            LocaleUtils.toLocale("en_US_x");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: en_US_x", e.getMessage());
        }
    }
}