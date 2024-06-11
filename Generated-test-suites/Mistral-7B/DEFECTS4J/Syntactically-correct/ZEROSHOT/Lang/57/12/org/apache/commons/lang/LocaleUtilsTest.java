package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "FR");
        actual = LocaleUtils.toLocale("fr_FR");
        assertEquals(expected, actual);

        expected = new Locale("es", "");
        actual = LocaleUtils.toLocale("es");
        assertEquals(expected, actual);

        // Test empty locale string
        actual = LocaleUtils.toLocale("");
        assertNull(actual);

        // Test invalid locale strings
        try {
            actual = LocaleUtils.toLocale("123");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            actual = LocaleUtils.toLocale("en_US_");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            actual = LocaleUtils.toLocale("en_US_xyz");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            actual = LocaleUtils.toLocale("en_US_xyz_123");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            actual = LocaleUtils.toLocale("en_US_xyz_123$");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            actual = LocaleUtils.toLocale("en_US_xyz_123$%");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("en", "US", "variant1");
        Locale defaultLocale = new Locale("en", "GB");

        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "US", "variant1"),
                new Locale("en", "US"),
                new Locale("en", ""),
                defaultLocale
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(expectedList, actualList);

        List<Locale> expectedList2 = Arrays.asList(defaultLocale);
        List<Locale> actualList2 = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expectedList2, actualList2);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Collections.list(Locale.getAvailableLocales());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualSet = new HashSet<>(LocaleUtils.availableLocaleList());
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "ES");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("non-existent", "non-existent");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("es", "MX")
        );
        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedList = Arrays.asList(
                new Locale("ca", "CA"),
                new Locale("mx", "MX"),
                new Locale("us", "US")
        );
        List<Locale> actualList = LocaleUtils.countriesByLanguage("es");
        assertEquals(expectedList, actualList);
    }
}