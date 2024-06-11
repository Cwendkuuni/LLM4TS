package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEqualsIgnoreCase;

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

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_Empty() {
        assertSame(Locale.ROOT, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        throwExceptionAndAssertMessage("Invalid locale format: ", () -> LocaleUtils.toLocale("_"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageLength() {
        throwExceptionAndAssertMessage("Invalid locale format: ", () -> LocaleUtils.toLocale("xx_"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        throwExceptionAndAssertMessage("Invalid locale format: ", () -> LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountryLength() {
        throwExceptionAndAssertMessage("Invalid locale format: ", () -> LocaleUtils.toLocale("en_xx"));
    }

    @Test
    public void testToLocale_InvalidFormat_DuplicateCountryCode() {
        throwExceptionAndAssertMessage("Invalid locale format: ", () -> LocaleUtils.toLocale("en_EN"));
    }

    @Test
    public void testToLocale_ValidFormat_1Language() {
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
    }

    @Test
    public void testToLocale_ValidFormat_2Language_1Country() {
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
    }

    @Test
    public void testToLocale_ValidFormat_2Language_2Country() {
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA_FR"));
    }

    @Test
    public void testToLocale_ValidFormat_3Language() {
        assertEquals(new Locale("fr", "CA", "QC"), LocaleUtils.toLocale("fr_CA_QC"));
    }

    @Test
    public void testToLocale_ValidFormat_3Language_2Country() {
        assertEquals(new Locale("fr", "FR", "QC"), LocaleUtils.toLocale("fr_FR_QC"));
    }

    @Test
    public void testToLocale_ValidFormat_3Language_3Country() {
        assertEquals(new Locale("fr", "FR", "QC"), LocaleUtils.toLocale("fr_FR_QC_FR"));
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertFalse(LocaleUtils.localeLookupList(null).isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(new Locale("")));
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        Locale locale = new Locale("fr", "CA", "qc");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_LocaleWithoutVariant() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", ""), new Locale(""));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        Locale locale = new Locale("fr", "CA");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr", ""), new Locale(""), defaultLocale);
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_EmptyAvailableLocales() {
        ConcurrentHashMap<String, List<Locale>> concurrentHashMap = new ConcurrentHashMap<>();
        LocaleUtils localeUtils = new LocaleUtils() {
            @Override
            protected ConcurrentMap<String, List<Locale>> cLanguagesByCountry() {
                return concurrentHashMap;
            }

            @Override
            protected ConcurrentMap<String, List<Locale>> cCountriesByLanguage() {
                return concurrentHashMap;
            }
        };
        List<Locale> result = localeUtils.localeLookupList(new Locale("fr", "CA"));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(LocaleUtils.AVAILABLE_LOCALE_LIST.size(), result.size());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(LocaleUtils.AVAILABLE_LOCALE_SET.size(), result.size());
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }

    @Test
    public void testIsAvailableLocale_UnavailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"));
        assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_EmptyAvailableLocales() {
        ConcurrentHashMap<String, List<Locale>> concurrentHashMap = new ConcurrentHashMap<>();
        LocaleUtils localeUtils = new LocaleUtils() {
            @Override
            protected ConcurrentMap<String, List<Locale>> cLanguagesByCountry() {
                return concurrentHashMap;
            }

            @Override
            protected ConcurrentMap<String, List<Locale>> cCountriesByLanguage() {
                return concurrentHashMap;
            }
        };
        List<Locale> result = localeUtils.languagesByCountry("US");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"));
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_EmptyAvailableLocales() {
        ConcurrentHashMap<String, List<Locale>> concurrentHashMap = new ConcurrentHashMap<>();
        LocaleUtils localeUtils = new LocaleUtils() {
            @Override
            protected ConcurrentMap<String, List<Locale>> cLanguagesByCountry() {
                return concurrentHashMap;
            }

            @Override
            protected ConcurrentMap<String, List<Locale>> cCountriesByLanguage() {
                return concurrentHashMap;
            }
        };
        List<Locale> result = localeUtils.countriesByLanguage("fr");
        assertTrue(result.isEmpty());
    }

    private void throwExceptionAndAssertMessage(String message, Runnable runnable) {
        try {
            runnable.run();
            fail("Expected exception: " + message);
        } catch (IllegalArgumentException e) {
            assertEquals(message, e.getMessage());
        }
    }
}