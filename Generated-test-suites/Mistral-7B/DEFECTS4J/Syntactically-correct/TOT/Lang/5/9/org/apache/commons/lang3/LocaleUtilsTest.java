package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid input
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Valid input with empty country and language
        expected = new Locale("", "");
        actual = LocaleUtils.toLocale("");
        assertEquals(expected, actual);

        // Valid input with empty language and country
        expected = new Locale("", "US");
        actual = LocaleUtils.toLocale("_US");
        assertEquals(expected, actual);

        // Valid input with empty language, empty country and variant
        expected = new Locale("");
        actual = LocaleUtils.toLocale("");
        assertEquals(expected, actual);

        // Valid input with language and country only
        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        // Valid input with language, country and variant
        expected = new Locale("fr", "CA", "xx");
        actual = LocaleUtils.toLocale("fr_CA_xx");
        assertEquals(expected, actual);

        // Invalid input with missing language code
        try {
            actual = LocaleUtils.toLocale("CA_US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input with missing country code
        try {
            actual = LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input with missing separator
        try {
            actual = LocaleUtils.toLocale("enCA");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input with empty string
        try {
            actual = LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input with single character language code
        try {
            actual = LocaleUtils.toLocale("a");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input with single character country code
        try {
            actual = LocaleUtils.toLocale("US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input with separator other than underscore
        try {
            actual = LocaleUtils.toLocale("en_CA_xx_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input with length less than 3
        try {
            actual = LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid input with length greater than 5
        try {
            actual = LocaleUtils.toLocale("en_GB_US_xx_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test with valid locale
        Locale locale = new Locale("fr", "CA", "xx");
        List<Locale> expected = LocaleUtils.localeLookupList(locale);
        List<Locale> actual = new ArrayList<>();
        actual.add(locale);
        actual.add(new Locale("fr", "CA"));
        actual.add(new Locale("fr"));
        assertEquals(expected, actual);

        // Test with null locale
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);

        // Test with empty locale
        locale = new Locale("");
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Test with empty variant
        locale = new Locale("fr", "CA");
        expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        actual = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expected, actual);

        // Test with empty country
        locale = new Locale("en");
        expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("en"));
        expected.add(new Locale(""));
        actual = LocaleUtils.localeLookupList(locale, new Locale(""));
        assertEquals(expected, actual);

        // Test with empty language
        locale = new Locale("", "CA");
        expected = new ArrayList<>();
        expected.add(new Locale(""));
        expected.add(new Locale("en"));
        expected.add(new Locale("fr"));
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);

        // Test with empty country and language
        locale = new Locale("");
        expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("en"));
        expected.add(new Locale("fr"));
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);

        // Test with default locale
        locale = new Locale("fr", "CA", "xx");
        expected = new ArrayList<>();
        expected.add(locale);
        expected.add(new Locale("fr", "CA"));
        expected.add(new Locale("fr"));
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);
        actual = LocaleUtils.localeLookupList(locale, new Locale(""));
        expected.add(new Locale(""));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = java.util.Locale.getAvailableLocales();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = java.util.Locale.getAvailableLocales();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale("en", "US"));
        locales.add(new Locale("fr", "FR"));
        locales.add(new Locale("es", "ES"));
        LocaleUtils.availableLocaleList = () -> locales;

        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "GB")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es", "MX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = LocaleUtils.languagesByCountry("CA");
        List<Locale> actual = new ArrayList<>();
        actual.add(new Locale("en", "CA"));
        actual.add(new Locale("fr", "CA"));
        assertEquals(expected, actual);

        // Test with empty country code
        expected = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, actual);

        // Test with empty string as country code
        expected = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry("");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = LocaleUtils.countriesByLanguage("en");
        List<Locale> actual = new ArrayList<>();
        actual.add(new Locale("US", "en"));
        actual.add(new Locale("CA", "en"));
        assertEquals(expected, actual);

        // Test with empty language code
        expected = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, actual);

        // Test with empty string as language code
        expected = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage("");
        assertEquals(expected, actual);
    }
}