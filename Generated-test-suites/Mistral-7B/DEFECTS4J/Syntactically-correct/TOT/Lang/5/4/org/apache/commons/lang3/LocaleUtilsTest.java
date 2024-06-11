package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("es", LocaleUtils.toLocale("es").toString());

        // Test invalid input
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_US_"));
        assertNull(LocaleUtils.toLocale("en_US_xxx"));
        assertNull(LocaleUtils.toLocale("en_US_xxxx"));
        assertNull(LocaleUtils.toLocale("en_US_xxx_xxxx"));
        assertNull(LocaleUtils.toLocale("en_US_xxx_xxxx_xxxx"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(4, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals("fr_CA", localeList.get(1).toString());
        assertEquals("fr", localeList.get(2).toString());
        assertEquals("en", localeList.get(3).toString());

        // Test null input
        locale = null;
        localeList = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        assertTrue(localeList.isEmpty());

        // Test empty country code
        locale = new Locale("fr", "");
        localeList = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        assertEquals(2, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals("fr", localeList.get(1).toString());

        // Test empty language code
        locale = new Locale("", "CA");
        localeList = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        assertEquals(1, localeList.size());
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.isEmpty());

        // Test population
        List<Locale> availableLocales = Locale.getAvailableLocales();
        List<Locale> localeListFromUtils = LocaleUtils.availableLocaleList();
        assertEquals(availableLocales.size(), localeListFromUtils.size());
        for (int i = 0; i < availableLocales.size(); i++) {
            assertTrue(localeListFromUtils.contains(availableLocales.get(i)));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Test known locale
        assertTrue(LocaleUtils.isAvailableLocale(Locale.getDefault()));

        // Test unknown locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty country code
        List<Locale> localeList = LocaleUtils.languagesByCountry(null);
        assertTrue(localeList.isEmpty());

        // Test valid country code
        List<Locale> localeListFr = LocaleUtils.languagesByCountry("FR");
        assertEquals(2, localeListFr.size());
        assertEquals("fr_FR", localeListFr.get(0).toString());
        assertEquals("fr_CA", localeListFr.get(1).toString());

        // Test unknown country code
        List<Locale> localeListUnknown = LocaleUtils.languagesByCountry("XX");
        assertTrue(localeListUnknown.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty language code
        List<Locale> localeList = LocaleUtils.countriesByLanguage(null);
        assertTrue(localeList.isEmpty());

        // Test valid language code
        List<Locale> localeListEn = LocaleUtils.countriesByLanguage("EN");
        assertEquals(2, localeListEn.size());
        assertEquals("en_US", localeListEn.get(0).toString());
        assertEquals("en_GB", localeListEn.get(1).toString());

        // Test unknown language code
        List<Locale> localeListUnknown = LocaleUtils.countriesByLanguage("XX");
        assertTrue(localeListUnknown.isEmpty());
    }
}