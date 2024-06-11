package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class LocaleUtilsTest {

    private static final int TEST_THREAD_COUNT = 10;

    @org.junit.BeforeClass
    public static void setup() {
        // No need to initialize the static fields as they are already initialized in the class
    }

    @org.junit.Test
    public void testToLocale() {
        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid locale format - missing language or country code
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));

        // Test invalid locale format - extra characters
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US_es"));

        // Test valid locales
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRANCE, Locale.FRANCE);
        assertEquals(Locale.GERMANY, Locale.GERMANY);
        assertEquals(Locale.ITALY, Locale.ITALY);
        assertEquals(Locale.JAPAN, Locale.JAPAN);
        assertEquals(Locale.KOREA, Locale.KOREA);
    }

    @org.junit.Test
    public void testLocaleLookupList() {
        // Test null input
        assertSame(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Test empty locale
        assertEquals(Collections.singletonList(Locale.EMPTY), LocaleUtils.localeLookupList(Locale.EMPTY));

        // Test valid locale with no variant or country or language
        assertEquals(Collections.singletonList(Locale.SIMPLIFIED_CHINESE), LocaleUtils.localeLookupList(new Locale("zh")));

        // Test valid locale with variant, country, and language
        Locale locale = new Locale("en", "US", "CA");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(4, list.size());
        assertTrue(list.contains(locale));
        assertTrue(list.contains(new Locale("en", "US")));
        assertTrue(list.contains(new Locale("en", "")));
        assertTrue(list.contains(Locale.CANADA));

        // Test concurrent access
        final AtomicReference<List<Locale>> result = new AtomicReference<>(null);
        ExecutorService executor = Executors.newFixedThreadPool(TEST_THREAD_COUNT);
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            executor.submit(() -> result.set(LocaleUtils.localeLookupList(Locale.ENGLISH)));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Locale> resultList = result.get();
        assertEquals(Collections.singletonList(Locale.ENGLISH), resultList);

        // Test invalid locale
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.localeLookupList(null));
    }

    @org.junit.Test
    public void testAvailableLocaleList() {
        // Test empty list
        assertTrue(LocaleUtils.availableLocaleList().isEmpty());

        // Test available locales
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(136, availableLocales.size());
        assertTrue(availableLocales.contains(Locale.CHINA));
        assertTrue(availableLocales.contains(Locale.ENGLISH));
        assertTrue(availableLocales.contains(Locale.FRANCE));
        assertTrue(availableLocales.contains(Locale.GERMANY));
        assertTrue(availableLocales.contains(Locale.ITALY));
        assertTrue(availableLocales.contains(Locale.JAPAN));
        assertTrue(availableLocales.contains(Locale.KOREA));
        assertTrue(availableLocales.contains(Locale.RUSSIA));
        assertTrue(availableLocales.contains(Locale.SPAIN));
        assertTrue(availableLocales.contains(Locale.TRADITIONAL_CHINESE));
        assertTrue(availableLocales.contains(Locale.UNITED_STATES));

        // Test concurrent access
        final AtomicReference<List<Locale>> result = new AtomicReference<>(null);
        ExecutorService executor = Executors.newFixedThreadPool(TEST_THREAD_COUNT);
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            executor.submit(() -> result.set(LocaleUtils.availableLocaleList()));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Locale> resultList = result.get();
        assertEquals(Collections.unmodifiableList(LocaleUtils.availableLocaleList()), resultList);
    }

    @org.junit.Test
    public void testAvailableLocaleSet() {
        // Test empty set
        assertTrue(LocaleUtils.availableLocaleSet().isEmpty());

        // Test available locales
        List<Locale> availableLocales = Arrays.asList(LocaleUtils.availableLocaleList().toArray(new Locale[0]));
        Set<Locale> availableLocaleSet = new CopyOnWriteArrayList<>(availableLocales);
        assertEquals(Collections.unmodifiableSet(LocaleUtils.availableLocaleSet()), availableLocaleSet);
        assertTrue(availableLocaleSet.contains(Locale.CHINA));
        assertTrue(availableLocaleSet.contains(Locale.ENGLISH));
        assertTrue(availableLocaleSet.contains(Locale.FRANCE));
        assertTrue(availableLocaleSet.contains(Locale.GERMANY));
        assertTrue(availableLocaleSet.contains(Locale.ITALY));
        assertTrue(availableLocaleSet.contains(Locale.JAPAN));
        assertTrue(availableLocaleSet.contains(Locale.KOREA));
        assertTrue(availableLocaleSet.contains(Locale.RUSSIA));
        assertTrue(availableLocaleSet.contains(Locale.SPAIN));
        assertTrue(availableLocaleSet.contains(Locale.TRADITIONAL_CHINESE));
        assertTrue(availableLocaleSet.contains(Locale.UNITED_STATES));

        // Test concurrent access
        final AtomicReference<Set<Locale>> result = new AtomicReference<>(null);
        ExecutorService executor = Executors.newFixedThreadPool(TEST_THREAD_COUNT);
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            executor.submit(() -> result.set(new CopyOnWriteArrayList<>(LocaleUtils.availableLocaleList())));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<Locale> resultSet = result.get();
        assertEquals(Collections.unmodifiableSet(LocaleUtils.availableLocaleSet()), resultSet);
    }

    @org.junit.Test
    public void testIsAvailableLocale() {
        // Test null input
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid_locale")));

        // Test available locales
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        // Test concurrent access
        final AtomicReference<List<Locale>> availableLocaleListRef = new AtomicReference<>(null);
        ExecutorService executor = Executors.newFixedThreadPool(TEST_THREAD_COUNT);
        executor.submit(() -> availableLocaleListRef.set(LocaleUtils.availableLocaleList()));
        List<Locale> availableLocaleList = availableLocaleListRef.get();
        for (Locale locale : availableLocaleList) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
        executor.shutdown();
    }

    @org.junit.Test
    public void testLanguagesByCountry() {
        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));

        // Test invalid country code
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));

        // Test valid country code with no languages
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("XX"));

        // Test valid country code with languages
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertEquals(25, languages.size());
        assertTrue(languages.contains(Locale.US));
        assertTrue(languages.contains(Locale.ENGLISH));
        assertTrue(languages.contains(Locale.ENGLISH_US));
        assertTrue(languages.contains(Locale.ENGLISH_IN));
        assertTrue(languages.contains(Locale.ENGLISH_GB));
        assertTrue(languages.contains(Locale.ENGLISH_AU));
        assertTrue(languages.contains(Locale.ENGLISH_CA));
        assertTrue(languages.contains(Locale.ENGLISH_IE));
        assertTrue(languages.contains(Locale.ENGLISH_NZ));
        assertTrue(languages.contains(Locale.ENGLISH_ZA));
        assertTrue(languages.contains(Locale.SIMPLIFIED_CHINESE_HONG_KONG));
        assertTrue(languages.contains(Locale.SIMPLIFIED_CHINESE_SINGAPORE));
        assertTrue(languages.contains(Locale.TRADITIONAL_CHINESE));
        assertTrue(languages.contains(Locale.TRADITIONAL_CHINESE_HONG_KONG));
        assertTrue(languages.contains(Locale.TRADITIONAL_CHINESE_TW));

        // Test concurrent access
        final AtomicReference<List<Locale>> result = new AtomicReference<>(null);
        ExecutorService executor = Executors.newFixedThreadPool(TEST_THREAD_COUNT);
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            executor.submit(() -> result.set(LocaleUtils.languagesByCountry("US")));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Locale> resultList = result.get();
        assertEquals(LocaleUtils.languagesByCountry("US"), resultList);
    }

    @org.junit.Test
    public void testCountriesByLanguage() {
        // Test null input
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));

        // Test invalid language code
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));

        // Test valid language code with no countries
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("xyz"));

        // Test valid language code with countries
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertEquals(11, countries.size());
        assertTrue(countries.contains(Locale.US));
        assertTrue(countries.contains(Locale.ENGLISH_AU));
        assertTrue(countries.contains(Locale.ENGLISH_CA));
        assertTrue(countries.contains(Locale.ENGLISH_GB));
        assertTrue(countries.contains(Locale.ENGLISH_IE));
        assertTrue(countries.contains(Locale.ENGLISH_IN));
        assertTrue(countries.contains(Locale.ENGLISH_NZ));
        assertTrue(countries.contains(Locale.ENGLISH_ZA));
        assertTrue(countries.contains(Locale.SIMPLIFIED_CHINESE_SINGAPORE));
        assertTrue(countries.contains(Locale.SIMPLIFIED_CHINESE_HONG_KONG));
        assertTrue(countries.contains(Locale.TRADITIONAL_CHINESE_TW));

        // Test concurrent access
        final AtomicReference<List<Locale>> result = new AtomicReference<>(null);
        ExecutorService executor = Executors.newFixedThreadPool(TEST_THREAD_COUNT);
        for (int i = 0; i < TEST_THREAD_COUNT; i++) {
            executor.submit(() -> result.set(LocaleUtils.countriesByLanguage("en")));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Locale> resultList = result.get();
        assertEquals(LocaleUtils.countriesByLanguage("en"), resultList);
    }
}