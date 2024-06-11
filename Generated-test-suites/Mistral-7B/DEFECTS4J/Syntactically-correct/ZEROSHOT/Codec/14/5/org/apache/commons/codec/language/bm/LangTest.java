package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

import org.junit.Test;

/**
 * JUnit 4 test file for the Lang class.
 */
public class LangTest {

    /**
     * Tests the static factory method instance().
     */
    @Test
    public void testInstance() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertNotNull(englishLang);
    }

    /**
     * Tests the guessLanguage() method with an English word.
     */
    @Test
    public void testGuessLanguageEnglish() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final String englishWord = "hello";
        final String expectedLanguage = "en";

        final String resultLanguage = englishLang.guessLanguage(englishWord).getLanguage();
        assertEquals(expectedLanguage, resultLanguage);
    }

    /**
     * Tests the guessLanguage() method with a non-English word.
     */
    @Test
    public void testGuessLanguageNonEnglish() {
        final Lang frenchLang = Lang.instance(NameType.FRENCH);

        final String frenchWord = "salut";
        final String expectedLanguage = "fr";

        final String resultLanguage = frenchLang.guessLanguage(frenchWord).getLanguage();
        assertEquals(expectedLanguage, resultLanguage);
    }

    /**
     * Tests the guessLanguages() method with an English word.
     */
    @Test
    public void testGuessLanguagesEnglish() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final String englishWord = "hello";

        final Set<String> expectedLanguages = Collections.singleton("en");
        final Set<String> resultLanguages = englishLang.guessLanguages(englishWord);
        assertEquals(expectedLanguages, resultLanguages);
    }

    /**
     * Tests the guessLanguages() method with a non-English word.
     */
    @Test
    public void testGuessLanguagesNonEnglish() {
        final Lang frenchLang = Lang.instance(NameType.FRENCH);

        final String frenchWord = "salut";

        final Set<String> expectedLanguages = Arrays.asList("fr", "oc").asList(); // Occitan is a close match
        final Set<String> resultLanguages = frenchLang.guessLanguages(frenchWord);
        assertEquals(expectedLanguages, resultLanguages);
    }

    /**
     * Tests the guessLanguages() method with a word that doesn't match any language.
     */
    @Test
    public void testGuessLanguagesNoMatch() {
        final Lang germanLang = Lang.instance(NameType.GERMAN);

        final String germanWord = "xoxo";
        final Set<String> expectedLanguages = Collections.singleton(Lang.Languages.ANY.getLanguage());
        final Set<String> resultLanguages = germanLang.guessLanguages(germanWord);
        assertEquals(expectedLanguages, resultLanguages);
    }

    /**
     * Tests the guessLanguages() method with an empty String.
     */
    @Test
    public void testGuessLanguagesEmptyString() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final String emptyString = "";
        final Set<String> expectedLanguages = Collections.singleton(Lang.Languages.ANY.getLanguage());
        final Set<String> resultLanguages = englishLang.guessLanguages(emptyString);
        assertEquals(expectedLanguages, resultLanguages);
    }
}