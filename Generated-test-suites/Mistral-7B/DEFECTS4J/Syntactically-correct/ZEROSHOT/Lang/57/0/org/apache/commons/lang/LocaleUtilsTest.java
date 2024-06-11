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
        // Test valid inputs
        List<String> validInputs = Arrays.asList(
                "en",
                "en_GB",
                "en_GB_xxx"
        );
        for (String input : validInputs) {
            Locale expected = new Locale(input.substring(0, 2), input.length() > 2 ? input.substring(3, 5) : "");
            Locale actual = LocaleUtils.toLocale(input);
            Assert.assertEquals("Unexpected locale returned for input: " + input, expected, actual);
        }

        // Test invalid inputs
        List<String> invalidInputs = Arrays.asList(
                null,
                "",
                " invalid_format",
                "ab_CD__",
                "en_ab",
                "en_AB_cd",
                "en_ab_cd__"
        );
        for (String input : invalidInputs) {
            try {
                LocaleUtils.toLocale(input);
                Assert.fail("Expected IllegalArgumentException for input: " + input);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");

        List<Locale> lookupList = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> expectedLookupList = Arrays.asList(
                baseLocale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                defaultLocale
        );
        Assert.assertEquals("Unexpected locale lookup list for baseLocale: " + baseLocale, expectedLookupList, lookupList);

        lookupList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        expectedLookupList = Arrays.asList(
                baseLocale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        Assert.assertEquals("Unexpected locale lookup list for baseLocale and defaultLocale: " + baseLocale + ", " + defaultLocale, expectedLookupList, lookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocales = LocaleUtils.availableLocaleSet();
        Assert.assertEquals("Unexpected list size for available locales", expectedAvailableLocales.size(), availableLocales.size());
        for (int i = 0; i < availableLocales.size(); i++) {
            Assert.assertEquals("Unexpected locale at index " + i, expectedAvailableLocales.get(i), availableLocales.get(i));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        List<Locale> testLocales = Arrays.asList(
                new Locale("en", "US"),
                new Locale("fr", "FR"),
                new Locale("es", "ES"),
                new Locale("es", "MX"),
                new Locale("es", "AR"),
                new Locale("es", "CL"),
                new Locale("es", "CO"),
                new Locale("es"),
                null
        );

        for (Locale locale : testLocales) {
            boolean expected = Collections.contains(availableLocales, locale);
            boolean actual = LocaleUtils.isAvailableLocale(locale);
            Assert.assertEquals("Unexpected result for locale: " + locale, expected, actual);
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLanguagesByFrench = Arrays.asList(
                new Locale("fr", "BE"),
                new Locale("fr", "CA"),
                new Locale("fr", "CH"),
                new Locale("fr", "FR"),
                new Locale("fr", "LU")
        );
        List<Locale> actualLanguagesByFrench = LocaleUtils.languagesByCountry("FR");
        Assert.assertEquals("Unexpected languages by country for country code: FR", expectedLanguagesByFrench, actualLanguagesByFrench);

        List<Locale> expectedLanguagesByEnglish = Collections.emptyList();
        List<Locale> actualLanguagesByEnglish = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals("Unexpected languages by country for null country code", expectedLanguagesByEnglish, actualLanguagesByEnglish);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedCountriesByEnglish = Arrays.asList(
                new Locale("CA", "en"),
                new Locale("IE", "en"),
                new Locale("US", "en")
        );
        List<Locale> actualCountriesByEnglish = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals("Unexpected countries by language for language code: en", expectedCountriesByEnglish, actualCountriesByEnglish);

        List<Locale> expectedCountriesByUnknown = Collections.emptyList();
        List<Locale> actualCountriesByUnknown = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals("Unexpected countries by language for null language code", expectedCountriesByUnknown, actualCountriesByUnknown);
    }
}