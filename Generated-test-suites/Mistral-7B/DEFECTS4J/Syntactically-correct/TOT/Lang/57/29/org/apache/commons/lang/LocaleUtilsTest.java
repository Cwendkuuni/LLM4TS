package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        Locale expectedLocale1 = new Locale("en", "US");
        Locale actualLocale1 = LocaleUtils.toLocale("en_US");
        assertEquals(expectedLocale1, actualLocale1);

        Locale expectedLocale2 = new Locale("fr", "");
        Locale actualLocale2 = LocaleUtils.toLocale("fr");
        assertEquals(expectedLocale2, actualLocale2);

        Locale expectedLocale3 = new Locale("es_ES", "");
        Locale actualLocale3 = LocaleUtils.toLocale("es_ES");
        assertEquals(expectedLocale3, actualLocale3);

        // Test invalid input
        try {
            Locale actualLocale4 = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidInput1 = "en_GB_XXX";
        try {
            Locale actualLocale5 = LocaleUtils.toLocale(invalidInput1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidInput2 = "en_GB__";
        try {
            Locale actualLocale6 = LocaleUtils.toLocale(invalidInput2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String invalidInput3 = "en_GB_XXX_YYY";
        try {
            Locale actualLocale7 = LocaleUtils.toLocale(invalidInput3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList1 = LocaleUtils.localeLookupList(locale1);
        List<Locale> actualList1 = LocaleUtils.localeLookupList(locale1, null);

        assertEquals(expectedList1.size(), actualList1.size());
        for (int i = 0; i < expectedList1.size(); i++) {
            assertEquals(expectedList1.get(i), actualList1.get(i));
        }

        Locale locale2 = new Locale("en");
        List<Locale> expectedList2 = LocaleUtils.localeLookupList(locale2, locale1);

        assertEquals(expectedList2.size(), 4);
        assertEquals(expectedList2.get(0), locale2);
        assertEquals(expectedList2.get(1), locale1);
        assertEquals(expectedList2.get(2), new Locale("en", "CA"));
        assertEquals(expectedList2.get(3), new Locale("en"));

        // Test null input
        Locale locale3 = null;
        List<Locale> expectedList3 = Collections.emptyList();
        List<Locale> actualList3 = LocaleUtils.localeLookupList(locale3);

        assertEquals(expectedList3, actualList3);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid input
        Locale locale1 = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale2));

        Locale locale3 = new Locale("fr");
        assertTrue(LocaleUtils.isAvailableLocale(locale3));

        // Test invalid input
        Locale locale4 = new Locale("fr", "CA", "yyy");
        assertFalse(LocaleUtils.isAvailableLocale(locale4));

        Locale locale5 = new Locale("fr", "");
        assertFalse(LocaleUtils.isAvailableLocale(locale5));

        Locale locale6 = new Locale("fr", "XX", "zzz");
        assertFalse(LocaleUtils.isAvailableLocale(locale6));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test valid input
        String countryCode1 = "CA";
        List<Locale> expectedList1 = LocaleUtils.languagesByCountry(countryCode1);

        assertEquals(expectedList1.size(), 3);
        assertTrue(expectedList1.contains(new Locale("en", "CA")));
        assertTrue(expectedList1.contains(new Locale("fr", "CA")));
        assertTrue(expectedList1.contains(new Locale("es", "CA")));

        // Test null input
        String countryCode2 = null;
        List<Locale> expectedList2 = Collections.emptyList();
        List<Locale> actualList2 = LocaleUtils.languagesByCountry(countryCode2);

        assertEquals(expectedList2, actualList2);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test valid input
        String languageCode1 = "fr";
        List<Locale> expectedList1 = LocaleUtils.countriesByLanguage(languageCode1);

        assertEquals(expectedList1.size(), 15);
        assertTrue(expectedList1.contains(new Locale("AD", "AD")));
        assertTrue(expectedList1.contains(new Locale("BE", "BE")));
        assertTrue(expectedList1.contains(new Locale("BI", "BI")));
        // ...

        // Test null input
        String languageCode2 = null;
        List<Locale> expectedList3 = Collections.emptyList();
        List<Locale> actualList3 = LocaleUtils.countriesByLanguage(languageCode2);

        assertEquals(expectedList3, actualList3);
    }
}