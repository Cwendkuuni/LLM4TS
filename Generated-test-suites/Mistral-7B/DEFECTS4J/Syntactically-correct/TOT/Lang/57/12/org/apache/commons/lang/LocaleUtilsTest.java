package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private String input;
    private Locale expected;

    public LocaleUtilsTest(String input, Locale expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: toLocale(\"{0}\") = {1}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"en", new Locale("en", "")},
                {"en_GB", new Locale("en", "GB")},
                {"en_GB_London", new Locale("en", "GB", "London")},
                {"fr", new Locale("fr", "")},
                {"fr_CA", new Locale("fr", "CA")},
                {"fr_CA_Vancouver", new Locale("fr", "CA", "Vancouver")},
                {"es", new Locale("es", "")},
                {"es_ES", new Locale("es", "ES")},
                {"es_ES_Madrid", new Locale("es", "ES", "Madrid")},
                {"ja", new Locale("ja", "")},
                {"ja_JP", new Locale("ja", "JP")},
                {"ja_JP_Tokyo", new Locale("ja", "JP", "Tokyo")},
                {"de", new Locale("de", "")},
                {"de_DE", new Locale("de", "DE")},
                {"de_DE_Munchen", new Locale("de", "DE", "Munchen")},
                {"ar", new Locale("ar", "")},
                {"ar_SA", new Locale("ar", "SA")},
                {"ar_SA_Riyadh", new Locale("ar", "SA", "Riyadh")},
                {"ko", new Locale("ko", "")},
                {"ko_KR", new Locale("ko", "KR")},
                {"ko_KR_Seoul", new Locale("ko", "KR", "Seoul")},
                {null, null}
        });
    }

    @Test
    public void testToLocale() {
        Locale actual = LocaleUtils.toLocale(input);
        assertEquals("Expected locale to be equal to the expected one", expected, actual);
    }

    // Test for edge cases and error scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleFormat() {
        LocaleUtils.toLocale("xy");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleFormat2() {
        LocaleUtils.toLocale("en_gb_xy");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleFormat3() {
        LocaleUtils.toLocale("en_gb_XY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleFormat4() {
        LocaleUtils.toLocale("en_gb_-_XY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLocaleFormat5() {
        LocaleUtils.toLocale("en_gb_xy_XY");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "Vancouver"));
        assertFalse("Expected the list to contain fewer locales than specified", localeList.size() > 4);
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA", "Vancouver"), new Locale("fr", "CA"), new Locale("fr", ""), new Locale(""));
        assertTrue("Expected the lists to be equal", localeList.equals(expectedLocaleList));
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "Vancouver"), new Locale("en"));
        assertFalse("Expected the list to contain more locales than specified", localeList.size() <= 4);
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA", "Vancouver"), new Locale("fr", "CA"), new Locale("fr", ""), new Locale("en"));
        assertTrue("Expected the lists to be equal", localeList.equals(expectedLocaleList));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertFalse("Expected the list to contain fewer locales than available", availableLocaleList.size() < LocaleUtils.availableLocaleSet().size());
        List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.ARABIC, Locale.SPANISH);
        assertTrue("Expected the lists to be equal", availableLocaleList.subList(0, 10).equals(expectedAvailableLocaleList.subList(0, 10)));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertTrue("Expected the sets to be equal", availableLocaleSet.containsAll(LocaleUtils.availableLocaleList()));
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> locales = Collections.singletonList(new Locale("fr", "CA"));
        List<Locale> unavailableLocales = Arrays.asList(new Locale("fr", ""), new Locale("de", ""), new Locale("es", ""));
        List<Locale> availableLocales = Arrays.asList(new Locale("fr", "CA"), new Locale("ja", "JP"));

        for (Locale locale : locales) {
            assertTrue("Expected the locale to be available", LocaleUtils.isAvailableLocale(locale));
        }

        for (Locale locale : unavailableLocales) {
            assertFalse("Expected the locale to be unavailable", LocaleUtils.isAvailableLocale(locale));
        }

        for (Locale locale : availableLocales) {
            assertTrue("Expected the locale to be available", LocaleUtils.isAvailableLocale(locale));
        }

        assertNull("Expected the result to be null", LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFrance = LocaleUtils.languagesByCountry("FR");
        List<Locale> expectedLanguagesByCountryFrance = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CA"), new Locale("fr", "CH"), new Locale("fr", "LU"));
        assertTrue("Expected the lists to be equal", languagesByCountryFrance.equals(expectedLanguagesByCountryFrance));
    }

    @Test
    public void testCountriesByLanguageEnglish() {
        List<Locale> countriesByLanguageEnglish = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguageEnglish = Arrays.asList(new Locale("EN", "AG"), new Locale("EN", "BZ"), new Locale("EN", "CA"), new Locale("EN", "GB"), new Locale("EN", "IE"), new Locale("EN", "JM"), new Locale("EN", "MT"), new Locale("EN", "NZ"), new Locale("EN", "SG"), new Locale("EN", "TT"), new Locale("EN", "US"), new Locale("EN", "ZW"));
        assertTrue("Expected the lists to be equal", countriesByLanguageEnglish.equals(expectedCountriesByLanguageEnglish));
    }

}