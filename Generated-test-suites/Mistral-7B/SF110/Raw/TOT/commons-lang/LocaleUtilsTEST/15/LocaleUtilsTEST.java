import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidFormat() {
        // Test valid formats with language and country
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Test valid formats with language only
        expected = new Locale("fr");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        // Test valid formats with language and empty country
        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr_");
        assertEquals(expected, actual);

        // Test valid formats with language, country, and variant
        expected = new Locale("es", "ES", "Castilian");
        actual = LocaleUtils.toLocale("es_ES_Castilian");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_InvalidFormat() {
        String[] invalidFormats = {"_invalid", "en_", "en_US#", "en_US_Castilian"};
        for (String invalidFormat : invalidFormats) {
            try {
                LocaleUtils.toLocale(invalidFormat);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("es", "ES");
        List<Locale> expected = Arrays.asList(locale, new Locale("es", ""), new Locale(""), new Locale("es"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(new Locale("es", "ES"), new Locale("es", ""), new Locale(""), new Locale("es"), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("es", "ES"), defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale(""));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        int size = expected.size();
        // Perform some checks on elements
        assertTrue(expected.contains(Locale.ENGLISH));
        assertTrue(expected.contains(Locale.FRENCH));
        // Check size
        assertEquals(size, expected.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        int size = expected.size();
        // Perform some checks on elements
        assertTrue(expected.contains(Locale.ENGLISH));
        assertTrue(expected.contains(Locale.FRENCH));
        // Check size
        assertEquals(size, expected.size());
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("es", "ES");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("invalid", "country");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "ES";
        List<Locale> expected = Arrays.asList(new Locale("es", "ES"), new Locale("es", "ES"), new Locale("es", "ES"), new Locale("es", "ES"), new Locale("es", "ES"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "es";
        List<Locale> expected = Arrays.asList(new Locale("es", "ES"), new Locale("es", "ES"), new Locale("es", "ES"), new Locale("es", "ES"), new Locale("es", "ES"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}