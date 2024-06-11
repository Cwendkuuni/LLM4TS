package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.test.util.AvailableLocales;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_NullString() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        String invalidLocale = "en";
        assertFalse("Expected IllegalArgumentException", invalidLocale.equals(LocaleUtils.toLocale(invalidLocale).toLanguageTag()));
        assertEquals("Expected IllegalArgumentException", "Invalid locale format: " + invalidLocale, LocaleUtils.getCause(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale)).getMessage());
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        String invalidLocale = "_ZA";
        assertFalse("Expected IllegalArgumentException", invalidLocale.equals(LocaleUtils.toLocale(invalidLocale).toLanguageTag()));
        assertEquals("Expected IllegalArgumentException", "Invalid locale format: " + invalidLocale, LocaleUtils.getCause(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale)).getMessage());
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageLength() {
        String invalidLocale = "fr_FR__";
        assertFalse("Expected IllegalArgumentException", invalidLocale.equals(LocaleUtils.toLocale(invalidLocale).toLanguageTag()));
        assertEquals("Expected IllegalArgumentException", "Invalid locale format: " + invalidLocale, LocaleUtils.getCause(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale)).getMessage());
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountryLength() {
        String invalidLocale = "fr_FR_Z";
        assertFalse("Expected IllegalArgumentException", invalidLocale.equals(LocaleUtils.toLocale(invalidLocale).toLanguageTag()));
        assertEquals("Expected IllegalArgumentException", "Invalid locale format: " + invalidLocale, LocaleUtils.getCause(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale)).getMessage());
    }

    @Test
    public void testToLocale_ValidFormat_NoLanguage() {
        String validLocale = "_ZA";
        Locale expectedLocale = new Locale("", "ZA");
        assertEquals(expectedLocale.getDisplayCountry(), LocaleUtils.toLocale(validLocale).getDisplayCountry());
        assertEquals(expectedLocale.getDisplayLanguage(), LocaleUtils.toLocale(validLocale).getDisplayLanguage());
        assertEquals(expectedLocale.toLanguageTag(), LocaleUtils.toLocale(validLocale).toLanguageTag());
    }

    @Test
    public void testToLocale_ValidFormat_NoCountry() {
        String validLocale = "en";
        Locale expectedLocale = new Locale("en");
        assertEquals(expectedLocale.getDisplayCountry(), LocaleUtils.toLocale(validLocale).getDisplayCountry());
        assertEquals(expectedLocale.getDisplayLanguage(), LocaleUtils.toLocale(validLocale).getDisplayLanguage());
        assertEquals(expectedLocale.toLanguageTag(), LocaleUtils.toLocale(validLocale).toLanguageTag());
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountry() {
        String validLocale = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        assertEquals(expectedLocale.getDisplayCountry(), LocaleUtils.toLocale(validLocale).getDisplayCountry());
        assertEquals(expectedLocale.getDisplayLanguage(), LocaleUtils.toLocale(validLocale).getDisplayLanguage());
        assertEquals(expectedLocale.toLanguageTag(), LocaleUtils.toLocale(validLocale).toLanguageTag());
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountryAndVariant() {
        String validLocale = "es_ES_ES";
        Locale expectedLocale = new Locale("es", "ES", "ES");
        assertEquals(expectedLocale.getDisplayCountry(), LocaleUtils.toLocale(validLocale).getDisplayCountry());
        assertEquals(expectedLocale.getDisplayLanguage(), LocaleUtils.toLocale(validLocale).getDisplayLanguage());
        assertEquals(expectedLocale.getVariant(), LocaleUtils.toLocale(validLocale).getVariant());
        assertEquals(expectedLocale.toLanguageTag(), LocaleUtils.toLocale(validLocale).toLanguageTag());
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyList() {
        Locale locale = new Locale("", "");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "")));
        assertTrue(result.contains(new Locale("", "US")));
    }

    @Test
    public void testLocaleLookupList_ValidLocaleWithVariant() {
        Locale locale = new Locale("es", "ES", "esIN");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("es", "ES")));
        assertTrue(result.contains(new Locale("es", "")));
        assertTrue(result.contains(new Locale("", "US")));
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, null);
        assertEquals(4, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("en", "")));
        assertTrue(result.contains(new Locale("", "US")));
    }

    @Test
    public void testLocaleLookupList_ValidDefaultLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale("fr", "FR"));
        assertEquals(5, result.size());
        assertTrue(result.contains(locale));
        assertTrue(result.contains(new Locale("en", "US")));
        assertTrue(result.contains(new Locale("en", "")));
        assertTrue(result.contains(new Locale("fr", "FR")));
        assertTrue(result.contains(new Locale("", "US")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertFalse(result.isEmpty());
        assertTrue(AvailableLocales.isValidLocaleList(result));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertFalse(result.isEmpty());
        assertTrue(AvailableLocales.isValidLocaleSet(result));
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale invalidLocale = new Locale("", "XXXX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale validLocale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(validLocale));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_EmptyList() {
        String countryCode = "";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        assertFalse(result.isEmpty());
        assertTrue(AvailableLocales.isValidLocaleList(result));
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_EmptyList() {
        String languageCode = "";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        assertFalse(result.isEmpty());
        assertTrue(AvailableLocales.isValidLocaleList(result));
    }
}