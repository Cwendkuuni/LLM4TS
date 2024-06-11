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
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de", LocaleUtils.toLocale("de").toString());
        assertEquals("es", LocaleUtils.toLocale("es").toString());

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xyz_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xyz_xyz_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xyz_xyz_xyz_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xyz_xyz_xyz_xyz_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xyz_xyz_xyz_xyz_xyz_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xyz_xyz_xyz_xyz_xyz_xyz_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_xyz_xyz_xyz_xyz_xyz_xyz_xyz_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Test empty list
        Locale locale = new Locale("en");
        assertEquals(Arrays.asList(locale), LocaleUtils.localeLookupList(locale));

        // Test valid inputs
        locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));

        locale = new Locale("fr", "CA");
        localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));

        locale = new Locale("fr");
        localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("en"), localeList.get(2));

        // Test invalid inputs
        locale = null;
        try {
            LocaleUtils.localeLookupList(locale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.isEmpty());

        // Test valid inputs
        List<Locale> expectedLocaleList = Arrays.asList(Locale.GERMAN, Locale.FRENCH, Locale.ENGLISH);
        localeList = LocaleUtils.availableLocaleList();
        assertEquals(expectedLocaleList.size(), localeList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i).toString(), localeList.get(i).toString());
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test empty set
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertTrue(localeSet.isEmpty());

        // Test valid inputs
        Set<Locale> expectedLocaleSet = new HashSet<>(Arrays.asList(Locale.GERMAN, Locale.FRENCH, Locale.ENGLISH));
        localeSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedLocaleSet.size(), localeSet.size());
        assertTrue(localeSet.containsAll(expectedLocaleSet));
    }

    @Test
    public void testIsAvailableLocale() {
        // Test null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test invalid inputs
        Locale locale = new Locale("xyz", "xyz");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        // Test valid inputs
        locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("de", "DE");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "ES");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));

        // Test empty list
        List<Locale> localeList = LocaleUtils.languagesByCountry("");
        assertTrue(localeList.isEmpty());

        // Test valid inputs
        localeList = LocaleUtils.languagesByCountry("US");
        assertEquals(2, localeList.size());
        assertEquals(new Locale("en", "US").toString(), localeList.get(0).toString());
        assertEquals(new Locale("es", "US").toString(), localeList.get(1).toString());

        localeList = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, localeList.size());
        assertEquals(new Locale("fr", "FR").toString(), localeList.get(0).toString());
        assertEquals(new Locale("fr", "CA").toString(), localeList.get(1).toString());

        localeList = LocaleUtils.languagesByCountry("DE");
        assertEquals(1, localeList.size());
        assertEquals(new Locale("de", "DE").toString(), localeList.get(0).toString());

        localeList = LocaleUtils.languagesByCountry("JP");
        assertEquals(1, localeList.size());
        assertEquals(new Locale("ja", "JP").toString(), localeList.get(0).toString());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));

        // Test empty list
        List<Locale> localeList = LocaleUtils.countriesByLanguage("");
        assertTrue(localeList.isEmpty());

        // Test valid inputs
        localeList = LocaleUtils.countriesByLanguage("en");
        assertEquals(1, localeList.size());
        assertEquals(new Locale("US", "en").toString(), localeList.get(0).toString());

        localeList = LocaleUtils.countriesByLanguage("fr");
        assertEquals(2, localeList.size());
        assertEquals(new Locale("CA", "fr").toString(), localeList.get(0).toString());
        assertEquals(new Locale("FR", "fr").toString(), localeList.get(1).toString());

        localeList = LocaleUtils.countriesByLanguage("de");
        assertEquals(1, localeList.size());
        assertEquals(new Locale("DE", "de").toString(), localeList.get(0).toString());

        localeList = LocaleUtils.countriesByLanguage("es");
        assertEquals(1, localeList.size());
        assertEquals(new Locale("ES", "es").toString(), localeList.get(0).toString());

        localeList = LocaleUtils.countriesByLanguage("ja");
        assertEquals(1, localeList.size());
        assertEquals(new Locale("JP", "ja").toString(), localeList.get(0).toString());
    }
}