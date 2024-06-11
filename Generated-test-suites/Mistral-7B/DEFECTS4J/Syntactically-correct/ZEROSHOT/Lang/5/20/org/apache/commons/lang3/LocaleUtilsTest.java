package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;

import org.junit.Test;

/**
 * Test class for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Test method for {@link LocaleUtils#toLocale(String)}.
     */
    @Test
    public void testToLocale() {
        // Valid cases
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_foo"));

        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR_bar"));

        // Invalid cases
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx"));

        assertFalse(LocaleUtils.toLocale("en_XX").equals(Locale.ENGLISH));
        assertFalse(LocaleUtils.toLocale("en_XX_xx").equals(Locale.ENGLISH));
        assertFalse(LocaleUtils.toLocale("en_XX_xx_xx").equals(Locale.ENGLISH));
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale)}.
     */
    @Test
    public void testLocaleLookupList() {
        List<Locale> list = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(4, list.size());
        assertSame(Locale.ENGLISH, list.get(0));
        assertEquals(Locale.ENGLISH, list.get(1));
        assertSame(Locale.ENGLISH, list.get(2));
        assertSame(Locale.US, list.get(3));

        list = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(5, list.size());
        assertSame(Locale.FRENCH, list.get(0));
        assertSame(Locale.FRENCH, list.get(1));
        assertSame(Locale.FRENCH, list.get(2));
        assertSame(Locale.FRENCH, list.get(3));
        assertSame(Locale.FRENCH, list.get(4));

        list = LocaleUtils.localeLookupList(Locale.GERMAN, Locale.US);
        assertEquals(6, list.size());
        assertSame(Locale.GERMAN, list.get(0));
        assertSame(Locale.GERMAN, list.get(1));
        assertSame(Locale.GERMAN, list.get(2));
        assertSame(Locale.GERMAN, list.get(3));
        assertSame(Locale.GERMAN, list.get(4));
        assertSame(Locale.US, list.get(5));
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleList()}.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.size() > 10);
        assertFalse(list.contains(null));
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleSet()}.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.size() > 10);
        assertFalse(set.contains(null));
    }

    /**
     * Test method for {@link LocaleUtils#isAvailableLocale(Locale)}.
     */
    @Test
    public void testIsAvailableLocale() {
        List<Locale> list = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        for (Locale locale : list) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale locale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test method for {@link LocaleUtils#languagesByCountry(String)}.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("GB");
        assertEquals(1, englishLocales.size());
        assertEquals(Locale.ENGLISH, englishLocales.get(0));

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, frenchLocales.size());
        assertSame(Locale.FRENCH, frenchLocales.get(0));
        assertSame(Locale.FRENCH, frenchLocales.get(1));

        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.languagesByCountry(null));
    }

    /**
     * Test method for {@link LocaleUtils#countriesByLanguage(String)}.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = LocaleUtils.countriesByLanguage("EN");
        assertEquals(2, englishLocales.size());
        assertSame(Locale.ENGLISH, englishLocales.get(0));
        assertSame(Locale.US, englishLocales.get(1));

        List<Locale> frenchLocales = LocaleUtils.countriesByLanguage("FR");
        assertEquals(1, frenchLocales.size());
        assertSame(Locale.FRENCH, frenchLocales.get(0));

        List<Locale> emptyList = Collections.emptyList();
        assertEquals(emptyList, LocaleUtils.countriesByLanguage(null));
    }
}