package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic functionality test
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);

        // Edge case: empty string
        assertNull(LocaleUtils.toLocale(""));

        // Edge case: invalid format
        assertNull(LocaleUtils.toLocale("enUS"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_US_"));
        assertNull(LocaleUtils.toLocale("en_US_xxx"));

        // Error scenario: null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic functionality test
        Locale expectedLocale = new Locale("fr", "CA", "xxx");
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(expectedLocale);
        List<Locale> expectedLocaleList = Arrays.asList(expectedLocale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case: empty list
        List<Locale> actualLocaleList2 = LocaleUtils.localeLookupList(null);
        assertEmpty(actualLocaleList2);

        // Error scenario: null input
        assertNull(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic functionality test
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case: empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        assertSame(emptyLocaleList, LocaleUtils.availableLocaleList());

        // Error scenario: null input
        assertNull(LocaleUtils.availableLocaleList(null));
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic functionality test
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge case: invalid Locale
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));

        // Error scenario: unavailable Locale
        Locale unavailableLocale = new Locale("non-existent", "non-existent");
        assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic functionality test
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case: empty list
        String emptyCountryCode = "";
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> actualLocaleList2 = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertEquals(emptyLocaleList, actualLocaleList2);

        // Error scenario: null input
        assertNull(LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic functionality test
        String languageCode = "fr";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case: empty list
        String emptyLanguageCode = "";
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> actualLocaleList2 = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertEquals(emptyLocaleList, actualLocaleList2);

        // Error scenario: null input
        assertNull(LocaleUtils.countriesByLanguage(null));
    }
}