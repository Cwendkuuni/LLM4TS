package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Tests {@link LocaleUtils#toLocale(String)} with valid input.
     */
    @Test
    public void testToLocale_ValidInput() {
        List<String> inputs = Arrays.asList(
                "en",
                "en_GB",
                "en_GB_London",
                "fr_FR",
                "fr_CA",
                "es_ES",
                "es_MX",
                "ja_JP",
                "ko_KR",
                "zh_CN",
                "zh_TW"
        );
        for (String input : inputs) {
            Locale expected = new Locale(input.split("_")[0], input.split("_")[1]);
            Locale actual = LocaleUtils.toLocale(input);
            assertEquals(expected.getLanguage(), actual.getLanguage());
            assertEquals(expected.getCountry(), actual.getCountry());
        }
    }

    /**
     * Tests {@link LocaleUtils#toLocale(String)} with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput() {
        String[] inputs = {"", "en_", "en_GB_", "en_GB_London_", "en_GB_London_London", "en_GB_London_123"};
        for (String input : inputs) {
            LocaleUtils.toLocale(input);
        }
    }

    /**
     * Tests {@link LocaleUtils#localeLookupList(Locale)} with valid input.
     */
    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale baseLocale = new Locale("en", "US");
        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "US", "US-positiveSort"),
                new Locale("en", "US"),
                new Locale("en", "CA")
        );
        List<Locale> actualList = LocaleUtils.localeLookupList(baseLocale, new Locale("en", "CA"));
        assertEquals(expectedList, actualList);
    }

    /**
     * Tests {@link LocaleUtils#localeLookupList(Locale)} with null input.
     */
    @Test
    public void testLocaleLookupList_NullInput() {
        List<Locale> actualList = LocaleUtils.localeLookupList(null);
        assertTrue(Collections.isEmpty(actualList));
    }

    /**
     * Tests {@link LocaleUtils#availableLocaleList()}
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocales = LocaleUtils.availableLocaleList();
        List<Locale> actualLocales = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Tests {@link LocaleUtils#availableLocaleSet()}
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedLocales = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocales = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Tests {@link LocaleUtils#isAvailableLocale(Locale)} with available locales.
     */
    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        for (Locale locale : LocaleUtils.availableLocaleList()) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    /**
     * Tests {@link LocaleUtils#isAvailableLocale(Locale)} with invalid locale.
     */
    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale invalidLocale = new Locale("nonexistent_language", "nonexistent_country");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    /**
     * Tests {@link LocaleUtils#languagesByCountry(String)} with available countries.
     */
    @Test
    public void testLanguagesByCountry_AvailableCountry() {
        for (String countryCode : new String[]{"US", "CA", "FR", "ES", "JP", "KR", "CN", "TW"}) {
            List<Locale> expectedLocales = Arrays.asList(
                    new Locale("en", "US"),
                    new Locale("es", "ES"),
                    new Locale("fr", "FR"),
                    new Locale("ja", "JP"),
                    new Locale("zh", "CN"),
                    new Locale("ko", "KR"),
                    new Locale("es", "MX"),
                    new Locale("fr", "CA")
            );
            List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
            assertEquals(expectedLocales, actualLocales);
        }
    }

    /**
     * Tests {@link LocaleUtils#languagesByCountry(String)} with null input.
     */
    @Test
    public void testLanguagesByCountry_NullInput() {
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(null);
        assertTrue(Collections.isEmpty(actualLocales));
    }

    /**
     * Tests {@link LocaleUtils#countriesByLanguage(String)} with available languages.
     */
    @Test
    public void testCountriesByLanguage_AvailableLanguage() {
        for (String languageCode : new String[]{"en", "es", "fr", "ja", "zh"}) {
            List<Locale> expectedLocales = Arrays.asList(
                    new Locale("en", "US"),
                    new Locale("en", "CA"),
                    new Locale("es", "ES"),
                    new Locale("es", "MX"),
                    new Locale("fr", "FR"),
                    new Locale("fr", "CA"),
                    new Locale("ja", "JP"),
                    new Locale("ja", "JP-jp-JP"),
                    new Locale("zh", "CN"),
                    new Locale("zh", "TW")
            );
            List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
            assertEquals(expectedLocales, actualLocales);
        }
    }

    /**
     * Tests {@link LocaleUtils#countriesByLanguage(String)} with null input.
     */
    @Test
    public void testCountriesByLanguage_NullInput() {
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(null);
        assertTrue(Collections.isEmpty(actualLocales));
    }
}