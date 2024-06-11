import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale actual1 = LocaleUtils.toLocale(input1);
        assertEquals(expected1, actual1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale actual2 = LocaleUtils.toLocale(input2);
        assertEquals(expected2, actual2);

        String input3 = "en_GB_xxx";
        Locale expected3 = new Locale("en", "GB", "xxx");
        Locale actual3 = LocaleUtils.toLocale(input3);
        assertEquals(expected3, actual3);

        // Test invalid inputs
        String input4 = null;
        Locale actual4 = LocaleUtils.toLocale(input4);
        assertNull(actual4);

        String input5 = "frAA_CA_xxx";
        Locale actual5 = LocaleUtils.toLocale(input5);
        assertNull(actual5);

        String input6 = "fr_aa";
        Locale actual6 = LocaleUtils.toLocale(input6);
        assertNull(actual6);

        String input7 = "fr_CA_aa";
        Locale actual7 = LocaleUtils.toLocale(input7);
        assertNull(actual7);

        String input8 = "fr_CA__";
        Locale actual8 = LocaleUtils.toLocale(input8);
        assertNull(actual8);

        String input9 = "fr_CA_xxx_aa";
        Locale actual9 = LocaleUtils.toLocale(input9);
        assertNull(actual9);
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        Locale defaultLocale = null;
        List<Locale> expectedEmptyList = Collections.emptyList();
        List<Locale> actualEmptyList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expectedEmptyList, actualEmptyList);

        // Test valid inputs
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> expectedList1 = Arrays.asList(locale1, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        List<Locale> actualList1 = LocaleUtils.localeLookupList(locale1, defaultLocale);
        assertEquals(expectedList1, actualList1);

        Locale locale2 = new Locale("fr", "CA");
        List<Locale> expectedList2 = Arrays.asList(locale2, new Locale("fr"), new Locale("en"), defaultLocale);
        List<Locale> actualList2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        assertEquals(expectedList2, actualList2);

        Locale locale3 = new Locale("fr");
        List<Locale> expectedList3 = Arrays.asList(locale3, new Locale("en"), defaultLocale);
        List<Locale> actualList3 = LocaleUtils.localeLookupList(locale3, defaultLocale);
        assertEquals(expectedList3, actualList3);

        // Test null input
        Locale locale4 = null;
        List<Locale> actualList4 = LocaleUtils.localeLookupList(locale4, defaultLocale);
        assertSame(expectedEmptyList, actualList4);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> expectedEmptyList = Collections.emptyList();
        List<Locale> actualEmptyList = LocaleUtils.availableLocaleList();
        assertEquals(expectedEmptyList, actualEmptyList);

        // Test valid list
        List<Locale> expectedList = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH);
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid locale
        Locale locale1 = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));

        // Test invalid locale
        Locale locale2 = new Locale("fr", "CA", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));

        // Test null input
        Locale locale3 = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale3));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        String countryCode = null;
        Collection<Locale> expectedEmptyList = Collections.emptyList();
        Collection<Locale> actualEmptyList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedEmptyList, actualEmptyList);

        // Test valid list
        String countryCode1 = "US";
        Collection<Locale> expectedList1 = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        Collection<Locale> actualList1 = LocaleUtils.languagesByCountry(countryCode1);
        assertEquals(expectedList1, actualList1);

        // Test null input
        String countryCode2 = null;
        Collection<Locale> actualList2 = LocaleUtils.languagesByCountry(countryCode2);
        assertEquals(expectedEmptyList, actualList2);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        String languageCode = null;
        Collection<Locale> expectedEmptyList = Collections.emptyList();
        Collection<Locale> actualEmptyList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedEmptyList, actualEmptyList);

        // Test valid list
        String languageCode1 = "en";
        Collection<Locale> expectedList1 = Arrays.asList(new Locale("EN", "US"), new Locale("EN", "GB"));
        Collection<Locale> actualList1 = LocaleUtils.countriesByLanguage(languageCode1);
        assertEquals(expectedList1, actualList1);

        // Test null input
        String languageCode2 = null;
        Collection<Locale> actualList2 = LocaleUtils.countriesByLanguage(languageCode2);
        assertEquals(expectedEmptyList, actualList2);
    }
}