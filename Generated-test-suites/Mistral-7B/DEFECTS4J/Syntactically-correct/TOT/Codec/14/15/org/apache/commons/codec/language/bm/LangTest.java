package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testEnglishLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "The quick brown fox jumps over the lazy dog.";
        String expectedLanguage = Languages.ENGLISH;
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }

    @Test
    public void testGermanLanguage() {
        Lang lang = Lang.instance(NameType.GERMAN);
        String text = "Das schnelle rote Fuchs springt über den faulen Hund.";
        String expectedLanguage = Languages.GERMAN;
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }

    @Test
    public void testMixedLanguage() {
        Lang lang = Lang.instance(NameType.OTHER);
        String text = "The quick brown fox jumps over the lazy dog. Aber das Fuchs ist rot.";
        String expectedLanguage = Languages.OTHER; // Multiple languages, no unique match
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = null;
        lang.guessLanguage(text); // Should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "";
        lang.guessLanguage(text); // Should throw an IllegalArgumentException
    }
}