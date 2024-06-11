package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("es", "ES");
        actual = LocaleUtils.toLocale("es_ES");
        assertEquals(expected, actual);

        String invalidFormat = "InvalidLocaleFormat";
        try {
            LocaleUtils.toLocale("InvalidFormat");
            fail(invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_variant");
            fail(invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_variant_variant");
            fail(invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_gb");
            fail(invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_variant_variant_variant");
            fail(invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_variant_variant_variant_variant");
            fail(invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("");
            fail(invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale(null);
            fail(invalidFormat);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("en", "US");
        Locale expected = new Locale("en", "CA");
        List<Locale> actual = LocaleUtils.localeLookupList(baseLocale);

        List<Locale> expectedList = Arrays.asList(baseLocale, expected);
        assertEquals(expectedList, actual);

        expected = new Locale("en", "");
        List<Locale> expectedList2 = Arrays.asList(baseLocale, expected);
        actual = LocaleUtils.localeLookupList(baseLocale, new Locale("fr"));
        assertEquals(expectedList2, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "GB");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "ES");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("invalid", "CA");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", null);
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale(null, "GB");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "GB", "variant");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(new Locale("CA", "en"), new Locale("US", "en"), new Locale("GB", "en"));
        assertEquals(expected, actual);
    }

}