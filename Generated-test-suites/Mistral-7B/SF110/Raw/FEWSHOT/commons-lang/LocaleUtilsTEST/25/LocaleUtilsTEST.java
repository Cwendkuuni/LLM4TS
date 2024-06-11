package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoLanguageOrCountry() {
        LocaleUtils.toLocale("--");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoLanguage() {
        LocaleUtils.toLocale("_en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoCountry() {
        LocaleUtils.toLocale("en_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCountryCode() {
        LocaleUtils.toLocale("en_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidLanguageCode() {
        LocaleUtils.toLocale("XXX_en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_MultipleUnderscores() {
        LocaleUtils.toLocale("en__xx_fr");
    }

    @Test
    public void testToLocale_ValidFormat_NoLanguageOrCountry() {
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_ValidFormat_NoLanguage() {
        assertEquals(new Locale("", "en"), LocaleUtils.toLocale("_en"));
    }

    @Test
    public void testToLocale_ValidFormat_NoCountry() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en_"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountry() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountryWithVariant() {
        assertEquals(new Locale("en", "US", "ms-latn"), LocaleUtils.toLocale("en_US_ms-latn"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "FR");
        List<Locale> locales = LocaleUtils.localeLookupList(locale);
        assertTrue(locales.contains(locale));
        assertTrue(locales.size() >= 3);
        assertEquals(new Locale("fr", ""), locales.get(1));
        assertEquals(Locale.getDefault(), locales.get(2));

        locale = null;
        locales = LocaleUtils.localeLookupList(locale);
        assertTrue(locales.isEmpty());
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> locales = LocaleUtils.localeLookupList(null);
        assertTrue(locales.contains(Locale.getDefault()));
        assertTrue(locales.size() >= 2);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.size() > 0);
        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertTrue(locales.size() > 0);
        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        boolean result = LocaleUtils.isAvailableLocale(new Locale("en", "US"));
        assertTrue(result);
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        boolean result = LocaleUtils.isAvailableLocale(new Locale("", ""));
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(new Locale("en", "US")));
        assertTrue(languages.contains(new Locale("es", "US")));

        languages = LocaleUtils.languagesByCountry("GB");
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(new Locale("en", "GB")));
        assertTrue(languages.contains(new Locale("sc", "GB")));

        languages = LocaleUtils.languagesByCountry("JP");
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(new Locale("ja", "JP")));
        assertTrue(languages.contains(new Locale("en", "JP")));

        languages = LocaleUtils.languagesByCountry(null);
        assertTrue(languages.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("US", "en")));
        assertTrue(countries.contains(new Locale("CA", "en")));
        assertTrue(countries.contains(new Locale("GB", "en")));

        countries = LocaleUtils.countriesByLanguage("fr");
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("FR", "fr")));
        assertTrue(countries.contains(new Locale("BE", "fr")));

        countries = LocaleUtils.countriesByLanguage("zh");
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("CN", "zh")));
        assertTrue(countries.contains(new Locale("HK", "zh")));

        countries = LocaleUtils.countriesByLanguage(null);
        assertTrue(countries.isEmpty());
    }
}