package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
        // Test empty string
        Locale locale = LocaleUtils.toLocale("");
        assertEquals(Locale.getDefault(), locale);

        // Test lowercase language and empty country
        locale = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale);

        // Test lowercase language and country
        locale = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale);

        // Test lowercase language, country and variant
        locale = LocaleUtils.toLocale("en_GB_XXX");
        assertEquals(new Locale("en", "GB", "XXX"), locale);
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        // Test null input
        LocaleUtils.toLocale(null);

        // Test invalid format - empty language
        LocaleUtils.toLocale(" ");

        // Test invalid format - empty country
        LocaleUtils.toLocale("en_");

        // Test invalid format - invalid separator
        LocaleUtils.toLocale("en_GB_");

        // Test invalid format - invalid length
        LocaleUtils.toLocale("en_GB_XXX_XXX");

        // Test invalid format - invalid country code
        LocaleUtils.toLocale("en_ab");

        // Test invalid format - invalid language code
        LocaleUtils.toLocale("ab_GB");
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Collection<Locale> localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> localeListAsList = new ArrayList<>(localeList);

        // Test empty list when locale is null
        locale = null;
        localeList = LocaleUtils.localeLookupList(locale);
        assertTrue(localeList.isEmpty());

        // Test list with only the specified locale
        locale = new Locale("en");
        localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> localeListAsList2 = new ArrayList<>(localeList);
        assertEquals(1, localeListAsList2.size());
        assertEquals(locale, localeListAsList2.get(0));

        // Test list with the specified locale and its parent locales
        locale = new Locale("fr", "CA", "xxx");
        localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> localeListAsList3 = new ArrayList<>(localeList);
        assertEquals(3, localeListAsList3.size());
        assertEquals(locale, localeListAsList3.get(0));
        assertEquals(new Locale("fr", "CA"), localeListAsList3.get(1));
        assertEquals(new Locale("fr"), localeListAsList3.get(2));

        // Test list with the specified locale, its parent locales, and the default locale
        locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.getDefault();
        localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> localeListAsList4 = new ArrayList<>(localeList);
        assertEquals(4, localeListAsList4.size());
        assertEquals(locale, localeListAsList4.get(0));
        assertEquals(new Locale("fr", "CA"), localeListAsList4.get(1));
        assertEquals(new Locale("fr"), localeListAsList4.get(2));
        assertEquals(defaultLocale, localeListAsList4.get(3));
    }

    /**
     * Test localeLookupList method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupListInvalidInput() {
        // Test null locale input
        Locale locale = null;
        LocaleUtils.localeLookupList(locale);

        // Test null defaultLocale input
        locale = new Locale("en");
        Locale defaultLocale = null;
        LocaleUtils.localeLookupList(locale, defaultLocale);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        Collection<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> availableLocalesAsList = new ArrayList<>(availableLocales);
        assertEquals(Locale.getAvailableLocales().length, availableLocalesAsList.size());
        for (Locale locale : availableLocalesAsList) {
            assertTrue(Locale.getAvailableLocales()[0].equals(locale) ||
                    Arrays.binarySearch(Locale.getAvailableLocales(), locale) >= 0);
        }
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Collection<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assertEquals(Locale.getAvailableLocales().length, availableLocales.size());
        assertTrue(availableLocales.containsAll(Arrays.asList(Locale.getAvailableLocales())));
    }

    /**
     * Test isAvailableLocale method with valid input.
     */
    @Test
    public void testIsAvailableLocaleValidInput() {
        // Test available locale
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test unavailable locale
        locale = new Locale("ab", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIsAvailableLocaleInvalidInput() {
        // Test null locale input
        Locale locale = null;
        LocaleUtils.isAvailableLocale(locale);
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        Collection<Locale> languages = LocaleUtils.languagesByCountry("CA");
        List<Locale> languagesAsList = new ArrayList<>(languages);
        assertEquals(2, languagesAsList.size());
        assertTrue(languagesAsList.contains(new Locale("en", "CA")));
        assertTrue(languagesAsList.contains(new Locale("fr", "CA")));
    }

    /**
     * Test languagesByCountry method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountryInvalidInput() {
        // Test null country code input
        String countryCode = null;
        LocaleUtils.languagesByCountry(countryCode);
    }
}