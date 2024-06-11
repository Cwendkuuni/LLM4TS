package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        // Test with 2-letter language code
        Locale expectedLocale1 = new Locale("en", "");
        Locale actualLocale1 = LocaleUtils.toLocale("en");
        assertEquals(expectedLocale1, actualLocale1);

        // Test with 2-letter language and country code
        Locale expectedLocale2 = new Locale("en", "GB");
        Locale actualLocale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(expectedLocale2, actualLocale2);

        // Test with 2-letter language, country code and variant
        Locale expectedLocale3 = new Locale("en", "GB", "xxx");
        Locale actualLocale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(expectedLocale3, actualLocale3);
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        // Test with empty string
        LocaleUtils.toLocale("");

        // Test with invalid language code
        LocaleUtils.toLocale("Fr");

        // Test with invalid country code
        LocaleUtils.toLocale("en_aA");

        // Test with missing separator
        LocaleUtils.toLocale("enGB");

        // Test with invalid length
        LocaleUtils.toLocale("en_GB_xxx_yyy");
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        // Test with single locale
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocalesList1 = Arrays.asList(baseLocale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualLocalesList1 = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(expectedLocalesList1, actualLocalesList1);

        // Test with default locale
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedLocalesList2 = Arrays.asList(baseLocale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actualLocalesList2 = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(expectedLocalesList2, actualLocalesList2);
    }

    /**
     * Test localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        // Test with null baseLocale
        List<Locale> actualLocalesList1 = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertTrue(actualLocalesList1.isEmpty());

        // Test with null defaultLocale
        List<Locale> actualLocalesList2 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null);
        assertTrue(actualLocalesList2.isEmpty());
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocalesList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocalesList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocalesList, actualLocalesList);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        List<Locale> availableLocalesList = LocaleUtils.availableLocaleList();
        Set<Locale> expectedLocaleSet = new HashSet<>(availableLocalesList);
        Set<Locale> actualLocaleSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedLocaleSet, actualLocaleSet);
    }

    /**
     * Test isAvailableLocale method with available locale.
     */
    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale method with invalid locale.
     */
    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        String countryCode = "CA";
        List<Locale> expectedLocalesList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualLocalesList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocalesList, actualLocalesList);
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        String countryCode = null;
        List<Locale> actualLocalesList = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(actualLocalesList.isEmpty());
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        String languageCode = "fr";
        List<Locale> expectedLocalesList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actualLocalesList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocalesList, actualLocalesList);
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        String languageCode = null;
        List<Locale> actualLocalesList = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(actualLocalesList.isEmpty());
    }
}