package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        // Test toLocale with a valid language code
        Locale expected = new Locale("en", "");
        Locale actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        // Test toLocale with a valid language and country code
        expected = new Locale("en", "GB");
        actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        // Test toLocale with a valid language, country and variant code
        expected = new Locale("en", "GB", "en_GB");
        actual = LocaleUtils.toLocale("en_GB_en_GB");
        assertEquals(expected, actual);
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale(null);

        // Test toLocale with invalid language code
        LocaleUtils.toLocale("Aa");

        // Test toLocale with invalid country code
        LocaleUtils.toLocale("en_aa");

        // Test toLocale with invalid separator
        LocaleUtils.toLocale("en_GB_xx");

        // Test toLocale with an empty locale string
        LocaleUtils.toLocale("");

        // Test toLocale with a locale string having the wrong number of parts
        LocaleUtils.toLocale("en_GB_xxx");
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        Locale defaultLocale = new Locale("en");

        // Test localeLookupList with a simple locale
        Locale testLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                testLocale,
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(testLocale);
        assertEquals(expected, actual);

        // Test localeLookupList with a default locale
        actual = LocaleUtils.localeLookupList(defaultLocale, testLocale);
        assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupListInvalidInput() {
        Locale defaultLocale = new Locale("en");

        // Test localeLookupList with a null locale
        LocaleUtils.localeLookupList(null, defaultLocale);

        // Test localeLookupList with an invalid locale
        LocaleUtils.localeLookupList(new Locale("fr", "", "xxx"), defaultLocale);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = Collections.newSetFromMap(Locale.getAvailableLocales(Locale.LanguageRange.ALL));
        assertEquals(expected, actual);
    }

    /**
     * Test isAvailableLocale method with valid input.
     */
    @Test
    public void testIsAvailableLocaleValidInput() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale method with invalid input.
     */
    @Test
    public void testIsAvailableLocaleInvalidInput() {
        Locale locale = new Locale("fr", "");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "en_GB");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List<Locale> expected = LocaleUtils.languagesByCountry("CA");
        List<Locale> actual = Arrays.asList(Locale.CANADA);
        assertEquals(expected, actual);
    }

    /**
     * Test
     * languagesByCountry
     * method with invalid input.
     */
    @Test
    public void testLanguagesByCountryInvalidInput() {
        List<Locale> expected = LocaleUtils.languagesByCountry(null);
        List<Locale> actual = Collections.emptyList();
        assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List<Locale> expected = LocaleUtils.countriesByLanguage("en");
        List<Locale> actual = Arrays.asList(Locale.US, Locale.GB, Locale.CA);
        assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage method with invalid input.
     */
    @Test
    public void testCountriesByLanguageInvalidInput() {
        List<Locale> expected = LocaleUtils.countriesByLanguage(null);
        List<Locale> actual = Collections.emptyList();
        assertEquals(expected, actual);
    }
}