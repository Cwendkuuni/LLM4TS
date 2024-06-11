package org.apache.commons.lang.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = new String[]{"en", "en_GB", "en_GB_xxx", "fr", "fr_CA", "fr_CA_xxx"};
        Locale[] expected = new Locale[]{Locale.ENGLISH, Locale.ENGLISH_UK, new Locale("en", "GB", "xxx"),
                Locale.FRENCH, Locale.FRENCH_CA, new Locale("fr", "CA", "xxx")};
        for (int i = 0; i < testCases.length; i++) {
            Locale actual = LocaleUtils.toLocale(testCases[i]);
            assertEquals(expected[i].getLanguage(), actual.getLanguage());
            assertEquals(expected[i].getCountry(), actual.getCountry());
            if (expected[i].getVariant() != null && expected[i].getVariant().length() > 0) {
                assertEquals(expected[i].getVariant(), actual.getVariant());
            } else {
                assertNull(actual.getVariant());
            }
        }
    }

    @Test
    public void testToLocaleNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleInvalidFormat() {
        String[] testCases = new String[]{"en_GB_xxx", "en_CA_en", "en_GB.UK", "en_GB_en_UK", "en_GB__"};
        for (String testCase : testCases) {
            try {
                LocaleUtils.toLocale(testCase);
                assertFalse("Should have thrown IllegalArgumentException", true);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(baseLocale, new Locale("fr", "CA"), new Locale("fr"),
                Locale.getDefault());
        List<Locale> actual = LocaleUtils.localeLookupList(baseLocale);
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));

        Locale defaultLocale = Locale.getDefault();
        actual = LocaleUtils.localeLookupList(defaultLocale, baseLocale);
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));
    }

    @Test
    public void testLocaleLookupListEmptyList() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLocaleLookupListDefaultLocaleOnly() {
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.getDefault());
        assertTrue(actual.contains(Locale.getDefault()));
        assertTrue(actual.isEmpty() || !actual.get(0).equals(Locale.getDefault()));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertNotNull(actual);
        assertTrue(actual.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertNotNull(actual);
        assertTrue(actual.size() > 0);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "MX");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "CA");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "ES");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es", "ES_ES");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("CA");
        List<Locale> expected = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));

        actual = LocaleUtils.languagesByCountry("ES");
        expected = Arrays.asList(new Locale("es", "ES"));
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));

        actual = LocaleUtils.languagesByCountry("US");
        expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_US);
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));

        actual = LocaleUtils.languagesByCountry(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("es");
        List<Locale> expected = Arrays.asList(new Locale("es", "ES"), new Locale("es", "MX"));
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));

        actual = LocaleUtils.countriesByLanguage("fr");
        expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "FR"));
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));

        actual = LocaleUtils.countriesByLanguage("en");
        expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_AU, Locale.ENGLISH_CA, Locale.ENGLISH_GB,
                Locale.ENGLISH_IE, Locale.ENGLISH_NZ, Locale.ENGLISH_US, Locale.ENGLISH_ZA);
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));

        actual = LocaleUtils.countriesByLanguage(null);
        assertTrue(actual.isEmpty());
    }
}