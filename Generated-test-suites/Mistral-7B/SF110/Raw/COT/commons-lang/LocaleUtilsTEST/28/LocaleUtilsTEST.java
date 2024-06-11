package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_Empty() {
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getDisplayLanguage());
        assertEquals("", LocaleUtils.toLocale("").getCountry());
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        String invalidLocale = "en";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_SingleLanguage() {
        String invalidLocale = "en_";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCharacter() {
        String invalidLocale = "en#fr";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat_SingleLanguage() {
        Locale expectedLocale = new Locale("en");
        Locale actualLocale = LocaleUtils.toLocale("en");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_ValidFormat_TwoParts() {
        Locale expectedLocale = new Locale("en_US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_ValidFormat_ThreeParts() {
        Locale expectedLocale = new Locale("en_US_POSIX");
        Locale actualLocale = LocaleUtils.toLocale("en_US_POSIX");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        Locale locale = new Locale("");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("en_US");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        Locale baseLocale = new Locale("en", "");
        assertEquals(baseLocale, localeList.get(1));
        Locale countryLocale = new Locale("en", "US");
        assertEquals(countryLocale, localeList.get(2));
        Locale defaultLocale = new Locale("fr", "FR");
        assertEquals(defaultLocale, localeList.get(3));
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null, new Locale("en", "US"));
        assertEquals(5, localeList.size());
        Locale locale = new Locale("en", "US");
        assertEquals(locale, localeList.get(0));
        Locale baseLocale = new Locale("en", "");
        assertEquals(baseLocale, localeList.get(1));
        Locale countryLocale = new Locale("en", "US");
        assertEquals(countryLocale, localeList.get(2));
        Locale defaultLocale = new Locale("fr", "FR");
        assertEquals(defaultLocale, localeList.get(3));
        Locale fallbackLocale = new Locale("en", "GB");
        assertEquals(fallbackLocale, localeList.get(4));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertTrue(!availableLocaleList.isEmpty());
        assertEquals(128, availableLocaleList.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertTrue(!availableLocaleSet.isEmpty());
        assertEquals(128, availableLocaleSet.size());
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale invalidLocale = new Locale("", "");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale availableLocale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(availableLocale));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("");
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> expectedLanguages = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "AU"));
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertEquals(expectedLanguages, languages);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("");
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> expectedCountries = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "AU"));
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedCountries, countries);
    }
}