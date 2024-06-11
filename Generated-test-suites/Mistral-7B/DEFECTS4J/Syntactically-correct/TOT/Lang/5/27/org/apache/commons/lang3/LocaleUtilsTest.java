package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de_CH", LocaleUtils.toLocale("de_CH").toString());
        assertEquals("it_IT", LocaleUtils.toLocale("it_IT").toString());

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_US_xx"));
        assertNull(LocaleUtils.toLocale("en_US_x"));
        assertNull(LocaleUtils.toLocale("en_US_xxxx"));
        assertNull(LocaleUtils.toLocale("en_US_xxxxxx"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test with a valid locale
        Locale locale = LocaleUtils.toLocale("fr_CA");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(LocaleUtils.toLocale("fr_CA"), localeList.get(0));
        assertEquals(LocaleUtils.toLocale("fr_CA"), localeList.get(1));
        assertEquals(LocaleUtils.toLocale("fr"), localeList.get(2));

        // Test with a null locale
        localeList = LocaleUtils.localeLookupList(null);
        assertTrue(localeList.isEmpty());

        // Test with an empty locale
        locale = LocaleUtils.toLocale("");
        localeList = LocaleUtils.localeLookupList(locale);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertFalse(localeList.isEmpty());
        assertEquals(171, localeList.size());

        // Test with a valid locale
        Locale locale = LocaleUtils.toLocale("en_US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test with an invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(LocaleUtils.toLocale("en_US_xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with a valid country code
        List<Locale> localeList = LocaleUtils.languagesByCountry("CA");
        assertFalse(localeList.isEmpty());
        assertEquals(11, localeList.size());

        // Test with an invalid country code
        localeList = LocaleUtils.languagesByCountry(null);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with a valid language code
        List<Locale> localeList = LocaleUtils.countriesByLanguage("en");
        assertFalse(localeList.isEmpty());
        assertEquals(24, localeList.size());

        // Test with an invalid language code
        localeList = LocaleUtils.countriesByLanguage(null);
        assertTrue(localeList.isEmpty());
    }
}