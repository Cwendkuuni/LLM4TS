package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid test cases
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de", LocaleUtils.toLocale("de").toString());

        // Invalid test cases
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
            LocaleUtils.toLocale("en_GB_xxx");
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
    }

    @Test
    public void testLocaleLookupList() {
        // Valid test cases
        List<Locale> list = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(3, list.size());
        assertEquals("en_US", list.get(0).toString());
        assertEquals("en", list.get(1).toString());
        assertEquals("en_CA", list.get(2).toString());

        List<Locale> list2 = LocaleUtils.localeLookupList(Locale.FRANCE);
        assertEquals(2, list2.size());
        assertEquals("fr_FR", list2.get(0).toString());
        assertEquals("fr", list2.get(1).toString());

        // Invalid test cases
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertEquals(186, list.size());

        // Test a few known locales
        assertTrue(list.contains(Locale.US));
        assertTrue(list.contains(Locale.FRANCE));
        assertTrue(list.contains(Locale.CHINA));
        assertTrue(list.contains(Locale.GERMAN));

        // Test a few unknown locales
        assertFalse(list.contains(Locale.ITALY));
        assertFalse(list.contains(Locale.SPAIN));
        assertFalse(list.contains(Locale.RUSSIA));
        assertFalse(list.contains(Locale.JAPAN));
    }

    @Test
    public void testLanguagesByCountry() {
        // Valid test cases
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(1, list.size());
        assertEquals("en_US", list.get(0).toString());

        list = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, list.size());
        assertEquals("fr_CA", list.get(0).toString());
        assertEquals("en_CA", list.get(1).toString());

        // Invalid test cases
        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.languagesByCountry("XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Valid test cases
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, list.size());
        assertEquals("en_US", list.get(0).toString());
        assertEquals("en_GB", list.get(1).toString());

        list = LocaleUtils.countriesByLanguage("fr");
        assertEquals(2, list.size());
        assertEquals("fr_FR", list.get(0).toString());
        assertEquals("fr_CA", list.get(1).toString());

        // Invalid test cases
        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.countriesByLanguage("XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}