import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNotNull(LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("US"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageLength() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_xx"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountryLength() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XX"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XXX"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageCase() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("En_US"));
    }

    @Test
    public void testToLocale_ValidFormat_NoLanguage_NoCountry() {
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
        assertEquals(Locale.ROOT, LocaleUtils.toLocale("_"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageOnly() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
    }

    @Test
    public void testToLocale_ValidFormat_Language_Country() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
    }

    @Test
    public void testToLocale_ValidFormat_Language_Country_Variant() {
        assertEquals(new Locale("en", "US", "en_US"), LocaleUtils.toLocale("en_US_en"));
        assertEquals(new Locale("fr", "FR", "fr_FR_fr"), LocaleUtils.toLocale("fr_FR_fr"));
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        assertNotNull(LocaleUtils.localeLookupList(Locale.ROOT));
        assertEquals(Collections.singletonList(Locale.ROOT), LocaleUtils.localeLookupList(Locale.ROOT));
    }

    @Test
    public void testLocaleLookupList_InputWithLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(Locale.ROOT, new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_InputWithDefaultLocale() {
        Locale defaultLocale = new Locale("en", "GB");
        Locale locale = new Locale("fr", "FR");
        List<Locale> expected = Arrays.asList(Locale.ROOT, new Locale("en", "GB"), new Locale("fr", "FR"), new Locale("en", "US"), new Locale("en", ""));
        List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        assertTrue(expected.size() > 0);
        assertFalse(expected.contains(null));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        assertTrue(expected.size() > 0);
        assertFalse(expected.contains(null));
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "US")));
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("es", "ES"), new Locale("fr", "CA"));
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en", "US"), new Locale("fr", "CA"), new Locale("fr", "FR"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);
    }
}