package org.apache.commons.lang;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(expected, actual);

        try {
            Locale actual = LocaleUtils.toLocale("invalidFormat");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale(null);
            fail("Expected null");
        } catch (NullPointerException e) {
            //Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale base = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List localeList = LocaleUtils.localeLookupList(base, defaultLocale);
        List expected = Arrays.asList(
                base,
                new Locale("fr", "CA"),
                new Locale("fr"),
                defaultLocale);
        assertEquals(expected, localeList);
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        List expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, availableLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();
        Set expected = new HashSet(Locale.getAvailableLocales());
        assertEquals(expected, availableLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr_XX", "CA");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List languages = LocaleUtils.languagesByCountry("CA");
        List expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertEquals(expected, languages);
    }

    @Test
    public void testCountriesByLanguage() {
        List countries = LocaleUtils.countriesByLanguage("fr");
        List expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"));
        assertEquals(expected, countries);
    }

}