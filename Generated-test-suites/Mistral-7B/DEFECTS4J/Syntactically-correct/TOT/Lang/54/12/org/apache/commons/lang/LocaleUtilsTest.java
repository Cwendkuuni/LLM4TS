package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de", LocaleUtils.toLocale("de").toString());

        // Test empty input
        assertNull(LocaleUtils.toLocale(null));
        assertEquals("", LocaleUtils.toLocale("").toString());

        // Test invalid input
        try {
            LocaleUtils.toLocale("aa_ZZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test no default locale
        List localeList = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(3, localeList.size());
        assertEquals(Locale.FRENCH, localeList.get(0));
        assertEquals(Locale.FRENCH, localeList.get(1));
        assertEquals(Locale.ENGLISH, localeList.get(2));

        // Test with default locale
        List localeList2 = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(4, localeList2.size());
        assertEquals(Locale.FRENCH, localeList2.get(0));
        assertEquals(Locale.FRENCH, localeList2.get(1));
        assertEquals(Locale.FRENCH, localeList2.get(2));
        assertEquals(Locale.ENGLISH, localeList2.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(54, availableLocaleList.size());
        assertTrue(availableLocaleList.contains(Locale.ENGLISH));
        assertTrue(availableLocaleList.contains(Locale.FRENCH));
        assertTrue(availableLocaleList.contains(Locale.GERMAN));

        // Test empty list
        List emptyLocaleList = LocaleUtils.availableLocaleList();
        assertTrue(emptyLocaleList.isEmpty());

        // Test null list
        List nullLocaleList = null;
        try {
            LocaleUtils.availableLocaleList(nullLocaleList);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));

        assertFalse(LocaleUtils.isAvailableLocale(new Locale("aa", "ZZ")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("US");
        assertEquals(2, languagesByCountry.size());
        assertEquals(Locale.ENGLISH, languagesByCountry.get(0));
        assertEquals(Locale.ENGLISH, languagesByCountry.get(1));

        List<Locale> languagesByCountry2 = LocaleUtils.languagesByCountry("FR");
        assertEquals(1, languagesByCountry2.size());
        assertEquals(Locale.FRENCH, languagesByCountry2.get(0));

        List<Locale> emptyLanguagesByCountry = LocaleUtils.languagesByCountry(null);
        assertTrue(emptyLanguagesByCountry.isEmpty());

        List<Locale> nullLanguagesByCountry = null;
        try {
            LocaleUtils.languagesByCountry(nullLanguagesByCountry);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, countriesByLanguage.size());
        assertEquals(Locale.US, countriesByLanguage.get(0));
        assertEquals(Locale.GB, countriesByLanguage.get(1));

        List<Locale> countriesByLanguage2 = LocaleUtils.countriesByLanguage("fr");
        assertEquals(1, countriesByLanguage2.size());
        assertEquals(Locale.FRANCE, countriesByLanguage2.get(0));

        List<Locale> emptyCountriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assertTrue(emptyCountriesByLanguage.isEmpty());

        List<Locale> nullCountriesByLanguage = null;
        try {
            LocaleUtils.countriesByLanguage(nullCountriesByLanguage);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}