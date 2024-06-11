package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_NullString() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        String invalidLocale = "_invalid";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException for invalid locale format: " + invalidLocale);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_MissingCountry() {
        String invalidLocale = "en_";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException for invalid locale format: " + invalidLocale);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat_TwoCharLanguage() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void testToLocale_ValidFormat_ThreeCharLanguage() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("", locale.getVariant());
    }

    @Test
    public void testToLocale_ValidFormat_ThreeCharLanguageWithVariant() {
        Locale locale = LocaleUtils.toLocale("en_US_POSIX");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("POSIX", locale.getVariant());
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> actualList = LocaleUtils.localeLookupList(null);
        assertEquals(Collections.emptyList(), actualList);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = Locale.getDefault();
        List<Locale> expectedList = Arrays.asList(defaultLocale);
        List<Locale> actualList = LocaleUtils.localeLookupList(defaultLocale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocales = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expectedLocales, availableLocales);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedLocales = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        assertEquals(expectedLocales, availableLocales);
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = Locale.US;
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale invalidLocale = new Locale("", "InvalidCountry");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "CA"));
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), actualLocales);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "AU"));
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), actualLocales);
    }
}