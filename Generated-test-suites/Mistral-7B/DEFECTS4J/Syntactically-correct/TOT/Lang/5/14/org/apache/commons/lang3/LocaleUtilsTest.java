package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        List<String> validInputs = Arrays.asList("en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA");
        for (String input : validInputs) {
            Locale expectedLocale = new Locale(input.substring(0, 2), input.substring(3));
            Locale actualLocale = LocaleUtils.toLocale(input);
            Assert.assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            Assert.assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            Assert.assertEquals(expectedLocale.getDisplayCountry(), actualLocale.getDisplayCountry());
            Assert.assertEquals(expectedLocale.getDisplayLanguage(), actualLocale.getDisplayLanguage());
            Assert.assertEquals(expectedLocale.getDisplayVariant(), actualLocale.getDisplayVariant());
        }

        // Test invalid input
        List<String> invalidInputs = Arrays.asList("", "en_GB_", "en_GB__", "en_GB_xyz", "en_xyz", "xyz");
        for (String input : invalidInputs) {
            try {
                LocaleUtils.toLocale(input);
                Assert.fail("Expected IllegalArgumentException for input: " + input);
            } catch (IllegalArgumentException e) {
                // expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        Locale defaultLocale = Locale.getDefault();
        List<Locale> expectedList = Arrays.asList(Locale.ENGLISH, defaultLocale);
        List<Locale> actualList = LocaleUtils.localeLookupList(Locale.ENGLISH, defaultLocale);
        Assert.assertEquals(expectedList, actualList);

        // Test non-empty list
        Locale locale = new Locale("fr", "CA", "xxx");
        expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedAvailableLocales = LocaleUtils.availableLocaleList();
        List<Locale> actualAvailableLocales = Locale.getAvailableLocales();
        Assert.assertEquals(expectedAvailableLocales, actualAvailableLocales);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        Locale unavailableLocale = new Locale("xyz", "xyz");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("en", "GB"), new Locale("en", "US"));
        List<Locale> actualLocales = LocaleUtils.languagesByCountry("GB");
        Assert.assertEquals(expectedLocales, actualLocales);

        expectedLocales = Arrays.asList(new Locale("fr"), new Locale("fr", "CA"), new Locale("fr", "BE"));
        actualLocales = LocaleUtils.languagesByCountry("fr");
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("GB"), new Locale("US"), new Locale("CA"));
        List<Locale> actualLocales = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expectedLocales, actualLocales);

        expectedLocales = Arrays.asList(new Locale("BE"), new Locale("CH"), new Locale("FR"));
        actualLocales = LocaleUtils.countriesByLanguage("fr");
        Assert.assertEquals(expectedLocales, actualLocales);
    }
}