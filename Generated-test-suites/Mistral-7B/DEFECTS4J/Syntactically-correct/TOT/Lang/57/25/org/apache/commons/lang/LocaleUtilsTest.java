package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locales
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "FR");
        actual = LocaleUtils.toLocale("fr_FR");
        assertEquals(expected, actual);

        expected = new Locale("es", "ES");
        actual = LocaleUtils.toLocale("es_ES");
        assertEquals(expected, actual);

        // Test invalid format
        try {
            Locale actual = LocaleUtils.toLocale("en_US_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_US_en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);

        // Test single locale
        Locale locale = new Locale("en", "US");
        expected = LocaleUtils.localeLookupList(locale);
        assertEquals(Collections.singletonList(locale), expected);

        // Test multiple locales
        expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en"));
        Locale defaultLocale = new Locale("fr", "FR");
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);

        // Test invalid locale
        Locale invalidLocale = null;
        expected = Arrays.asList(locale, defaultLocale);
        actual = LocaleUtils.localeLookupList(invalidLocale, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);

        // Test non-empty list
        List<Locale> locales = Arrays.asList(new Locale("en", "US"), new Locale("fr", "FR"), new Locale("es", "ES"));
        expected = locales;
        actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test invalid locale
        Locale invalidLocale = null;
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, actual);

        // Test single country
        String countryCode = "US";
        expected = Arrays.asList(new Locale("en", "US"));
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Test multiple countries
        countryCode = "FR, CA";
        expected = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "CA"));
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, actual);

        // Test single language
        String languageCode = "en";
        expected = Arrays.asList(new Locale("en", "US"));
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Test multiple languages
        languageCode = "fr, es";
        expected = Arrays.asList(new Locale("fr", "FR"), new Locale("es", "ES"));
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}