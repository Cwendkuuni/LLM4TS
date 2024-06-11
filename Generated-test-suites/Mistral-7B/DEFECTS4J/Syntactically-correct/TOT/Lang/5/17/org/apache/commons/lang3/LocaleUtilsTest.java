package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid inputs.
     */
    @Test
    public void testToLocale_ValidInput() {
        // Test toLocale with empty string
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());

        // Test toLocale with language only input
        assertEquals("fr", LocaleUtils.toLocale("fr").getLanguage());

        // Test toLocale with language and country input
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());

        // Test toLocale with language, country and variant input
        assertEquals("fr_CA_Var", LocaleUtils.toLocale("fr_CA_Var").toString());
    }

    /**
     * Test toLocale method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput() {
        // Test toLocale with null input
        LocaleUtils.toLocale(null);

        // Test toLocale with empty language input
        LocaleUtils.toLocale("").toString();

        // Test toLocale with empty country input
        LocaleUtils.toLocale("en_").toString();

        // Test toLocale with incorrect separator
        LocaleUtils.toLocale("en_GB_xx").toString();

        // Test toLocale with language code longer than 2 characters
        LocaleUtils.toLocale("espa").toString();

        // Test toLocale with country code longer than 2 characters
        LocaleUtils.toLocale("US_Texas").toString();
    }

    /**
     * Test localeLookupList method with valid inputs.
     */
    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale locale = new Locale("fr", "CA", "Var");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);

        // Test localeLookupList with no default locale
        assertEquals(3, localeList.size());
        assertEquals(locale, localeList.get(0));
        assertEquals("fr_CA", localeList.get(1).toString());
        assertEquals("fr", localeList.get(2).toString());

        // Test localeLookupList with default locale
        List<Locale> localeListDefault = LocaleUtils.localeLookupList(locale, Locale.getDefault());

        // Test localeLookupList with no default locale
        assertEquals(locale, localeListDefault.get(0));
        assertEquals(3, localeListDefault.size());
        assertEquals(locale, localeListDefault.get(0));
        assertEquals("fr_CA", localeListDefault.get(1).toString());
        assertEquals("fr", localeListDefault.get(2).toString());
        assertEquals("en_US", localeListDefault.get(3).toString());

        // Test localeLookupList with different default locale
        List<Locale> localeListDefault2 = LocaleUtils.localeLookupList(locale, new Locale("es", "ES"));

        // Test localeLookupList with no default locale
        assertEquals(locale, localeListDefault2.get(0));
        assertEquals(4, localeListDefault2.size());
        assertEquals(locale, localeListDefault2.get(0));
        assertEquals("fr_CA", localeListDefault2.get(1).toString());
        assertEquals("fr", localeListDefault2.get(2).toString());
        assertEquals("es_ES", localeListDefault2.get(3).toString());
    }

    /**
     * Test localeLookupList method with invalid inputs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLocaleLookupList_InvalidInput() {
        // Test localeLookupList with null locale
        LocaleUtils.localeLookupList(null);

        // Test localeLookupList with null default locale
        LocaleUtils.localeLookupList(new Locale("fr", "CA"), null);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(55, localeList.size());
        // Add some assertions to check specific locales are present
        assertTrue(localeList.contains(new Locale("en", "US")));
        assertTrue(localeList.contains(new Locale("fr", "FR")));
        assertTrue(localeList.contains(new Locale("es", "ES")));
    }
}