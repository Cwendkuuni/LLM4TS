package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        Assert.assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_Null() {
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageLength() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_en"));
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_"));
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en__"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountryLength() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_a"));
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_aa"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountryChar() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_#"));
    }

    @Test
    public void testToLocale_ValidFormat_EmptyCountry() {
        Assert.assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_ValidFormat_OneSegment() {
        Assert.assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("fr_FR"), LocaleUtils.toLocale("fr"));
    }

    @Test
    public void testToLocale_ValidFormat_TwoSegments() {
        Assert.assertEquals(new Locale("en_US"), LocaleUtils.toLocale("en_US"));
        Assert.assertEquals(new Locale("fr_FR"), LocaleUtils.toLocale("fr_FR"));
        Assert.assertEquals(new Locale("es_ES"), LocaleUtils.toLocale("es_ES"));
    }

    @Test
    public void testToLocale_ValidFormat_ThreeSegments() {
        Assert.assertEquals(new Locale("en_US_POSIX"), LocaleUtils.toLocale("en_US_POSIX"));
        Assert.assertEquals(new Locale("fr_FR_POSIX"), LocaleUtils.toLocale("fr_FR_POSIX"));
        Assert.assertEquals(new Locale("es_ES_POSIX"), LocaleUtils.toLocale("es_ES_POSIX"));
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testLocaleLookupList_EmptyStringLocale() {
        List<Locale> result = LocaleUtils.localeLookupList("");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        List<Locale> expected = Arrays.asList(defaultLocale);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_Empty() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testAvailableLocaleList_NonEmpty() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet_Empty() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testAvailableLocaleSet_NonEmpty() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testLanguagesByCountry_EmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("es"), new Locale("fr"), new Locale("it"), new Locale("nl"));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testCountriesByLanguage_EmptyList() {
        List<Locale> result = LocaleUtils.countriesByLanguage("");
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("IE"), new Locale("AU"), new Locale("NZ"), new Locale("JM"), new Locale("TT"), new Locale("BE"), new Locale("GB-GUERNSEY"), new Locale("GB-JERSEY"), new Locale("GB-ISLE_MAN"));
        Assert.assertEquals(expected, result);
    }
}