package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;
import static org.junit.Assert.*;

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
        final String englishWord = "hello";

        final String guessedLanguage = englishLang.guessLanguage(englishWord);
        assertEquals(Languages.ENGLISH, guessedLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String englishWord = "hello";

        final Languages.LanguageSet guessedLanguages = englishLang.guessLanguages(englishWord);
        assertEquals(Languages.ENGLISH, guessedLanguages.getAny());
        assertTrue(guessedLanguages.contains(Languages.ENGLISH));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with a word that doesn't match any language.
     */
    @Test
    public void testGuessLanguages_noMatch() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String notEnglishWord = "こんにちは"; // Japanese word

        final Languages.LanguageSet guessedLanguages = englishLang.guessLanguages(notEnglishWord);
        assertEquals(Languages.ANY, guessedLanguages.getAny());
        assertFalse(guessedLanguages.contains(Languages.ENGLISH));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with a word that matches multiple languages.
     */
    @Test
    public void testGuessLanguages_multipleMatches() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String englishSpanishWord = "hola";

        final Languages.LanguageSet guessedLanguages = englishLang.guessLanguages(englishSpanishWord);
        assertEquals(Languages.ENGLISH, guessedLanguages.getAny());
        assertEquals(2, guessedLanguages.size());
        assertTrue(guessedLanguages.contains(Languages.ENGLISH));
        assertTrue(guessedLanguages.contains(Languages.SPANISH));
    }
}