package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test cases for {@link LocaleUtils}.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(expectedLocale, actualLocale);
    }

    /**
     * Test toLocale method with empty input.
     */
    @Test
    public void testToLocaleEmptyInput() {
        Assert.assertNull(LocaleUtils.toLocale(""));
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        LocaleUtils.toLocale("Fr_mA");
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en"), new Locale("fr"), new Locale("fr_CA"));
        List<Locale> actualLocales = LocaleUtils.localeLookupList(new Locale("fr_CA"), new Locale("en"));
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> actualLocales = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(Collections.emptyList(), actualLocales);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocales = LocaleUtils.availableLocaleList();
        List<Locale> actualLocales = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedLocales = LocaleUtils.availableLocaleSet();
        Set<Locale> actualLocales = new HashSet<>(Locale.getAvailableLocales());
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test isAvailableLocale method with valid input.
     */
    @Test
    public void testIsAvailableLocaleValidInput() {
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale method with invalid input.
     */
    @Test
    public void testIsAvailableLocaleInvalidInput() {
        Locale locale = new Locale("fr", "CA");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"), new Locale("en", "US"));
        List<Locale> actualLocales = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        List<Locale> actualLocales = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(Collections.emptyList(), actualLocales);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("de", "DE"));
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(Collections.emptyList(), actualLocales);
    }
}