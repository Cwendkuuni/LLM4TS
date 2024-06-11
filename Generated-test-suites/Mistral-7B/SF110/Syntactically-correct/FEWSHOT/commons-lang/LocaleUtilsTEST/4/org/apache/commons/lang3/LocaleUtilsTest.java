package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleEmpty() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleNull() {
        LocaleUtils.toLocale(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatEmpty() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatNoLanguage() {
        LocaleUtils.toLocale("_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatNoCountry() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatInvalidLanguage() {
        LocaleUtils.toLocale("ab_CD");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatInvalidCountry() {
        LocaleUtils.toLocale("en_XX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatLanguageCountry() {
        LocaleUtils.toLocale("en_en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatLanguageVariant() {
        LocaleUtils.toLocale("en_en_US-UTF-8");
    }

    @Test
    public void testToLocaleValid() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
    }

    @Test
    public void testToLocaleWithVariants() {
        Locale locale = new Locale("fr", "FR", "fr_CH");
        assertEquals(locale, LocaleUtils.toLocale("fr_FR_fr_CH"));
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        assertNull(LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupListEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(new Locale("")));
    }

    @Test
    public void testLocaleLookupListSingleLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("en", "US"));
        assertEquals(1, localeList.size());
        assertEquals(new Locale("en", "US"), localeList.get(0));
    }

    @Test
    public void testLocaleLookupListMultipleLocales() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("en", "US", "POSIX"));
        assertEquals(3, localeList.size());
        assertSame(localeList.get(0), new Locale("en", "US"));
        assertSame(localeList.get(1), new Locale("en", ""));
        assertSame(localeList.get(2), new Locale("", "POSIX"));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testLocaleLookupListConcurrentModification() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("en", "US"));
        localeList.add(new Locale("fr", "FR"));
    }

    @Test
    public void testLocaleLookupListConstantList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(true, localeList.isEmpty());
        List<Locale> localeList2 = LocaleUtils.availableLocaleList();
        assertEquals(localeList, localeList2);
    }

    @Test
    public void testAvailableLocaleSetNullLocale() {
        assertNull(LocaleUtils.availableLocaleSet(null));
    }

    @Test
    public void testAvailableLocaleSetEmptySet() {
        assertTrue(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testAvailableLocaleSetSingleLocale() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        localeSet.add(new Locale("en", "US"));
        assertEquals(1, localeSet.size());
        assertTrue(localeSet.contains(new Locale("en", "US")));
    }

    @Test
    public void testAvailableLocaleSetMultipleLocales() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        localeSet.add(new Locale("en", "US"));
        localeSet.add(new Locale("fr", "FR"));
        assertEquals(2, localeSet.size());
        assertTrue(localeSet.contains(new Locale("en", "US")));
        assertTrue(localeSet.contains(new Locale("fr", "FR")));
    }

    @Test
    public void testIsAvailableLocaleNullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocaleEmptyList() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("")));
    }

    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }

    @Test
    public void testIsAvailableLocaleNotAvailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
    }

    @Test(expected = NullPointerException.class)
    public void testLanguagesByCountryNullCountryCode() {
        LocaleUtils.languagesByCountry(null);
    }

    @Test
    public void testLanguagesByCountryEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testLanguagesByCountrySingleLocale() {
        List<Locale> localeList = LocaleUtils.languagesByCountry("US");
        assertEquals(1, localeList.size());
        assertEquals(new Locale("en", "US"), localeList.get(0));
    }

    @Test
    public void testLanguagesByCountryMultipleLocales() {
        List<Locale> localeList = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, localeList.size());
        assertSame(localeList.get(0), new Locale("fr", "FR"));
        assertSame(localeList.get(1), new Locale("fr", "BE"));
    }

    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        assertNull(LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguageEmptyList() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));
    }

    @Test
    public void testCountriesByLanguageSingleLocale() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage("en");
        assertEquals(1, localeList.size());
        assertEquals(new Locale("en", "US"), localeList.get(0));
    }

    @Test
    public void testCountriesByLanguageMultipleLocales() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage("fr");
        assertEquals(2, localeList.size());
        assertSame(localeList.get(0), new Locale("fr", "FR"));
        assertSame(localeList.get(1), new Locale("fr", "BE"));
    }

}