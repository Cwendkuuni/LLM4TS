package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidFormat() {
        List<String> testCases = Arrays.asList("en", "en_US", "fr_FR", "de_DE", "es_ES", "it_IT", "ja_JP", "ko_KR", "zh_CN", "zh_TW");
        for (String localeString : testCases) {
            Locale expectedLocale = new Locale(localeString);
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            assertEquals(expectedLocale, actualLocale);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_EmptyString() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("en_US_");
        LocaleUtils.toLocale("en_US#");
        LocaleUtils.toLocale("en_US_ja");
        LocaleUtils.toLocale("en_US_ja#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCharacter() {
        LocaleUtils.toLocale("en_US%");
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expectedLocales = Arrays.asList(locale, new Locale("en", ""), new Locale("", "US"));
        List<Locale> actualLocales = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> actualLocales = LocaleUtils.localeLookupList(null);
        assertTrue(actualLocales.isEmpty());
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"));
        List<Locale> actualLocales = LocaleUtils.localeLookupList(null, new Locale("en", "US"));
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertFalse(availableLocales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("en", "US_Invalid");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "CA"));
        List<Locale> actualLocales = LocaleUtils.languagesByCountry("GB");
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> actualLocales = LocaleUtils.languagesByCountry("");
        assertTrue(actualLocales.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "CA"), new Locale("en", "US"));
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage("");
        assertTrue(actualLocales.isEmpty());
    }
}