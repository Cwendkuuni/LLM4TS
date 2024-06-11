import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "fr_CA_FR"};
        for (String testCase : testCases) {
            Locale expected = new Locale(testCase.substring(0, 2), testCase.substring(3));
            Locale actual = LocaleUtils.toLocale(testCase);
            assertEquals(expected.getDisplayName(), actual.getDisplayName());
        }

        // Test invalid input
        String[] invalidTestCases = new String[]{"en_GB_xyz", "en_GB_xyz_xyz", "en_xyz", "en_XY"};
        for (String testCase : invalidTestCases) {
            try {
                LocaleUtils.toLocale(testCase);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("en", "GB", "US");
        Locale defaultLocale = new Locale("en", "US");

        List<Locale> expectedList = new ArrayList<>();
        expectedList.add(baseLocale);
        expectedList.add(new Locale("en", "GB"));
        expectedList.add(new Locale("en", ""));
        expectedList.add(defaultLocale);

        List<Locale> actualList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        assertEquals(expectedList, actualList);

        // Test empty list
        Locale emptyLocale = null;
        List<Locale> actualListEmpty = LocaleUtils.localeLookupList(emptyLocale, defaultLocale);
        assertTrue(Collections.isEmpty(actualListEmpty));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> actualList = Locale.getAvailableLocales();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        Set<Locale> actualSet = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale availableLocale = new Locale("en", "US");
        Locale unavailableLocale = new Locale("xyz", "xyz");
        assertTrue(LocaleUtils.isAvailableLocale(availableLocale));
        assertFalse(LocaleUtils.isAvailableLocale(unavailableLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedList = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actualList = new ArrayList<>();
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            if (locale.getCountry().equals(countryCode) && locale.getVariant().length() == 0) {
                actualList.add(locale);
            }
        }
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedList = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actualList = new ArrayList<>();
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            if (locale.getLanguage().equals(languageCode) && locale.getCountry().length() != 0 && locale.getVariant().length() == 0) {
                actualList.add(locale);
            }
        }
        assertEquals(expectedList, actualList);
    }
}