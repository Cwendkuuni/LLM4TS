package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class LangTest {

    @Test
    public void testEnglishLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        String input = "The quick brown fox jumps over the lazy dog.";
        Languages.LanguageSet expectedOutput = Languages.languageSet("en");

        Languages.LanguageSet actualOutput = englishLang.guessLanguages(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSpanishLanguage() {
        Lang spanishLang = Lang.instance(NameType.SPANISH);
        String input = "El gato lavo la mano del hombre.";
        Languages.LanguageSet expectedOutput = Languages.languageSet("es");

        Languages.LanguageSet actualOutput = spanishLang.guessLanguages(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMixedLanguageInput() {
        Lang mixedLang = Lang.instance(NameType.MIXED);
        String input = "The quick brown fox jumps over the lazy dog. El gato lavo la mano del hombre.";
        Languages.LanguageSet expectedOutput = Languages.languageSet("en", "es");

        Languages.LanguageSet actualOutput = mixedLang.guessLanguages(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        String input = "";

        Languages.LanguageSet actualOutput = englishLang.guessLanguages(input);
        // No languages should be returned for an empty input
        Languages.LanguageSet expectedOutput = Languages.NO_LANGUAGES;
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidNameType() {
        String invalidNameType = "INVALID";
        Lang lang = null;

        try {
            lang = Lang.instance(NameType.valueOf(invalidNameType));
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unexpected IllegalArgumentException: " + e.getMessage());
        }

        if (lang != null) {
            throw new IllegalStateException("Lang instance created for invalid NameType: " + invalidNameType);
        }
    }
}