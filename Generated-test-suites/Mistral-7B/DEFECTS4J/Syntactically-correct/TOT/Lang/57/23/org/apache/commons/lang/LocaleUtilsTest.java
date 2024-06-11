package org.apache.commons.lang.test;

import static org.junit.Assert.*;

import org.apache.commons.lang.LocaleUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Tests for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocale() {
        // Test empty string
        assertEquals(Locale.EMPTY, LocaleUtils.toLocale(""));

        // Test English language
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "London"), LocaleUtils.toLocale("en_GB_London"));

        // Test French language
        assertEquals(new Locale("fr", ""), LocaleUtils.toLocale("fr"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr", "CA", "Montreal"), LocaleUtils.toLocale("fr_CA_Montreal"));
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        // Test invalid language code (uppercase)
        LocaleUtils.toLocale("en_GB_xx");

        // Test invalid country code (lowercase)
        LocaleUtils.toLocale("en_gb_US");

        // Test invalid separator
        LocaleUtils.toLocale("en_GBxx");

        // Test missing separator
        LocaleUtils.toLocale("enGB");

        // Test empty language code
        LocaleUtils.toLocale("");

        // Test empty country code
        LocaleUtils.toLocale("", "US");

        // Test empty locale string
        LocaleUtils.toLocale(null);
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "Montreal");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        // Check list contains the original locale
        assertTrue(localeList.contains(locale));

        // Check list contains country-specific locale
        Locale countrySpecificLocale = new Locale("fr", "CA");
        assertTrue(localeList.contains(countrySpecificLocale));

        // Check list contains language-specific locale
        Locale languageSpecificLocale = new Locale("fr", "");
        assertTrue(localeList.contains(languageSpecificLocale));

        // Check list contains default locale
        Locale defaultLocale = Locale.getDefault();
        assertTrue(localeList.contains(defaultLocale));
    }

    /**
     * Test localeLookupList method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupListInvalidInput() {
        Locale locale = new Locale("fr", "CA", "Montreal");
        Locale defaultLocale = Locale.getDefault();

        // Test empty locale
        LocaleUtils.localeLookupList(null);

        // Test empty defaultLocale
        LocaleUtils.localeLookupList(locale, null);

        // Test invalid locale
        Locale invalidLocale = new Locale("fr", "CA", "Montreale");
        LocaleUtils.localeLookupList(invalidLocale, defaultLocale);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(Locale.getAvailableLocales().length, locales.size());
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, locales.size());
    }

    /**
     * Test isAvailableLocale method with valid input.
     */
    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test invalid locale
        Locale invalidLocale = new Locale("fr", "CA", "Montreale");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, languages.size());
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("fr", "CA")));
    }

    /**
     * Test languagesByCountry method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountryInvalidInput() {
        LocaleUtils.languagesByCountry(null);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, countries.size());
        assertTrue(countries.contains(new Locale("en", "US")));
        assertTrue(countries.contains(new Locale("en", "GB")));
    }

    /**
     * Test countriesByLanguage method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguageInvalidInput() {
        LocaleUtils.countriesByLanguage(null);
    }
}