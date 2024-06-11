package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {

    @org.junit.Test
    public void testToLocale_ValidInput() {
        // Test case for valid input with no country or language code
        assertEquals(new Locale(""), LocaleUtils.toLocale(""));

        // Test case for valid input with two-letter language code
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));

        // Test case for valid input with two-letter language and country code
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));

        // Test case for valid input with three-letter country code
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoLanguageCode() {
        // Test case for invalid input with no language code
        LocaleUtils.toLocale("_US");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoCountryCode() {
        // Test case for invalid input with no country code
        LocaleUtils.toLocale("en");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCharacters() {
        // Test case for invalid input with invalid characters
        LocaleUtils.toLocale("en#US");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_EmptyLanguageCode() {
        // Test case for invalid input with empty language code
        LocaleUtils.toLocale("_");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_EmptyCountryCode() {
        // Test case for invalid input with empty country code
        LocaleUtils.toLocale("en_");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_EmptyBoth() {
        // Test case for invalid input with both empty
        LocaleUtils.toLocale("_ _");
    }

    @org.junit.Test
    public void testLocaleLookupList_ValidInput() {
        // Test case for input with a valid locale
        Locale locale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", ""));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Test case for input with a null locale and default locale
        Locale defaultLocale = new Locale("en", "CA");
        expected = Arrays.asList(locale, locale, defaultLocale);
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);

        // Test case for input with a null locale and empty default locale
        expected = Arrays.asList(locale, locale);
        actual = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testLocaleLookupList_NullLocale() {
        // Test case for null locale input
        LocaleUtils.localeLookupList(null);
    }

    @org.junit.Test
    public void testLocaleLookupList_EmptyInput() {
        // Test case for empty input
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale(""));
        assertEquals(Collections.emptyList(), actual);
    }

    @org.junit.Test
    public void testAvailableLocaleList_ValidInput() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testAvailableLocaleSet_ValidInput() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testIsAvailableLocale_ValidInput() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "CA");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testIsAvailableLocale_NullInput() {
        // Test case for null locale input
        LocaleUtils.isAvailableLocale(null);
    }

    @org.junit.Test
    public void testLanguagesByCountry_ValidInput() {
        String countryCode = "US";
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "CA"), new Locale("fr", "US"));
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testCountriesByLanguage_ValidInput() {
        String languageCode = "en";
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "JM"), new Locale("en", "IE"), new Locale("en", "IN"));
        assertEquals(expected, actual);
    }
}