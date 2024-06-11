package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private String localeString;
    private Locale expectedLocale;

    public LocaleUtilsTest(String localeString, Locale expectedLocale) {
        this.localeString = localeString;
        this.expectedLocale = expectedLocale;
    }

    @Parameterized.Parameters(name = "localeString = {0}, expectedLocale = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"en", new Locale("en")},
                {"en_GB", new Locale("en", "GB")},
                {"en_GB_XXX", new Locale("en", "GB", "XXX")},
                {"fr", new Locale("fr")},
                {"fr_CA", new Locale("fr", "CA")},
                {"fr_CA_XXX", new Locale("fr", "CA", "XXX")},
                {"fr_CH", new Locale("fr", "CH")},
                {"es_ES", new Locale("es", "ES")},
                {"es_ES_XXX", new Locale("es", "ES", "XXX")},
                {"es_AR", new Locale("es", "AR")},
                {"es_AR_XXX", new Locale("es", "AR", "XXX")},
                {"es_CL", new Locale("es", "CL")},
                {"es_CL_XXX", new Locale("es", "CL", "XXX")},
                {"invalid", null}
        });
    }

    @Test
    public void testToLocale() {
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
        assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
        assertEquals(expectedLocale.getVariant(), actualLocale.getVariant());
    }

    // Additional tests for localeLookupList method
    @Test
    public void testLocaleLookupList_en() {
        List<Locale> localeList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(3, localeList.size());
        assertEquals(Locale.ENGLISH, localeList.get(0));
        assertEquals(Locale.ENGLISH, localeList.get(1));
        assertEquals(Locale.US, localeList.get(2));
    }

    @Test
    public void testLocaleLookupList_fr() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA"));
        assertEquals(4, localeList.size());
        assertEquals(new Locale("fr", "CA"), localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(Locale.CANADA, localeList.get(2));
        assertEquals(Locale.US, localeList.get(3));
    }

    @Test
    public void testLocaleLookupList_es() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("es", "ES"));
        assertEquals(2, localeList.size());
        assertEquals(new Locale("es", "ES"), localeList.get(0));
        assertEquals(Locale.SPAIN, localeList.get(1));
    }

    @Test
    public void testLocaleLookupList_invalidLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertTrue(localeList.isEmpty());
    }

    // Additional tests for availableLocaleList and availableLocaleSet methods
    @Test
    public void testAvailableLocaleListAndSet_size() {
        int listSize = LocaleUtils.availableLocaleList().size();
        int setSize = LocaleUtils.availableLocaleSet().size();
        assertEquals(listSize, setSize);
    }

    @Test
    public void testAvailableLocaleListAndSet_containValidLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.availableLocaleList().contains(locale));
        assertTrue(LocaleUtils.availableLocaleSet().contains(locale));
    }

    @Test
    public void testAvailableLocaleListAndSet_containInvalidLocale() {
        Locale locale = new Locale("fr", null);
        assertFalse(LocaleUtils.availableLocaleList().contains(locale));
        assertFalse(LocaleUtils.availableLocaleSet().contains(locale));
    }
}