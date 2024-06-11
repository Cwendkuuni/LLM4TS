package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.junit.Test;

public class LocaleUtilsTest {

    private static final List<Locale> AVAILABLE_LOCALE_LIST = Arrays.asList(Locale.getAvailableLocales());
    private static final Set<Locale> AVAILABLE_LOCALE_SET = new HashSet<>(AVAILABLE_LOCALE_LIST);

    @Test
    public void testToLocale() {
        assertEquals(new Locale("en_US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("de_DE"), LocaleUtils.toLocale("de_DE"));
        assertEquals(new Locale("fr_FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("es_ES"), LocaleUtils.toLocale("es_ES"));
        assertEquals(new Locale("it_IT"), LocaleUtils.toLocale("it_IT"));
        assertEquals(new Locale("ja_JP"), LocaleUtils.toLocale("ja_JP"));
        assertEquals(new Locale("ko_KR"), LocaleUtils.toLocale("ko_KR"));
        assertEquals(new Locale("zh_CN"), LocaleUtils.toLocale("zh_CN"));
        assertEquals(new Locale("zh_TW"), LocaleUtils.toLocale("zh_TW"));
        assertEquals(new Locale("ru_RU"), LocaleUtils.toLocale("ru_RU"));

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_#");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_es");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_es");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_es_es");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("es_es_es");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("es_es_es#");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("--foo");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("--foo--bar");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("--foo--bar--baz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("es_ES_es_ES_es_ES_es_ES_es_ES_es_ES_es_ES_es_ES_es_ES");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");

        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("en", ""), list.get(1));
        assertEquals(new Locale("", "US"), list.get(2));

        list = LocaleUtils.localeLookupList(locale, new Locale("fr", "FR"));
        assertEquals(4, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("en", ""), list.get(1));
        assertEquals(new Locale("", "US"), list.get(2));
        assertEquals(new Locale("fr", "FR"), list.get(3));
    }

    @Test
    public void testLocaleLookupListNull() {
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertEquals(1, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testLocaleLookupListNullDefault() {
        List<Locale> list = LocaleUtils.localeLookupList(new Locale("en", "US"), null);
        assertEquals(2, list.size());
        assertEquals(new Locale("en", "US"), list.get(0));
        assertNull(list.get(1));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertEquals(AVAILABLE_LOCALE_LIST.size(), list.size());
        assertTrue(list.containsAll(AVAILABLE_LOCALE_LIST));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertEquals(AVAILABLE_LOCALE_SET.size(), set.size());
        assertTrue(set.containsAll(AVAILABLE_LOCALE_SET));
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> list = Arrays.asList(Locale.getAvailableLocales());
        for (Locale locale : list) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        Locale locale = new Locale("foo", "bar");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        List<String> expectedEnglishCountryCodes = Arrays.asList("US", "ZM", "ZW");
        List<Locale> expectedEnglishLocales = expectedEnglishCountryCodes.stream()
                .map(countryCode -> new Locale(countryCode))
                .collect(Collectors.toList());

        assertEquals(expectedEnglishLocales.size(), englishLocales.size());
        assertTrue(englishLocales.containsAll(expectedEnglishLocales));
    }

    @Test
    public void testLanguagesByCountryNull() {
        List<Locale> list = LocaleUtils.languagesByCountry(null);
        assertEquals(0, list.size());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> englishLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "IE"));
        List<Locale> englishUSLocales = LocaleUtils.countriesByLanguage("en");

        assertEquals(englishLocales.size(), englishUSLocales.size());
        assertTrue(englishUSLocales.containsAll(Collections.singletonList(new Locale("US"))));
    }

    @Test
    public void testCountriesByLanguageNull() {
        List<Locale> list = LocaleUtils.countriesByLanguage(null);
        assertEquals(0, list.size());
    }
}