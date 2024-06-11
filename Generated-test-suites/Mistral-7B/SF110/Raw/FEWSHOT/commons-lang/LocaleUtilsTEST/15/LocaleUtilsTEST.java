package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.JAPAN, LocaleUtils.toLocale("ja_JP"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.GERMAN, LocaleUtils.toLocale("de_DE"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT"));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale("foo"));
        assertNull(LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_FOO"));
        assertNull(LocaleUtils.toLocale("en_US#"));
        assertNull(LocaleUtils.toLocale("en_US#FOO"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US#bar"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US_FOO"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US_-FOO"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US__FOO"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US---FOO"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US----FOO"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> locales = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(3, locales.size());
        assertEquals(Locale.US, locales.get(0));
        assertEquals(Locale.US, locales.get(1));
        assertEquals(Locale.ENGLISH, locales.get(2));

        locales = LocaleUtils.localeLookupList(Locale.JAPAN, Locale.ENGLISH);
        assertEquals(2, locales.size());
        assertEquals(Locale.JAPAN, locales.get(0));
        assertEquals(Locale.ENGLISH, locales.get(1));

        locales = LocaleUtils.localeLookupList(Locale.FRENCH, null);
        assertEquals(3, locales.size());
        assertEquals(Locale.FRENCH, locales.get(0));
        assertEquals(Locale.FRENCH, locales.get(1));
        assertEquals(Locale.FRENCH, locales.get(2));

        locales = LocaleUtils.localeLookupList(null, Locale.GERMAN);
        assertEquals(2, locales.size());
        assertNull(locales.get(0));
        assertEquals(Locale.GERMAN, locales.get(1));

        locales = LocaleUtils.localeLookupList(Locale.ITALIAN, Locale.ITALIAN);
        assertEquals(1, locales.size());
        assertEquals(Locale.ITALIAN, locales.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(233, locales.size());
        assertFalse(locales.contains(null));
        assertFalse(locales.contains(Locale.ROOT));
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRENCH));
        assertTrue(locales.contains(Locale.GERMAN));
        assertTrue(locales.contains(Locale.ITALIAN));
        assertTrue(locales.contains(Locale.JAPAN));
        assertTrue(locales.contains(Locale.CHINESE));
        assertTrue(locales.contains(Locale.AMERICAN));
        assertTrue(locales.contains(Locale.FRENCH_CANADIAN));
        assertTrue(locales.contains(Locale.GERMAN_AUSTRIA));
        assertTrue(locales.contains(Locale.GERMAN_SWISS));
        assertTrue(locales.contains(Locale.ITALIAN_ITALY));
        assertTrue(locales.contains(Locale.JAPAN_JAPAN));
        assertTrue(locales.contains(Locale.CHINESE_SIMPLIFIED_CHINA));
        assertTrue(locales.contains(Locale.CHINESE_TRADITIONAL_TAIWAN));
        assertTrue(locales.contains(Locale.RUSSIAN));
        assertTrue(locales.contains(Locale.SPANISH));
        // Add more assertions as needed
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertEquals(233, locales.size());
        assertFalse(locales.contains(null));
        assertFalse(locales.contains(Locale.ROOT));
        assertTrue(locales.contains(Locale.ENGLISH));
        // Add more assertions as needed
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CHINESE));
        // Add more assertions as needed
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("foo", "bar")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> langs = LocaleUtils.languagesByCountry("US");
        assertEquals(24, langs.size());
        assertTrue(langs.contains(Locale.ENGLISH));
        assertTrue(langs.contains(new Locale("cs", "CZ")));
        assertTrue(langs.contains(new Locale("da", "DK")));
        // Add more assertions as needed

        langs = LocaleUtils.languagesByCountry("JP");
        assertEquals(2, langs.size());
        assertTrue(langs.contains(Locale.JAPAN));
        assertTrue(langs.contains(new Locale("zh", "CN")));

        langs = LocaleUtils.languagesByCountry("FR");
        assertEquals(11, langs.size());
        assertTrue(langs.contains(Locale.FRENCH));
        assertTrue(langs.contains(new Locale("br", "FR")));
        // Add more assertions as needed
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(25, countries.size());
        assertTrue(countries.contains(Locale.US));
        assertTrue(countries.contains(Locale.CA));
        assertTrue(countries.contains(Locale.GB));
        // Add more assertions as needed

        countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(11, countries.size());
        assertTrue(countries.contains(Locale.FR));
        assertTrue(countries.contains(Locale.BE));
        // Add more assertions as needed
    }
}