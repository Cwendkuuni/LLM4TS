package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(input1);
        assertEquals(expected1, actual1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(input2);
        assertEquals(expected2, actual2);

        String input3 = "en_GB_London";
        Locale expected3 = new Locale("en", "GB", "London");
        Locale actual3 = LocaleUtils.toLocale(input3);
        assertEquals(expected3, actual3);

        // Test invalid inputs
        String input4 = null;
        Locale actual4 = LocaleUtils.toLocale(input4);
        assertNull(actual4);

        String input5 = "ab_CD_XXX";
        try {
            Locale actual5 = LocaleUtils.toLocale(input5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid inputs
        String inputLocale = "fr_CA";
        Locale defaultLocale = new Locale("en");
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", ""), new Locale("fr", "CA"), new Locale("fr", ""), new Locale("en"));
        List<Locale> actualList = LocaleUtils.localeLookupList(new Locale(inputLocale), defaultLocale);
        assertEquals(expectedList, actualList);

        // Test empty input
        Locale emptyInputLocale = null;
        List<Locale> emptyInputActualList = LocaleUtils.localeLookupList(emptyInputLocale, defaultLocale);
        assertEquals(Collections.EMPTY_LIST, emptyInputActualList);

        // Test defaultLocale as input
        Locale defaultLocaleInput = new Locale(inputLocale);
        List<Locale> defaultLocaleInputExpectedList = Arrays.asList(new Locale("en"));
        List<Locale> defaultLocaleInputActualList = LocaleUtils.localeLookupList(defaultLocaleInput, defaultLocaleInput);
        assertEquals(defaultLocaleInputExpectedList, defaultLocaleInputActualList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test default behavior
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        List<Locale> expectedList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedList, actualList);

        // Test with a custom list of locales
        List<Locale> customList = Arrays.asList(new Locale("es"), new Locale("it"), new Locale("fr"));
        LocaleUtils.cAvailableLocaleList = customList;
        List<Locale> customActualList = LocaleUtils.availableLocaleList();
        assertEquals(customList, customActualList);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid inputs
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("de", "DE");
        assertTrue(LocaleUtils.isAvailableLocale(locale2));

        // Test invalid inputs
        Locale locale3 = new Locale("ab", "CD");
        assertFalse(LocaleUtils.isAvailableLocale(locale3));

        Locale locale4 = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale4));
    }
}