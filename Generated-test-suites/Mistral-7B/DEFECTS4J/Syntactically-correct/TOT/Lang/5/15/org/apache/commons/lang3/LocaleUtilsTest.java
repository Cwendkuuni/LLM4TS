package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale().
     */
    @Test
    public void testToLocale() {
        // Test valid cases
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en_GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en_GB_xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid cases
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("EN_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_xxx_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            LocaleUtils.toLocale("en_GB_XXX_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test localeLookupList().
     */
    @Test
    public void testLocaleLookupList() {
        // Test empty list
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Test valid cases
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList, list);

        locale = new Locale("fr");
        list = LocaleUtils.localeLookupList(locale);
        expectedList = Arrays.asList(new Locale("fr"), new Locale("fr", ""));
        assertEquals(expectedList, list);

        locale = new Locale("en");
        list = LocaleUtils.localeLookupList(locale, new Locale("fr"));
        expectedList = Arrays.asList(new Locale("fr"), new Locale("en"));
        assertEquals(expectedList, list);

        // Test invalid cases
        locale = new Locale("fr", null, "xxx");
        list = LocaleUtils.localeLookupList(locale);
        assertNull(list);

        locale = new Locale("fr", null, "");
        list = LocaleUtils.localeLookupList(locale);
        assertNull(list);
    }

    /**
     * Test availableLocaleList().
     */
    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        assertEquals(Collections.emptyList(), LocaleUtils.availableLocaleList());

        // Test valid cases
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.size() > 0);
        List<Locale> expectedList = Arrays.asList(Locale.GERMANY, Locale.US);
        assertEquals(expectedList, list.subList(0, 2));

        // Test edge cases
        locale = new Locale("zh_CN");
        list = LocaleUtils.localeLookupList(locale);
        assertEquals(new Locale("zh", "CN"), list.get(0));
        list = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(new Locale("en"), list.get(list.size() - 1));

        locale = new Locale("");
        list = LocaleUtils.localeLookupList(locale);
        assertNull(list);

        locale = new Locale("es_AR");
        list = LocaleUtils.localeLookupList(locale);
        assertEquals(new Locale("es", "AR"), list.get(0));
        list = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(new Locale("en"), list.get(list.size() - 1));

        // Test error cases
        locale = new Locale(null, "ES");
        list = LocaleUtils.localeLookupList(locale);
        assertNull(list);

        locale = new Locale("zh_CN-CN");
        list = LocaleUtils.localeLookupList(locale);
        assertNull(list);
    }

    /**
     * Test isAvailableLocale().
     */
    @Test
    public void testIsAvailableLocale() {
        // Test valid cases
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));

        // Test invalid cases
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "AR")));
    }

    /**
     * Test languagesByCountry().
     */
    @Test
    public void testLanguagesByCountry() {
        // Test valid cases
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        assertEquals(new Locale("en"), list.get(0));
        assertEquals(new Locale("fr"), list.get(1));

        // Test edge cases
        list = LocaleUtils.languagesByCountry("US");
        assertEquals(Locale.US, list.get(0));
        assertFalse(list.contains(new Locale("es")));

        // Test empty list
        list = LocaleUtils.languagesByCountry(null);
        assertNull(list);

        // Test invalid cases
        list = LocaleUtils.languagesByCountry("es");
        assertNull(list);
    }

    /**
     * Test countriesByLanguage().
     */
    @Test
    public void testCountriesByLanguage() {
        // Test valid cases
        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertEquals(new Locale("US"), list.get(0));
        assertEquals(new Locale("GB"), list.get(1));

        // Test edge cases
        list = LocaleUtils.countriesByLanguage("es");
        assertEquals(new Locale("ES"), list.get(0));
        assertEquals(new Locale("AR"), list.get(1));

        // Test empty list
        list = LocaleUtils.countriesByLanguage(null);
        assertNull(list);

        // Test invalid cases
        list = LocaleUtils.countriesByLanguage("fr");
        assertNull(list);
    }
}