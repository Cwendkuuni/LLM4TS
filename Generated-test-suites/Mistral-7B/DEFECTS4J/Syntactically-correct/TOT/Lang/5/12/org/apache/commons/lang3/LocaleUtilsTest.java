package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link LocaleUtils} class.
 */
public class LocaleUtilsTest {

    /**
     * Tests toLocale method with valid inputs.
     */
    @Test
    public void testToLocaleValidInputs() {
        // Test empty string
        assertEquals("", LocaleUtils.toLocale("").getDisplayLanguage());

        // Test valid locale strings
        List<String> testLocales = Arrays.asList(
                "en", "en_US", "en_US_POSIX", "fr_CA", "fr_CA_POSIX", "de_DE", "de_DE_POSIX");
        for (String localeString : testLocales) {
            Locale expectedLocale = new Locale(localeString.split("[_\\-]")[0], localeString.split("[_\\-]")[1]);
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            assertEquals(expectedLocale.getDisplayLanguage(), actualLocale.getDisplayLanguage());
            assertEquals(expectedLocale.getDisplayCountry(), actualLocale.getDisplayCountry());
        }
    }

    /**
     * Tests toLocale method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInputs() {
        // Test null input
        LocaleUtils.toLocale(null);

        // Test empty locale string
        LocaleUtils.toLocale("");

        // Test locale string with incorrect format
        LocaleUtils.toLocale("en_US_POSIX_fr");

        // Test locale string with empty language or country code
        LocaleUtils.toLocale("en_");
        LocaleUtils.toLocale("_CA");
    }

    /**
     * Tests localeLookupList method with valid inputs.
     */
    @Test
    public void testLocaleLookupListValidInputs() {
        // Test locales with different levels of specificity
        List<Locale> testLocales = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"));

        List<Locale> expectedLocales = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")); // defaultLocale

        List<Locale> actualLocales = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));

        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Tests localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        // Test null locale
        List<Locale> actualLocales = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertTrue(actualLocales.isEmpty());
    }

    /**
     * Tests availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocales = LocaleUtils.availableLocaleList();
        List<Locale> actualLocales = Locale.getAvailableLocales();

        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Tests availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedLocales = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocales = Collections.newSetFromMap(Locale.getAvailableLocales(Locale.LanguageRange.ALL));

        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Tests isAvailableLocale method with valid inputs.
     */
    @Test
    public void testIsAvailableLocaleValidInputs() {
        // Test available locale
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Test unavailable locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
    }

    /**
     * Tests languagesByCountry method with valid inputs.
     */
    @Test
    public void testLanguagesByCountryValidInputs() {
        // Test empty country code
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));

        // Test valid country code
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "US"),
                new Locale("es", "ES"));
        List<Locale> actualLocales = LocaleUtils.languagesByCountry("CA");

        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Tests languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        // Test null country code
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    /**
     * Tests countriesByLanguage method with valid inputs.
     */
    @Test
    public void testCountriesByLanguageValidInputs() {
        // Test empty language code
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));

        // Test valid language code
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("CA", "fr"),
                new Locale("US", "en"),
                new Locale("ES", "es"));
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage("fr");

        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Tests countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        // Test null language code
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}