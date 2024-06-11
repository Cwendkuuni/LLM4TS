package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLocaleFormat_ReturnsLocaleObject() {
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLocaleFormat_ThrowsIllegalArgumentException() {
        String invalidLocale = "en";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LocaleWithEmptyLanguage_ThrowsIllegalArgumentException() {
        String invalidLocale = "_US";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LocaleWithEmptyCountry_ThrowsIllegalArgumentException() {
        String invalidLocale = "en_";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LocaleWithMoreThanThreeCharacters_ThrowsIllegalArgumentException() {
        String invalidLocale = "en_US_ED";
        LocaleUtils.toLocale(invalidLocale);
    }

    @Test
    public void testLocaleLookupList_ValidLocale_ReturnsListWithLocaleAndDerivedLocales() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", ""), new Locale("", "CA"), new Locale("", ""));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_NullLocale_ReturnsEmptyList() {
        List<Locale> actualList = LocaleUtils.localeLookupList(null);
        assertTrue(actualList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_ReturnsListWithAvailableLocales() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testIsAvailableLocale_ValidLocale_ReturnsTrue() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale_ReturnsFalse() {
        Locale invalidLocale = new Locale("en", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode_ReturnsListWithAvailableLocalesForGivenCountry() {
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("es", "CA"));
        List<Locale> actualList = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode_ReturnsEmptyList() {
        List<Locale> actualList = LocaleUtils.languagesByCountry(null);
        assertTrue(actualList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode_ReturnsListWithAvailableCountriesForGivenLanguage() {
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode_ReturnsEmptyList() {
        List<Locale> actualList = LocaleUtils.countriesByLanguage(null);
        assertTrue(actualList.isEmpty());
    }

}