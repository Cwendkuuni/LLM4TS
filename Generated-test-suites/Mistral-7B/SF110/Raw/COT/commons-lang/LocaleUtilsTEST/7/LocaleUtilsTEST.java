import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_EmptyInput() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LocaleWithInvalidFormat_NoLanguage() {
        String invalidLocale = "_aa";
        assertFalse(LocaleUtils.isAvailableLocale(new Locale(invalidLocale)));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LocaleWithInvalidFormat_NoCountry() {
        String invalidLocale = "aa_";
        assertFalse(LocaleUtils.isAvailableLocale(new Locale(invalidLocale)));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LocaleWithInvalidFormat_InvalidLanguageLength() {
        String invalidLocale = "aa__";
        assertFalse(LocaleUtils.isAvailableLocale(new Locale(invalidLocale)));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LocaleWithInvalidFormat_InvalidCountryLength() {
        String invalidLocale = "aa_aa";
        assertFalse(LocaleUtils.isAvailableLocale(new Locale(invalidLocale)));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LocaleWithInvalidFormat_InvalidCountryUppercase() {
        String invalidLocale = "aa_aaA";
        assertFalse(LocaleUtils.isAvailableLocale(new Locale(invalidLocale)));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_LocaleWithInvalidFormat_EmptyCountry() {
        String invalidLocale = "aa_";
        assertFalse(LocaleUtils.isAvailableLocale(new Locale(invalidLocale)));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocale));
    }

    @Test
    public void testToLocale_ValidLocale() {
        String validLocale = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        assertEquals(expectedLocale, LocaleUtils.toLocale(validLocale));
    }

    @Test
    public void testToLocale_LocaleWithLanguageCodeLengthGreaterThan3() {
        String validLocale = "en_US_LocaleKey";
        Locale expectedLocale = new Locale("en", "US", "LocaleKey");
        assertEquals(expectedLocale, LocaleUtils.toLocale(validLocale));
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> resultList = LocaleUtils.localeLookupList(null);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expectedList = new ArrayList<Locale>(3);
        expectedList.add(locale);
        expectedList.add(new Locale("en", ""));
        expectedList.add(Locale.getDefault());

        List<Locale> resultList = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expectedList = new ArrayList<Locale>(3);
        expectedList.add(locale);
        expectedList.add(new Locale("en", ""));
        expectedList.add(Locale.getDefault());

        List<Locale> resultList = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        List<Locale> expectedList = new ArrayList<Locale>(1);
        expectedList.add(Locale.getDefault());

        List<Locale> resultList = LocaleUtils.localeLookupList(Locale.EMPTY);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> expectedList = new ArrayList<Locale>(1);
        expectedList.add(Locale.getDefault());

        List<Locale> resultList = LocaleUtils.localeLookupList(null, Locale.getDefault());
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedList = LocaleUtils.availableLocaleList();
        List<Locale> resultList = Locale.getAvailableLocales();
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
        Set<Locale> resultSet = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expectedSet, resultSet);
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        Locale invalidLocale = new Locale("", "");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> expectedList = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> resultList = new ArrayList<Locale>();
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            if (locale.getCountry().equals(countryCode) && StringUtils.isBlank(locale.getLanguage())) {
                resultList.add(locale);
            }
        }
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expectedList = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> resultList = new ArrayList<Locale>();
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        for (Locale locale : availableLocales) {
            if (locale.getLanguage().equals(languageCode) && locale.getCountry().length() > 0) {
                resultList.add(locale);
            }
        }
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        String emptyCountryCode = "";
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> resultList = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertEquals(expectedList, resultList);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        String emptyLanguageCode = "";
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> resultList = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertEquals(expectedList, resultList);
    }
}