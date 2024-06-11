package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNotNull(LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getCountry());
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoLanguage() {
        LocaleUtils.toLocale("US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoCountry() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidLocaleFormat() {
        LocaleUtils.toLocale("en_US#");
    }

    @Test
    public void testToLocale_ValidFormat_TwoLetterLanguageCode() {
        assertEquals("fr", LocaleUtils.toLocale("fr").getLanguage());
    }

    @Test
    public void testToLocale_ValidFormat_ThreeLetterLanguageCode() {
        assertEquals("fr_FR", LocaleUtils.toLocale("fr_FR").toString());
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountry() {
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountryVariant() {
        assertEquals("en_US_POSIX", LocaleUtils.toLocale("en_US_POSIX").toString());
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale(""));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_SingleInput() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("en_US"));
        List<Locale> expected = Arrays.asList(new Locale("en_US"), new Locale("en", "US"), new Locale("en", ""), new Locale("", ""));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null, new Locale("en_US"));
        List<Locale> expected = Arrays.asList(new Locale("en_US"), new Locale("en", "US"), new Locale("en", ""), new Locale(""), new Locale("en_US"));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_EmptyAvailableLocales() {
        List<Locale> result = LocaleUtils.localeLookupList(null, new Locale(""));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_MultipleInput() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("fr_CA"), new Locale("fr_FR"));
        List<Locale> expected = Arrays.asList(new Locale("fr_CA"), new Locale("fr_CA"), new Locale("fr", "CA"), new Locale("fr", ""), new Locale("fr_FR"), new Locale("fr", ""), new Locale("", "fr"), new Locale("", ""));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_MissingDefaultLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("es_ES"), null);
        List<Locale> expected = Arrays.asList(new Locale("es_ES"), new Locale("es", "ES"), new Locale("es", ""), new Locale("es", "ES"), new Locale("es", ""));
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList_EmptyList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = LocaleUtils.AVAILABLE_LOCALE_LIST;
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet_EmptySet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = LocaleUtils.AVAILABLE_LOCALE_SET;
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_Invalid")));
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en_US")));
    }

    @Test
    public void testLanguagesByCountry_NullInput() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_EmptyAvailableLocales() {
        List<Locale> result = LocaleUtils.languagesByCountry("ZZ");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("es"), new Locale("fr"));
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_NullInput() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_EmptyAvailableLocales() {
        List<Locale> result = LocaleUtils.countriesByLanguage("xx");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("AU"), new Locale("ZA"), new Locale("IE"), new Locale("JM"), new Locale("NZ"), new Locale("SG"), new Locale("TT"), new Locale("TRINIDAD_AND_TOBAGO"), new Locale("ZM"));
        assertEquals(expected, result);
    }
}