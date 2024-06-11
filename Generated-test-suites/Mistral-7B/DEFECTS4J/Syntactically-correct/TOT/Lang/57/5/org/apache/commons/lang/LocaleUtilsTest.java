package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Assert.assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        Assert.assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        Assert.assertEquals(new Locale("de", "DE"), LocaleUtils.toLocale("de_DE"));

        // Test invalid locale strings
        Assert.assertNull(LocaleUtils.toLocale(null));
        Assert.assertNull(LocaleUtils.toLocale(""));
        Assert.assertNull(LocaleUtils.toLocale("xx"));
        Assert.assertNull(LocaleUtils.toLocale("en_"));
        Assert.assertNull(LocaleUtils.toLocale("en_XX"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_XX"));

        // Test invalid characters in locale strings
        Assert.assertNull(LocaleUtils.toLocale("en_US#"));
        Assert.assertNull(LocaleUtils.toLocale("en_US%"));
        Assert.assertNull(LocaleUtils.toLocale("en_US@"));
        Assert.assertNull(LocaleUtils.toLocale("en_US!"));

        // Test empty country and language codes
        Assert.assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        Assert.assertEquals(new Locale("", "US"), LocaleUtils.toLocale(""));

        // Test empty separator
        Assert.assertEquals(new Locale("en", "_US"), LocaleUtils.toLocale("en_US"));
        Assert.assertEquals(new Locale("en", "US_"), LocaleUtils.toLocale("en_US"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test with a valid locale
        List lookupList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "XX"));
        Assert.assertEquals(4, lookupList.size());
        Assert.assertEquals(new Locale("fr", "CA", "XX"), lookupList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        Assert.assertEquals(new Locale("fr", ""), lookupList.get(2));
        Assert.assertEquals(Locale.getDefault(), lookupList.get(3));

        // Test with a null locale
        lookupList = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(1, lookupList.size());
        Assert.assertNull(lookupList.get(0));

        // Test with an empty locale
        lookupList = LocaleUtils.localeLookupList(new Locale(""));
        Assert.assertEquals(1, lookupList.size());
        Assert.assertNull(lookupList.get(0));

        // Test with a default locale
        lookupList = LocaleUtils.localeLookupList(Locale.getDefault());
        Assert.assertEquals(1, lookupList.size());
        Assert.assertEquals(Locale.getDefault(), lookupList.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertEquals(117, availableLocales.size());

        List<String> validLocales = new ArrayList<>();
        validLocales.add("en_US");
        validLocales.add("fr_CA");
        validLocales.add("de_DE");

        for (int i = 0; i < availableLocales.size(); i++) {
            String localeString = (String) availableLocales.get(i);
            Locale locale = LocaleUtils.toLocale(localeString);
            if (LocaleUtils.isAvailableLocale(locale)) {
                validLocales.add(localeString);
            }
        }

        Assert.assertEquals(114, validLocales.size()); // subtract the ones that failed validation in testToLocale()
        Assert.assertTrue(validLocales.containsAll(Arrays.asList("en_US", "fr_CA", "de_DE")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> englishLocales = LocaleUtils.languagesByCountry("US");
        Assert.assertEquals(4, englishLocales.size());
        Assert.assertEquals(new Locale("en", "US"), englishLocales.get(0));
        Assert.assertEquals(new Locale("en", "CA"), englishLocales.get(1));
        Assert.assertEquals(new Locale("en", "GB"), englishLocales.get(2));
        Assert.assertEquals(new Locale("en", "IE"), englishLocales.get(3));

        List<Locale> frenchLocales = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(1, frenchLocales.size());
        Assert.assertEquals(new Locale("fr", "CA"), frenchLocales.get(0));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> usLocales = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(5, usLocales.size());
        Assert.assertEquals(new Locale("en", "US"), usLocales.get(0));
        Assert.assertEquals(new Locale("en", "CA"), usLocales.get(1));
        Assert.assertEquals(new Locale("en", "GB"), usLocales.get(2));
        Assert.assertEquals(new Locale("en", "IE"), usLocales.get(3));
        Assert.assertEquals(new Locale("en", "JM"), usLocales.get(4));

        List<Locale> germanLocales = LocaleUtils.countriesByLanguage("de");
        Assert.assertEquals(3, germanLocales.size());
        Assert.assertEquals(new Locale("de", "DE"), germanLocales.get(0));
        Assert.assertEquals(new Locale("de", "AT"), germanLocales.get(1));
        Assert.assertEquals(new Locale("de", "CH"), germanLocales.get(2));
    }
}