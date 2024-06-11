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
        Assert.assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        Assert.assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        Assert.assertEquals(new Locale("it", "IT"), LocaleUtils.toLocale("it_IT"));

        // Test empty locale string
        Assert.assertNull(LocaleUtils.toLocale(""));

        // Test invalid locale strings
        Assert.assertNull(LocaleUtils.toLocale("xx_YY")); // wrong length
        Assert.assertNull(LocaleUtils.toLocale("en_XX")); // wrong country code
        Assert.assertNull(LocaleUtils.toLocale("en_GB_xx")); // invalid variant
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> emptyList = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(Collections.emptyList(), emptyList);

        // Test single locale
        List<Locale> singleLocaleList = LocaleUtils.localeLookupList(new Locale("en", "US"));
        Assert.assertEquals(Arrays.asList(new Locale("en", "US")), singleLocaleList);

        // Test multiple locales with varying specificity
        List<Locale> multipleLocalesList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        Assert.assertEquals(Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr")), multipleLocalesList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> emptyList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(Collections.emptyList(), emptyList);

        // Test non-empty list
        List<Locale> nonEmptyList = LocaleUtils.availableLocaleList();
        Assert.assertFalse(nonEmptyList.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        Assert.assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Test invalid locale
        Assert.assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        List<Locale> emptyList = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(Collections.emptyList(), emptyList);

        // Test valid country code
        List<Locale> validList = LocaleUtils.languagesByCountry("US");
        Assert.assertFalse(validList.isEmpty());
        Assert.assertTrue(validList.contains(new Locale("en", "US")));
        Assert.assertTrue(validList.contains(new Locale("es", "US")));

        // Test invalid country code
        List<Locale> invalidList = LocaleUtils.languagesByCountry("XX");
        Assert.assertEquals(Collections.emptyList(), invalidList);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        List<Locale> emptyList = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(Collections.emptyList(), emptyList);

        // Test valid language code
        List<Locale> validList = LocaleUtils.countriesByLanguage("en");
        Assert.assertFalse(validList.isEmpty());
        Assert.assertTrue(validList.contains(new Locale("US", "en")));
        Assert.assertTrue(validList.contains(new Locale("CA", "en")));

        // Test invalid language code
        List<Locale> invalidList = LocaleUtils.countriesByLanguage("XX");
        Assert.assertEquals(Collections.emptyList(), invalidList);
    }
}