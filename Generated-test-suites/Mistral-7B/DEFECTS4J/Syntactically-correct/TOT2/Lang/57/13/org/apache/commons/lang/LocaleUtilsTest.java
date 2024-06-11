import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA"};
        for (String testCase : testCases) {
            Locale expected = new Locale(testCase.substring(0, 2), testCase.substring(3, testCase.length()));
            Locale actual = LocaleUtils.toLocale(testCase);
            assertEquals(expected, actual);
        }

        // Test invalid input
        String[] invalidTestCases = new String[]{"en_GB_abc", "en_ABC", "en_GB_123"};
        for (String invalidTestCase : invalidTestCases) {
            try {
                LocaleUtils.toLocale(invalidTestCase);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test simple cases
        Locale baseLocale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(Arrays.asList(baseLocale), result);

        result = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(Arrays.asList(Locale.FRENCH), result);

        // Test more complex cases
        Locale validLocale = new Locale("fr", "CA");
        result = LocaleUtils.localeLookupList(validLocale, Locale.ENGLISH);
        List<Locale> expected = Arrays.asList(
                validLocale,
                new Locale("fr", "CA"),
                new Locale("fr"),
                Locale.ENGLISH);
        assertEquals(expected, result);

        // Test invalid input
        Locale invalidLocale = new Locale("ab", "CD");
        result = LocaleUtils.localeLookupList(invalidLocale, Locale.ENGLISH);
        assertEquals(Collections.singletonList(Locale.ENGLISH), result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale validLocale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(validLocale));

        Locale invalidLocale = new Locale("ab", "CD");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_US, Locale.ENGLISH_GB, Locale.ENGLISH_CA);
        assertEquals(expected, result);

        // Test invalid input
        countryCode = null;
        result = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_US, Locale.ENGLISH_GB, Locale.ENGLISH_CA, Locale.ENGLISH_IN, Locale.ENGLISH_ZA, Locale.ENGLISH_IE, Locale.ENGLISH_SG, Locale.ENGLISH_AU, Locale.ENGLISH_NZ, Locale.ENGLISH_JM, Locale.ENGLISH_TT, Locale.ENGLISH_PK, Locale.ENGLISH_BM, Locale.ENGLISH_VI, Locale.ENGLISH_MP, Locale.ENGLISH_HK, Locale.ENGLISH_HM, Locale.ENGLISH_PH, Locale.ENGLISH_GU, Locale.ENGLISH_AS, Locale.ENGLISH_MH, Locale.ENGLISH_KN, Locale.ENGLISH_LC, Locale.ENGLISH_SB, Locale.ENGLISH_BI, Locale.ENGLISH_SI, Locale.ENGLISH_KE, Locale.ENGLISH_TZ, Locale.ENGLISH_UG, Locale.ENGLISH_NE, Locale.ENGLISH_ET, Locale.ENGLISH_EE, Locale.ENGLISH_LV, Locale.ENGLISH_LT, Locale.ENGLISH_LV, Locale.ENGLISH_EE, Locale.ENGLISH_LV, Locale.ENGLISH_LV, Locale.ENGLISH_EE, Locale.ENGLISH_LV, Locale.ENGLISH_LV, Locale.ENGLISH_EE, Locale.ENGLISH_LV, Locale.ENGLISH_EE);
        assertEquals(expected, result);

        // Test invalid input
        languageCode = null;
        result = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(Collections.emptyList(), result);
    }
}