package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidFormat() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("es_ES"), new Locale("fr_CA"));
        List<Locale> actual = Arrays.asList(
                LocaleUtils.toLocale("en_US"),
                LocaleUtils.toLocale("es_ES"),
                LocaleUtils.toLocale("fr_CA")
        );
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_EmptyString() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("en_US#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLanguage() {
        LocaleUtils.toLocale("AB_CD");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidCountry() {
        LocaleUtils.toLocale("en_AB");
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("", "US"), new Locale("", ""));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", "US"), new Locale("", "US"), new Locale("", ""));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        List<Locale> expected = Arrays.asList(Locale.ROOT, new Locale(""), new Locale(""));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale(""), new Locale(""));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_InvalidLocale() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "US"), new Locale("", "US"), new Locale("", ""), new Locale("en", "US"));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("en_USX"), new Locale("en_US"));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_MultipleValidLocales() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("es", "ES");
        List<Locale> expected = Arrays.asList(locale1, locale2, new Locale("en", "US"), new Locale("es", "ES"), new Locale("", "US"), new Locale("", "ES"), new Locale("", ""));
        List<Locale> actual = LocaleUtils.localeLookupList(locale1, locale2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_EmptyAvailableLocaleList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.ROOT);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<Locale>(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("en", "US");
        boolean expected = true;
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("en_USX");
        boolean expected = false;
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("es", "MX"));
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry("");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("es", "ES"), new Locale("es", "MX"), new Locale("es", "DO"), new Locale("es", "AR"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("es");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage("");
        Assert.assertEquals(expected, actual);
    }
}