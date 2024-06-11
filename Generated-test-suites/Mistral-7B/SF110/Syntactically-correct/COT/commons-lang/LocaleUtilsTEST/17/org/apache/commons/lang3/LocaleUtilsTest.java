import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNotNull(LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
        assertEquals("", LocaleUtils.toLocale("").getCountry());
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        assertNull(LocaleUtils.toLocale("#"));
        assertNull(LocaleUtils.toLocale("_"));
        assertNull(LocaleUtils.toLocale("_AB"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        assertNull(LocaleUtils.toLocale("AA"));
        assertNull(LocaleUtils.toLocale("AA_"));
        assertNull(LocaleUtils.toLocale("AA_BB"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCountry() {
        LocaleUtils.toLocale("AA__BB");
    }

    @Test
    public void testToLocale_ValidFormat_TwoParts() {
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
        assertEquals("US", LocaleUtils.toLocale("en_US").getCountry());
    }

    @Test
    public void testToLocale_ValidFormat_ThreeParts() {
        assertEquals("de_DE_AT", LocaleUtils.toLocale("de_DE_AT").toString());
        assertEquals("de", LocaleUtils.toLocale("de_DE_AT").getLanguage());
        assertEquals("DE", LocaleUtils.toLocale("de_DE_AT").getCountry());
        assertEquals("AT", LocaleUtils.toLocale("de_DE_AT").getVariant());
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale locale = new Locale("fr", "CA");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", ""), new Locale("", "CA"), new Locale("", ""));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(null, defaultLocale);
        List<Locale> expected = Arrays.asList(defaultLocale, new Locale(""), new Locale(""));
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        assertEquals(expected.size(), result.size());
        for (Locale locale : expected) {
            assertTrue(result.contains(locale));
        }
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("", "")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "US"), new Locale("it", "US"));
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), result.get(i).toString());
        }
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"));
        assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i).toString(), result.get(i).toString());
        }
    }
}