package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test with empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test with empty locale
        assertNull(LocaleUtils.toLocale(null));

        // Test with valid locale
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Test with invalid locale
        String invalidLocale = "en_Ca_XXX";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with valid locale with variant
        Locale expectedWithVariant = new Locale("en", "GB", "XXX");
        Locale actualWithVariant = LocaleUtils.toLocale("en_GB_XXX");
        assertEquals(expectedWithVariant, actualWithVariant);
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> expected = Arrays.asList(
                baseLocale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                defaultLocale
        );

        List<Locale> actual = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(expected, actual);

        // Test with empty list
        assertSame(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertFalse(actual.isEmpty());

        Locale localeToFind = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(localeToFind));
        assertTrue(actual.contains(localeToFind));

        Locale localeNotToFind = new Locale("xyz", "xyz");
        assertFalse(LocaleUtils.isAvailableLocale(localeNotToFind));
        assertFalse(actual.contains(localeNotToFind));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA")
        );

        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        // Test with empty string
        assertSame(Collections.emptyList(), LocaleUtils.languagesByCountry(""));

        // Test with null
        assertSame(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "CH")
        );

        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        // Test with empty string
        assertSame(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));

        // Test with null
        assertSame(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}