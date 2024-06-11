import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        // Test valid input
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("ja_JP", LocaleUtils.toLocale("ja_JP").toString());
    }

    @Test
    public void testToLocale_EmptyInput() {
        // Test empty input
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_InvalidInput_LanguageLowerCase() {
        // Test invalid input with lowercase language
        String invalidInput1 = "en_GB";
        try {
            LocaleUtils.toLocale(invalidInput1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocale_InvalidInput_LanguageUpperCase() {
        // Test invalid input with uppercase language
        String invalidInput2 = "EN_GB";
        try {
            LocaleUtils.toLocale(invalidInput2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocale_InvalidInput_Separator() {
        // Test invalid input with wrong separator
        String invalidInput3 = "en_GB_";
        try {
            LocaleUtils.toLocale(invalidInput3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocale_InvalidInput_Length() {
        // Test invalid input with wrong length
        String invalidInput4 = "en_GB_xx";
        try {
            LocaleUtils.toLocale(invalidInput4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        // Test valid input
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(Locale.US, localeList.get(0));
        assertEquals(Locale.ITALY, localeList.get(1));
        assertEquals(Locale.FRANCE, localeList.get(2));
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        // Test empty input
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testLocaleLookupList_InvalidInput() {
        // Test invalid input
        Locale locale = new Locale("xx", "xx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(Collections.singletonList(locale), localeList);
    }

    @Test
    public void testAvailableLocaleList_ValidInput() {
        // Test valid input
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(!localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_ValidInput() {
        // Test valid input
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertTrue(!localeSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_ValidInput() {
        // Test valid input
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        // Test invalid input
        Locale locale = new Locale("xx", "xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_ValidInput() {
        // Test valid input
        List<Locale> languageList = LocaleUtils.languagesByCountry("CA");
        assertEquals(2, languageList.size());
        assertTrue(languageList.contains(new Locale("en", "CA")));
        assertTrue(languageList.contains(new Locale("fr", "CA")));
    }

    @Test
    public void testLanguagesByCountry_EmptyInput() {
        // Test empty input
        List<Locale> languageList = LocaleUtils.languagesByCountry(null);
        assertTrue(languageList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_ValidInput() {
        // Test valid input
        List<Locale> countryList = LocaleUtils.countriesByLanguage("en");
        assertEquals(2, countryList.size());
        assertTrue(countryList.contains(new Locale("CA", "en")));
        assertTrue(countryList.contains(new Locale("GB", "en")));
    }

    @Test
    public void testCountriesByLanguage_EmptyInput() {
        // Test empty input
        List<Locale> countryList = LocaleUtils.countriesByLanguage(null);
        assertTrue(countryList.isEmpty());
    }
}