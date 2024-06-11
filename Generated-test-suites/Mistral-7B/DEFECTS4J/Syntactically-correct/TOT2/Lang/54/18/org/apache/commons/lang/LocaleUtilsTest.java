package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] validInputs = new String[]{"en", "en_GB", "en_GB_US"};
        for (String input : validInputs) {
            Locale expected = new Locale(input.substring(0, 2), input.substring(3));
            if (input.length() > 5) {
                expected = new Locale(input.substring(0, 2), input.substring(3, 5), input.substring(6));
            }
            Locale actual = LocaleUtils.toLocale(input);
            Assert.assertEquals(expected.toString(), actual.toString());
        }

        // Test invalid input
        String[] invalidInputs = new String[]{"xyz", "en_GB_xyz", "en_xyz"};
        for (String input : invalidInputs) {
            try {
                LocaleUtils.toLocale(input);
                Assert.fail("Expected an IllegalArgumentException for input: " + input);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale en = new Locale("en", "");
        Locale en_US = new Locale("en", "US");
        Locale en_GB = new Locale("en", "GB");
        Locale fr = new Locale("fr", "");
        Locale fr_CA = new Locale("fr", "CA");

        // Test empty input
        List<Locale> expectedEmpty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(expectedEmpty, actualEmpty);

        // Test valid input
        List<Locale> expectedValid = Arrays.asList(en, en_US, en_GB, fr, fr_CA);
        List<Locale> actualValid = LocaleUtils.localeLookupList(en, fr);
        Assert.assertEquals(expectedValid, actualValid);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale en = new Locale("en", "");
        Locale fr = new Locale("fr", "");
        Locale en_US = new Locale("en", "US");
        Locale fr_CA = new Locale("fr", "CA");
        List<Locale> locales = new ArrayList<>(Arrays.asList(en, en_US, fr, fr_CA));

        // Test available locale
        Locale locale = new Locale("en", "");
        boolean expectedAvailable = true;
        boolean actualAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expectedAvailable, actualAvailable);

        // Test unavailable locale
        locale = new Locale("xyz", "");
        expectedAvailable = false;
        actualAvailable = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expectedAvailable, actualAvailable);

        // Test locales in list
        for (Locale testLocale : locales) {
            expectedAvailable = LocaleUtils.isAvailableLocale(testLocale);
            actualAvailable = LocaleUtils.isAvailableLocale(testLocale);
            Assert.assertEquals(expectedAvailable, actualAvailable);
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedFrance = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "FR"));
        List<Locale> actualFrance = LocaleUtils.languagesByCountry("FR");
        Assert.assertEquals(expectedFrance, actualFrance);

        List<Locale> expectedUSA = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "US"));
        List<Locale> actualUSA = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(expectedUSA, actualUSA);

        List<Locale> expectedEmpty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedEnglish = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
        List<Locale> actualEnglish = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expectedEnglish, actualEnglish);

        List<Locale> expectedEmpty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(expectedEmpty, actualEmpty);
    }
}