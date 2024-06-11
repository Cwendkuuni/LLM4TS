package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_Empty() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        try {
            LocaleUtils.toLocale("_");
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        try {
            LocaleUtils.toLocale("fr");
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguage() {
        try {
            LocaleUtils.toLocale("xx_YY");
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToLocale_ValidFormat_LanguageOnly() {
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountry() {
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountryVarant() {
        assertEquals(new Locale("fr", "FR", "latin"), LocaleUtils.toLocale("fr_FR_latin"));
    }

    @Test
    public void testToLocale_ValidFormat_CountryOnly() {
        assertEquals(new Locale("FR"), LocaleUtils.toLocale("FR"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountryVarantWithEmptyCountry() {
        assertEquals(new Locale("fr", ""), LocaleUtils.toLocale("fr_"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> locales = LocaleUtils.localeLookupList(Locale.FRANCE);
        assertEquals(4, locales.size());
        assertEquals(Locale.FRANCE, locales.get(0));
        assertEquals(new Locale("fr", ""), locales.get(1));
        assertEquals(Locale.FRANCE, locales.get(2));
        assertEquals(Locale.getDefault(), locales.get(3));

        locales = LocaleUtils.localeLookupList(Locale.getDefault());
        assertEquals(1, locales.size());
        assertEquals(Locale.getDefault(), locales.get(0));
    }

    @Test
    public void testLocaleLookupList_DefaultLocaleNull() {
        List<Locale> locales = LocaleUtils.localeLookupList(null);
        assertEquals(1, locales.size());
        assertEquals(Locale.getDefault(), locales.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertEquals(230, locales.size());
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRANCE));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertEquals(230, locales.size());
        assertTrue(locales.contains(Locale.ENGLISH));
        assertTrue(locales.contains(Locale.FRANCE));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "YY")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertEquals(26, languages.size());
        assertTrue(languages.contains(new Locale("en", "US")));
        assertTrue(languages.contains(new Locale("es", "US")));

        languages = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, languages.size());
        assertTrue(languages.contains(Locale.FRENCH));
        assertTrue(languages.contains(Locale.FRANCE));

        languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(32, countries.size());
        assertTrue(countries.contains(new Locale("US", "en")));
        assertTrue(countries.contains(new Locale("GB", "en")));

        countries = LocaleUtils.countriesByLanguage("fr");
        assertEquals(11, countries.size());
        assertTrue(countries.contains(Locale.FRANCE));
        assertTrue(countries.contains(new Locale("BE", "fr")));

        countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
}