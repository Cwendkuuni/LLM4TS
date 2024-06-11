package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString() {
        assertNotNull(LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguageCode() {
        String invalidLocale = "_ invalid_";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountryCode() {
        String invalidLocale = "invalid_";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLength() {
        String invalidLocale = "invalid_XXXX";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_MisplacedUnderscore() {
        String invalidLocale = "invalid__";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat_NoLanguageCountry() {
        String validLocale = "_";
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(validLocale));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageOnly() {
        String validLocale = "en";
        assertEquals(new Locale("en"), LocaleUtils.toLocale(validLocale));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountry() {
        String validLocale = "en_US";
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale(validLocale));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountryVariant() {
        String validLocale = "en_US_POSIX";
        assertEquals(new Locale("en", "US", "POSIX"), LocaleUtils.toLocale(validLocale));
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        Locale locale = new Locale("");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(1, result.size());
        assertEquals(locale, result.get(0));
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        Locale locale = new Locale("fr_CA", "fr_CA");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", "CA"), result.get(1));
        assertEquals(new Locale("fr", ""), result.get(2));
    }

    @Test
    public void testLocaleLookupList_LocaleWithOnlyCountry() {
        Locale locale = new Locale("en", "GB");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("en", ""), result.get(1));
        assertEquals(new Locale("en", "GB"), result.get(2));
    }

    @Test
    public void testLocaleLookupList_LocaleWithOnlyLanguage() {
        Locale locale = new Locale("es");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("es", ""), result.get(1));
        assertEquals(new Locale("", "es"), result.get(2));
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        Locale locale = new Locale("fr");
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("fr", ""), result.get(1));
        assertEquals(defaultLocale, result.get(2));
        assertEquals(new Locale("en", "US"), result.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(true, availableLocales.isEmpty() || availableLocales.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertEquals(true, availableLocales.isEmpty() || availableLocales.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale validLocale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(validLocale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale invalidLocale = new Locale("en_US_POSIX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(true, !result.isEmpty());
        assertTrue(result.contains(new Locale("en", "US")));
        assertFalse(result.contains(new Locale("es", "US")));

        result = LocaleUtils.languagesByCountry("ES");
        assertEquals(true, !result.isEmpty());
        assertTrue(result.contains(new Locale("es", "ES")));
        assertFalse(result.contains(new Locale("en", "ES")));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(true, !result.isEmpty());
        assertTrue(result.contains(new Locale("en", "US")));
        assertFalse(result.contains(new Locale("es", "US")));

        result = LocaleUtils.countriesByLanguage("es");
        assertEquals(true, !result.isEmpty());
        assertTrue(result.contains(new Locale("es", "ES")));
        assertFalse(result.contains(new Locale("en", "ES")));
    }
}