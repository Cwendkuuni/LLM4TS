package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test method for {@link LocaleUtils#toLocale(java.lang.String)}.
     */
    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("en", LocaleUtils.toLocale("en").toString());
        assertEquals("fr", LocaleUtils.toLocale("fr").toString());

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_US_"));
        assertNull(LocaleUtils.toLocale("en_US_xx"));
        assertNull(LocaleUtils.toLocale("en_US_xx_"));

        // Test invalid format
        try {
            LocaleUtils.toLocale("en_US_xx_YY");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("EN_US");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_US_xx_YY");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test method for {@link LocaleUtils#localeLookupList(Locale)}.
     */
    @Test
    public void testLocaleLookupList() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        assertEquals(Locale.ENGLISH, localeList.get(2));

        locale = LocaleUtils.toLocale("en");
        localeList = LocaleUtils.localeLookupList(locale);

        assertEquals(2, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));

        locale = null;
        localeList = LocaleUtils.localeLookupList(locale);

        assertEquals(0, localeList.size());
    }

    /**
     * Test method for {@link LocaleUtils#languagesByCountry(java.lang.String)}.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");

        assertEquals(2, languages.size());
        assertEquals(Locale.CANADA_FRENCH, languages.get(0));
        assertEquals(Locale.CANADA, languages.get(1));

        languages = LocaleUtils.languagesByCountry("US");

        assertEquals(2, languages.size());
        assertEquals(Locale.US, languages.get(0));
        assertEquals(Locale.ENGLISH, languages.get(1));

        languages = LocaleUtils.languagesByCountry("GB");

        assertEquals(1, languages.size());
        assertEquals(Locale.ENGLISH, languages.get(0));

        languages = LocaleUtils.languagesByCountry("XX");

        assertEquals(0, languages.size());
    }

    /**
     * Test method for {@link LocaleUtils#countriesByLanguage(java.lang.String)}.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");

        assertEquals(5, countries.size());
        assertEquals(Locale.US, countries.get(0));
        assertEquals(Locale.CANADA, countries.get(1));
        assertEquals(Locale.GB, countries.get(2));
        assertEquals(Locale.AU, countries.get(3));
        assertEquals(Locale.IE, countries.get(4));

        countries = LocaleUtils.countriesByLanguage("fr");

        assertEquals(2, countries.size());
        assertEquals(Locale.CA, countries.get(0));
        assertEquals(Locale.FR, countries.get(1));

        countries = LocaleUtils.countriesByLanguage("xx");

        assertEquals(0, countries.size());
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleList()}.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();

        assertEquals(88, availableLocales.size());

        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.GERMAN));
        assertTrue(availableLocales.contains(Locale.ITALIAN));
        assertTrue(availableLocales.contains(Locale.JAPANESE));
        assertTrue(availableLocales.contains(Locale.SPANISH));
        assertTrue(availableLocales.contains(Locale.CHINESE));
        assertTrue(availableLocales.contains(Locale.KOREAN));

        assertTrue(availableLocales.contains(Locale.US));
        assertTrue(availableLocales.contains(Locale.CA));
        assertTrue(availableLocales.contains(Locale.GB));
        assertTrue(availableLocales.contains(Locale.DE));
        assertTrue(availableLocales.contains(Locale.IT));
        assertTrue(availableLocales.contains(Locale.JA));
        assertTrue(availableLocales.contains(Locale.ES));
        assertTrue(availableLocales.contains(Locale.ZH));

        assertTrue(availableLocales.contains(Locale.CHINESE_TAIWAN));
        assertTrue(availableLocales.contains(Locale.HUNGARIAN));
        assertTrue(availableLocales.contains(Locale.POLISH));
        assertTrue(availableLocales.contains(Locale.PORTUGAL));
        assertTrue(availableLocales.contains(Locale.RUSSIAN));
        assertTrue(availableLocales.contains(Locale.SWEDISH));
        // Add more assertions as needed
    }

    /**
     * Test method for {@link LocaleUtils#availableLocaleSet()}.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();

        assertEquals(88, availableLocales.size());

        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRENCH));
        assertTrue(availableLocales.contains(Locale.GERMAN));
        assertTrue(availableLocales.contains(Locale.ITALIAN));
        assertTrue(availableLocales.contains(Locale.JAPANESE));
        assertTrue(availableLocales.contains(Locale.SPANISH));
        assertTrue(availableLocales.contains(Locale.CHINESE));
        assertTrue(availableLocales.contains(Locale.KOREAN));

        assertTrue(availableLocales.contains(Locale.US));
        assertTrue(availableLocales.contains(Locale.CA));
        assertTrue(availableLocales.contains(Locale.GB));
        assertTrue(availableLocales.contains(Locale.DE));
        assertTrue(availableLocales.contains(Locale.IT));
        assertTrue(availableLocales.contains(Locale.JA));
        assertTrue(availableLocales.contains(Locale.ES));
        assertTrue(availableLocales.contains(Locale.ZH));

        assertTrue(availableLocales.contains(Locale.CHINESE_TAIWAN));
        assertTrue(availableLocales.contains(Locale.HUNGARIAN));
        assertTrue(availableLocales.contains(Locale.POLISH));
        assertTrue(availableLocales.contains(Locale.PORTUGAL));
        assertTrue(availableLocales.contains(Locale.RUSSIAN));
        assertTrue(availableLocales.contains(Locale.SWEDISH));
        // Add more assertions as needed
    }
}