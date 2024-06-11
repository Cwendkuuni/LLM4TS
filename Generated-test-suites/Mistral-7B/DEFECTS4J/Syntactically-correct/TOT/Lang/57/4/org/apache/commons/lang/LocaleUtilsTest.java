import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));

        // Test empty input
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid input
        try {
            LocaleUtils.toLocale("en_US_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US__");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_invalidCountry");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_invalidLanguage");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "CA"),
                new Locale("en"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("de"),
                Locale.getDefault());

        List<Locale> result = LocaleUtils.localeLookupList(new Locale("en", "US", "en"), null);
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = Arrays.asList(
                new Locale("en", "US"),
                new Locale("fr", "FR"),
                new Locale("de", "DE"),
                new Locale("en", "GB"),
                new Locale("es", "ES"),
                new Locale("it", "IT"),
                new Locale("ja", "JP"),
                new Locale("ko", "KR"),
                new Locale("zh", "CN"),
                new Locale("zh", "TW"));

        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("es"));
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testLanguagesByCountryNull() {
        LocaleUtils.languagesByCountry(null);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"));
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testCountriesByLanguageNull() {
        LocaleUtils.countriesByLanguage(null);
    }
}