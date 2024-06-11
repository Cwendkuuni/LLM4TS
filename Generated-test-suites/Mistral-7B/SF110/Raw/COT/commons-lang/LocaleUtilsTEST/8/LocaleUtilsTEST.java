package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidFormat() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_EmptyString() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_NullString() {
        LocaleUtils.toLocale(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoLanguage() {
        LocaleUtils.toLocale("_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoCountry() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoLanguageOrCountry() {
        LocaleUtils.toLocale("_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCharacter() {
        LocaleUtils.toLocale("en_US#");
    }

    @Test
    public void testToLocale_ValidFormat_ThreeLetterCountryCode() {
        Locale expected = new Locale("", "GB");
        Locale actual = LocaleUtils.toLocale("", "GB");
        assertEquals(expected, actual);
    }

    @Test
    public void testToLocale_ValidFormat_ThreeLetterLanguageCode() {
        Locale expected = new Locale("de", "");
        Locale actual = LocaleUtils.toLocale("de", "");
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale("", "CA"), new Locale("fr", ""));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en", "US"));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        // Add assertions based on the actual list of available locales
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("invalid", "invalid");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("de", "DE"), new Locale("fr", "CH"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CH");
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("es", "ES"), new Locale("fr", "BE"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expected, actual);
    }
}