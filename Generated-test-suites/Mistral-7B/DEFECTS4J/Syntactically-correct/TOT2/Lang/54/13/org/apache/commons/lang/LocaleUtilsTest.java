import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.Locale;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for LocaleUtils class
 */
public class LocaleUtilsTest {

    /**
     * Test the toLocale method when given a valid locale string
     */
    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    /**
     * Test the toLocale method when given an invalid locale string
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_invalid");
    }

    /**
     * Test the localeLookupList method with a known locale
     */
    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = LocaleUtils.localeLookupList(new Locale("en", "US"));
        List<Locale> actual = new ArrayList<>();
        actual.add(new Locale("en", "US"));
        actual.add(new Locale("en", "US", ""));
        actual.add(new Locale("en", ""));
        actual.add(new Locale("en"));
        assertEquals(expected, actual);
    }

    /**
     * Test the localeLookupList method with an unknown locale
     */
    @Test
    public void testLocaleLookupListUnknownLocale() {
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("xy", ""));
        assertTrue(actual.isEmpty());
    }

    /**
     * Test the availableLocaleList method
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertNotNull(actual);
        assertEquals(324, actual.size()); // Assuming there are 324 available locales
    }

    /**
     * Test the isAvailableLocale method
     */
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        Locale invalidLocale = new Locale("xy", "xy");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    /**
     * Test the languagesByCountry method
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(
                new Locale("en", "US"), // English (United States)
                new Locale("es", "US"), // Spanish (United States)
                // Add other languages available for US here
                new Locale("fa", "IR") // Persian (Iran) as an edge case
        );
        assertEquals(expected, actual);
    }

    /**
     * Test the countriesByLanguage method
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(
                new Locale("US", "en"), // United States (English)
                new Locale("CA", "en"), // Canada (English)
                // Add other countries available for English here
                new Locale("GB", "en") // United Kingdom (English) as an edge case
        );
        assertEquals(expected, actual);
    }
}