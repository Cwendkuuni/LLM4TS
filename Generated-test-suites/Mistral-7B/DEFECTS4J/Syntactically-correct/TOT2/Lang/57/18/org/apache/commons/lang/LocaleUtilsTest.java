import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

        String localeString3 = "en_GB_XXX";
        Locale expectedLocale3 = new Locale("en", "GB", "XXX");
        Locale actualLocale3 = LocaleUtils.toLocale(localeString3);
        assertEquals(expectedLocale3, actualLocale3);

        // Test invalid input
        String invalidLocaleString1 = null;
        Locale actualLocaleInvalid1 = LocaleUtils.toLocale(invalidLocaleString1);
        assertNull(actualLocaleInvalid1);

        String invalidLocaleString2 = "ab_CD_EFG";
        Locale actualLocaleInvalid2 = LocaleUtils.toLocale(invalidLocaleString2);
        assertNull(actualLocaleInvalid2);

        String invalidLocaleString3 = "en_ab";
        Locale actualLocaleInvalid3 = LocaleUtils.toLocale(invalidLocaleString3);
        assertNull(actualLocaleInvalid3);

        String invalidLocaleString4 = "en_CD_EFG";
        Locale actualLocaleInvalid4 = LocaleUtils.toLocale(invalidLocaleString4);
        assertNull(actualLocaleInvalid4);
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        Locale emptyLocale = null;
        List<Locale> expectedLocaleList1 = Collections.emptyList();
        List<Locale> actualLocaleList1 = LocaleUtils.localeLookupList(emptyLocale);
        assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test simple input
        Locale simpleLocale = Locale.US;
        List<Locale> expectedLocaleList2 = Arrays.asList(Locale.US, Locale.ENGLISH, Locale.EN);
        List<Locale> actualLocaleList2 = LocaleUtils.localeLookupList(simpleLocale);
        assertEquals(expectedLocaleList2, actualLocaleList2);

        // Test specific locale with variant
        Locale specificLocaleWithVariant = new Locale("fr", "CA", "XXX");
        List<Locale> expectedLocaleList3 = Arrays.asList(specificLocaleWithVariant, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualLocaleList3 = LocaleUtils.localeLookupList(specificLocaleWithVariant);
        assertEquals(expectedLocaleList3, actualLocaleList3);

        // Test locale with country
        Locale localeWithCountry = new Locale("fr", "CA");
        List<Locale> expectedLocaleList4 = Arrays.asList(localeWithCountry, new Locale("fr"), new Locale("fr", ""));
        List<Locale> actualLocaleList4 = LocaleUtils.localeLookupList(localeWithCountry);
        assertEquals(expectedLocaleList4, actualLocaleList4);

        // Test locale with language
        Locale localeWithLanguage = new Locale("fr");
        List<Locale> expectedLocaleList5 = Arrays.asList(localeWithLanguage, new Locale(""), new Locale("en"));
        List<Locale> actualLocaleList5 = LocaleUtils.localeLookupList(localeWithLanguage, Locale.ENGLISH);
        assertEquals(expectedLocaleList5, actualLocaleList5);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> expectedLocaleList1 = Collections.EMPTY_LIST;
        List<Locale> actualLocaleList1 = LocaleUtils.availableLocaleList();
        assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test non-empty list
        List<Locale> expectedLocaleList2 = Arrays.asList(Locale.US, Locale.ENGLISH, Locale.EN, new Locale("fr", "FR"), new Locale("fr", "CA"));
        List<Locale> actualLocaleList2 = LocaleUtils.availableLocaleList();
        assertEquals(expectedLocaleList2, actualLocaleList2);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test invalid locale
        Locale invalidLocale = null;
        boolean result = LocaleUtils.isAvailableLocale(invalidLocale);
        assertFalse(result);

        // Test known locale
        Locale knownLocale = Locale.US;
        result = LocaleUtils.isAvailableLocale(knownLocale);
        assertTrue(result);

        // Test unknown locale
        Locale unknownLocale = new Locale("ab", "CD");
        result = LocaleUtils.isAvailableLocale(unknownLocale);
        assertFalse(result);
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty input
        String emptyCountryCode = null;
        List<Locale> expectedLocaleList1 = Collections.emptyList();
        List<Locale> actualLocaleList1 = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test valid country code
        String validCountryCode = "CA";
        List<Locale> expectedLocaleList2 = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"), new Locale("en", ""));
        List<Locale> actualLocaleList2 = LocaleUtils.languagesByCountry(validCountryCode);
        assertEquals(expectedLocaleList2, actualLocaleList2);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty input
        String emptyLanguageCode = null;
        List<Locale> expectedLocaleList1 = Collections.emptyList();
        List<Locale> actualLocaleList1 = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertEquals(expectedLocaleList1, actualLocaleList1);

        // Test valid language code
        String validLanguageCode = "fr";
        List<Locale> expectedLocaleList2 = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actualLocaleList2 = LocaleUtils.countriesByLanguage(validLanguageCode);
        assertEquals(expectedLocaleList2, actualLocaleList2);
    }
}