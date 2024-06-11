package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private String localeString;
    private Locale expectedLocale;

    public LocaleUtilsTest(String localeString, Locale expectedLocale) {
        this.localeString = localeString;
        this.expectedLocale = expectedLocale;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                {"en", new Locale("en", "")},
                {"en_GB", new Locale("en", "GB")},
                {"en_GB_xxx", new Locale("en", "GB", "xxx")},
                {"fr", new Locale("fr", "")},
                {"fr_CA", new Locale("fr", "CA")},
                {"fr_CA_xxx", new Locale("fr", "CA", "xxx")},
                {"fr_FR", new Locale("fr", "FR")},
                {"de_DE", new Locale("de", "DE")},
                {"es_ES", new Locale("es", "ES")},
                {"es_MX", new Locale("es", "MX")},
                {"it_IT", new Locale("it", "IT")},
                {"ja_JP", new Locale("ja", "JP")},
                {"ko_KR", new Locale("ko", "KR")},
                {"pt_BR", new Locale("pt", "BR")},
                {"zh_CN", new Locale("zh", "CN")},
                {"zh_TW", new Locale("zh", "TW")},
                {null, null}
        };
        return Arrays.asList(data);
    }

    @Test
    public void testToLocale() {
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals("toLocale incorrect result for " + localeString, expectedLocale, actualLocale);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = LocaleUtils.toLocale(localeString);
        boolean result = LocaleUtils.isAvailableLocale(locale);
        if (locale == null) {
            assertFalse("isAvailableLocale should return false for null input");
        } else {
            assertTrue("isAvailableLocale should return true for available locale: " + locale, result);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("xxxxx");
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> localeList = LocaleUtils.localeLookupList(expectedLocale, null);
        assertEquals("localeLookupList incorrect result for " + expectedLocale, expectedLocale, localeList.get(0));
        assertTrue("localeLookupList should contain more than one element for " + expectedLocale, localeList.size() > 1);
        Locale firstMoreGeneralLocale = localeList.get(1);
        assertEquals("localeLookupList should return a more general locale for " + expectedLocale,
                expectedLocale.getLanguage(), firstMoreGeneralLocale.getLanguage());
        assertFalse("localeLookupList should not return the same locale for " + expectedLocale,
                expectedLocale.equals(firstMoreGeneralLocale));
        if (expectedLocale.getCountry().length() > 0) {
            assertEquals("localeLookupList should return the empty country locale for " + expectedLocale,
                    "", firstMoreGeneralLocale.getCountry());
        }
    }

    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null, null);
        assertTrue("localeLookupList should return an empty list for null input", localeList.isEmpty());
    }

    @Test
    public void testLocaleLookupListDefaultLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(expectedLocale, new Locale("en"));
        assertTrue("localeLookupList should contain the default locale for " + expectedLocale,
                localeList.contains(new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue("availableLocaleList should not be empty", !localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertTrue("availableLocaleSet should not be empty", !localeSet.isEmpty());
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> localeList = LocaleUtils.languagesByCountry(expectedLocale.getCountry());
        assertEquals("languagesByCountry incorrect result for " + expectedLocale.getCountry(),
                Collections.singletonList(expectedLocale), localeList);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage(expectedLocale.getLanguage());
        assertEquals("countriesByLanguage incorrect result for " + expectedLocale.getLanguage(),
                Collections.singletonList(expectedLocale), localeList);
    }
}