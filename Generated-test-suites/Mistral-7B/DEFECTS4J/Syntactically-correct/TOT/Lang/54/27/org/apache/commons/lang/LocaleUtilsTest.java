package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] validInputs = new String[]{"en", "en_US", "fr_CA"};
        for (String input : validInputs) {
            Locale expected = new Locale(input.substring(0, input.indexOf("_")), input.substring(input.indexOf("_") + 1));
            Locale actual = LocaleUtils.toLocale(input);
            assertEquals(expected.getLanguage(), actual.getLanguage());
            assertEquals(expected.getCountry(), actual.getCountry());
        }

        // Test empty input
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid input
        String[] invalidInputs = new String[]{"en_GB_xx", "en_US_xx", "en_GB"};
        for (String input : invalidInputs) {
            try {
                LocaleUtils.toLocale(input);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = LocaleUtils.localeLookupList(locale);
        List<Locale> actualList = new ArrayList<>();
        actualList.add(locale);
        actualList.add(new Locale("fr", "CA"));
        actualList.add(new Locale("fr", ""));
        actualList.add(Locale.getDefault());
        assertEquals(expectedList, actualList);

        // Test empty input
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(""));

        // Test invalid input
        try {
            LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Locale.getAvailableLocales();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        assertTrue(locales.contains(locale));

        locale = new Locale("fr", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
        assertFalse(locales.contains(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedList = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actualList = new ArrayList<>();
        actualList.add(new Locale("fr", countryCode));
        actualList.add(new Locale("en", countryCode));
        // Add other expected languages for CA here
        assertEquals(expectedList, actualList);

        countryCode = "ES";
        expectedList = LocaleUtils.languagesByCountry(countryCode);
        actualList = new ArrayList<>();
        actualList.add(new Locale("es", countryCode));
        // Add other expected languages for ES here
        assertEquals(expectedList, actualList);

        countryCode = "XX";
        expectedList = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(expectedList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedList = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actualList = new ArrayList<>();
        actualList.add(new Locale(languageCode, "CA"));
        actualList.add(new Locale(languageCode, "FR"));
        // Add other expected countries for FR here
        assertEquals(expectedList, actualList);

        languageCode = "en";
        expectedList = LocaleUtils.countriesByLanguage(languageCode);
        actualList = new ArrayList<>();
        actualList.add(new Locale(languageCode, "US"));
        actualList.add(new Locale(languageCode, "UK"));
        // Add other expected countries for EN here
        assertEquals(expectedList, actualList);

        languageCode = "XX";
        expectedList = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(expectedList.isEmpty());
    }

}