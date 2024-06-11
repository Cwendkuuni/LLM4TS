package org.apache.commons.lang3.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang3.LocaleUtils;
import java.util.Locale;
import java.util.Arrays;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLocaleFormat() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_InvalidEmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidLocaleFormat_NoLanguage() {
        String invalidLocale = "_US";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidLocaleFormat_NoCountry() {
        String invalidLocale = "en";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidLocaleFormat_MissingLanguageUpperCase() {
        String invalidLocale = "en_fr";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidLocaleFormat_MissingCountryUpperCase() {
        String invalidLocale = "en_fr_CA";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidLocaleFormat_InvalidCountryCode() {
        String invalidLocale = "en_ABC";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.US);
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("fr", "FR");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale(""), "fr");
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        Locale locale = new Locale("fr", "FR");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale(""), Locale.ENGLISH);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_NullLocaleAndDefaultLocale() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH);
        List<Locale> actual = LocaleUtils.localeLookupList(null, Locale.ENGLISH);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("en", "ABC");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        String countryCode = "US";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        String countryCode = "";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        String languageCode = "";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}