package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale locale1 = LocaleUtils.toLocale("en_US");
        assertEquals("en_US", locale1.toString());
        Locale locale2 = LocaleUtils.toLocale("fr_FR");
        assertEquals("fr_FR", locale2.toString());
        Locale locale3 = LocaleUtils.toLocale("es_ES_ES");
        assertEquals("es_ES_ES", locale3.toString());

        // Test invalid inputs
        try {
            Locale locale4 = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            Locale locale5 = LocaleUtils.toLocale("en_gb_xyz");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            Locale locale6 = LocaleUtils.toLocale("en_gb_xyz_123");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            Locale locale7 = LocaleUtils.toLocale("en_gb_xyz_123.456");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            Locale locale8 = LocaleUtils.toLocale("en_gb_xyz.123");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            Locale locale9 = LocaleUtils.toLocale("en_gb_xyz_123.456_XYZ");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test empty inputs
        Locale locale10 = LocaleUtils.toLocale("");
        assertTrue(locale10 == null);
        Locale locale11 = LocaleUtils.toLocale(" ");
        assertTrue(locale11 == null);
        Locale locale12 = LocaleUtils.toLocale("en");
        assertEquals("en", locale12.getLanguage());
        assertTrue(locale12.getCountry() == null);
        Locale locale13 = LocaleUtils.toLocale("en_");
        assertEquals("en", locale13.getLanguage());
        assertTrue(locale13.getCountry() == null);
        Locale locale14 = LocaleUtils.toLocale("en_US_");
        assertEquals("en_US", locale14.toString());
        Locale locale15 = LocaleUtils.toLocale("en_US_");
        assertEquals("en_US", locale15.toString());
        Locale locale16 = LocaleUtils.toLocale("en_US_");
        assertEquals("en_US", locale16.toString());
    }

    @Test
    public void testLocaleLookupList() {
        // Test with valid inputs
        List<Locale> lookupList1 = LocaleUtils.localeLookupList(Locale.US);
        List<Locale> expectedList1 = Arrays.asList(Locale.US, Locale.ENGLISH, Locale.EN);
        assertEquals(expectedList1, lookupList1);

        List<Locale> lookupList2 = LocaleUtils.localeLookupList(Locale.FRANCE, Locale.US);
        List<Locale> expectedList2 = Arrays.asList(Locale.FRANCE, Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.US);
        assertEquals(expectedList2, lookupList2);

        // Test with null inputs
        List<Locale> lookupList3 = LocaleUtils.localeLookupList(null);
        assertTrue(lookupList3.isEmpty());
        List<Locale> lookupList4 = LocaleUtils.localeLookupList(Locale.US, null);
        assertTrue(lookupList4.isEmpty());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales1 = LocaleUtils.availableLocaleList();
        List<Locale> expectedAvailableLocales1 = Arrays.asList(Locale.CHINESE, Locale.CHINA, Locale.ENGLISH, Locale.EN, Locale.FRENCH, Locale.FRANCE, Locale.GERMAN, Locale.GERMANY, Locale.ITALIAN, Locale.ITALY, // Add other available locales here
                Locale.JAPANESE, Locale.JAPAN, Locale.KOREAN, Locale.KOREA, // Add other available locales here
                Locale.RUSSIAN, Locale.RUSSIA); // Add other available locales here
        assertEquals(expectedAvailableLocales1, availableLocales1);

        List<Locale> availableLocales2 = LocaleUtils.availableLocaleList();
        assertEquals(availableLocales1, availableLocales2);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocalesSet1 = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedAvailableLocalesSet1 = new HashSet<>(LocaleUtils.availableLocaleList());
        assertEquals(expectedAvailableLocalesSet1, availableLocalesSet1);

        Set<Locale> availableLocalesSet2 = LocaleUtils.availableLocaleSet();
        assertEquals(availableLocalesSet1, availableLocalesSet2);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test with available locales
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CHINESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CHINA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.EN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALY));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.JAPAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.KOREAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.KOREA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.RUSSIAN));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.RUSSIA));

        // Test with invalid inputs
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_US));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH_US_VARIANT));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test with valid inputs
        List<Locale> languagesByCountry1 = LocaleUtils.languagesByCountry("US");
        List<Locale> expectedLanguagesByCountry1 = Arrays.asList(Locale.ENGLISH, Locale.EN, Locale.ENGLISH_US, Locale.EN_US, Locale.ENGLISH_US_MISSISSIPPI, Locale.EN_US_MISSISSIPPI, Locale.ENGLISH_US_GEORGIA, Locale.EN_US_GEORGIA); // Add other expected locales for US here
        assertEquals(expectedLanguagesByCountry1, languagesByCountry1);

        List<Locale> languagesByCountry2 = LocaleUtils.languagesByCountry("FR");
        List<Locale> expectedLanguagesByCountry2 = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, // Add other expected locales for FR here
                Locale.FRENCH_BE, Locale.FRENCH_BE_BELGIUM, Locale.FRENCH_BE_BELGIUM_WALLONIA, Locale.FRENCH_BE_BELGIUM_BRUSSELS_CAPITAL, // Add other expected locales for FR (BE) here
                Locale.FRENCH_CH, Locale.FRENCH_CH_SWITZERLAND); // Add other expected locales for FR (CH) here
        assertEquals(expectedLanguagesByCountry2, languagesByCountry2);

        // Test with invalid inputs
        List<Locale> languagesByCountry3 = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountry3.isEmpty());
        List<Locale> languagesByCountry4 = LocaleUtils.languagesByCountry("XX");
        assertTrue(languagesByCountry4.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        // Test with valid inputs
        List<Locale> countriesByLanguage1 = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguage1 = Arrays.asList(Locale.ENGLISH, Locale.EN, Locale.ENGLISH_US, Locale.EN_US, Locale.ENGLISH_US_MISSISSIPPI, Locale.EN_US_MISSISSIPPI, Locale.ENGLISH_US_GEORGIA, Locale.EN_US_GEORGIA, // Add other expected locales for EN here
                Locale.ENGLISH_AUSTRALIA, Locale.EN_AUSTRALIA, // Add other expected locales for EN (AU) here
                Locale.ENGLISH_CANADA, Locale.EN_CA, // Add other expected locales for EN (CA) here
                Locale.ENGLISH_IE, Locale.EN_IE, // Add other expected locales for EN (IE) here
                Locale.ENGLISH_NZ, Locale.EN_NZ, // Add other expected locales for EN (NZ) here
                Locale.ENGLISH_ZA, Locale.EN_ZA); // Add other expected locales for EN (ZA) here
        assertEquals(expectedCountriesByLanguage1, countriesByLanguage1);

        List<Locale> countriesByLanguage2 = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expectedCountriesByLanguage2 = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, Locale.FRENCH, // Add other expected locales for FR here
                Locale.FRENCH_BE, Locale.FRENCH_BE_BELGIUM, Locale.FRENCH_BE_BELGIUM_WALLONIA, Locale.FRENCH_BE_BELGIUM_BRUSSELS_CAPITAL, // Add other expected locales for FR (BE) here
                Locale.FRENCH_CH, Locale.FRENCH_CH_SWITZERLAND); // Add other expected locales for FR (CH) here
        assertEquals(expectedCountriesByLanguage2, countriesByLanguage2);

        // Test with invalid inputs
        List<Locale> countriesByLanguage3 = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguage3.isEmpty());
        List<Locale> countriesByLanguage4 = LocaleUtils.countriesByLanguage("XX");
        assertTrue(countriesByLanguage4.isEmpty());
    }
}