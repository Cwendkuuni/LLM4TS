import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));

        // Test valid locales
        Locale localeEN = LocaleUtils.toLocale("en");
        assertEquals("en", localeEN.getLanguage());
        assertEquals("", localeEN.getCountry());

        Locale localeENGB = LocaleUtils.toLocale("en_GB");
        assertEquals("en", localeENGB.getLanguage());
        assertEquals("GB", localeENGB.getCountry());

        Locale localeENGBXX = LocaleUtils.toLocale("en_GB_xx");
        assertEquals("en", localeENGBXX.getLanguage());
        assertEquals("GB", localeENGBXX.getCountry());
        assertEquals("xx", localeENGBXX.getVariant());

        // Test invalid locales
        try {
            LocaleUtils.toLocale("abc");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_ab_cd");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xx_ab_cd_ef");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        Collection<Locale> localeListEmpty = LocaleUtils.localeLookupList(null);
        assertTrue(Collections.isEmpty(localeListEmpty));

        // Test empty string
        Collection<Locale> localeListEmptyLocale = LocaleUtils.localeLookupList("");
        assertTrue(Collections.isEmpty(localeListEmptyLocale));

        // Test simple locale
        Collection<Locale> localeListEN = LocaleUtils.localeLookupList("en");
        assertEquals(3, localeListEN.size());
        assertTrue(localeListEN.contains(Locale.ENGLISH));
        assertTrue(localeListEN.contains(Locale.ENGLISH_US));
        assertTrue(localeListEN.contains(Locale.ENGLISH_GB));

        // Test country locale
        Collection<Locale> localeListENCA = LocaleUtils.localeLookupList("en_CA");
        assertEquals(4, localeListENCA.size());
        assertTrue(localeListENCA.contains(Locale.ENGLISH_CA));
        assertTrue(localeListENCA.contains(Locale.ENGLISH_CA_CA));
        assertTrue(localeListENCA.contains(Locale.ENGLISH));
        assertTrue(localeListENCA.contains(Locale.ENGLISH_US));

        // Test country and variant locale
        Collection<Locale> localeListENCAXX = LocaleUtils.localeLookupList("en_CA_XX");
        assertEquals(5, localeListENCAXX.size());
        assertTrue(localeListENCAXX.contains(Locale.ENGLISH_CA_XX));
        assertTrue(localeListENCAXX.contains(Locale.ENGLISH_CA));
        assertTrue(localeListENCAXX.contains(Locale.ENGLISH_CA_CA));
        assertTrue(localeListENCAXX.contains(Locale.ENGLISH));
        assertTrue(localeListENCAXX.contains(Locale.ENGLISH_US));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> localeListEmpty = LocaleUtils.availableLocaleList();
        assertTrue(Collections.isEmpty(localeListEmpty));

        // Test non-empty list
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertEquals(44, localeList.size());
        // Use assertions to check that the list contains expected locales
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test empty set
        Set<Locale> localeSetEmpty = LocaleUtils.availableLocaleSet();
        assertTrue(Collections.isEmpty(localeSetEmpty.toArray()));

        // Test non-empty set
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        // Use assertions to check that the set contains expected locales
    }

    @Test
    public void testIsAvailableLocale() {
        // Test false cases
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(""));
        assertFalse(LocaleUtils.isAvailableLocale("ab"));

        // Test true cases
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH_US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH_GB));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        List<Locale> languagesByCountryEmpty = LocaleUtils.languagesByCountry(null);
        assertTrue(Collections.isEmpty(languagesByCountryEmpty));

        // Test non-empty list
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        // Use assertions to check that the list contains expected locales
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        List<Locale> countriesByLanguageEmpty = LocaleUtils.countriesByLanguage(null);
        assertTrue(Collections.isEmpty(countriesByLanguageEmpty));

        // Test non-empty list
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        // Use assertions to check that the list contains expected locales
    }
}