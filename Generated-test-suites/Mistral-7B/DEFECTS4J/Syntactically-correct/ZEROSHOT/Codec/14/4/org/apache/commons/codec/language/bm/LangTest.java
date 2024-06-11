package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test class for Lang.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertNotNull(englishLang);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test empty string
        assertEquals(Lang.Languages.ANY, englishLang.guessLanguage(""));

        // Test English words
        assertEquals(Lang.Languages.ENGLISH, englishLang.guessLanguage("apple"));
        assertEquals(Lang.Languages.ENGLISH, englishLang.guessLanguage("banana"));
        assertEquals(Lang.Languages.ENGLISH, englishLang.guessLanguage("orange"));

        // Test non-English words
        assertEquals(Lang.Languages.GERMAN, englishLang.guessLanguage("Apfel"));
        assertEquals(Lang.Languages.FRENCH, englishLang.guessLanguage("banane"));
        assertEquals(Lang.Languages.SPANISH, englishLang.guessLanguage("naranja"));

        // Test ambiguous words
        assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("java"));
        assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("koala"));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test empty string
        assertEquals(Collections.emptySet(), englishLang.guessLanguages(""));

        // Test English words
        final Set<String> expectedEnglish = new HashSet<String>(Arrays.asList(
                Lang.Languages.ENGLISH.getName(),
                Lang.Languages.GERMAN.getName(),
                Lang.Languages.DUTCH.getName()));
        assertEquals(expectedEnglish, englishLang.guessLanguages("apple"));

        final Set<String> expectedEnglishBanana = new HashSet<String>(Arrays.asList(
                Lang.Languages.ENGLISH.getName(),
                Lang.Languages.FRENCH.getName(),
                Lang.Languages.GERMAN.getName(),
                Lang.Languages.ITALIAN.getName(),
                Lang.Languages.SPANISH.getName()));
        assertEquals(expectedEnglishBanana, englishLang.guessLanguages("banana"));

        final Set<String> expectedEnglishOrange = new HashSet<String>(Arrays.asList(
                Lang.Languages.ENGLISH.getName(),
                Lang.Languages.GERMAN.getName(),
                Lang.Languages.ITALIAN.getName()));
        assertEquals(expectedEnglishOrange, englishLang.guessLanguages("orange"));

        // Test non-English words
        final Set<String> expectedGermanApfel = new HashSet<String>(Arrays.asList(
                Lang.Languages.ENGLISH.getName(),
                Lang.Languages.GERMAN.getName()));
        assertEquals(expectedGermanApfel, englishLang.guessLanguages("Apfel"));

        final Set<String> expectedFrenchBanane = new HashSet<String>(Arrays.asList(
                Lang.Languages.FRENCH.getName(),
                Lang.Languages.ENGLISH.getName()));
        assertEquals(expectedFrenchBanane, englishLang.guessLanguages("banane"));

        // Test ambiguous words
        final Set<String> expectedAnyJava = new HashSet<String>(EnumSet.allOf(Lang.Languages.class).stream()
                .map(l -> l.getName()).collect(Collectors.toList()));
        assertEquals(expectedAnyJava, englishLang.guessLanguages("java"));

        final Set<String> expectedAnyKoala = new HashSet<String>(EnumSet.allOf(Lang.Languages.class).stream()
                .map(l -> l.getName()).collect(Collectors.toList()));
        assertEquals(expectedAnyKoala, englishLang.guessLanguages("koala"));
    }
}