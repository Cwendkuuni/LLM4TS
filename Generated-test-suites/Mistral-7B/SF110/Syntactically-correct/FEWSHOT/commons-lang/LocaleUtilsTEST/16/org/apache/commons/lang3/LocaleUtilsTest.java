package org.apache.commons.lang3;

import static org.junit.Assert.*;
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
    public void testToLocale_InvalidFormat_NoLanguage() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguageOrCountry() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_en"));
    }

    @Test
    public void testToLocale_InvalidFormat_ExtraCharacters() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US#"));
    }

    @Test
    public void testToLocale_ValidFormat_TwoLetterLanguageCode() {
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
    }

    @Test
    public void testToLocale_ValidFormat_ThreeLetterLanguageCode() {
        assertEquals(new Locale("de", "DE"), LocaleUtils.toLocale("de-DE"));
    }

    @Test
    public void testTolocale_ValidFormat_LanguageAndCountry() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en-US"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountryWithVariants() {
        assertEquals(new Locale("en", "US", "en-US"), LocaleUtils.toLocale("en-US_en"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> locales = LocaleUtils.localeLookupList(new Locale("en", "US"));
        assertEquals(3, locales.size());
        assertEquals(new Locale("en", "US"), locales.get(0));
        assertEquals(new Locale("en", ""), locales.get(1));
        assertEquals(new Locale("", "US"), locales.get(2));

        locales = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(4, locales.size());
        assertEquals(new Locale("en", "US"), locales.get(0));
        assertEquals(new Locale("en", ""), locales.get(1));
        assertEquals(Locale.ENGLISH, locales.get(2));
        assertEquals(new Locale("", "US"), locales.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertFalse(locales.isEmpty());
        assertEquals(631, locales.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertFalse(locales.isEmpty());
        assertEquals(631, locales.size());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("foo", "bar")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> locales = LocaleUtils.languagesByCountry("US");
        assertTrue(locales.isEmpty() || locales.size() > 0);

        locales = LocaleUtils.languagesByCountry("fr");
        assertEquals(2, locales.size());
        assertEquals(new Locale("fr", "CA"), locales.get(0));
        assertEquals(new Locale("fr", "FR"), locales.get(1));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> locales = LocaleUtils.countriesByLanguage("en");
        assertTrue(locales.isEmpty() || locales.size() > 0);

        locales = LocaleUtils.countriesByLanguage("de");
        assertEquals(2, locales.size());
        assertEquals(new Locale("DE", "AT"), locales.get(0));
        assertEquals(new Locale("DE", "DE"), locales.get(1));
    }
}