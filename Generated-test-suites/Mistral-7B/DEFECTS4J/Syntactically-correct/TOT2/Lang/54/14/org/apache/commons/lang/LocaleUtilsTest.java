package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test: valid input
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);

        // Test edge cases: empty input
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));

        // Test edge cases: invalid input
        try {
            LocaleUtils.toLocale("fr_CA_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test: valid input
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocales = Arrays.asList(
                baseLocale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.getDefault()
        );
        List<Locale> actualLocales = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(expectedLocales, actualLocales);

        // Test edge cases: null input
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertEmpty(LocaleUtils.localeLookupList(Locale.getDefault()));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test: valid input
        List<Locale> expectedLocales = LocaleUtils.availableLocaleList();
        List<Locale> actualLocales = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocales, actualLocales);

        // Test edge cases: empty list
        assertTrue(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test: valid input
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test edge cases: null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test edge cases: invalid input
        Locale invalidLocale = new Locale("fr", "CA", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test: valid input
        String countryCode = "US";
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "CA"),
                new Locale("en"),
                Locale.getDefault()
        );
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocales, actualLocales);

        // Test edge cases: null input
        assertNotNull(LocaleUtils.languagesByCountry(null));
        assertTrue(LocaleUtils.languagesByCountry("").isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test: valid input
        String languageCode = "en";
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "CA"),
                new Locale("en", "GB"),
                Locale.getDefault()
        );
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocales, actualLocales);

        // Test edge cases: null input
        assertNotNull(LocaleUtils.countriesByLanguage(null));
        assertTrue(LocaleUtils.countriesByLanguage("").isEmpty());
    }

    private void assertEmpty(Collection<?> collection) {
        assertTrue(collection.isEmpty());
    }

    private void assertNotNull(Object object) {
        assertNotNull(object, "Object should not be null");
    }
}