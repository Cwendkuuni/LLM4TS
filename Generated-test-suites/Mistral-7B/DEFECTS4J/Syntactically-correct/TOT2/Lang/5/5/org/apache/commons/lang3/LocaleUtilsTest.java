package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for {@link LocaleUtils}
 */
public class LocaleUtilsTest {

    /**
     * Test method for {@link LocaleUtils#toLocale(String)}.
     */
    @Test
    public void testToLocale() {
        // basic test case
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        // edge case: empty string
        assertNull(LocaleUtils.toLocale(""));
        // edge case: invalid format
        try {
            LocaleUtils.toLocale("enUS");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // edge case: null input
        assertNull(LocaleUtils.toLocale(null));
        // error scenario: missing language code
        try {
            LocaleUtils.toLocale("GB");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // error scenario: missing country code
        try {
            LocaleUtils.toLocale("en");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // error scenario: invalid separator
        try {
            LocaleUtils.toLocale("en_GA");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // error scenario: invalid length
        try {
            LocaleUtils.toLocale("en_US_");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // error scenario: invalid character
        try {
            LocaleUtils.toLocale("en_US#");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // error scenario: invalid language code (non-ASCII)
        try {
            LocaleUtils.toLocale("français");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        // error scenario: invalid country code (non-ASCII)
        try {
            LocaleUtils.toLocale("fr_中国");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale)}.
     */
    @Test
    public void testLocaleLookupList() {
        // basic test case
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(8, localeList.size());
        // edge case: null input
        assertNotNull(LocaleUtils.localeLookupList(null));
        // edge case: empty list
        assertNotNull(LocaleUtils.localeLookupList(Locale.ROOT));
        // error scenario: invalid locale
        try {
            LocaleUtils.localeLookupList(new Locale("xx", "xx"));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleList()}.
     */
    @Test
    public void testAvailableLocaleList() {
        // basic test case
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(223, localeList.size());
        // edge case: null result
        assertNotNull(LocaleUtils.availableLocaleList());
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleSet()}.
     */
    @Test
    public void testAvailableLocaleSet() {
        // basic test case
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertEquals(223, localeSet.size());
        // edge case: null result
        assertNotNull(LocaleUtils.availableLocaleSet());
    }

    /**
     * Test method for {@link LocaleUtils#isAvailableLocale(Locale)}.
     */
    @Test
    public void testIsAvailableLocale() {
        // basic test case
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        // edge case: null input
        assertFalse(LocaleUtils.isAvailableLocale(null));
        // edge case: invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));
    }

    /**
     * Test method for {@link LocaleUtils#languagesByCountry(String)}.
     */
    @Test
    public void testLanguagesByCountry() {
        // basic test case
        List<Locale> languageList = LocaleUtils.languagesByCountry("US");
        assertEquals(13, languageList.size());
        // edge case: null input
        assertNotNull(LocaleUtils.languagesByCountry(null));
        // edge case: empty list
        assertFalse(LocaleUtils.languagesByCountry("").isEmpty());
        // error scenario: invalid country code
        try {
            List<Locale> languageList2 = LocaleUtils.languagesByCountry("XX");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link LocaleUtils#countriesByLanguage(String)}.
     */
    @Test
    public void testCountriesByLanguage() {
        // basic test case
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        assertEquals(32, countryList.size());
        // edge case: null input
        assertNotNull(LocaleUtils.countriesByLanguage(null));
        // edge case: empty list
        assertFalse(LocaleUtils.countriesByLanguage("").isEmpty());
        // error scenario: invalid language code
        try {
            List<Locale> countryList2 = LocaleUtils.countriesByLanguage("XX");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}