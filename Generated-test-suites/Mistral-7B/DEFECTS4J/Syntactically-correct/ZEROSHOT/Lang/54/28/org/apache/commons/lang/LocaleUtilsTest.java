package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale result = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, result);

        expected = new Locale("en");
        result = LocaleUtils.toLocale("en");
        assertEquals(expected, result);

        expected = new Locale("en", "");
        result = LocaleUtils.toLocale("en");
        assertEquals(expected, result);

        expected = new Locale("fr", "CA", "xxx");
        result = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(expected, result);

        try {
            result = LocaleUtils.toLocale("fr_CA_xxx_yyy");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            result = LocaleUtils.toLocale("en_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            result = LocaleUtils.toLocale("en_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            result = LocaleUtils.toLocale("en_GB_en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            result = LocaleUtils.toLocale("en_GB_en_en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            result = LocaleUtils.toLocale("en_GB_en_en_en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            result = LocaleUtils.toLocale("en_GB_en_en_en_en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            result = LocaleUtils.toLocale(null);
            fail("Expected null");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(
            new Locale("en", "GB"),
            new Locale("en", ""),
            new Locale("en")
        );
        Locale locale = new Locale("en", "GB", "xxx");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);

        locale = new Locale("en", "GB");
        result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);

        locale = new Locale("en", "");
        result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);

        locale = null;
        result = LocaleUtils.localeLookupList(locale);
        assertEquals(Collections.emptyList(), result);

        locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        result = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expectedDefaultLocale = Arrays.asList(new Locale("en"));
        assertEquals(expected.concat(expectedDefaultLocale), result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(new Locale("fr"), new Locale("en"));
        assertEquals(expected, result);

        result = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("US"), new Locale("GB"), new Locale("AU"));
        assertEquals(expected, result);

        result = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), result);
    }
}