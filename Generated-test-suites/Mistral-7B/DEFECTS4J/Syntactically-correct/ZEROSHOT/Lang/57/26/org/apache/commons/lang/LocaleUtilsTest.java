package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        Assert.assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        Assert.assertEquals(expected, actual);

        expected = new Locale("es", "ES", "Castilian");
        actual = LocaleUtils.toLocale("es_ES_Castilian");
        Assert.assertEquals(expected, actual);

        // Test invalid locale strings
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("xx");
            Assert.fail("Expected IllegalArgumentException for invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_XX");
            Assert.fail("Expected IllegalArgumentException for invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX");
            Assert.fail("Expected IllegalArgumentException for invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_gb_XXX");
            Assert.fail("Expected IllegalArgumentException for invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_XXX.xxx");
            Assert.fail("Expected IllegalArgumentException for invalid locale format");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List actual = LocaleUtils.localeLookupList(locale);
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());

        locale = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List expected = LocaleUtils.availableLocaleList();
        List actual = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set expected = LocaleUtils.availableLocaleSet();
        Set actual = Collections.newHashSet(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "xxx");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "ES", "Castilian");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("xx", "XX");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List expected = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode));
        List actual = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expected, actual);

        countryCode = null;
        actual = Collections.emptyList();
        Assert.assertEquals(actual, LocaleUtils.languagesByCountry(countryCode));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List expected = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "FR"));
        List actual = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expected, actual);

        languageCode = null;
        actual = Collections.emptyList();
        Assert.assertEquals(actual, LocaleUtils.countriesByLanguage(languageCode));
    }
}