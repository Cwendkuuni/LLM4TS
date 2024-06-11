package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locales
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de_DE", LocaleUtils.toLocale("de_DE").toString());

        // Test empty locale
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid locale format - missing separator
        try {
            LocaleUtils.toLocale("en_USx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid locale format - missing language or country
        try {
            LocaleUtils.toLocale("x_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid locale format - invalid country code
        try {
            LocaleUtils.toLocale("en_zZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid locale format - invalid language code
        try {
            LocaleUtils.toLocale("zZ_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertTrue(localeList.isEmpty());

        // Test single locale
        List<Locale> localeList1 = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(Locale.US, localeList1.get(0));

        // Test multiple locales
        List<Locale> localeList2 = LocaleUtils.localeLookupList(Locale.US, Locale.FRANCE);
        assertEquals(2, localeList2.size());
        assertEquals(Locale.US, localeList2.get(0));
        assertEquals(Locale.FRANCE, localeList2.get(1));

        // Test invalid locale
        List<Locale> localeList3 = LocaleUtils.localeLookupList(Locale.US, null);
        assertTrue(localeList3.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        // Assert list size
        assertEquals(176, availableLocaleList.size());

        // Assert some valid locales
        assertTrue(availableLocaleList.contains(Locale.US));
        assertTrue(availableLocaleList.contains(Locale.FRANCE));
        assertTrue(availableLocaleList.contains(Locale.GERMANY));
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        // Assert list size
        assertEquals(2, englishLocales.size());
        assertEquals(Locale.US, englishLocales.get(0));
        assertEquals(Locale.ENGLISH, englishLocales.get(1));

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
        // Assert list size
        assertEquals(1, frenchLocales.size());
        assertEquals(Locale.FRENCH, frenchLocales.get(0));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishCountries = LocaleUtils.countriesByLanguage("en");
        // Assert list size
        assertEquals(2, englishCountries.size());
        assertEquals(Locale.US, englishCountries.get(0));
        assertEquals(Locale.GB, englishCountries.get(1));

        List<Locale> frenchCountries = LocaleUtils.countriesByLanguage("fr");
        // Assert list size
        assertEquals(1, frenchCountries.size());
        assertEquals(Locale.CA, frenchCountries.get(0));
    }
}