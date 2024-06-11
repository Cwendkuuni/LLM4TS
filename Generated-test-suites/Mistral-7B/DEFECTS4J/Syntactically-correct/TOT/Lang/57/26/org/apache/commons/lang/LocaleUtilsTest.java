package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals("en_GB", LocaleUtils.toLocale("en_GB").toString());
        assertEquals("fr", LocaleUtils.toLocale("fr").getLanguage());
        assertEquals("CA", LocaleUtils.toLocale("fr_CA").getCountry());

        // Test invalid input
        try {
            LocaleUtils.toLocale(null);
            fail("Null input should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("");
            fail("Empty string input should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("xx_XYZ");
            fail("Invalid locale format should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale localeEN = new Locale("en", "US");
        Locale localeFR_CA = new Locale("fr", "CA");
        Locale localeFR = new Locale("fr");
        Locale defaultLocale = new Locale("en");

        List<Locale> localeList = LocaleUtils.localeLookupList(localeFR_CA);
        assertEquals(localeFR_CA, localeList.get(0));
        assertEquals(localeFR, localeList.get(1));
        assertEquals(localeEN, localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(localeEN, localeFR_CA);
        assertEquals(localeFR_CA, localeList.get(0));
        assertEquals(localeFR, localeList.get(1));
        assertEquals(localeEN, localeList.get(2));
        assertEquals(defaultLocale, localeList.get(3));

        localeList = LocaleUtils.localeLookupList(localeFR, localeEN);
        assertEquals(localeFR, localeList.get(0));
        assertEquals(localeEN, localeList.get(1));
        assertEquals(defaultLocale, localeList.get(2));

        localeList = LocaleUtils.localeLookupList(localeEN);
        assertEquals(localeEN, localeList.get(0));
        assertEquals(defaultLocale, localeList.get(1));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(272, localeList.size());

        // Test some specific locales
        assertTrue(localeList.contains(Locale.ENGLISH));
        assertTrue(localeList.contains(Locale.FRENCH));
        assertTrue(localeList.contains(Locale.GERMAN));
        assertTrue(localeList.contains(Locale.JAPANESE));
        assertTrue(localeList.contains(Locale.CHINESE));
        assertTrue(localeList.contains(Locale.SPANISH));
        assertTrue(localeList.contains(Locale.ITALIAN));

        // Test some locales for specific countries
        List<Locale> langsByCountry = LocaleUtils.languagesByCountry("US");
        assertEquals(11, langsByCountry.size());
        assertTrue(langsByCountry.contains(Locale.ENGLISH));
        assertTrue(langsByCountry.contains(Locale.SPANISH));
        assertTrue(langsByCountry.contains(Locale.FRENCH));

        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("es");
        assertEquals(15, countriesByLanguage.size());
        assertTrue(countriesByLanguage.contains(Locale.SPAIN));
        assertTrue(countriesByLanguage.contains(Locale.COLOMBIA));
        assertTrue(countriesByLanguage.contains(Locale.Cuba));
    }
}