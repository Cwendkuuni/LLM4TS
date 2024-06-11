import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid locale strings
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("aa"));
        assertNull(LocaleUtils.toLocale("aa_XX"));
        assertNull(LocaleUtils.toLocale("aa_XX_xx"));
        assertNull(LocaleUtils.toLocale("aa_XX_xx_xx"));
        assertNull(LocaleUtils.toLocale("aa_XX_xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("aa_XX_xx_xx_xx_xx"));

        // Test invalid characters
        assertNull(LocaleUtils.toLocale("en_GB_#"));
        assertNull(LocaleUtils.toLocale("en_GB_#_#"));
        assertNull(LocaleUtils.toLocale("en_GB_#_#_#"));
        assertNull(LocaleUtils.toLocale("en_GB_#_#_#_#"));
        assertNull(LocaleUtils.toLocale("en_GB_#_#_#_#_#"));

        // Test invalid language and country codes
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("xx_XX"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_xx_xx_xx"));

        // Test empty language and country codes
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("_"));
        assertNull(LocaleUtils.toLocale("__"));
        assertNull(LocaleUtils.toLocale("___"));
        assertNull(LocaleUtils.toLocale("____"));
        assertNull(LocaleUtils.toLocale("_____"));

        // Test invalid separator
        assertNull(LocaleUtils.toLocale("en_GB-xx"));
        assertNull(LocaleUtils.toLocale("en_GB-xx_"));
        assertNull(LocaleUtils.toLocale("en_GB-xx__"));
        assertNull(LocaleUtils.toLocale("en_GB-xx___"));
        assertNull(LocaleUtils.toLocale("en_GB-xx____"));
        assertNull(LocaleUtils.toLocale("en_GB-xx_____"));

        // Test invalid country code length
        assertNull(LocaleUtils.toLocale("en_xx"));
        assertNull(LocaleUtils.toLocale("en_xx_"));
        assertNull(LocaleUtils.toLocale("en_xx__"));
        assertNull(LocaleUtils.toLocale("en_xx___"));
        assertNull(LocaleUtils.toLocale("en_xx____"));
        assertNull(LocaleUtils.toLocale("en_xx_____"));
        assertNull(LocaleUtils.toLocale("en_xx______"));
        assertNull(LocaleUtils.toLocale("en_xx_______"));
        assertNull(LocaleUtils.toLocale("en_xx________"));
        assertNull(LocaleUtils.toLocale("en_xx_________"));

        // Test invalid language code length
        assertNull(LocaleUtils.toLocale("x_GB"));
        assertNull(LocaleUtils.toLocale("x_GB_"));
        assertNull(LocaleUtils.toLocale("x_GB__"));
        assertNull(LocaleUtils.toLocale("x_GB___"));
        assertNull(LocaleUtils.toLocale("x_GB____"));
        assertNull(LocaleUtils.toLocale("x_GB_____"));
        assertNull(LocaleUtils.toLocale("x_GB______"));
        assertNull(LocaleUtils.toLocale("x_GB________"));
        assertNull(LocaleUtils.toLocale("x_GB_________"));

        // Test invalid variant code length
        assertNull(LocaleUtils.toLocale("en_GB_xxxx"));
        assertNull(LocaleUtils.toLocale("en_GB_xxxx___"));
        assertNull(LocaleUtils.toLocale("en_GB_xxxx____"));
        assertNull(LocaleUtils.toLocale("en_GB_xxxx_____"));
        assertNull(LocaleUtils.toLocale("en_GB_xxxx______"));
        assertNull(LocaleUtils.toLocale("en_GB_xxxx________"));
        assertNull(LocaleUtils.toLocale("en_GB_xxxx_________"));

        // Test invalid variant code characters
        assertNull(LocaleUtils.toLocale("en_GB_xx#"));
        assertNull(LocaleUtils.toLocale("en_GB_xx#_"));
        assertNull(LocaleUtils.toLocale("en_GB_xx#__"));
        assertNull(LocaleUtils.toLocale("en_GB_xx#___"));
        assertNull(LocaleUtils.toLocale("en_GB_xx#____"));
        assertNull(LocaleUtils.toLocale("en_GB_xx#_____"));
        assertNull(LocaleUtils.toLocale("en_GB_xx#______"));
        assertNull(LocaleUtils.toLocale("en_GB_xx#________"));
        assertNull(LocaleUtils.toLocale("en_GB_xx#_________"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, lookupList);

        locale = new Locale("fr");
        lookupList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"));
        assertEquals(expected, lookupList);

        locale = new Locale("fr", "CA", "xxx");
        lookupList = LocaleUtils.localeLookupList(locale, new Locale("fr"));
        expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, lookupList);

        locale = new Locale("fr", "CA", "xxx");
        lookupList = LocaleUtils.localeLookupList(locale, null);
        expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, lookupList);

        locale = null;
        lookupList = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertNotNull(lookupList);
        assertTrue(lookupList.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> locales = Arrays.asList(
            new Locale("en"),
            new Locale("fr"),
            new Locale("fr_CA"),
            new Locale("fr_CA_FR"),
            new Locale("fr_CA_FR_XX"),
            new Locale("es"),
            new Locale("es_ES"),
            new Locale("es_MX"),
            new Locale("es_MX_ES"),
            new Locale("es_MX_ES_XX"),
            new Locale("de"),
            new Locale("de_DE"),
            new Locale("de_AT"),
            new Locale("de_AT_DE"),
            new Locale("de_AT_DE_XX"),
            new Locale("it"),
            new Locale("it_IT"),
            new Locale("it_IT_IT"),
            new Locale("it_IT_IT_XX")
        );

        for (Locale locale : locales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        locale = new Locale("xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("xx_XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("xx_XX_xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("xx_XX_xx_xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("xx_XX_xx_xx_xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("xx_XX_xx_xx_xx_xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("xx_XX_xx_xx_xx_xx_xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("xx_XX_xx_xx_xx_xx_xx_xx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("de"));
        assertEquals(expected, LocaleUtils.languagesByCountry("CA"));

        expected = Arrays.asList(new Locale("fr"), new Locale("de"), new Locale("it"));
        assertEquals(expected, LocaleUtils.languagesByCountry("CH"));

        expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("AT"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("de"));

        expected = Arrays.asList(new Locale("MX"), new Locale("ES"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("es"));

        expected = Arrays.asList(new Locale("GB"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));

        expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.countriesByLanguage(null));
    }

}