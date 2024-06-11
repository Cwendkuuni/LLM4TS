package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoLanguage() {
        LocaleUtils.toLocale("FR");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoCountry() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidLanguageLength() {
        LocaleUtils.toLocale("en_ABCDEFGHIJK");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCountryLength() {
        LocaleUtils.toLocale("en_ABC");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidLanguageUppercase() {
        LocaleUtils.toLocale("en_US_fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCountryUppercase() {
        LocaleUtils.toLocale("en_US_FR");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidLanguageLowercase() {
        LocaleUtils.toLocale("en_us_fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCountryLowercase() {
        LocaleUtils.toLocale("en_US_fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_MissingUnderline() {
        LocaleUtils.toLocale("enUS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_ExtraUnderline() {
        LocaleUtils.toLocale("en_US___");
    }

    @Test
    public void testToLocale_ValidFormat() {
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, Locale.FRENCH);
        assertEquals(Locale.FRENCH_CANADA, new Locale("fr", "CA"));
        assertEquals(Locale.GERMANY, new Locale("de", "DE"));
        assertEquals(Locale.GERMANY_AUSTRIA, new Locale("de", "AT"));
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertTrue(LocaleUtils.localeLookupList(null).isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        assertNotNull(LocaleUtils.localeLookupList(Locale.EMPTY));
        assertTrue(LocaleUtils.localeLookupList(Locale.EMPTY).isEmpty());
    }

    @Test
    public void testLocaleLookupList_BasicFunctionality() {
        Locale locale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(4, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("en", "US"), list.get(0));
        assertEquals(new Locale("en", "US"), list.get(1));
        assertEquals(new Locale("en", ""), list.get(2));
        assertEquals(Locale.US, list.get(3));
    }

    @Test
    public void testLocaleLookupList_WithDefaultLocale() {
        Locale defaultLocale = new Locale("en", "GB");
        Locale locale = new Locale("fr", "CA");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(5, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr", ""), list.get(2));
        assertEquals(defaultLocale, list.get(3));
        assertEquals(Locale.US, list.get(4));
    }

    @Test
    public void testAvailableLocaleList_Empty() {
        assertTrue(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleList_BasicFunctionality() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.size() > 0);
        for (Locale locale : list) {
            assertTrue(locale.getLanguage().length() > 0);
            assertTrue(locale.getCountry().length() > 0 || locale.getCountry().isEmpty());
        }
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry_NullInput() {
        LocaleUtils.languagesByCountry(null);
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        List<Locale> languages = LocaleUtils.languagesByCountry("");
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_BasicFunctionality() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertTrue(languages.size() > 0);
        for (Locale locale : languages) {
            assertTrue(locale.getLanguage().equals("en"));
        }
    }

    @Test
    public void testCountriesByLanguage_NullInput() {
        LocaleUtils.countriesByLanguage(null);
    }

    @Test
    public void testCountriesByLanguage_EmptyInput() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("");
        assertTrue(countries.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_BasicFunctionality() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertTrue(countries.size() > 0);
        for (Locale locale : countries) {
            assertTrue(locale.getCountry().equals("US"));
        }
    }
}