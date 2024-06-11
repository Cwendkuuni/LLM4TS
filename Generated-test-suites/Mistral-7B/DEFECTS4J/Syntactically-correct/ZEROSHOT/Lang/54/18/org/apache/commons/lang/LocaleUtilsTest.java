import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Collections;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));

        // Test valid locales
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid locales
        try {
            LocaleUtils.toLocale("en_GB_xxx_zzz");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List lookupList = LocaleUtils.localeLookupList(baseLocale);
        List expectedList = new ArrayList();
        expectedList.add(baseLocale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr", ""));
        expectedList.add(Locale.getDefault());
        assertEquals(expectedList, lookupList);
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = Locale.US;
        List lookupList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
        List expectedList = new ArrayList();
        expectedList.add(baseLocale);
        expectedList.add(new Locale("fr", "CA"));
        expectedList.add(new Locale("fr", ""));
        expectedList.add(defaultLocale);
        assertEquals(expectedList, lookupList);
    }

    @Test
    public void testAvailableLocaleList() {
        List availableLocaleList = LocaleUtils.availableLocaleList();
        List expectedAvailableLocaleList = Collections.unmodifiableList(Locale.getAvailableLocales());
        assertEquals(expectedAvailableLocaleList, availableLocaleList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();
        Set expectedAvailableLocaleSet = Collections.unmodifiableSet(Locale.getAvailableLocales());
        assertEquals(expectedAvailableLocaleSet, availableLocaleSet);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("fr", "FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        assertTrue(LocaleUtils.isAvailableLocale(locale2));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ROOT));
        List availableLocaleList = new ArrayList();
        availableLocaleList.add(Locale.US);
        availableLocaleList.add(Locale.FRENCH);
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "ES")));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ITALIAN));
        for (Locale locale : availableLocaleList) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List languagesByCountry = LocaleUtils.languagesByCountry("CA");
        List expectedLanguagesByCountry = new ArrayList();
        expectedLanguagesByCountry.add(Locale.CANADA);
        expectedLanguagesByCountry.add(Locale.ENGLISH);
        expectedLanguagesByCountry.add(Locale.FRENCH);
        assertEquals(expectedLanguagesByCountry, languagesByCountry);

        List emptyLanguagesByCountry = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.EMPTY_LIST, emptyLanguagesByCountry);
    }

    @Test
    public void testCountriesByLanguage() {
        List countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        List expectedCountriesByLanguage = new ArrayList();
        expectedCountriesByLanguage.add(Locale.ENGLISH_CANADA);
        expectedCountriesByLanguage.add(Locale.ENGLISH_US);
        expectedCountriesByLanguage.add(Locale.ENGLISH_UK);
        assertEquals(expectedCountriesByLanguage, countriesByLanguage);

        List emptyCountriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.EMPTY_LIST, emptyCountriesByLanguage);
    }
}