package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.LocaleUtilsTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        Assert.assertEquals(expected, actual);

        expected = new Locale("es", "ES");
        actual = LocaleUtils.toLocale("es_ES");
        Assert.assertEquals(expected, actual);

        // Test invalid input
        try {
            LocaleUtils.toLocale(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_v1.0");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("es_ES_v1.0");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_US_en");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("es_ES_es");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale expected = new Locale("fr", "CA", "xxx");
        List<Locale> actual = LocaleUtils.localeLookupList(expected);
        List<Locale> expectedList = Arrays.asList(expected, new Locale("fr", "CA"), new Locale("fr"));
        Assert.assertEquals(expectedList, actual);

        expected = new Locale("en");
        actual = LocaleUtils.localeLookupList(expected);
        expectedList = Arrays.asList(expected, new Locale("en"));
        Assert.assertEquals(expectedList, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.localeLookupList(expected, new Locale("en"));
        expectedList = Arrays.asList(expected, new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        Assert.assertEquals(expectedList, actual);

        expected = null;
        actual = LocaleUtils.localeLookupList(expected);
        Assert.assertEquals(Collections.emptyList(), actual);

        // Test invalid input
        try {
            LocaleUtils.localeLookupList(null, new Locale("en"));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Test valid input
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);

        // Test empty input
        actual = LocaleUtils.availableLocaleList();
        expected = Collections.emptyList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid input
        Locale locale = new Locale("en", "US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "ES");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Test invalid input
        Locale invalidLocale = new Locale("en", "US", "v1.0");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));

        locale = new Locale("es", "ES", "es_ES");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test valid input
        String countryCode = "US";
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Locale.getAvailableLocales();
        List<Locale> filteredExpected = new ArrayList<>();
        for (Locale locale : expected) {
            if (Locale.US.getCountry().equals(countryCode) && locale.getVariant().length() == 0) {
                filteredExpected.add(locale);
            }
        }
        Assert.assertEquals(filteredExpected, actual);

        countryCode = "CA";
        actual = LocaleUtils.languagesByCountry(countryCode);
        filteredExpected.clear();
        for (Locale locale : expected) {
            if (Locale.CA.getCountry().equals(countryCode) && locale.getVariant().length() == 0) {
                filteredExpected.add(locale);
            }
        }
        Assert.assertEquals(filteredExpected, actual);

        // Test empty input
        actual = LocaleUtils.languagesByCountry(null);
        expected = Collections.emptyList();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test valid input
        String languageCode = "en";
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Locale.getAvailableLocales();
        List<Locale> filteredExpected = new ArrayList<>();
        for (Locale locale : expected) {
            if (locale.getLanguage().equals(languageCode) && locale.getCountry().length() > 0 && locale.getVariant().length() == 0) {
                filteredExpected.add(locale);
            }
        }
        Assert.assertEquals(filteredExpected, actual);

        languageCode = "fr";
        actual = LocaleUtils.countriesByLanguage(languageCode);
        filteredExpected.clear();
        for (Locale locale : expected) {
            if (locale.getLanguage().equals(languageCode) && locale.getCountry().length() > 0 && locale.getVariant().length() == 0) {
                filteredExpected.add(locale);
            }
        }
        Assert.assertEquals(filteredExpected, actual);

        // Test empty input
        actual = LocaleUtils.countriesByLanguage(null);
        expected = Collections.emptyList();
        Assert.assertEquals(expected, actual);
    }
}