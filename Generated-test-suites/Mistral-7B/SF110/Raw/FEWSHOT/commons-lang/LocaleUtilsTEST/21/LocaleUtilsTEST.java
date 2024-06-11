package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleEmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocaleInvalidFormat() {
        String[] invalidFormats = new String[] {"", "--", "foo_bar--baz", "_foo", "foo__bar", "foo_bar_baz#123"};
        for (String invalidFormat : invalidFormats) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidFormat));
            assertEquals("Invalid locale format: " + invalidFormat, exception.getMessage());
        }
    }

    @Test
    public void testToLocaleValidFormat() {
        String[] validFormats = new String[] {"en", "en_US", "en_GB", "fr_CA", "fr_FR", "es_ES", "es_MX", "de_DE"};
        for (String validFormat : validFormats) {
            Locale locale = LocaleUtils.toLocale(validFormat);
            assertNotNull(locale);
            assertEquals(validFormat, locale.toString());
        }
    }

    @Test
    public void testToLocaleSameLanguageDifferentCountry() {
        Locale localeENUS = new Locale("en", "US");
        Locale localeENGB = new Locale("en", "GB");
        assertEquals(localeENUS.toString(), LocaleUtils.toLocale("en-US").toString());
        assertEquals(localeENGB.toString(), LocaleUtils.toLocale("en-GB").toString());
        assertNotEquals(localeENUS, localeENGB);
    }

    @Test
    public void testToLocaleSameLanguageSameCountry() {
        Locale localeENUS = new Locale("en", "US");
        assertEquals(localeENUS, LocaleUtils.toLocale("en-US"));
    }

    @Test
    public void testToLocaleDifferentLanguage() {
        Locale localeENUS = new Locale("en", "US");
        Locale localeFRFR = new Locale("fr", "FR");
        assertNotEquals(localeENUS, LocaleUtils.toLocale("fr-FR"));
        assertEquals(localeFRFR.toString(), LocaleUtils.toLocale("fr-FR").toString());
    }

    @Test
    public void testLocaleLookupList() {
        Locale localeENUS = new Locale("en", "US");
        List<Locale> lookups = LocaleUtils.localeLookupList(localeENUS);
        assertEquals(3, lookups.size());
        assertEquals(localeENUS, lookups.get(0));
        assertEquals(new Locale("en", ""), lookups.get(1));
        assertEquals(new Locale("", "US"), lookups.get(2));

        Locale localeFRFR = new Locale("fr", "FR");
        lookups = LocaleUtils.localeLookupList(localeFRFR);
        assertEquals(4, lookups.size());
        assertEquals(localeFRFR, lookups.get(0));
        assertEquals(new Locale("fr", ""), lookups.get(1));
        assertEquals(new Locale("", "FR"), lookups.get(2));
        assertEquals(localeENUS, lookups.get(3));
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        List<Locale> lookups = LocaleUtils.localeLookupList(null);
        assertEquals(0, lookups.size());
        assertNull(lookups.get(0));
    }

    @Test
    public void testLocaleLookupListDefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> lookups = LocaleUtils.localeLookupList(defaultLocale);
        assertEquals(4, lookups.size());
        assertEquals(defaultLocale, lookups.get(0));
        assertEquals(new Locale("en", ""), lookups.get(1));
        assertEquals(new Locale("", "US"), lookups.get(2));
        assertEquals(defaultLocale, lookups.get(3));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertTrue(availableLocales.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertTrue(availableLocales.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale invalidLocale = new Locale("foo", "bar");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        assertEquals(3, englishLocales.size());
        assertEquals(new Locale("en", "US"), englishLocales.get(0));
        assertEquals(new Locale("en", ""), englishLocales.get(1));
        assertEquals(new Locale("en", "CA"), englishLocales.get(2));

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, frenchLocales.size());
        assertEquals(new Locale("fr", "FR"), frenchLocales.get(0));
        assertEquals(new Locale("fr", "CA"), frenchLocales.get(1));

        List<Locale> emptyList = LocaleUtils.languagesByCountry(null);
        assertEquals(0, emptyList.size());
        assertNull(emptyList.get(0));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = LocaleUtils.availableLocaleList();
        assertEquals(3, englishLocales.size());

        List<Locale> usLocales = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, usLocales.size());
        assertEquals(new Locale("en", "US"), usLocales.get(0));
        assertEquals(new Locale("en", "CA"), usLocales.get(1));

        List<Locale> frLocales = LocaleUtils.countriesByLanguage("fr");
        assertEquals(2, frLocales.size());
        assertEquals(new Locale("fr", "FR"), frLocales.get(0));
        assertEquals(new Locale("fr", "CA"), frLocales.get(1));

        List<Locale> emptyList = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, emptyList.size());
        assertNull(emptyList.get(0));
    }
}