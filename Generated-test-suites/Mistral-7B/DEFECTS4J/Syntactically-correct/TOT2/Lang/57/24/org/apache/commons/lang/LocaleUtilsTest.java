package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    // Test case for method: public static Locale toLocale(String str)
    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "XXX"), LocaleUtils.toLocale("en_GB_XXX"));

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            LocaleUtils.toLocale("en_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        try {
            LocaleUtils.toLocale("fr_CA_xxx_YYY");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    // Test case for method: public static List localeLookupList(Locale locale)
    @Test
    public void testLocaleLookupList() {
        // Test valid inputs
        List lookupList = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(5, lookupList.size());
        assertTrue(lookupList.contains(Locale.ENGLISH));
        assertTrue(lookupList.contains(Locale.ENGLISH.withCountry("US")));
        assertTrue(lookupList.contains(Locale.ENGLISH.withLanguage("en_US")));
        assertTrue(lookupList.contains(Locale.ENGLISH.withLanguage("en")));
        assertTrue(lookupList.contains(Locale.getDefault()));

        // Test invalid input
        List lookupListNull = LocaleUtils.localeLookupList(null);
        assertTrue(lookupListNull.isEmpty());
    }

    // Test case for method: public static List availableLocaleList()
    @Test
    public void testAvailableLocaleList() {
        List availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(207, availableLocales.size());

        // Check that all languages are present
        List languages = new ArrayList(availableLocales);
        Collections.sort(languages, new Comparator<Locale>() {
            @Override
            public int compare(Locale o1, Locale o2) {
                return o1.getLanguage().compareTo(o2.getLanguage());
            }
        });
        List expectedLanguages = Arrays.asList("ab", "aa", "af", "ak", "am", "an", "ar", "as", "ast", "av", "ay", "az", "ba", "be", "bg", "bh", "bi", "bm", "bn", "bo", "br", "bs", "ca", "ceb", "ch", "cs", "cu", "cv", "cy", "da", "de", "dv", "dz", "ee", "el", "eml", "en", "eo", "es", "et", "eu", "fa", "ff", "fi", "fo", "fr", "fy", "ga", "gd", "gl", "gn", "gu", "gv", "ha", "haw", "he", "hi", "hr", "ht", "hu", "hy", "hz", "ia", "id", "ie", "ig", "ii", "ik", "ilo", "is", "it", "ja", "jv", "ka", "kg", "ki", "kj", "kk", "kl", "km", "kn", "ko", "ks", "ku", "ky", "la", "lb", "lg", "li", "ln", "lo", "lt", "lv", "mg", "mh", "mi", "mk", "ml", "mn", "mo", "ms", "mt", "my", "na", "nb", "nd", "ne", "ng", "nl", "nn", "no", "oc", "oj", "om", "or", "os", "pa", "pl", "pms", "pt", "ro", "ru", "rue", "sa", "sc", "sd", "si", "sk", "sl", "sm", "sn", "so", "sq", "sr", "ss", "st", "su", "sv", "sw", "ta", "te", "tg", "th", "ti", "tk", "to", "tr", "ts", "tt", "tw", "ty", "ug", "uk", "ur", "uz", "ve", "vi", "vo", "wa", "wo", "xh", "yi", "yo", "zu");
        List actualLanguages = new ArrayList();
        for (Object obj : availableLocales) {
            Locale locale = (Locale) obj;
            actualLanguages.add(locale.getLanguage());
        }
        assertEquals(expectedLanguages, actualLanguages);
    }
}