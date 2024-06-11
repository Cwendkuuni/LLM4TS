package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());
        assertEquals("en", LocaleUtils.toLocale("en").toString());

        // Test invalid locale strings
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("fr_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_ZZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxx_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Locale defaultLocale = new Locale("en", "", "");

        // Test empty input
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Test valid inputs
        List<Locale> list = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        assertEquals(4, list.size());
        assertEquals("fr_CA_xxx", list.get(0).toString());
        assertEquals("fr_CA", list.get(1).toString());
        assertEquals("fr", list.get(2).toString());
        assertEquals("en", list.get(3).toString());

        list = LocaleUtils.localeLookupList(new Locale("fr", "CA"), defaultLocale);
        assertEquals(5, list.size());
        assertEquals("fr_CA_xxx", list.get(0).toString());
        assertEquals("fr_CA", list.get(1).toString());
        assertEquals("fr", list.get(2).toString());
        assertEquals("en", list.get(4).toString());

        // Test invalid input
        try {
            LocaleUtils.localeLookupList(null, defaultLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.localeLookupList(new Locale("fr", "XX"), defaultLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(148, availableLocales.size());

        // Test empty list
        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.availableLocaleList());

        // Test valid inputs
        List<Locale> list = new ArrayList<>();
        for (Locale locale : availableLocales) {
            if (locale.getCountry().equals("US")) {
                list.add(locale);
            }
        }
        assertEquals(31, list.size());
        assertEquals("en_US", list.get(0).toString());
        assertEquals("es_US", list.get(30).toString());
        assertEquals("fr_CA.CA", list.get(31).toString()); // Canada has both "CA" and "CA.CA" entries

        list = new ArrayList<>();
        for (Locale locale : availableLocales) {
            if (locale.getLanguage().equals("fr")) {
                list.add(locale);
            }
        }
        assertEquals(28, list.size());
        assertEquals("fr_FR", list.get(0).toString());
        assertEquals("fr_BE", list.get(27).toString());
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        // Test empty input
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));

        // Test valid inputs
        List<Locale> list = LocaleUtils.languagesByCountry("US");
        assertEquals(10, list.size());
        assertEquals("en_US", list.get(0).toString());
        assertEquals("es_US", list.get(4).toString());
        assertEquals("fr_CA.CA", list.get(9).toString()); // Canada has both "CA" and "CA.CA" entries

        // Test invalid input
        try {
            LocaleUtils.languagesByCountry("XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        // Test empty input
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));

        // Test valid inputs
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(42, list.size());
        assertEquals("en_US", list.get(0).toString());
        assertEquals("en_CA.CA", list.get(11).toString()); // Canada has both "CA" and "CA.CA" entries
        assertEquals("en_GB", list.get(41).toString());

        // Test invalid input
        try {
            LocaleUtils.countriesByLanguage("XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}