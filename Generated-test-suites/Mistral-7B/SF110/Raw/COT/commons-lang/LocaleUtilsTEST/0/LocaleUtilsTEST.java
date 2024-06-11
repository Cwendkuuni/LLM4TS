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
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
        assertEquals("", LocaleUtils.toLocale("").getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoLanguage() {
        LocaleUtils.toLocale("#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoCountry() {
        LocaleUtils.toLocale("ja");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidSeparator() {
        LocaleUtils.toLocale("ja#kan");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidLanguageLength() {
        LocaleUtils.toLocale("ja_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCountryLength() {
        LocaleUtils.toLocale("ja_kan_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCountryUppercase() {
        LocaleUtils.toLocale("ja_kan");
    }

    @Test
    public void testToLocale_ValidInput() {
        assertEquals("ja_JP", LocaleUtils.toLocale("ja_JP").toString());
        assertEquals("ja", LocaleUtils.toLocale("ja_JP").getLanguage());
        assertEquals("JP", LocaleUtils.toLocale("ja_JP").getCountry());
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale(""));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<String> expected = Arrays.asList("en_US", "en", "");
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_WithDefaultLocale() {
        Locale defaultLocale = new Locale("en", "GB");
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("fr"), defaultLocale);
        List<String> expected = Arrays.asList("fr_FR", "fr", "en_GB");
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_Empty() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_Populated() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<String> expected = Arrays.asList("ja_JP", "en_US", "es_ES", "fr_FR", "de_DE");
        assertEquals(expected, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAvailableLocaleSet_Empty() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        result.add(new Locale("en", "US"));
    }

    @Test
    public void testAvailableLocaleSet_Populated() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        List<String> expected = Arrays.asList("ja_JP", "en_US", "es_ES", "fr_FR", "de_DE");
        List<Locale> locales = new ArrayList<Locale>(result);
        for (String s : expected) {
            assertEquals(s.split("_")[0], locales.remove(0).getLanguage());
        }
        assertTrue(locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "US")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("ja", "")));
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("de", "DE")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("ja", "JP")));
    }

    @Test
    public void testLanguagesByCountry_NullInput() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        List<Locale> result = LocaleUtils.languagesByCountry("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<String> expected = Arrays.asList("en_US", "en");
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_NullInput() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_EmptyInput() {
        List<Locale> result = LocaleUtils.countriesByLanguage("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<String> expected = Arrays.asList("US", "GB", "CA", "AU", "NZ", "IE", "ZA");
        assertEquals(expected, result);
    }
}