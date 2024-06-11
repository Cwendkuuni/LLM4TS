package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;
import java.util.List;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        // Test with empty string
        Locale localeEmpty = LocaleUtils.toLocale("");
        assertEquals("Empty string should return null", null, localeEmpty);

        // Test with single language code
        Locale localeEn = LocaleUtils.toLocale("en");
        assertEquals("en should be equal to Locale('en')", new Locale("en"), localeEn);

        // Test with single language and country code
        Locale localeEnUs = LocaleUtils.toLocale("en_US");
        assertEquals("en_US should be equal to Locale('en', 'US')", new Locale("en", "US"), localeEnUs);

        // Test with language, country and variant code
        Locale localeEnUs_In = LocaleUtils.toLocale("en_US_in");
        assertEquals("en_US_in should be equal to Locale('en', 'US', 'in')", new Locale("en", "US", "in"), localeEnUs_In);

        // Test with all possible combinations
        Locale localeFrCa_CA_Paris = LocaleUtils.toLocale("fr_CA_CA_Paris");
        assertEquals("fr_CA_CA_Paris should be equal to Locale('fr', 'CA', 'CA', 'Paris')", new Locale("fr", "CA", "CA", "Paris"), localeFrCa_CA_Paris);
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        // Test with null input
        LocaleUtils.toLocale(null);

        // Test with invalid format - missing language code
        LocaleUtils.toLocale("CA");

        // Test with invalid format - missing country code
        LocaleUtils.toLocale("en");

        // Test with invalid format - missing separator
        LocaleUtils.toLocale("enCA");

        // Test with invalid format - country code is lowercase
        LocaleUtils.toLocale("en_ca");

        // Test with invalid format - language code is uppercase
        LocaleUtils.toLocale("EN_GB");

        // Test with invalid format - separator is not underscore
        LocaleUtils.toLocale("en_CA_fr");

        // Test with invalid format - language code length is not 2
        LocaleUtils.toLocale("fr_FR");

        // Test with invalid format - country code length is not 2
        LocaleUtils.toLocale("en_FR");

        // Test with invalid format - variant code length is not empty
        LocaleUtils.toLocale("en_GB_XXX");
    }

    /**
     * Test localeLookupList method.
     */
    @Test
    public void testLocaleLookupList() {
        // Test with valid input
        Locale localeFrCA = new Locale("fr", "CA");
        List<Locale> localeList = LocaleUtils.localeLookupList(localeFrCA);
        List<Locale> expectedLocaleList = Arrays.asList(localeFrCA, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals("localeLookupList should return the expected list", expectedLocaleList, localeList);

        // Test with null input
        Locale localeNull = null;
        localeList = LocaleUtils.localeLookupList(localeNull);
        assertTrue("localeLookupList should return an empty list for null input", localeList.isEmpty());

        // Test with defaultLocale input
        Locale localeEn = new Locale("en");
        localeList = LocaleUtils.localeLookupList(localeEn, new Locale("fr"));
        expectedLocaleList = Arrays.asList(localeEn, new Locale("fr"));
        assertEquals("localeLookupList should return the expected list with defaultLocale input", expectedLocaleList, localeList);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        // Test with empty input
        List<Locale> localeListEmpty = LocaleUtils.availableLocaleList();
        assertTrue("availableLocaleList should return an empty list for empty input", localeListEmpty.isEmpty());

        // Test with available locales
        List<Locale> localeListAvailable = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocaleListAvailable = Arrays.asList(Locale.GERMAN, Locale.FRENCH, Locale.SPANISH);
        assertEquals("availableLocaleList should return the expected list for available locales", expectedLocaleListAvailable, localeListAvailable);

        // Test with non-available locale
        Locale localeInvalid = new Locale("xy", "xy");
        List<Locale> localeListInvalid = LocaleUtils.availableLocaleList();
        assertFalse("availableLocaleList should not contain the invalid locale", localeListInvalid.contains(localeInvalid));
    }

    /**
     * Test languagesByCountry method.
     */
    @Test
    public void testLanguagesByCountry() {
        // Test with empty input
        String countryCodeEmpty = "";
        List<Locale> localeListEmpty = LocaleUtils.languagesByCountry(countryCodeEmpty);
        assertTrue("languagesByCountry should return an empty list for empty input", localeListEmpty.isEmpty());

        // Test with valid country code
        String countryCodeUS = "US";
        List<Locale> localeListUS = LocaleUtils.languagesByCountry(countryCodeUS);
        List<Locale> expectedLocaleListUS = Arrays.asList(Locale.ENGLISH);
        assertEquals("languagesByCountry should return the expected list for valid country code", expectedLocaleListUS, localeListUS);

        // Test with invalid country code
        String countryCodeInvalid = "XY";
        List<Locale> localeListInvalid = LocaleUtils.languagesByCountry(countryCodeInvalid);
        assertTrue("languagesByCountry should return an empty list for invalid country code", localeListInvalid.isEmpty());
    }

    /**
     * Test countriesByLanguage method.
     */
    @Test
    public void testCountriesByLanguage() {
        // Test with empty input
        String languageCodeEmpty = "";
        List<Locale> localeListEmpty = LocaleUtils.countriesByLanguage(languageCodeEmpty);
        assertTrue("countriesByLanguage should return an empty list for empty input", localeListEmpty.isEmpty());

        // Test with valid language code
        String languageCodeEN = "en";
        List<Locale> localeListEN = LocaleUtils.countriesByLanguage(languageCodeEN);
        List<Locale> expectedLocaleListEN = Arrays.asList(Locale.US, Locale.GB, Locale.CA, Locale.AU);
        assertEquals("countriesByLanguage should return the expected list for valid language code", expectedLocaleListEN, localeListEN);

        // Test with invalid language code
        String languageCodeInvalid = "XY";
        List<Locale> localeListInvalid = LocaleUtils.countriesByLanguage(languageCodeInvalid);
        assertTrue("countriesByLanguage should return an empty list for invalid language code", localeListInvalid.isEmpty());
    }
}