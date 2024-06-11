package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method.
     */
    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("es", LocaleUtils.toLocale("es").toString());

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale("en_US_"));
        assertNull(LocaleUtils.toLocale("en_US_XXX"));
        assertNull(LocaleUtils.toLocale("en_US_xxx"));
        assertNull(LocaleUtils.toLocale("en_US_XXXxxx"));
        assertNull(LocaleUtils.toLocale("en_US_xxxXXX"));
    }

    /**
     * Test localeLookupList method.
     */
    @Test
    public void testLocaleLookupList() {
        // Test empty input
        List<Locale> list = LocaleUtils.localeLookupList(null);
        assertTrue(list.isEmpty());

        // Test valid inputs
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> resultList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, resultList);

        // Test default locale
        Locale defaultLocale = new Locale("en");
        resultList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedList, resultList);
        assertEquals(1, resultList.indexOf(defaultLocale));
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertFalse(list.isEmpty());
    }

    /**
     * Test languagesByCountry method.
     */
    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        assertFalse(list.isEmpty());
        assertEquals("en", list.get(0).getLanguage());
        assertEquals("fr", list.get(1).getLanguage());
    }

    /**
     * Test countriesByLanguage method.
     */
    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertFalse(list.isEmpty());
        assertEquals("CA", list.get(0).getCountry());
        assertEquals("GB", list.get(1).getCountry());
    }
}