import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNotNull(LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
        assertEquals("", LocaleUtils.toLocale("").getCountry());
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        String invalidLocale = "_FR";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: _" + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_MissingUpperCase() {
        String invalidLocale = "fr_";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: fr_", e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLength() {
        String invalidLocale = "fr_FR_";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: fr_FR_", e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat() {
        Locale expectedLocale = new Locale("fr", "FR");
        Locale actualLocale = LocaleUtils.toLocale("fr_FR");
        assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
        assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        assertEmpty(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        Locale locale = new Locale("");
        List<Locale> expectedList = Collections.singletonList(locale);
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_InvalidLocale() {
        Locale locale = new Locale(" invalid-locale");
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("en", ""), new Locale("en"), new Locale(""));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale, new Locale(""));
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale invalidLocale = new Locale("invalid-locale");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_NullInput() {
        assertEmpty(LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        assertEmpty(LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testLanguagesByCountry_InvalidCountryCode() {
        assertEmpty(LocaleUtils.languagesByCountry("invalid-country-code"));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "BE"));
        List<Locale> actualList = LocaleUtils.languagesByCountry("FR");
        assertEquals(expectedList, actualList);
    }
}