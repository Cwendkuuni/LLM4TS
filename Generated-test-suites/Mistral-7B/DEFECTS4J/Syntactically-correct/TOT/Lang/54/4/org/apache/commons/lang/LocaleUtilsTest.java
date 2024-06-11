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
        // Test valid input
        List<String> testCases = Arrays.asList(
                "en",
                "en_GB",
                "en_GB_London",
                "fr_FR",
                "fr_CA_Quebec",
                "es_ES_Madrid",
                "de_DE_Berlin"
        );
        for (String localeString : testCases) {
            Locale expectedLocale = new Locale(localeString.split("_")[0], localeString.split("_")[1]);
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            if (localeString.split("_").length > 2) {
                assertEquals(localeString.split("_")[2], actualLocale.getVariant());
            } else {
                assertNull(actualLocale.getVariant());
            }
        }

        // Test invalid input
        List<String> invalidInputTestCases = Arrays.asList(
                null,
                "aa_bb_cccc",
                "en_GB_London_xx",
                "en_GB_London_London",
                "en_London"
        );
        for (String invalidInput : invalidInputTestCases) {
            try {
                LocaleUtils.toLocale(invalidInput);
                fail("Expected IllegalArgumentException for invalid input: " + invalidInput);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        List<Locale> testCases = Arrays.asList(
                Locale.FRENCH,
                Locale.FRENCH_CANADA,
                Locale.FRENCH_CANADA_MONTREAL,
                Locale.ENGLISH,
                null
        );
        for (Locale locale : testCases) {
            List<Locale> expectedLocaleList = Arrays.asList(
                    locale,
                    new Locale(locale.getLanguage(), locale.getCountry()),
                    new Locale(locale.getLanguage()),
                    Locale.getDefault()
            );
            List<Locale> actualLocaleList = LocaleUtils.localeLookupList(locale);
            assertEquals(expectedLocaleList, actualLocaleList);
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> testCases = Arrays.asList(
                Locale.FRENCH,
                new Locale("fr", "CA"),
                new Locale("fr", "FR"),
                Locale.ITALIAN,
                null
        );
        for (Locale locale : testCases) {
            boolean expectedResult = (locale != null && LocaleUtils.isAvailableLocale(locale));
            boolean actualResult = LocaleUtils.isAvailableLocale(locale);
            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<String> testCases = Arrays.asList(
                "FR",
                "CA",
                null
        );
        for (String countryCode : testCases) {
            List<Locale> expectedLocaleList = null;
            if (countryCode != null) {
                expectedLocaleList = Arrays.asList(
                        new Locale("fr", "CA"),
                        new Locale("fr", "FR"),
                        new Locale("fr")
                );
            }
            List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
            assertEquals(expectedLocaleList, actualLocaleList);
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<String> testCases = Arrays.asList(
                "EN",
                "FR",
                null
        );
        for (String languageCode : testCases) {
            List<Locale> expectedLocaleList = null;
            if (languageCode != null) {
                expectedLocaleList = Arrays.asList(
                        new Locale("en", "US"),
                        new Locale("en", "GB"),
                        new Locale("en")
                );
            }
            List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
            assertEquals(expectedLocaleList, actualLocaleList);
        }
    }

}