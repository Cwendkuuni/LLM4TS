import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        String invalidLocale = "_invalidCountry";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        String invalidLocale = "invalidLanguage_";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguage() {
        String invalidLocale = "invalidLanguage__Canada";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_EmptyCountry() {
        String invalidLocale = "en_";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_ValidFormat_EmptyVariant() {
        Locale expected = new Locale("fr", "");
        Locale actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_ValidFormat_ThreeElements() {
        Locale expected = new Locale("fr_CA", "Quebec");
        Locale actual = LocaleUtils.toLocale("fr_CA_Quebec");
        assertEquals(expected, actual);
    }

    
}