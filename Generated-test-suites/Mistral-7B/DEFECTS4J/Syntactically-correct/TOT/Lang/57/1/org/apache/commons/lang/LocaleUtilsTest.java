package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Valid input
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.US, LocaleUtils.toLocale("en_US"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_FR"));
        assertEquals(Locale.ITALIAN, LocaleUtils.toLocale("it_IT"));
        assertEquals(Locale.SPANISH, LocaleUtils.toLocale("es_ES"));

        // Invalid input
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en_GBx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_x"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_xx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_xx_x"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_xx_xx"));

        // Test empty country
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_ "));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_   "));

        // Test empty language
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("_FR"));
        assertNull(LocaleUtils.toLocale("_FR_"));
        assertNull(LocaleUtils.toLocale("_FR_FR"));
        assertNull(LocaleUtils.toLocale("_FR_FR_"));
        assertNull(LocaleUtils.toLocale("_FR_FR_FR"));
        assertNull(LocaleUtils.toLocale("_FR_FR_FR_"));
        assertNull(LocaleUtils.toLocale("_FR_FR_FR_FR"));

        // Test invalid language
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx_xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx_xx_xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_xx_xx_xx_xx_xx_xx_xx"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> list1 = LocaleUtils.localeLookupList(null);
        assertTrue(list1.isEmpty());

        // Test single locale
        List<Locale> list2 = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(1, list2.size());
        assertEquals(Locale.ENGLISH, list2.get(0));

        // Test multiple locales
        List<Locale> list3 = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(2, list3.size());
        assertSame(Locale.US, list3.get(0));
        assertSame(Locale.ENGLISH, list3.get(1));

        // Test default locale
        List<Locale> list4 = LocaleUtils.localeLookupList(Locale.US, Locale.ENGLISH);
        assertEquals(3, list4.size());
        assertSame(Locale.US, list4.get(0));
        assertSame(Locale.ENGLISH, list4.get(1));
        assertSame(Locale.ENGLISH, list4.get(2));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> list1 = LocaleUtils.availableLocaleList();
        assertTrue(list1.isEmpty());

        // Test available locales
        List<Locale> list2 = LocaleUtils.availableLocaleList();
        assertEquals(263, list2.size());
        assertTrue(list2.contains(Locale.ENGLISH));
        assertTrue(list2.contains(Locale.FRENCH));
        assertTrue(list2.contains(Locale.ITALIAN));
        assertTrue(list2.contains(Locale.SPANISH));
    }

    @Test
    public void testIsAvailableLocale() {
        // Test empty list
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Test available locales
        List<Locale> locales = LocaleUtils.availableLocaleList();
        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        // Test invalid locale
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list1 = LocaleUtils.languagesByCountry("US");
        assertEquals(1, list1.size());
        assertEquals(Locale.ENGLISH, list1.get(0));

        List<Locale> list2 = LocaleUtils.languagesByCountry("FR");
        assertEquals(3, list2.size());
        assertSame(Locale.FRENCH, list2.get(0));
        assertSame(Locale.FRENCH, list2.get(1));
        assertSame(Locale.FRENCH_BELGIUM, list2.get(2));

        List<Locale> list3 = LocaleUtils.languagesByCountry("IT");
        assertEquals(2, list3.size());
        assertSame(Locale.ITALIAN, list3.get(0));
        assertSame(Locale.ITALIAN_Switzerland, list3.get(1));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list1 = LocaleUtils.countriesByLanguage("en");
        assertEquals(6, list1.size());
        assertSame(Locale.ENGLISH, list1.get(0));
        assertSame(Locale.ENGLISH_AUSTRALIA, list1.get(1));
        assertSame(Locale.ENGLISH_CANADA, list1.get(2));
        assertSame(Locale.ENGLISH_IRELAND, list1.get(3));
        assertSame(Locale.ENGLISH_NEW_ZEALAND, list1.get(4));
        assertSame(Locale.ENGLISH_South_Africa, list1.get(5));

        List<Locale> list2 = LocaleUtils.countriesByLanguage("fr");
        assertEquals(18, list2.size());
        // List too long to include here
    }
}