package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertNotNull(englishLang);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // English word
        String englishWord = "hello";
        String expectedLang = Languages.ENGLISH;
        String resultLang = englishLang.guessLanguage(englishWord);
        assertEquals(expectedLang, resultLang);

        // Non-English word
        String nonEnglishWord = " hallo ";
        resultLang = englishLang.guessLanguage(nonEnglishWord);
        assertEquals(Languages.ANY, resultLang);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // English word
        String englishWord = "hello";
        Languages.LanguageSet expectedLangs = new Languages.LanguageSet(Languages.ENGLISH);
        Languages.LanguageSet resultLangs = englishLang.guessLanguages(englishWord);
        assertEquals(expectedLangs, resultLangs);

        // Non-English word
        String nonEnglishWord = " hallo ";
        resultLangs = englishLang.guessLanguages(nonEnglishWord);
        assertTrue(resultLangs.isEmpty());

        // Multiple languages
        String multipleLanguagesWord = " hallo world ";
        resultLangs = englishLang.guessLanguages(multipleLanguagesWord);
        assertTrue(resultLangs.size() > 0);
        assertFalse(resultLangs.contains(Languages.ENGLISH));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with empty input.
     */
    @Test
    public void testGuessLanguagesEmptyInput() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        String emptyInput = "";
        Languages.LanguageSet resultLangs = englishLang.guessLanguages(emptyInput);
        assertTrue(resultLangs.isEmpty());
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessLanguagesNullInput() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        String nullInput = null;
        englishLang.guessLanguages(nullInput);
    }
}