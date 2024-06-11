package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests {@link LocaleUtils#toLocale(String)} method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        // Test simple locale
        Locale expected = new Locale("en", "");
        Locale actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        // Test locale with country
        expected = new Locale("en", "GB");
        actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Test locale with country and variant
        expected = new Locale("en", "GB", "XXX");
        actual = LocaleUtils.toLocale("en_GB_XXX");
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link LocaleUtils#toLocale(String)} method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        // Test empty string
        LocaleUtils.toLocale("");

        // Test language code with uppercase letter
        LocaleUtils.toLocale("en_GB_A");

        // Test country code with lowercase letter
        LocaleUtils.toLocale("en_gb_XXX");

        // Test separator other than underscore
        LocaleUtils.toLocale("en_GB-XXX");

        // Test invalid length
        LocaleUtils.toLocale("en_GB_XXX_XXX");
    }

    /**
     * Tests {@link LocaleUtils#localeLookupList(Locale, Locale)} method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        // Test default locale
        Locale defaultLocale = new Locale("en");
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en"), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.ENGLISH, defaultLocale);
        assertEquals(expected, actual);

        // Test locale with country and variant
        Locale locale = new Locale("fr", "CA", "XXX");
        expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link LocaleUtils#localeLookupList(Locale, Locale)} method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        // Test null locale
        List<Locale> actual = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertTrue(Collections.isEmpty(actual));

        // Test null defaultLocale
        actual = LocaleUtils.localeLookupList(new Locale("en_GB"), null);
        assertTrue(Collections.isEmpty(actual));
    }

    /**
     * Tests {@link LocaleUtils#availableLocaleList()} method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.SPANISH, Locale.TRADITIONAL_TURKISH);
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link LocaleUtils#availableLocaleSet()} method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = new HashSet<>(Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.SPANISH, Locale.TRADITIONAL_TURKISH));
        assertEquals(expected, actual);
    }

    /**
     * Tests {@link LocaleUtils#isAvailableLocale(Locale)} method.
     */
    @Test
    public void testIsAvailableLocale() {
        // Test valid locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "yy")));
    }

    /**
     * Tests {@link LocaleUtils#languagesByCountry(String)} method.
     */
    @Test
    public void testLanguagesByCountry() {
        // Test valid country code
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertEquals(expected, actual);

        // Test invalid country code
        actual = LocaleUtils.languagesByCountry("XX");
        assertTrue(Collections.isEmpty(actual));

        // Test null country code
        actual = LocaleUtils.languagesByCountry(null);
        assertTrue(Collections.isEmpty(actual));
    }

    /**
     * Tests {@link LocaleUtils#countriesByLanguage(String)} method.
     */
    @Test
    public void testCountriesByLanguage() {
        // Test valid language code
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("GB"), new Locale("IE"), new Locale("US"));
        assertEquals(expected, actual);

        // Test invalid language code
        actual = LocaleUtils.countriesByLanguage("XX");
        assertTrue(Collections.isEmpty(actual));

        // Test null language code
        actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(Collections.isEmpty(actual));
    }
}