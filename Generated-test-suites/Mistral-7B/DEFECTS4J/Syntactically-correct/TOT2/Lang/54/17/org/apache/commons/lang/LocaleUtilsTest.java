import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        List<String> validLocales = Arrays.asList(
                "en", "en_US", "en_GB", "fr_CA", "fr_FR", "es_ES", "de_DE"
        );
        for (String localeString : validLocales) {
            Locale expectedLocale = new Locale(localeString.substring(0, 2), localeString.substring(3));
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            assertEquals("Expected locale " + expectedLocale + " for input " + localeString, expectedLocale, actualLocale);
        }

        // Test invalid locale strings
        List<String> invalidLocales = Arrays.asList(
                "", "xx_XX", "en_xx", "en_GB_xxx", "en_GB_xx", "en_GB_xx_xx", "en_GB.xx"
        );
        for (String invalidLocaleString : invalidLocales) {
            try {
                LocaleUtils.toLocale(invalidLocaleString);
                fail("Expected IllegalArgumentException for input " + invalidLocaleString);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        Locale defaultLocale = Locale.getDefault();
        List<Locale> expectedLocales = Collections.emptyList();
        List<Locale> actualLocales = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals("Expected empty list for null input", expectedLocales, actualLocales);

        // Test valid input
        Locale locale = new Locale("fr", "CA");
        expectedLocales = Arrays.asList(
                new Locale("fr", "CA", "xxx"), locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale
        );
        actualLocales = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals("Expected " + expectedLocales + " for inputs " + locale + " and " + defaultLocale, expectedLocales, actualLocales);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test known locales
        List<Locale> knownLocales = Arrays.asList(
                new Locale("en", "US"), new Locale("fr", "CA"), new Locale("es", "ES"), new Locale("de", "DE")
        );
        for (Locale knownLocale : knownLocales) {
            assertTrue("Expected " + knownLocale + " to be available", LocaleUtils.isAvailableLocale(knownLocale));
        }

        // Test unknown locales
        List<Locale> unknownLocales = Arrays.asList(
                new Locale("xx", "XX"), new Locale("en", "XX"), new Locale("en", "GB_XX"), new Locale("en_GB", "XX")
        );
        for (Locale unknownLocale : unknownLocales) {
            assertFalse("Expected " + unknownLocale + " to not be available", LocaleUtils.isAvailableLocale(unknownLocale));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Test known country
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("fr", "CA"), new Locale("en", "CA")
        );
        List<Locale> actualLocales = LocaleUtils.languagesByCountry("CA");
        assertEquals("Expected " + expectedLocales + " for country CA", expectedLocales, actualLocales);

        // Test unknown country
        List<Locale> emptyLocales = Collections.emptyList();
        actualLocales = LocaleUtils.languagesByCountry("XX");
        assertEquals("Expected empty list for country XX", emptyLocales, actualLocales);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test known language
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("fr", "CA"), new Locale("fr", "FR"), new Locale("fr", "BE")
        );
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage("fr");
        assertEquals("Expected " + expectedLocales + " for language fr", expectedLocales, actualLocales);

        // Test unknown language
        List<Locale> emptyLocales = Collections.emptyList();
        actualLocales = LocaleUtils.countriesByLanguage("XX");
        assertEquals("Expected empty list for language XX", emptyLocales, actualLocales);
    }
}