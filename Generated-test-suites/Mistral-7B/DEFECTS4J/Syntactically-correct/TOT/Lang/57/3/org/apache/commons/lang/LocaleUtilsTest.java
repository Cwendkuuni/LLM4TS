package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());
        assertEquals("de_DE", LocaleUtils.toLocale("de_DE").toString());

        // Test invalid input: language code is empty
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid input: country code is empty
        assertNull(LocaleUtils.toLocale("en"));

        // Test invalid input: separator is not an underscore
        assertNull(LocaleUtils.toLocale("en_GB_UK"));

        // Test invalid input: language code is not lowercase
        assertNull(LocaleUtils.toLocale("EN_US"));

        // Test invalid input: country code is not uppercase
        assertNull(LocaleUtils.toLocale("en_gb"));

        // Test invalid input: country code has wrong length
        assertNull(LocaleUtils.toLocale("en_US_"));

        // Test invalid input: variant is not empty
        assertNull(LocaleUtils.toLocale("en_US_en"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertTrue(localeList.isEmpty());

        // Test list with one locale
        Locale locale = LocaleUtils.toLocale("en_US");
        List<Locale> localeList1 = LocaleUtils.localeLookupList(locale);
        assertEquals(1, localeList1.size());
        assertEquals(locale, localeList1.get(0));

        // Test list with multiple locales
        Locale locale2 = LocaleUtils.toLocale("fr_FR");
        List<Locale> localeList2 = LocaleUtils.localeLookupList(locale, locale);
        assertEquals(2, localeList2.size());
        assertEquals(locale, localeList2.get(0));
        assertEquals(locale2, localeList2.get(1));

        // Test list with default locale
        Locale defaultLocale = Locale.getDefault();
        List<Locale> localeList3 = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(3, localeList3.size());
        assertEquals(locale, localeList3.get(0));
        assertEquals(locale2, localeList3.get(1));
        assertEquals(defaultLocale, localeList3.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.isEmpty());

        // Test list with available locales
        List<Locale> availableLocales = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        List<Locale> localeListExpected = LocaleUtils.availableLocaleList();
        assertEquals(availableLocales, localeListExpected);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Locale locale = Locale.ENGLISH;
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test invalid locale
        Locale localeInvalid = new Locale("invalid", "invalid");
        assertFalse(LocaleUtils.isAvailableLocale(localeInvalid));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        List<Locale> localeList = LocaleUtils.languagesByCountry(null);
        assertTrue(localeList.isEmpty());

        // Test list with languages for a country
        List<Locale> expectedLocaleList = Arrays.asList(Locale.FRENCH, Locale.CANADIAN_FRENCH, Locale.CHINESE);
        List<Locale> localeList = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedLocaleList, localeList);

        // Test list with empty country code
        localeList = LocaleUtils.languagesByCountry("");
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        List<Locale> localeList = LocaleUtils.countriesByLanguage(null);
        assertTrue(localeList.isEmpty());

        // Test list with countries for a language
        List<Locale> expectedLocaleList = Arrays.asList(Locale.CANADA, Locale.SWITZERLAND, Locale.AUSTRIA);
        List<Locale> localeList = LocaleUtils.countriesByLanguage("DE");
        assertEquals(expectedLocaleList, localeList);

        // Test list with empty language code
        localeList = LocaleUtils.countriesByLanguage("");
        assertTrue(localeList.isEmpty());
    }
}