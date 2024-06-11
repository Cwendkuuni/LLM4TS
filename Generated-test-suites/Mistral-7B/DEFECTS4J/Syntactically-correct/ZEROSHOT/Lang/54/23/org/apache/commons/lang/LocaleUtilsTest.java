package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_GB_xxx");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(new Locale("pt", "BR"), new Locale("en", "US"), new Locale("fr", "FR"), new Locale("de", "DE"), new Locale("es", "ES"), new Locale("zh", "CN"), new Locale("ja", "JP"), new Locale("ko", "KR"), new Locale("ru", "RU"));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "ES_es")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("GB"), new Locale("IE"), new Locale("US"));
        assertEquals(expected, actual);
    }
}