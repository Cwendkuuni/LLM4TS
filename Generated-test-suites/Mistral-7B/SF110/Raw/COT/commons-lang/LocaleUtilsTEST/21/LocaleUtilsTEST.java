package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNotNull(LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getCountry());
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        String invalidLocale = "_US";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format: " + invalidLocale));
        }
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        String invalidLocale = "en";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format: " + invalidLocale));
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCharacters() {
        String invalidLocale = "en_#US";
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid locale format: " + invalidLocale));
        }
    }

    @Test
    public void testToLocale_ValidFormat_TwoCharacters() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
    }

    @Test
    public void testToLocale_ValidFormat_ThreeCharacters() {
        Locale locale = LocaleUtils.toLocale("en_US");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
    }

    @Test
    public void testToLocale_ValidFormat_FourCharacters() {
        Locale locale = LocaleUtils.toLocale("en_US_POSIX");
        assertEquals("en", locale.getLanguage());
        assertEquals("US", locale.getCountry());
        assertEquals("POSIX", locale.getVariant());
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyArguments() {
        Locale locale = new Locale("");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_SameLocale() {
        Locale locale = new Locale("en_US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(locale);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_DifferentLocales() {
        Locale locale1 = new Locale("en_US");
        Locale locale2 = new Locale("fr_FR");
        List<Locale> result = LocaleUtils.localeLookupList(locale1, locale2);
        List<Locale> expected = new ArrayList<Locale>();
        expected.add(locale1);
        expected.add(locale2);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_EmptyAvailableLocales() {
        List<Locale> result = LocaleUtils.localeLookupList(null, new Locale("", ""));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleUtils_AvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testLocaleUtils_AvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = new Locale("en_US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_EmptyAvailableLocales() {
        List<Locale> result = LocaleUtils.languagesByCountry("XX");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = new ArrayList<Locale>();
        // Add all available locales that match the given country code
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            if (locale.getCountry().equals("US")) {
                expected.add(locale);
            }
        }
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_EmptyAvailableLocales() {
        List<Locale> result = LocaleUtils.countriesByLanguage("zz");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = new ArrayList<Locale>();
        // Add all available locales that match the given language code
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            if (locale.getLanguage().equals("en")) {
                expected.add(locale);
            }
        }
        assertEquals(expected, result);
    }
}