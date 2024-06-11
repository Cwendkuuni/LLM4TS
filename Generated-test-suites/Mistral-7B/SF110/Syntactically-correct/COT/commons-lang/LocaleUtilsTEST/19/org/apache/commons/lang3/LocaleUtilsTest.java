package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        Assert.assertEquals("", LocaleUtils.toLocale(""));
        Assert.assertEquals("", LocaleUtils.toLocale("").getCountry());
        Assert.assertEquals("", LocaleUtils.toLocale("").getLanguage());
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("AA"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageLength() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_"));
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XXX"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountryLength() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XXX_"));
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XXX_YYY"));
    }

    @Test
    public void testToLocale_ValidFormat_NoVariant() {
        List<Locale> expected = Arrays.asList(Locale.FRENCH, Locale.FRENCH);
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.FRENCH);
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testToLocale_ValidFormat_HasVariant() {
        List<Locale> expected = Arrays.asList(Locale.FRENCH, Locale.FRENCH_BE, Locale.FRENCH, Locale.ENGLISH);
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_NullInputLocale() {
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null, Locale.ENGLISH));
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        Assert.assertEquals(Collections.singletonList(Locale.ENGLISH), LocaleUtils.localeLookupList(Locale.ENGLISH));
    }

    @Test
    public void testLocaleUtils_EmptyAvailableLocaleList() {
        ConcurrentHashMap<String, List<Locale>> cLanguagesByCountryMock = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, List<Locale>> cCountriesByLanguageMock = new ConcurrentHashMap<>();
        LocaleUtils localeUtilsMock = new LocaleUtils() {
            @Override
            public static List<Locale> availableLocaleList() {
                return new ArrayList<>();
            }

            @Override
            private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = cLanguagesByCountryMock;

            @Override
            private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = cCountriesByLanguageMock;
        };

        List<Locale> actual = localeUtilsMock.languagesByCountry("US");
        Assert.assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testLanguagesByCountry_NullInput() {
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountry_EmptyAvailableLocaleList() {
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("US"));
    }

    @Test
    public void testLanguagesByCountry_FoundLocale() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH_US, Locale.ENGLISH_CA, Locale.ENGLISH_GB, Locale.ENGLISH, Locale.FRENCH, Locale.FRENCH_BE, Locale.FRENCH_CH, Locale.FRENCH, Locale.GERMAN, Locale.GERMAN_AT, Locale.GERMAN_CH, Locale.GERMAN, Locale.ITALIAN, Locale.ITALIAN_IT, Locale.ITALIAN_CH, Locale.ITALIAN, Locale.SPANISH, Locale.SPANISH_ES, Locale.SPANISH_MX, Locale.SPANISH, Locale.CHINESE, Locale.CHINESE_CN, Locale.CHINESE_TW, Locale.CHINESE, Locale.JAPANESE, Locale.JAPANESE_JP);
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry_InvalidCountryCode() {
        LocaleUtils.languagesByCountry("XX");
    }

    @Test
    public void testCountriesByLanguage_NullInput() {
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguage_EmptyAvailableLocaleList() {
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("en"));
    }

    @Test
    public void testCountriesByLanguage_FoundLocale() {
        List<Locale> expected = Arrays.asList(Locale.US, Locale.CA, Locale.GB, Locale.ENGLISH, Locale.FR, Locale.BE, Locale.CH, Locale.FR, Locale.DE, Locale.AT, Locale.CH, Locale.DE, Locale.IT, Locale.IT, Locale.CH, Locate.IT, Locale.ES, Locale.ES, Locale.MX, Locale.ES, Locale.ZH_CN, Locale.ZH_TW, Locale.ZH, Locale.JA, Locale.JA_JP);
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountriesByLanguage_InvalidLanguageCode() {
        LocaleUtils.countriesByLanguage("XX");
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("XX", "XX")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("XX", "XX")));
    }
}