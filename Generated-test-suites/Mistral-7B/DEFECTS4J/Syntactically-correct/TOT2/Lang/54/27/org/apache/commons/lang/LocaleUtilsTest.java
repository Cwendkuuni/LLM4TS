import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String localeString1 = "en";
        Locale expectedLocale1 = new Locale("en", "");
        Locale actualLocale1 = LocaleUtils.toLocale(localeString1);
        assertEquals(expectedLocale1, actualLocale1);

        String localeString2 = "en_GB";
        Locale expectedLocale2 = new Locale("en", "GB");
        Locale actualLocale2 = LocaleUtils.toLocale(localeString2);
        assertEquals(expectedLocale2, actualLocale2);

        String localeString3 = "en_GB_London";
        Locale expectedLocale3 = new Locale("en", "GB", "London");
        Locale actualLocale3 = LocaleUtils.toLocale(localeString3);
        assertEquals(expectedLocale3, actualLocale3);

        // Test invalid input
        String invalidLocaleString1 = "xx";
        Locale actualLocaleInvalid1 = LocaleUtils.toLocale(invalidLocaleString1);
        assertNull(actualLocaleInvalid1);

        String invalidLocaleString2 = "en_XX";
        Locale actualLocaleInvalid2 = LocaleUtils.toLocale(invalidLocaleString2);
        assertNull(actualLocaleInvalid2);

        String invalidLocaleString3 = "en_GB__";
        Locale actualLocaleInvalid3 = LocaleUtils.toLocale(invalidLocaleString3);
        assertNull(actualLocaleInvalid3);
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expectedLocaleList1 = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> actualLocaleList1 = expectedLocaleList1; // save expected list for next assertion
        List<Locale> actualLocaleList2 = LocaleUtils.localeLookupList(baseLocale, null);
        assertEquals(expectedLocaleList1.size(), actualLocaleList2.size());
        for (int i = 0; i < expectedLocaleList1.size(); i++) {
            assertEquals(expectedLocaleList1.get(i), actualLocaleList2.get(i));
        }

        // Test empty input
        Locale emptyLocale = null;
        List<Locale> expectedLocaleListEmpty = Collections.emptyList();
        List<Locale> actualLocaleListEmpty = LocaleUtils.localeLookupList(emptyLocale);
        assertEquals(expectedLocaleListEmpty, actualLocaleListEmpty);

        // Test null defaultLocale
        Locale nullDefaultLocale = null;
        List<Locale> expectedLocaleListNullDefault = new ArrayList<>();
        List<Locale> actualLocaleListNullDefault = LocaleUtils.localeLookupList(baseLocale, nullDefaultLocale);
        assertEquals(expectedLocaleList1.size() + 1, actualLocaleListNullDefault.size());
        assertTrue(actualLocaleListNullDefault.contains(nullDefaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());
        assertEquals(expectedLocaleList, actualLocaleList);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("es", "ES");
        assertTrue(LocaleUtils.isAvailableLocale(locale2));

        Locale locale3 = new Locale("xx", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale3));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedLocaleList = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actualLocaleList = expectedLocaleList; // save expected list for next assertion
        List<Locale> actualLocaleListFromUtils = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList.size(), actualLocaleListFromUtils.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i), actualLocaleListFromUtils.get(i));
        }
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expectedLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actualLocaleList = expectedLocaleList; // save expected list for next assertion
        List<Locale> actualLocaleListFromUtils = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList.size(), actualLocaleListFromUtils.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals(expectedLocaleList.get(i), actualLocaleListFromUtils.get(i));
        }
    }
}