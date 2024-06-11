import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_NullString() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguageCode() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_"));
    }

    @Test
    public void testToLocale_InvalidFormat_MissingLanguageCode() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_InvalidFormat_MissingCountryCode() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_"));
    }

    @Test
    public void testToLocale_InvalidFormat_MissingCountryCodeAndLanguageCode() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_"));
    }

    @Test
    public void testToLocale_ValidFormat_EmptyString() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_ValidFormat_SingleLanguageCode() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
    }

    @Test
    public void testToLocale_ValidFormat_SingleLanguageCodeWithCountryCode() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCodeWithVariant() {
        assertEquals(new Locale("en", "US", "en-US"), LocaleUtils.toLocale("en_US-en"));
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        assertEquals(Collections.singletonList(Locale.ROOT), LocaleUtils.localeLookupList(new Locale("")));
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        Locale locale = new Locale("en", "US", "en-US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(Arrays.asList(locale, new Locale("en", "US"), new Locale("en"), Locale.ROOT), list);
    }

    @Test
    public void testLocaleLookupList_LocaleWithoutVariant() {
        Locale locale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(Arrays.asList(locale, new Locale("en"), Locale.ROOT), list);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> list = LocaleUtils.localeLookupList(null, Locale.getDefault());
        assertFalse(list.isEmpty());
        assertTrue(Collections.disjoint(list, Arrays.asList(Locale.ROOT)));
    }

    @Test
    public void testAvailableLocaleList_Empty() {
        assertNotNull(LocaleUtils.availableLocaleList());
        assertTrue(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleList_NonEmpty() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertFalse(list.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_Empty() {
        assertNotNull(LocaleUtils.availableLocaleSet());
        assertTrue(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_NonEmpty() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertFalse(set.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_EmptyLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid", "country")));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("US");
        assertFalse(languages.isEmpty());
        assertTrue(languages.contains(new Locale("en", "US")));
        assertTrue(languages.contains(new Locale("en", "CA")));
        assertTrue(languages.contains(new Locale("es", "US")));
        //...
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertFalse(countries.isEmpty());
        assertTrue(countries.contains(new Locale("en", "US")));
        assertTrue(countries.contains(new Locale("en", "GB")));
        assertTrue(countries.contains(new Locale("en", "CA")));
        //...
    }
}