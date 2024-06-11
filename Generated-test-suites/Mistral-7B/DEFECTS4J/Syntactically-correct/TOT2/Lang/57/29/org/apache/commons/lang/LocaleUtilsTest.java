import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    // Test method for toLocale(String)
    @Test
    public void testToLocale() {
        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test valid input
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "US"),
                new Locale("fr", "FR"),
                new Locale("es", "ES"),
                new Locale("de", "DE")
        );

        List<Locale> actualLocales = new ArrayList<>();
        actualLocales.add(LocaleUtils.toLocale("en_US"));
        actualLocales.add(LocaleUtils.toLocale("fr_FR"));
        actualLocales.add(LocaleUtils.toLocale("es_ES"));
        actualLocales.add(LocaleUtils.toLocale("de_DE"));

        assertEquals("ToLocale method did not return correct locales", expectedLocales, actualLocales);
    }

    // Test method for localeLookupList(Locale)
    @Test
    public void testLocaleLookupList() {
        // Test null input
        List<Locale> actualLocales = LocaleUtils.localeLookupList(null);
        assertTrue("LocaleLookupList method should return empty list for null input", actualLocales.isEmpty());

        // Test valid input
        Locale baseLocale = new Locale("fr", "CA");
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );

        List<Locale> actualLocales2 = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));

        assertEquals("LocaleLookupList method did not return correct locales", expectedLocales, actualLocales2);
    }

    // Test method for isAvailableLocale(Locale)
    @Test
    public void testIsAvailableLocale() {
        // Test null input
        assertFalse("IsAvailableLocale method should return false for null input", LocaleUtils.isAvailableLocale(null));

        // Test available locale
        Locale expectedLocale = new Locale("en", "US");
        assertTrue("IsAvailableLocale method did not return true for available locale", LocaleUtils.isAvailableLocale(expectedLocale));

        // Test invalid locale
        Locale invalidLocale = new Locale("xy", "xy");
        assertFalse("IsAvailableLocale method should return false for invalid locale", LocaleUtils.isAvailableLocale(invalidLocale));
    }

    // Test method for languagesByCountry(String)
    @Test
    public void testLanguagesByCountry() {
        // Test null input
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(null);
        assertTrue("LanguagesByCountry method should return empty list for null input", actualLocales.isEmpty());

        // Test valid input
        String countryCode = "US";
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "US"),
                new Locale("es", "US"),
                new Locale("fr", "US")
        );

        List<Locale> actualLocales2 = LocaleUtils.languagesByCountry(countryCode);

        assertEquals("LanguagesByCountry method did not return correct locales", expectedLocales, actualLocales2);
    }

    // Test method for countriesByLanguage(String)
    @Test
    public void testCountriesByLanguage() {
        // Test null input
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(null);
        assertTrue("CountriesByLanguage method should return empty list for null input", actualLocales.isEmpty());

        // Test valid input
        String languageCode = "en";
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("US"),
                new Locale("CA"),
                new Locale("GB")
        );

        List<Locale> actualLocales2 = LocaleUtils.countriesByLanguage(languageCode);

        assertEquals("CountriesByLanguage method did not return correct locales", expectedLocales, actualLocales2);
    }
}