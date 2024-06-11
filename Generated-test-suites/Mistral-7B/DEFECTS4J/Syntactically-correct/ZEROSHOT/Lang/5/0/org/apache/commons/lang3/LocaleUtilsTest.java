package org.apache.commons.lang3;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        List<String> validLocales = Arrays.asList("en", "en_GB", "en_GB_xxx", "fr_CA", "fr_CA_xxx", "fr_FR", "fr_FR_xxx");
        for (String localeString : validLocales) {
            Locale expectedLocale = new Locale(localeString.split("_")[0], localeString.split("_")[1]);
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            if (localeString.split("_").length > 2) {
                assertEquals(expectedLocale.getVariant(), actualLocale.getVariant());
            } else {
                assertTrue(actualLocale.getVariant().isEmpty());
            }
        }

        // Test invalid locale strings
        List<String> invalidLocales = Arrays.asList("en_GB_XXX", "en_GB_xx", "en_GB__", "en_GB_en", "en_GB_XXX_XXX");
        for (String invalidLocaleString : invalidLocales) {
            try {
                LocaleUtils.toLocale(invalidLocaleString);
                fail("Expected IllegalArgumentException for invalid locale string: " + invalidLocaleString);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> localeLookupList = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> expectedLocaleLookupList = Arrays.asList(
                baseLocale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        assertEquals(expectedLocaleLookupList, localeLookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        // Check size
        int expectedSize = Locale.getAvailableLocales().length;
        assertEquals(expectedSize, availableLocaleList.size());

        // Check first few elements
        List<String> localeStringsToCheck = Arrays.asList("en_US", "es_MX", "fr_CA", "de_DE", "ja_JP");
        for (int i = 0; i < 5; i++) {
            String expectedLocaleString = localeStringsToCheck.get(i);
            Locale expectedLocale = new Locale(expectedLocaleString.split("_")[0], expectedLocaleString.split("_")[1]);
            Locale actualLocale = availableLocaleList.get(i);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            assertTrue(actualLocale.getVariant().isEmpty());
        }

        // Check last element
        Locale lastLocale = availableLocaleList.get(availableLocaleList.size() - 1);
        String expectedLastLocaleString = Locale.getDefault().toLanguageTag();
        assertEquals(expectedLastLocaleString, lastLocale.toLanguageTag());
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("de", "CA"),
                new Locale("es", "CA")
        );
        String countryCode = "CA";
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedLocales = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("en", "US"),
                new Locale("fr", "CA"),
                new Locale("fr", "FR"),
                new Locale("de", "DE"),
                new Locale("de", "AT"),
                new Locale("es", "ES"),
                new Locale("es", "MX")
        );
        String languageCode = "en";
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "GB");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "GB_XXX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }
}