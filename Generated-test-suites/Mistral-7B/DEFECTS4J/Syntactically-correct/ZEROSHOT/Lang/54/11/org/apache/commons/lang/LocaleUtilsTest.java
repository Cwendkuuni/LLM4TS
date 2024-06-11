package org.apache.commons.lang;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale(null);
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en"));
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    /**
     * Test localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedLocaleList = Collections.singletonList(defaultLocale);
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedLocaleSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expectedLocaleSet, actualLocaleSet);
    }

    /**
     * Test isAvailableLocale method with available locale.
     */
    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale method with invalid locale.
     */
    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        Locale locale = new Locale("en", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("en", "US"),
                new Locale("fr", "FR")
        );
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        List<Locale> expectedLocaleList = Collections.emptyList();
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(null);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("CA", "en"),
                new Locale("FR", "fr"),
                new Locale("US", "en")
        );
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage("en");
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        List<Locale> expectedLocaleList = Collections.emptyList();
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(null);
        assertEquals(expectedLocaleList, actualLocaleList);
    }

}