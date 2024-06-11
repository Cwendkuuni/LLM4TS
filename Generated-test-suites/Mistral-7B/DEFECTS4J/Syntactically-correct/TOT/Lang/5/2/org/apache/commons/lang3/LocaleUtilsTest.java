package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid inputs with lowercase language code
        try {
            LocaleUtils.toLocale("EN_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxx_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxx_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid inputs with non-underscore separator
        try {
            LocaleUtils.toLocale("en_GB_xxxxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid inputs with empty country code
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid inputs with empty language code
        try {
            LocaleUtils.toLocale("", "GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("", "GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("", "GB_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("", "GB_xxx_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertTrue(list.isEmpty());

        // Test base case
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        list = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(3, list.size());
        assertEquals(baseLocale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));

        // Test empty default locale
        list = LocaleUtils.localeLookupList(baseLocale, null);
        assertEquals(4, list.size());
        assertEquals(baseLocale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(Locale.getDefault(), list.get(3));

        // Test non-empty default locale
        Locale defaultLocale = new Locale("en");
        list = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(5, list.size());
        assertEquals(baseLocale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(defaultLocale, list.get(3));
        assertEquals(Locale.getDefault(), list.get(4));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(191, availableLocales.size());
        // Add some random checks for a few locales
        assertEquals(new Locale("en", "US"), availableLocales.get(0));
        assertEquals(new Locale("fr", "FR"), availableLocales.get(42));
        assertEquals(new Locale("de", "DE"), availableLocales.get(77));
        assertEquals(new Locale("es", "ES"), availableLocales.get(121));
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        // Test known locales
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));

        // Test unknown locales
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xy", "xy")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        assertEquals(11, languagesByCountry.size());
        // Add some random checks for a few locales
        assertEquals(new Locale("fr", "CA"), languagesByCountry.get(0));
        assertEquals(new Locale("en", "CA"), languagesByCountry.get(1));
        assertEquals(new Locale("es", "CA"), languagesByCountry.get(2));
        assertEquals(new Locale("it", "CA"), languagesByCountry.get(3));
        assertEquals(new Locale("nl", "CA"), languagesByCountry.get(4));
        assertEquals(new Locale("pt", "CA"), languagesByCountry.get(5));
        assertEquals(new Locale("fa", "CA"), languagesByCountry.get(6));
        assertEquals(new Locale("ru", "CA"), languagesByCountry.get(7));
        assertEquals(new Locale("pl", "CA"), languagesByCountry.get(8));
        assertEquals(new Locale("uk", "CA"), languagesByCountry.get(9));
        assertEquals(new Locale("zh_CN", "CA"), languagesByCountry.get(10));

        // Test empty country code
        List<Locale> languagesByEmptyCountry = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByEmptyCountry.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("fr");
        assertEquals(12, countriesByLanguage.size());
        // Add some random checks for a few locales
        assertEquals(new Locale("fr", "CA"), countriesByLanguage.get(0));
        assertEquals(new Locale("fr", "BE"), countriesByLanguage.get(1));
        assertEquals(new Locale("fr", "CH"), countriesByLanguage.get(2));
        assertEquals(new Locale("fr", "DZ"), countriesByLanguage.get(3));
        assertEquals(new Locale("fr", "FR"), countriesByLanguage.get(4));
        assertEquals(new Locale("fr", "GA"), countriesByLanguage.get(5));
        assertEquals(new Locale("fr", "LU"), countriesByLanguage.get(6));
        assertEquals(new Locale("fr", "MC"), countriesByLanguage.get(7));
        assertEquals(new Locale("fr", "MON"), countriesByLanguage.get(8));
        assertEquals(new Locale("fr", "Reunion"), countriesByLanguage.get(9));
        assertEquals(new Locale("fr", "TF"), countriesByLanguage.get(10));
        assertEquals(new Locale("fr", "VU"), countriesByLanguage.get(11));

        // Test empty language code
        List<Locale> countriesByEmptyLanguage = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByEmptyLanguage.isEmpty());
    }
}