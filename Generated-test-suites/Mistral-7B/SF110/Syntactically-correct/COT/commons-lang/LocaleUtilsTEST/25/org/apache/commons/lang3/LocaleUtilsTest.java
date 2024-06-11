package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyString() {
        assertSame(Locale.ROOT, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        String invalidLocale = "_xx";
        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid locale format: " + invalidLocale);
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        String invalidLocale = "xx_";
        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid locale format: " + invalidLocale);
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguageAndCountry() {
        String invalidLocale = "__";
        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid locale format: " + invalidLocale);
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_LanguageLengthLessThanTwo() {
        String invalidLocale = "_ab";
        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid locale format: " + invalidLocale);
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_LanguageLengthGreaterThanThree() {
        String invalidLocale = "_abcd";
        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid locale format: " + invalidLocale);
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_CountryLengthLessThanTwo() {
        String invalidLocale = "_en_a";
        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid locale format: " + invalidLocale);
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testToLocale_InvalidFormat_CountryLengthGreaterThanTwo() {
        String invalidLocale = "_en_abc";
        IllegalArgumentException expectedException = new IllegalArgumentException("Invalid locale format: " + invalidLocale);
        try {
            LocaleUtils.toLocale(invalidLocale);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testToLocale_ValidFormat_TwoElements() {
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocale_ValidFormat_ThreeElements() {
        Locale expectedLocale = new Locale("en", "US", "POSIX");
        Locale actualLocale = LocaleUtils.toLocale("en_US_POSIX");
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.localeLookupList(null);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_EmptyString() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.localeLookupList("");
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        Locale locale = new Locale("fr_FR", "fr_BE");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(locale);
        expectedList.add(new Locale("fr", "FR"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_LocaleWithEmptyCountry() {
        Locale locale = new Locale("de", "");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(locale);
        expectedList.add(new Locale("de", "DE"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> expectedList = new ArrayList<Locale>();
        expectedList.add(new Locale("en", "US"));
        expectedList.add(defaultLocale);
        List<Locale> actualList = LocaleUtils.localeLookupList(new Locale("fr", "FR"), defaultLocale);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList_Empty() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList_NotEmpty() {
        List<Locale> expectedList = Arrays.asList(Locale.US, Locale.FRENCH, Locale.SIMPLIFIED_CHINESE);
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet_Empty() {
        Set<Locale> expectedSet = Collections.emptySet();
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testAvailableLocaleSet_NotEmpty() {
        Set<Locale> expectedSet = new HashSet<Locale>(Arrays.asList(Locale.US, Locale.FRENCH, Locale.SIMPLIFIED_CHINESE));
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale invalidLocale = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale availableLocale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(availableLocale));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.languagesByCountry(null);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        String countryCode = "";
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountry_CountryWithLocales() {
        String countryCode = "US";
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"));
        List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.countriesByLanguage(null);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        String languageCode = "";
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_LanguageWithLocales() {
        String languageCode = "en";
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, actualList);
    }
}