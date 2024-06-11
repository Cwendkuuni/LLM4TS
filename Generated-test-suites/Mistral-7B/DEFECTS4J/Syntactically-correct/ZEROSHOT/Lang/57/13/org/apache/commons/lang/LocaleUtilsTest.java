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
        String[] testCases = new String[]{"en", "en_GB", "en_GB_xxx", "fr", "fr_CA", "fr_CA_xxx"};
        Locale[] expectedLocales = new Locale[]{new Locale("en", ""), new Locale("en", "GB"), new Locale("en", "GB", "xxx"), new Locale("fr", ""), new Locale("fr", "CA"), new Locale("fr", "CA", "xxx")};

        for (int i = 0; i < testCases.length; i++) {
            Locale locale = LocaleUtils.toLocale(testCases[i]);
            assertEquals(expectedLocales[i].getLanguage(), locale.getLanguage());
            assertEquals(expectedLocales[i].getCountry(), locale.getCountry());
            if (expectedLocales[i].getVariant().length() > 0) {
                assertEquals(expectedLocales[i].getVariant(), locale.getVariant());
            } else {
                assertNull(locale.getVariant());
            }
        }

        // Test null input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> testLocales = Arrays.asList(new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));

        List<Locale> lookupList = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(testLocales.size(), lookupList.size());
        for (int i = 0; i < testLocales.size(); i++) {
            assertEquals(testLocales.get(i), lookupList.get(i));
        }

        lookupList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(testLocales.size() + 1, lookupList.size());
        assertTrue(lookupList.contains(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> testLocales = Arrays.asList(Locale.getAvailableLocales().toArray(new Locale[0]));
        assertEquals(testLocales.size(), availableLocales.size());
        for (int i = 0; i < testLocales.size(); i++) {
            assertEquals(testLocales.get(i), availableLocales.get(i));
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        Set<Locale> testLocales = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(testLocales.size(), availableLocales.size());
        assertTrue(availableLocales.containsAll(testLocales));
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> testLocales = Arrays.asList(Locale.GERMAN, Locale.FRENCH, Locale.ENGLISH, Locale.ITALIAN);
        for (Locale locale : testLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale localeNotAvailable = new Locale("xy", "xy");
        assertFalse(LocaleUtils.isAvailableLocale(localeNotAvailable));
    }

    @Test
    public void testLanguagesByCountry() {
        String testCountryCode = "CA";
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));

        List<Locale> testLocales = LocaleUtils.languagesByCountry(testCountryCode);
        assertEquals(expectedLocales.size(), testLocales.size());
        for (int i = 0; i < expectedLocales.size(); i++) {
            assertEquals(expectedLocales.get(i), testLocales.get(i));
        }

        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        String testLanguageCode = "fr";
        List<Locale> expectedLocales = Arrays.asList(new Locale("fr", "CA"));

        List<Locale> testLocales = LocaleUtils.countriesByLanguage(testLanguageCode);
        assertEquals(expectedLocales.size(), testLocales.size());
        for (int i = 0; i < expectedLocales.size(); i++) {
            assertEquals(expectedLocales.get(i), testLocales.get(i));
        }

        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}