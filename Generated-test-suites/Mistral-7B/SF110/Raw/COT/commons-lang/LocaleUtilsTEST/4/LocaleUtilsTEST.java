package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_Empty() {
        assertSame(Locale.EMPTY, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_Invalid_Format_NoLanguage() {
        String invalidLocale = "_country";
        throw new IllegalArgumentException("Invalid locale format: " + invalidLocale); // Thrown by the method
        // Test for the exception
    }

    @Test
    public void testToLocale_Invalid_Format_NoCountry() {
        String invalidLocale = "language_";
        throw new IllegalArgumentException("Invalid locale format: " + invalidLocale); // Thrown by the method
        // Test for the exception
    }

    @Test
    public void testToLocale_Invalid_Format_MissingLanguageCode() {
        String invalidLocale = "_country";
        throw new IllegalArgumentException("Invalid locale format: " + invalidLocale); // Thrown by the method
        // Test for the exception
    }

    @Test
    public void testToLocale_Invalid_Format_MissingCountryCode() {
        String invalidLocale = "language_";
        throw new IllegalArgumentException("Invalid locale format: " + invalidLocale); // Thrown by the method
        // Test for the exception
    }

    @Test
    public void testToLocale_Invalid_Format_InvalidCharacters() {
        String invalidLocale = "language#Country";
        throw new IllegalArgumentException("Invalid locale format: " + invalidLocale); // Thrown by the method
        // Test for the exception
    }

    @Test
    public void testToLocale_Valid_Format_1_Level() {
        assertEquals(new Locale("language"), LocaleUtils.toLocale("language"));
    }

    @Test
    public void testToLocale_Valid_Format_2_Level() {
        assertEquals(new Locale("language", "Country"), LocaleUtils.toLocale("language_Country"));
    }

    @Test
    public void testToLocale_Valid_Format_3_Level() {
        assertEquals(new Locale("language", "Country", "variant"), LocaleUtils.toLocale("language_Country_variant"));
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("language", "Country");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = Arrays.asList(locale, new Locale("language"), new Locale("language", ""));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_ValidLocale_WithDefault() {
        Locale defaultLocale = new Locale("defaultLanguage", "defaultCountry");
        List<Locale> result = LocaleUtils.localeLookupList(Locale.US, defaultLocale);
        List<Locale> expected = Arrays.asList(Locale.US, new Locale("", ""), defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = new Locale("language", "Country");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale locale = new Locale("language", "Country");
        locale.setVariant("variant");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("Country");
        List<Locale> expected = Arrays.asList(new Locale("Language1", "Country"), new Locale("Language2", "Country"), new Locale("Language3", "Country"));
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("Language");
        List<Locale> expected = Arrays.asList(new Locale("Language", "Country1"), new Locale("Language", "Country2"), new Locale("Language", "Country3"));
        assertEquals(expected, result);
    }

}