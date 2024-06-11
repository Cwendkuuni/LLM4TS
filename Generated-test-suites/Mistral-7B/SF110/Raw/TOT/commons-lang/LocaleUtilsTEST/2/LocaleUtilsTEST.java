
package org.apache.commons.lang3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locales
        assertEquals(new Locale("en_US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr_FR"), LocaleUtils.toLocale("fr_FR"));
        assertEquals(new Locale("es_ES"), LocaleUtils.toLocale("es_ES"));

        // Test invalid locale without country
        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid locale with empty country
        try {
            LocaleUtils.toLocale("en_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid locale with '#' character
        try {
            LocaleUtils.toLocale("en#US");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid locale with less than required characters
        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test null locale
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Test valid locale
        Locale locale = new Locale("en", "US");
        assertEquals(Arrays.asList(locale, new Locale("en", ""), new Locale("", "US")), LocaleUtils.localeLookupList(locale));

        // Test locale with variant
        Locale localeWithVariant = new Locale("en", "US", "en_US");
        assertEquals(Arrays.asList(localeWithVariant, new Locale("en", "US"), new Locale("en", ""), new Locale("", "US")), LocaleUtils.localeLookupList(localeWithVariant));

        // Test default locale
        Locale defaultLocale = new Locale("", "");
        List<Locale> list = LocaleUtils.localeLookupList(localeWithVariant, defaultLocale);
        assertEquals(Collections.unmodifiableList(Arrays.asList(localeWithVariant, new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), defaultLocale)), list);

        // Test empty locale
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(new Locale("", "")));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        assertTrue(LocaleUtils.availableLocaleList().isEmpty());

        // Test available locale list
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(131, availableLocaleList.size());

        // Test specific locales
        assertTrue(availableLocaleList.contains(new Locale("en", "US")));
        assertTrue(availableLocaleList.contains(new Locale("fr", "FR")));
        assertTrue(availableLocaleList.contains(new Locale("es", "ES")));
    }
}