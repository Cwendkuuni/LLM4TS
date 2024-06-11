package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("es", "");
        actual = LocaleUtils.toLocale("es");
        assertEquals(expected, actual);

        // Test invalid input
        try {
            Locale actual = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_GB_xxX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_GB_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_GB_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_GB_xxx_xxx_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_GB_xxx_xxx_xxx_xx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_GB_xxx_xxx_xxx_xx_yy");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        List<Locale> expectedEmpty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.localeLookupList(null);
        assertEquals(expectedEmpty, actualEmpty);

        List<Locale> expectedOne = Arrays.asList(Locale.ENGLISH);
        List<Locale> actualOne = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(expectedOne, actualOne);

        List<Locale> expectedTwo = Arrays.asList(Locale.US, Locale.ENGLISH);
        List<Locale> actualTwo = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(expectedTwo, actualTwo);

        List<Locale> expectedThree = Arrays.asList(Locale.ENGLISH, Locale.US, Locale.ENGLISH);
        List<Locale> actualThree = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.US);
        assertEquals(expectedThree, actualThree);

        List<Locale> expectedFour = Arrays.asList(Locale.ENGLISH, Locale.US, Locale.ENGLISH, Locale.ENGLISH);
        List<Locale> actualFour = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.US, Locale.ENGLISH);
        assertEquals(expectedFour, actualFour);

        // Test invalid input
        try {
            LocaleUtils.localeLookupList(null, Locale.ENGLISH);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.localeLookupList(Locale.ENGLISH, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty input
        List<Locale> expectedEmpty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.languagesByCountry(null);
        assertEquals(expectedEmpty, actualEmpty);

        List<Locale> expectedOne = Arrays.asList(Locale.FRENCH, Locale.CANADA_FRENCH);
        List<Locale> actualOne = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedOne, actualOne);

        List<Locale> expectedTwo = Arrays.asList(Locale.FRENCH, Locale.FRENCH, Locale.FRENCH);
        List<Locale> actualTwo = LocaleUtils.languagesByCountry("CA", Locale.FRENCH);
        assertEquals(expectedTwo, actualTwo);

        // Test invalid input
        try {
            LocaleUtils.languagesByCountry(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty input
        List<Locale> expectedEmpty = Collections.emptyList();
        List<Locale> actualEmpty = LocaleUtils.countriesByLanguage(null);
        assertEquals(expectedEmpty, actualEmpty);

        List<Locale> expectedOne = Arrays.asList(Locale.CANADA, Locale.FRANCE);
        List<Locale> actualOne = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expectedOne, actualOne);

        List<Locale> expectedTwo = Arrays.asList(Locale.FRANCE, Locale.FRANCE, Locale.FRANCE);
        List<Locale> actualTwo = LocaleUtils.countriesByLanguage("fr", Locale.FRENCH);
        assertEquals(expectedTwo, actualTwo);

        // Test invalid input
        try {
            LocaleUtils.countriesByLanguage(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid input
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRENCH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA_FRENCH));

        // Test invalid input
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid", "invalid")));
    }

}